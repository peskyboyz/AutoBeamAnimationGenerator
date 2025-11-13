-- This Source Code Form is subject to the terms of the bCDDL, v. 1.1.
-- If a copy of the bCDDL was not distributed with this
-- file, You can obtain one at http://beamng.com/bCDDL-1.1.txt
-- Odometer and Trip Meter with Realistic Digit Rolling
local M = {}

-- USER CONFIGURABLE VARIABLES
local UNITS = "km"              -- "km" or "miles"
local CLICK_START = 0.90        -- When digit starts rolling (95% = 0.95)
local CLICK_END = 1.0           -- When digit finishes rolling (100% = 1.0)
local MAX_VALUE = 999999        -- Maximum value before rollover (6 digits)
-- END USER CONFIGURABLE VARIABLES

local METERS_PER_KM = 1000
local METERS_PER_MILE = 1609.34

local function calculateDigitPosition(value, digitPlace)
    -- For mechanical odometer behavior, we need to trace back to digit 0
    -- to determine if THIS digit should be moving
    
    -- Get value for this digit place
    local scaledValue = value / (10 ^ digitPlace)
    local digit = math.floor(scaledValue % 10)
    
    -- Check if this digit should be moving by checking if ALL previous digits
    -- are at 9 and the first digit (digit 0) is in its click zone
    local shouldMove = true
    local moveProgress = 0
    
    -- Check all digits from 0 to digitPlace-1
    for checkPlace = 0, digitPlace - 1 do
        local checkValue = value / (10 ^ checkPlace)
        local checkDigit = math.floor(checkValue % 10)
        local checkFraction = (checkValue % 10) - checkDigit
        
        if checkPlace == 0 then
            -- This is digit 0 - check if it's in the click zone
            if checkDigit == 9 and checkFraction >= CLICK_START then
                -- Digit 0 is rolling from 9->0, calculate progress
                moveProgress = (checkFraction - CLICK_START) / (CLICK_END - CLICK_START)
            else
                -- Digit 0 is not in click zone, so nothing should move
                shouldMove = false
                break
            end
        else
            -- For digits 1 and above, they must be at 9 for the chain to continue
            if checkDigit ~= 9 then
                shouldMove = false
                break
            end
        end
    end
    
    if shouldMove and digitPlace > 0 then
        -- This digit is moving - transition to next number
        -- All digits move at the SAME speed based on digit 0's progress
        local currentAngle = digit * 36
        local nextDigit = (digit + 1) % 10
        local nextAngle = nextDigit * 36
        
        -- Handle 9->0 rollover (360->0)
        if digit == 9 then
            nextAngle = 360
        end
        
        return currentAngle + (nextAngle - currentAngle) * moveProgress
    elseif digitPlace == 0 then
        -- Digit 0 rotates smoothly without clicks
        local fraction = (scaledValue % 10) - digit
        local currentAngle = digit * 36
        local nextDigit = (digit + 1) % 10
        local nextAngle = nextDigit * 36
        
        if digit == 9 then
            nextAngle = 360
        end
        
        -- Smooth rotation through the entire range
        return currentAngle + (nextAngle - currentAngle) * fraction
    else
        -- Digit is stationary at current number
        return digit * 36
    end
end

local function onInit()
    -- Initialize odometer digit outputs (7 digits)
    for i = 0, 6 do
        electrics.values['odo_digit_' .. i] = 0
    end
    
    -- Initialize trip digit outputs (7 digits)
    for i = 0, 6 do
        electrics.values['trip_digit_' .. i] = 0
    end
end

local function reset()
    onInit()
end

local function updateGFX(dt)
    -- Get raw odometer and trip values in meters
    local odometerMeters = electrics.values.odometer or 0
    local tripMeters = electrics.values.trip or 0
    
    -- Convert to selected units
    local conversionFactor = (UNITS == "miles") and METERS_PER_MILE or METERS_PER_KM
    local odometerValue = odometerMeters * 1000 / conversionFactor  -- odometer is in km, convert to meters first
    local tripValue = tripMeters * 1000 / conversionFactor
    
    -- Clamp to max value
    odometerValue = odometerValue % (MAX_VALUE + 1)
    tripValue = tripValue % (MAX_VALUE + 1)
    
    -- Calculate each digit position for odometer
    -- Digit 0 = 100m (0.1 units), Digit 1 = 1km (1 unit), Digit 2 = 10km (10 units), etc.
    -- We need to divide by 100 to shift the decimal place by two positions
    local odometerShifted = odometerValue / 100
    local tripShifted = tripValue / 100
    
    for i = 0, 6 do
        electrics.values['odo_digit_' .. i] = calculateDigitPosition(odometerShifted, i)
    end
    
    -- Calculate each digit position for trip
    for i = 0, 6 do
        electrics.values['trip_digit_' .. i] = calculateDigitPosition(tripShifted, i)
    end
end

-- public interface
M.onInit    = onInit
M.reset     = reset
M.updateGFX = updateGFX

return M