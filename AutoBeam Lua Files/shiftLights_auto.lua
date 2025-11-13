-- This Source Code Form is subject to the terms of the bCDDL, v. 1.1.
-- If a copy of the bCDDL was not distributed with this
-- file, You can obtain one at http://beamng.com/bCDDL-1.1.txt
-- Shift Lights for Automation cars
-- Created by peskyboyz

local M = {}

-- USER CONFIGURABLE VARIABLES
local NUM_LEDS = 9              -- Number of shift light LEDs
local RPM_RANGE_START = 6000    -- RPM where first LED turns on
local RPM_RANGE_END = 9200      -- RPM where last LED turns on (typically redline)
local FLASH_AT_REDLINE = true   -- Flash all LEDs at redline
local FLASH_DURATION = 0.1      -- Flash interval in seconds
local KEEP_LOWER_LEDS_ON = true -- Keep lower LEDs lit as RPM increases
-- END USER CONFIGURABLE VARIABLES

local flashTimer = 0
local flashState = false

local function onInit()
    -- Initialize all LED outputs
    for i = 1, NUM_LEDS do
        electrics.values['shift_led_' .. i] = 0
    end
    electrics.values['shift_flash'] = 0
    flashTimer = 0
    flashState = false
end

local function reset()
    onInit()
end

local function updateGFX(dt)
    local rpm = electrics.values.rpm or 0
    local maxrpm = electrics.values.maxrpm or RPM_RANGE_END
    local ignitionLevel = electrics.values.ignitionLevel or 0
    
    -- Only operate if ignition is on
    if ignitionLevel > 0 then
        -- Calculate RPM range and step size
        local rpmRange = RPM_RANGE_END - RPM_RANGE_START
        local rpmStep = rpmRange / NUM_LEDS
        
        -- Determine if we're at or over redline
        local atRedline = rpm >= RPM_RANGE_END
        
        -- Update flash timer
        if atRedline and FLASH_AT_REDLINE then
            flashTimer = flashTimer + dt
            if flashTimer >= FLASH_DURATION then
                flashState = not flashState
                flashTimer = 0
            end
        else
            flashState = false
            flashTimer = 0
        end
        
        -- Update each LED
        for i = 1, NUM_LEDS do
            local ledStartRPM = RPM_RANGE_START + (i - 1) * rpmStep
            local ledEndRPM = RPM_RANGE_START + i * rpmStep
            
            if atRedline and FLASH_AT_REDLINE then
                -- Flash all LEDs at redline
                electrics.values['shift_led_' .. i] = flashState and 1 or 0
            elseif rpm >= ledStartRPM and (KEEP_LOWER_LEDS_ON or rpm < ledEndRPM) then
                -- LED should be on
                electrics.values['shift_led_' .. i] = 1
            else
                -- LED should be off
                electrics.values['shift_led_' .. i] = 0
            end
        end
        
        -- Output flash state for additional use
        electrics.values['shift_flash'] = (atRedline and flashState) and 1 or 0
    else
        -- Ignition off - turn off all LEDs
        for i = 1, NUM_LEDS do
            electrics.values['shift_led_' .. i] = 0
        end
        electrics.values['shift_flash'] = 0
    end
end

-- public interface
M.onInit    = onInit
M.reset     = reset
M.updateGFX = updateGFX

return M