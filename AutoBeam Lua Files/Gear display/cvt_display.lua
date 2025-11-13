-- This Source Code Form is subject to the terms of the bCDDL, v. 1.1.
-- If a copy of the bCDDL was not distributed with this
-- file, You can obtain one at http://beamng.com/bCDDL-1.1.txt
-- Modified by peskyboyz - added ignition level check
-- CVT/Electric Transmission Gear Indicator (P, R, N, D only)
local M = {}

local gearA = 0

local function onInit()
    electrics.values['disp_P'] = 0
    electrics.values['disp_R'] = 0
    electrics.values['disp_N'] = 0
    electrics.values['disp_D'] = 0
end

local function reset()
    onInit()
end

local function updateGFX(dt)
    gearA = electrics.values['gear_A'] or 0
    local ignitionLevel = electrics.values.ignitionLevel or 0
    
    -- Only display gear indicators if ignition is on (> 0)
    if ignitionLevel > 0 then
        -- Park
        if gearA < 0.25 then
            electrics.values['disp_P'] = 1
        else
            electrics.values['disp_P'] = 0
        end
        
        -- Reverse
        if gearA >= 0.25 and gearA < 0.5 then
            electrics.values['disp_R'] = 1
        else
            electrics.values['disp_R'] = 0
        end
        
        -- Neutral
        if gearA >= 0.5 and gearA < 0.75 then
            electrics.values['disp_N'] = 1
        else
            electrics.values['disp_N'] = 0
        end
        
        -- Drive
        if gearA >= 0.75 then
            electrics.values['disp_D'] = 1
        else
            electrics.values['disp_D'] = 0
        end
    else
        -- Ignition is off, turn off all displays
        electrics.values['disp_P'] = 0
        electrics.values['disp_R'] = 0
        electrics.values['disp_N'] = 0
        electrics.values['disp_D'] = 0
    end
end

-- public interface
M.onInit      = onInit
M.reset     = reset
M.updateGFX = updateGFX

return M