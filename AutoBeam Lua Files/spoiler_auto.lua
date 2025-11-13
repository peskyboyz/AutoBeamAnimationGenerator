-- This Source Code Form is subject to the terms of the bCDDL, v. 1.1.
-- If a copy of the bCDDL was not distributed with this
-- file, You can obtain one at http://beamng.com/bCDDL-1.1.txt
-- Active Spoiler Controller for Automation cars
-- Created by peskyboyz

local M = {}

-- USER CONFIGURABLE VARIABLES
local SPEED_THRESHOLD_NORMAL = 23    -- Speed (m/s) to deploy spoiler to normal position (~50 mph)
local SPEED_THRESHOLD_LOW = 12       -- Speed (m/s) to retract spoiler when slowing down (~27 mph)
local SPEED_THRESHOLD_HIGH = 8       -- Speed (m/s) minimum for high position when braking (~18 mph)
local BRAKE_THRESHOLD_HIGH = 0.5     -- Brake input (0-1) to deploy to high position

local SPOILER_LOW = 0.0      -- Fully retracted
local SPOILER_NORMAL = 0.58  -- Partially deployed
local SPOILER_HIGH = 1.0     -- Fully deployed

local SMOOTHING_FACTOR = 0.1 -- How quickly the spoiler moves (lower = slower/smoother)

-- Drive mode configurations
-- Each mode can specify:
--   lowSpeedPosition: Position when below speed threshold (number or nil for speed-based logic)
--   allowSpeedDeploy: If true, spoiler will deploy at SPEED_THRESHOLD_NORMAL (default: true)
--   allowAirbrake: If true, spoiler will deploy to high position when braking (default: true)
local DRIVEMODE_CONFIGS = {
    ["comfort"] = {
        lowSpeedPosition = 0.0,      -- Retracted at low speeds
        allowSpeedDeploy = true,     -- Will deploy at speed
        allowAirbrake = true         -- Airbrake enabled
    },
    ["offroad"] = {
        lowSpeedPosition = 0.0,      -- Retracted at low speeds
        allowSpeedDeploy = false,    -- Stay retracted even at speed
        allowAirbrake = true         -- Airbrake still works
    },
    ["sport"] = {
        lowSpeedPosition = 0.4,      -- Slightly deployed at low speeds
        allowSpeedDeploy = true,     -- Will deploy further at speed
        allowAirbrake = true         -- Airbrake enabled
    },
    ["off"] = {
        lowSpeedPosition = 0.5,      -- More deployed at low speeds
        allowSpeedDeploy = true,     -- Will deploy at speed
        allowAirbrake = true         -- Airbrake enabled
    }
}
-- END USER CONFIGURABLE VARIABLES

local targetSpoilerPosition = 0
local currentSpoilerPosition = 0
local driveModeController = nil

local function onInit()
    electrics.values['spoiler_active'] = 0
    targetSpoilerPosition = 0
    currentSpoilerPosition = 0
    driveModeController = nil
end

local function reset()
    onInit()
end

local function updateGFX(dt)
    local speed = electrics.values.wheelspeed or 0
    local brake = electrics.values.brake or 0
    local ignitionLevel = electrics.values.ignitionLevel or 0
    
    -- Try to get drive mode controller
    if not driveModeController then
        driveModeController = controller.getController("driveModes")
    end
    
    -- Get current drive mode
    local currentMode = nil
    if driveModeController and driveModeController.getCurrentDriveModeKey then
        currentMode = driveModeController.getCurrentDriveModeKey()
    end
    
    -- Get configuration for current drive mode (with defaults if mode not configured)
    local modeConfig = DRIVEMODE_CONFIGS[currentMode] or {}
    local lowSpeedPosition = modeConfig.lowSpeedPosition
    local allowSpeedDeploy = modeConfig.allowSpeedDeploy ~= false  -- Default to true
    local allowAirbrake = modeConfig.allowAirbrake ~= false        -- Default to true
    
    -- Only operate spoiler if ignition is on
    if ignitionLevel > 0 then
        -- Check for airbrake condition first (highest priority)
        if brake > BRAKE_THRESHOLD_HIGH and allowAirbrake then
            -- Heavy braking - deploy to high position if speed is sufficient
            if speed >= SPEED_THRESHOLD_HIGH then
                targetSpoilerPosition = SPOILER_HIGH
            elseif lowSpeedPosition then
                -- Too slow for airbrake, return to low speed position
                targetSpoilerPosition = lowSpeedPosition
            else
                -- No configured low speed position - retract
                targetSpoilerPosition = SPOILER_LOW
            end
        -- Check for speed-based deployment
        elseif speed >= SPEED_THRESHOLD_NORMAL and allowSpeedDeploy then
            -- Above deployment speed and mode allows it - deploy to normal position
            targetSpoilerPosition = SPOILER_NORMAL
        -- Below speed threshold or speed deploy not allowed
        else
            if lowSpeedPosition then
                -- Mode has a configured low speed position
                targetSpoilerPosition = lowSpeedPosition
            else
                -- No configured position - use speed-based hysteresis logic
                if currentSpoilerPosition >= SPOILER_NORMAL and speed >= SPEED_THRESHOLD_LOW then
                    -- Keep spoiler deployed until we drop below the lower threshold
                    targetSpoilerPosition = SPOILER_NORMAL
                else
                    -- Retract spoiler
                    targetSpoilerPosition = SPOILER_LOW
                end
            end
        end
    else
        -- Ignition off - retract spoiler
        targetSpoilerPosition = SPOILER_LOW
    end
    
    -- Smooth the spoiler movement
    currentSpoilerPosition = currentSpoilerPosition + (targetSpoilerPosition - currentSpoilerPosition) * SMOOTHING_FACTOR
    
    electrics.values['spoiler_active'] = currentSpoilerPosition
end

-- public interface
M.onInit    = onInit
M.reset     = reset
M.updateGFX = updateGFX

return M