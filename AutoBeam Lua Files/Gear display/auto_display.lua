-- This Source Code Form is subject to the terms of the bCDDL, v. 1.1.
-- If a copy of the bCDDL was not distributed with this
-- file, You can obtain one at http://beamng.com/bCDDL-1.1.txt
-- Modified by peskyboyz - added ignition level check
local M = {}

local gearA = 0

local function onInit()
    electrics.values['disp_P'] = 0
    electrics.values['disp_R'] = 0
    electrics.values['disp_N'] = 0
    electrics.values['disp_D'] = 0
    electrics.values['disp_1'] = 0
    electrics.values['disp_2'] = 0
end

local function reset()
    onInit()
end

local function updateGFX(dt)
    gearA = electrics.values['gear_A'] or 0
    local ignitionLevel = electrics.values.ignitionLevel or 0
    
    -- Only display gear indicators if ignition is on (> 0)
    if ignitionLevel > 0 then
        if gearA < 0.15 then
            electrics.values['disp_P'] = 1
        else
            electrics.values['disp_P'] = 0
        end
        if gearA >= 0.15 and gearA < 0.3 then
            electrics.values['disp_R'] = 1
        else
            electrics.values['disp_R'] = 0
        end
        if gearA >= 0.3 and gearA < 0.51 then
            electrics.values['disp_N'] = 1
        else
            electrics.values['disp_N'] = 0
        end

        if gearA >= 0.51 and gearA < 0.68 then
            electrics.values['disp_D'] = 1
        else
            electrics.values['disp_D'] = 0
        end
        if gearA >= 0.68 and gearA < 0.90 then
            electrics.values['disp_2'] = 1
        else
            electrics.values['disp_2'] = 0
        end
        if gearA >= 0.90 then
            electrics.values['disp_1'] = 1
        else
            electrics.values['disp_1'] = 0
        end
    else
        -- Ignition is off, turn off all displays
        electrics.values['disp_P'] = 0
        electrics.values['disp_R'] = 0
        electrics.values['disp_N'] = 0
        electrics.values['disp_D'] = 0
        electrics.values['disp_1'] = 0
        electrics.values['disp_2'] = 0
    end
end

-- public interface
M.onInit      = onInit
M.reset     = reset
M.updateGFX = updateGFX

return M