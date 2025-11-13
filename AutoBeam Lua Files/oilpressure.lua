-- This Source Code Form is subject to the terms of the bCDDL, v. 1.1.
-- If a copy of the bCDDL was not distributed with this
-- file, You can obtain one at http://beamng.com/bCDDL-1.1.txt
-- Oil Pressure Gauge Simulator
-- Created by peskyboyz

local M = {}

-- USER CONFIGURABLE VARIABLES
local MAX_OIL_PRESSURE = 60    -- Maximum oil pressure in PSI (adjust based on vehicle/gauge)
local IDLE_PRESSURE = 10       -- Pressure at idle in PSI
local PRESSURE_PER_1000RPM = 10 -- PSI increase per 1000 RPM
-- END USER CONFIGURABLE VARIABLES

local targetPressure = 0
local currentPressure = 0
local smoothingFactor = 0.08

local function onInit()
    electrics.values['oil_pressure'] = 0
    electrics.values['oil_pressure_psi'] = 0
    currentPressure = 0
    targetPressure = 0
end

local function reset()
    onInit()
end

local function updateGFX(dt)
    local ignitionLevel = electrics.values.ignitionLevel or 0
    local rpm = electrics.values.rpm or 0
    local oiltemp = electrics.values.oiltemp or 90
    local engineLoad = electrics.values.engineLoad or 0
    
    -- Determine target oil pressure based on engine state
    if ignitionLevel == 0 then
        -- Engine off - no pressure
        targetPressure = 0
    elseif ignitionLevel == 1 then
        -- Accessory mode only - no oil pressure (engine not running)
        targetPressure = 0
    elseif ignitionLevel == 3 then
        -- Starter cranking - very low pressure from oil pump turning slowly
        targetPressure = 3
    elseif ignitionLevel == 2 and rpm < 100 then
        -- Ignition on but engine not running yet
        targetPressure = 0
    else
        -- Engine running - calculate pressure based on RPM
        local basePressure = IDLE_PRESSURE + (rpm / 1000) * PRESSURE_PER_1000RPM
        
        -- Cap at maximum pressure
        basePressure = math.min(basePressure, MAX_OIL_PRESSURE)
        
        -- Oil temperature affects pressure (cold oil = higher pressure)
        local tempFactor = 1.0
        if oiltemp < 60 then
            -- Cold oil - higher pressure (up to 50% higher)
            tempFactor = 1.0 + ((60 - oiltemp) / 60) * 0.5
        elseif oiltemp > 110 then
            -- Hot oil - lower pressure (up to 30% lower)
            tempFactor = 1.0 - ((oiltemp - 110) / 50) * 0.3
            tempFactor = math.max(tempFactor, 0.7)
        end
        
        -- Engine load slightly increases pressure
        local loadEffect = engineLoad * 3
        
        targetPressure = basePressure * tempFactor + loadEffect
        
        -- Add slight random fluctuation for realism
        local fluctuation = (math.random() - 0.5) * 1.5
        targetPressure = targetPressure + fluctuation
    end
    
    -- Smooth the pressure changes
    currentPressure = currentPressure + (targetPressure - currentPressure) * smoothingFactor
    
    -- Normalize to 0-1 range for gauge animation
    local normalizedPressure = currentPressure / MAX_OIL_PRESSURE
    normalizedPressure = math.max(0, math.min(1, normalizedPressure))
    
    electrics.values['oil_pressure'] = normalizedPressure
    electrics.values['oil_pressure_psi'] = currentPressure
end

-- public interface
M.onInit    = onInit
M.reset     = reset
M.updateGFX = updateGFX

return M