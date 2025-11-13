-- This Source Code Form is subject to the terms of the bCDDL, v. 1.1.
-- If a copy of the bCDDL was not distributed with this
-- file, You can obtain one at http://beamng.com/bCDDL-1.1.txt
-- Torque and Power Gauge Controller
-- Created by peskyboyz

local M = {}

-- USER CONFIGURABLE VARIABLES
local MAX_TORQUE_NM = 500       -- Maximum torque for gauge in Nm (set to your engine's max torque)
local MAX_POWER_HP = 400        -- Maximum power for gauge in HP (set to your engine's max HP)
local MAX_POWER_KW = 300        -- Maximum power for gauge in kW (set to your engine's max kW)
local SMOOTHING_FACTOR = 0.15   -- How quickly gauges respond (lower = smoother)
local USE_GEARBOX_TORQUE = false -- Use gearbox output torque instead of engine torque
-- END USER CONFIGURABLE VARIABLES

local engineDevice = nil
local gearboxDevice = nil
local currentTorque = 0
local currentPowerHP = 0
local currentPowerKW = 0

local function onInit()
    electrics.values['torque_gauge'] = 0
    electrics.values['power_gauge_hp'] = 0
    electrics.values['power_gauge_kw'] = 0
    electrics.values['torque_nm'] = 0
    electrics.values['power_hp'] = 0
    electrics.values['power_kw'] = 0
    
    engineDevice = nil
    gearboxDevice = nil
    currentTorque = 0
    currentPowerHP = 0
    currentPowerKW = 0
end

local function reset()
    onInit()
end

local function updateGFX(dt)
    -- Try to get powertrain devices if we don't have them
    if not engineDevice then
        if powertrain and powertrain.getDevicesByCategory then
            local engines = powertrain.getDevicesByCategory("engine")
            if engines and #engines > 0 then
                engineDevice = engines[1]
            end
        end
    end
    
    if not gearboxDevice and USE_GEARBOX_TORQUE then
        if powertrain and powertrain.getDevicesByType then
            local gearboxes = powertrain.getDevicesByType("automaticGearbox")
            if not gearboxes or #gearboxes == 0 then
                gearboxes = powertrain.getDevicesByType("manualGearbox")
            end
            if gearboxes and #gearboxes > 0 then
                gearboxDevice = gearboxes[1]
            end
        end
    end
    
    local ignitionLevel = electrics.values.ignitionLevel or 0
    
    -- Only show torque/power if engine is running
    if ignitionLevel > 0 and engineDevice then
        local rpm = electrics.values.rpm or 0
        local torqueNm = 0
        
        -- Get torque value
        if USE_GEARBOX_TORQUE and gearboxDevice and gearboxDevice.outputTorque1 then
            torqueNm = math.abs(gearboxDevice.outputTorque1)
        elseif engineDevice.outputTorque1 then
            torqueNm = math.abs(engineDevice.outputTorque1)
        end
        
        -- Calculate power
        -- HP = (Torque in Nm × RPM) / 7127
        -- kW = (Torque in Nm × RPM) / 9549
        local powerHP = 0
        local powerKW = 0
        if rpm > 0 then
            powerHP = (torqueNm * rpm) / 7127
            powerKW = (torqueNm * rpm) / 9549
        end
        
        -- Smooth the values
        currentTorque = currentTorque + (torqueNm - currentTorque) * SMOOTHING_FACTOR
        currentPowerHP = currentPowerHP + (powerHP - currentPowerHP) * SMOOTHING_FACTOR
        currentPowerKW = currentPowerKW + (powerKW - currentPowerKW) * SMOOTHING_FACTOR
        
        -- Normalize to 0-1 for gauges
        local normalizedTorque = currentTorque / MAX_TORQUE_NM
        local normalizedPowerHP = currentPowerHP / MAX_POWER_HP
        local normalizedPowerKW = currentPowerKW / MAX_POWER_KW
        
        -- Clamp values
        normalizedTorque = math.max(0, math.min(1, normalizedTorque))
        normalizedPowerHP = math.max(0, math.min(1, normalizedPowerHP))
        normalizedPowerKW = math.max(0, math.min(1, normalizedPowerKW))
        
        -- Output normalized gauge values
        electrics.values['torque_gauge'] = normalizedTorque
        electrics.values['power_gauge_hp'] = normalizedPowerHP
        electrics.values['power_gauge_kw'] = normalizedPowerKW
        
        -- Output actual values for debugging or digital displays
        electrics.values['torque_nm'] = currentTorque
        electrics.values['power_hp'] = currentPowerHP
        electrics.values['power_kw'] = currentPowerKW
    else
        -- Engine off - reset to zero
        currentTorque = 0
        currentPowerHP = 0
        currentPowerKW = 0
        
        electrics.values['torque_gauge'] = 0
        electrics.values['power_gauge_hp'] = 0
        electrics.values['power_gauge_kw'] = 0
        electrics.values['torque_nm'] = 0
        electrics.values['power_hp'] = 0
        electrics.values['power_kw'] = 0
    end
end

-- public interface
M.onInit    = onInit
M.reset     = reset
M.updateGFX = updateGFX

return M