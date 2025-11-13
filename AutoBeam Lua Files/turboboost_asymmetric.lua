-- This Source Code Form is subject to the terms of the bCDDL, v. 1.1.
-- If a copy of the bCDDL was not distributed with this
-- file, You can obtain one at http://beamng.com/bCDDL-1.1.txt
-- Custom Turbo Boost Gauge with asymmetric scale support
local M = {}

-- USER CONFIGURABLE VARIABLES
-- Set these values to match your gauge's scale

-- Negative side (vacuum) range in PSI
local VACUUM_MIN = -14.5  -- Minimum vacuum (full vacuum, typically -14.5 PSI or -1 bar)
local VACUUM_MAX = 0      -- Zero point (atmospheric pressure)

-- Positive side (boost) range in PSI  
local BOOST_MIN = 0       -- Zero point (atmospheric pressure)
local BOOST_MAX = 14.5    -- Maximum boost (typically 14.5 PSI or 1 bar, adjust for your turbo)

-- Gauge output range
-- The negative side will output from 0 to ZERO_POINT
-- The positive side will output from ZERO_POINT to 1.0
local ZERO_POINT = 0.333  -- Where zero pressure sits on the gauge (0.333 = 33.3% = 90° out of 270°)
                          -- Examples:
                          -- 0.333 for -1 to 0 over 90°, 0 to 1 over 180° (90° + 180° = 270° total)
                          -- 0.25 for equal negative and positive sides
                          -- 0.333 for -1 to 0 over 90°, 0 to 4 over 180° (change BOOST_MAX to 58 PSI)

-- END USER CONFIGURABLE VARIABLES

local currentBoost = 0
local smoothingFactor = 0.15

local function onInit()
    electrics.values['turboboost_asym'] = ZERO_POINT
    currentBoost = 0
end

local function reset()
    onInit()
end

local function updateGFX(dt)
    local turboBoost = electrics.values.turboBoost or 0  -- In PSI
    local ignitionLevel = electrics.values.ignitionLevel or 0
    
    -- Smooth the boost value for realistic gauge movement
    currentBoost = currentBoost + (turboBoost - currentBoost) * smoothingFactor
    
    local normalizedBoost = 0
    
    if ignitionLevel > 0 then
        if currentBoost < 0 then
            -- Negative side (vacuum)
            -- Map from VACUUM_MIN to VACUUM_MAX (e.g., -14.5 to 0 PSI)
            -- to 0 to ZERO_POINT (e.g., 0 to 0.333)
            local vacuumRange = VACUUM_MAX - VACUUM_MIN
            local vacuumPosition = (currentBoost - VACUUM_MIN) / vacuumRange
            normalizedBoost = vacuumPosition * ZERO_POINT
        else
            -- Positive side (boost)
            -- Map from BOOST_MIN to BOOST_MAX (e.g., 0 to 14.5 PSI)
            -- to ZERO_POINT to 1.0 (e.g., 0.333 to 1.0)
            local boostRange = BOOST_MAX - BOOST_MIN
            local boostPosition = currentBoost / boostRange
            normalizedBoost = ZERO_POINT + (boostPosition * (1.0 - ZERO_POINT))
        end
    else
        -- Ignition off - gauge at minimum position (full vacuum/end stop)
        normalizedBoost = 0
    end
    
    -- Clamp to valid range
    normalizedBoost = math.max(0, math.min(1, normalizedBoost))
    
    electrics.values['turboboost_asym'] = normalizedBoost
    
    -- Optional: expose actual boost PSI for debugging
    electrics.values['turboboost_psi'] = currentBoost
end

-- public interface
M.onInit    = onInit
M.reset     = reset
M.updateGFX = updateGFX

return M