-- This Source Code Form is subject to the terms of the bCDDL, v. 1.1.
-- If a copy of the bCDDL was not distributed with this
-- file, You can obtain one at http://beamng.com/bCDDL-1.1.txt
-- Electrical System Simulator - Voltage Gauge & Ammeter
-- Created by peskyboyz

local M = {}

local targetVoltage = 0
local currentVoltage = 0
local targetCurrent = 0
local currentCurrent = 0
local batteryCharge = 1.0
local voltageSmoothingFactor = 0.1
local currentSmoothingFactor = 0.15

-- USER CONFIGURABLE VARIABLES FOR AMMETER
local DISCHARGE_MAX = -60  -- Maximum discharge in amps (negative)
local CHARGE_MAX = 60      -- Maximum charge in amps (positive)
local AMMETER_ZERO_POINT = 0.5  -- Where zero sits on ammeter gauge (0.5 = center)
-- END USER CONFIGURABLE VARIABLES

local function onInit()
    electrics.values['voltage_gauge'] = 0
    electrics.values['ammeter'] = AMMETER_ZERO_POINT
    electrics.values['battery_charge'] = 1.0
    currentVoltage = 0
    targetVoltage = 0
    currentCurrent = 0
    targetCurrent = 0
    batteryCharge = 1.0
end

local function reset()
    onInit()
end

