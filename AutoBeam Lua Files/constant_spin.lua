-- This Source Code Form is subject to the terms of the bCDDL, v. 1.1.
-- If a copy of the bCDDL was not distributed with this
-- file, You can obtain one at http://beamng.com/bCDDL-1.1.txt
-- Constant Speed Spinner
-- Created by peskyboyz

local M = {}

-- USER CONFIGURABLE VARIABLES
local ROTATION_SPEED = 60  -- Degrees per second (360 = 1 full rotation per second)
local IGNITION_DEPENDENT = true  -- Only spin when ignition is on
-- END USER CONFIGURABLE VARIABLES

local currentAngle = 0

local function onInit()
    electrics.values['constant_spin'] = 0
    currentAngle = 0
end

local function reset()
    onInit()
end

local function updateGFX(dt)
    local ignitionLevel = electrics.values.ignitionLevel or 0
    
    -- Check if we should spin
    local shouldSpin = true
    if IGNITION_DEPENDENT and ignitionLevel == 0 then
        shouldSpin = false
    end
    
    if shouldSpin then
        -- Increment angle based on rotation speed and delta time
        currentAngle = currentAngle + (ROTATION_SPEED * dt)
        
        -- Keep angle in 0-360 range
        currentAngle = currentAngle % 360
    end
    
    electrics.values['constant_spin'] = currentAngle
end

-- public interface
M.onInit    = onInit
M.reset     = reset
M.updateGFX = updateGFX

return M