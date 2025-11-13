-- This Source Code Form is subject to the terms of the bCDDL, v. 1.1.
-- If a copy of the bCDDL was not distributed with this
-- file, You can obtain one at http://beamng.com/bCDDL-1.1.txt
-- Created by peskyboyz
-- Dynamic Advanced Manual Gear Indicator with automatic range calculation and ignition level check

local M = {}

local gearA = 0
local maxGears = 10  -- Default to 10-speed
local gearRanges = {}
local detectedMaxGears = false

local function calculateGearRanges(numGears)
    -- Total positions = R, N, D, S + numGears (1, 2, 3, ... numGears)
    -- No Park for advanced manual
    local totalPositions = 4 + numGears
    local rangeSize = 1.0 / totalPositions
    
    -- Calculate midpoint boundaries between positions
    gearRanges = {
        R = {min = 0, max = rangeSize * 0.5},
        N = {min = rangeSize * 0.5, max = rangeSize * 1.5},
        D = {min = rangeSize * 1.5, max = rangeSize * 2.5},
        S = {min = rangeSize * 2.5, max = rangeSize * 3.5}
    }
end

local function detectMaxGears()
    -- Try to detect max gears from electrics values
    local maxGearFromElectrics = electrics.values.maxGearIndex
    
    if maxGearFromElectrics and maxGearFromElectrics > 0 then
        local detectedGears = math.floor(maxGearFromElectrics)
        if detectedGears ~= maxGears then
            maxGears = detectedGears
            calculateGearRanges(maxGears)
            detectedMaxGears = true
            print("Max gears detected from maxGearIndex: " .. maxGears)
        end
    end
end

local function onInit()
    electrics.values['disp_R'] = 0
    electrics.values['disp_N'] = 0
    electrics.values['disp_D'] = 0
    electrics.values['disp_S'] = 0
    
    -- Initialize numbered gear displays
    for i = 1, 10 do
        electrics.values['disp_' .. i] = 0
    end
    
    maxGears = 10
    detectedMaxGears = false
    calculateGearRanges(maxGears)
end

local function reset()
    onInit()
end

local function updateGFX(dt)
    gearA = electrics.values['gear_A'] or 0
    local gearIndex = electrics.values.gearIndex or 0
    local ignitionLevel = electrics.values.ignitionLevel or 0
    
    -- Try to detect max gears if not yet detected
    if not detectedMaxGears then
        detectMaxGears()
    end
    
    -- Only display gear indicators if ignition is on (> 0)
    if ignitionLevel > 0 then
        -- Check gear_A first (automatic transmission modes)
        -- Reverse Auto
        if gearA >= gearRanges.R.min and gearA < gearRanges.R.max then
            electrics.values['disp_R'] = 1
        else
            electrics.values['disp_R'] = 0
        end
        
        -- Neutral Auto
        if gearA >= gearRanges.N.min and gearA < gearRanges.N.max then
            electrics.values['disp_N'] = 1
        else
            electrics.values['disp_N'] = 0
        end
        
        -- Drive
        if gearA >= gearRanges.D.min and gearA < gearRanges.D.max then
            electrics.values['disp_D'] = 1
            -- If in D ignore the gearIndex digits
            for i = 1, 10 do
                electrics.values['disp_' .. i] = 0
            end
        else
            electrics.values['disp_D'] = 0
        end
        
        -- Sport
        if gearA >= gearRanges.S.min and gearA < gearRanges.S.max then
            electrics.values['disp_S'] = 1
            -- If in S ignore the gearIndex digits
            for i = 1, 10 do
                electrics.values['disp_' .. i] = 0
            end
        else
            electrics.values['disp_S'] = 0
        end
        
        -- Only check gearIndex if NOT in Neutral, Drive or Sport mode
        if not (gearA >= gearRanges.N.min and gearA < gearRanges.S.max) then
            -- Use gearIndex for manual transmission gears            
            -- First
            if gearIndex >= 0.5 and gearIndex < 1.5 then
                electrics.values['disp_1'] = 1
            else
                electrics.values['disp_1'] = 0
            end
            
            -- Second
            if gearIndex >= 1.5 and gearIndex < 2.5 then
                electrics.values['disp_2'] = 1
            else
                electrics.values['disp_2'] = 0
            end
            
            -- Third
            if gearIndex >= 2.5 and gearIndex < 3.5 then
                electrics.values['disp_3'] = 1
            else
                electrics.values['disp_3'] = 0
            end
            
            -- Fourth
            if gearIndex >= 3.5 and gearIndex < 4.5 then
                electrics.values['disp_4'] = 1
            else
                electrics.values['disp_4'] = 0
            end
            
            -- Fifth
            if gearIndex >= 4.5 and gearIndex < 5.5 then
                electrics.values['disp_5'] = 1
            else
                electrics.values['disp_5'] = 0
            end
            
            -- Sixth
            if gearIndex >= 5.5 and gearIndex < 6.5 then
                electrics.values['disp_6'] = 1
            else
                electrics.values['disp_6'] = 0
            end
            
            -- Seventh
            if gearIndex >= 6.5 and gearIndex < 7.5 then
                electrics.values['disp_7'] = 1
            else
                electrics.values['disp_7'] = 0
            end
            
            -- Eighth
            if gearIndex >= 7.5 and gearIndex < 8.5 then
                electrics.values['disp_8'] = 1
            else
                electrics.values['disp_8'] = 0
            end
            
            -- Ninth
            if gearIndex >= 8.5 and gearIndex < 9.5 then
                electrics.values['disp_9'] = 1
            else
                electrics.values['disp_9'] = 0
            end
            
            -- Tenth
            if gearIndex >= 9.5 then
                electrics.values['disp_10'] = 1
            else
                electrics.values['disp_10'] = 0
            end
        end
    else
        -- Ignition is off, turn off all displays
        electrics.values['disp_R'] = 0
        electrics.values['disp_N'] = 0
        electrics.values['disp_D'] = 0
        electrics.values['disp_S'] = 0
        
        for i = 1, 10 do
            electrics.values['disp_' .. i] = 0
        end
    end
end

-- public interface
M.onInit    = onInit
M.reset     = reset
M.updateGFX = updateGFX

return M