local function updateGFX(dt)
    local ignitionLevel = electrics.values.ignitionLevel or 0
    local electricalLoad = electrics.values.electricalLoadCoef or 0
    local rpm = electrics.values.rpm or 0
    local lights = electrics.values.lights or 0
    
    -- Battery charge/discharge simulation
    if ignitionLevel == 0 then
        -- Ignition off - very slow self-discharge
        batteryCharge = batteryCharge - (0.0001 * dt)
    elseif ignitionLevel == 1 then
        -- Accessory mode - moderate drain
        batteryCharge = batteryCharge - (0.001 * dt * (1 + electricalLoad * 0.5))
    elseif ignitionLevel == 3 then
        -- Starter cranking - heavy drain
        batteryCharge = batteryCharge - (0.02 * dt)
    elseif ignitionLevel == 2 then
        if rpm > 500 then
            -- Engine running - alternator charging
            if batteryCharge < 1.0 then
                local chargeRate = 0.01
                batteryCharge = batteryCharge + (chargeRate * dt)
            end
            
            -- Heavy electrical loads slow charging
            if electricalLoad > 0.5 then
                local loadDrain = (electricalLoad - 0.5) * 0.003 * dt
                batteryCharge = batteryCharge - loadDrain
            end
        else
            -- Ignition on but engine not running - draining
            batteryCharge = batteryCharge - (0.003 * dt * (1 + electricalLoad))
        end
    end
    
    -- Additional drain from lights when engine not running
    if rpm <= 500 then
        if lights >= 0.5 and lights < 1.5 then
            -- Low beams
            batteryCharge = batteryCharge - (0.002 * dt)
        elseif lights >= 1.5 then
            -- High beams (more drain)
            batteryCharge = batteryCharge - (0.004 * dt)
        end
    end
    
    -- Clamp battery charge between 0 and 1
    batteryCharge = math.max(0, math.min(1, batteryCharge))
    
    -- Calculate base voltages based on battery state
    local batteryVoltage = 11.5 + (batteryCharge * 1.5)  -- 11.5V (dead) to 13.0V (full)
    local alternatorVoltage = 14.2
    
    -- ====================
    -- VOLTAGE GAUGE CALCULATION
    -- ====================
    if ignitionLevel == 0 then
        -- Ignition off - no voltage displayed
        targetVoltage = 0
    elseif ignitionLevel == 3 then
        -- Starter cranking - voltage drops based on battery state
        local crankVoltage = 9.0 + (batteryCharge * 2.0)  -- 9V (weak) to 11V (strong)
        targetVoltage = crankVoltage - (electricalLoad * 0.8)
    elseif ignitionLevel == 1 then
        -- Accessory mode - battery voltage with load
        targetVoltage = batteryVoltage - (electricalLoad * 0.5)
    elseif ignitionLevel == 2 then
        -- Ignition on
        if rpm > 500 then
            -- Engine running - alternator charging
            targetVoltage = alternatorVoltage
            
            -- Only apply load effect if electrical load is unusually high
            if electricalLoad > 0.8 then
                local loadEffect = (electricalLoad - 0.8) * 0.5
                targetVoltage = targetVoltage - loadEffect
            end
            
            -- Slight voltage drop if battery is very low
            local batteryEffect = (1.0 - batteryCharge) * 0.1
            targetVoltage = targetVoltage - batteryEffect
        else
            -- Engine not running but ignition on - battery voltage
            targetVoltage = batteryVoltage - (electricalLoad * 0.8)
        end
    end
    
    -- Smooth the voltage changes for realistic gauge movement
    currentVoltage = currentVoltage + (targetVoltage - currentVoltage) * voltageSmoothingFactor
    
    -- Normalize to 0-1 range for voltage gauge animation (9V to 19V range)
    local normalizedVoltage = (currentVoltage - 9) / 10
    normalizedVoltage = math.max(0, math.min(1, normalizedVoltage))
    
    electrics.values['voltage_gauge'] = normalizedVoltage
    
    -- ====================
    -- AMMETER CALCULATION
    -- ====================
    if ignitionLevel == 0 then
        -- Engine off - gauge rests at full discharge position
        targetCurrent = DISCHARGE_MAX
    elseif ignitionLevel == 3 then
        -- Starter - heavy discharge
        targetCurrent = -40 - (10 * (1.0 - batteryCharge))
    elseif ignitionLevel == 1 then
        -- Accessory mode - moderate discharge
        targetCurrent = -10 - (electricalLoad * 5)
        
        -- Lights add load in accessory mode
        if lights >= 0.5 and lights < 1.5 then
            targetCurrent = targetCurrent - 8
        elseif lights >= 1.5 then
            targetCurrent = targetCurrent - 12
        end
    elseif ignitionLevel == 2 then
        if rpm > 500 then
            -- Engine running - alternator is charging
            if batteryCharge < 0.95 then
                -- Alternator produces constant output based on RPM
                local rpmFactor = math.min(rpm / 2000, 1.0)  -- Full output at 2000 RPM
                local alternatorOutput = 50 * rpmFactor  -- Alternator produces 50 amps at full RPM
                
                -- Calculate total electrical load
                local totalLoad = electricalLoad * 10
                
                -- Add lights load
                if lights >= 0.5 and lights < 1.5 then
                    totalLoad = totalLoad + 5
                elseif lights >= 1.5 then
                    totalLoad = totalLoad + 8
                end
                
                -- Net current going to battery = alternator output - loads
                local netToBattery = alternatorOutput - totalLoad
                
                -- If battery is nearly full, reduce charging current
                if batteryCharge > 0.90 then
                    netToBattery = netToBattery * (1.0 - batteryCharge) * 10  -- Taper off as battery fills
                end
                
                targetCurrent = netToBattery
            else
                -- Battery charged - sit at zero (balanced)
                targetCurrent = 0
            end
        else
            -- Ignition on but engine not running - discharging
            targetCurrent = -15 - (electricalLoad * 10)
            
            -- Lights add significant load when engine not running
            if lights >= 0.5 and lights < 1.5 then
                targetCurrent = targetCurrent - 8
            elseif lights >= 1.5 then
                targetCurrent = targetCurrent - 12
            end
        end
    end
    
    -- Smooth the current changes
    currentCurrent = currentCurrent + (targetCurrent - currentCurrent) * currentSmoothingFactor
    
    -- Normalize to 0-1 range for ammeter
    local normalizedCurrent = 0
    if currentCurrent < 0 then
        -- Discharge side
        local dischargePosition = currentCurrent / DISCHARGE_MAX
        normalizedCurrent = AMMETER_ZERO_POINT - (dischargePosition * AMMETER_ZERO_POINT)
    else
        -- Charge side
        local chargePosition = currentCurrent / CHARGE_MAX
        normalizedCurrent = AMMETER_ZERO_POINT + (chargePosition * (1.0 - AMMETER_ZERO_POINT))
    end
    
    normalizedCurrent = math.max(0, math.min(1, normalizedCurrent))
    
    electrics.values['ammeter'] = normalizedCurrent
    
    -- ====================
    -- DEBUG/SHARED VALUES
    -- ====================
    electrics.values['battery_charge'] = batteryCharge
    electrics.values['ammeter_amps'] = currentCurrent
    electrics.values['voltage_volts'] = currentVoltage
end

-- public interface
M.onInit    = onInit
M.reset     = reset
M.updateGFX = updateGFX

return M