-- This Source Code Form is subject to the terms of the bCDDL, v. 1.1.
-- If a copy of the bCDDL was not distributed with this
-- file, You can obtain one at http://beamng.com/bCDDL-1.1.txt
-- Drive Mode Indicator for gauge/dial display
-- Created by peskyboyz

local M = {}

-- USER CONFIGURABLE VARIABLES
-- Manually specify your vehicle's enabled drive modes in order
-- The positions will be automatically distributed evenly across the gauge (0 to 1)
-- Common modes: "off", "comfort", "sport", "offroad", "eco", "race", "track", "snow"
local ENABLED_MODES = {"comfort", "sport", "off"}
-- END USER CONFIGURABLE VARIABLES

local driveModeController = nil
local currentModePosition = 0
local smoothingFactor = 0.2
local MODE_POSITIONS = {}

local function onInit()
    electrics.values['drivemode_dial'] = 0
    electrics.values['drivemode_name'] = ""
    currentModePosition = 0
    driveModeController = nil
    
    -- Calculate positions for enabled modes
    MODE_POSITIONS = {}
    local modeCount = #ENABLED_MODES
    if modeCount > 0 then
        for i, modeName in ipairs(ENABLED_MODES) do
            local position = (i - 1) / math.max(1, modeCount - 1)
            MODE_POSITIONS[modeName] = position
            -- Initialize binary outputs for each mode
            electrics.values['drivemode_' .. modeName] = 0
        end
    end
end

local function reset()
    onInit()
end

local function updateGFX(dt)
    -- Try to get the drive mode controller if we don't have it yet
    if not driveModeController then
        driveModeController = controller.getController("driveModes")
    end
    
    -- Get current mode and update gauge
    if driveModeController and driveModeController.getCurrentDriveModeKey then
        local currentModeKey = driveModeController.getCurrentDriveModeKey()
        
        if currentModeKey and MODE_POSITIONS[currentModeKey] then
            local targetPosition = MODE_POSITIONS[currentModeKey]
            
            -- Smooth the transition
            currentModePosition = currentModePosition + (targetPosition - currentModePosition) * smoothingFactor
            
            electrics.values['drivemode_dial'] = currentModePosition
            electrics.values['drivemode_name'] = currentModeKey
            
            -- Update binary outputs for each mode
            for modeName, _ in pairs(MODE_POSITIONS) do
                electrics.values['drivemode_' .. modeName] = (modeName == currentModeKey) and 1 or 0
            end
        else
            -- Mode not in our list, default to 0
            electrics.values['drivemode_dial'] = 0
            electrics.values['drivemode_name'] = currentModeKey or "none"
            
            -- Turn off all binary outputs
            for modeName, _ in pairs(MODE_POSITIONS) do
                electrics.values['drivemode_' .. modeName] = 0
            end
        end
    else
        -- No drive mode controller available
        electrics.values['drivemode_dial'] = 0
        electrics.values['drivemode_name'] = "none"
        
        -- Turn off all binary outputs
        for modeName, _ in pairs(MODE_POSITIONS) do
            electrics.values['drivemode_' .. modeName] = 0
        end
    end
end

-- public interface
M.onInit    = onInit
M.reset     = reset
M.updateGFX = updateGFX

return M