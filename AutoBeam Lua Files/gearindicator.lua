-- This Source Code Form is subject to the terms of the bCDDL, v. 1.1.
-- If a copy of the bCDDL was not distributed with this
-- file, You can obtain one at http://beamng.com/bCDDL-1.1.txt
local M = {}

local gearA = 0

local function onInit()
    electrics.values['auto_p'] = 0
    electrics.values['auto_r'] = 0
    electrics.values['auto_n'] = 0
    electrics.values['auto_d'] = 0
    electrics.values['auto_1'] = 0
    electrics.values['auto_2'] = 0
end

local function reset()
    onInit()
end

local function updateGFX(dt)
    gearA = electrics.values['gear_A'] or 0
    if gearA < 0.15 then
        electrics.values['auto_p'] = 1
    else
        electrics.values['auto_p'] = 0
    end
    if gearA >= 0.15 and gearA < 0.3 then
        electrics.values['auto_r'] = 1
    else
        electrics.values['auto_r'] = 0
    end
    if gearA >= 0.3 and gearA < 0.51 then
        electrics.values['auto_n'] = 1
    else
        electrics.values['auto_n'] = 0
    end

    if gearA >= 0.51 and gearA < 0.68 then
        electrics.values['auto_d'] = 1
    else
        electrics.values['auto_d'] = 0
    end
    if gearA >= 0.68 and gearA < 0.90 then
        electrics.values['auto_2'] = 1
    else
        electrics.values['auto_2'] = 0
    end
    if gearA >= 0.90 then
        electrics.values['auto_1'] = 1
    else
        electrics.values['auto_1'] = 0
    end
end

-- public interface
M.onInit      = onInit
M.reset     = reset
M.updateGFX = updateGFX

return M