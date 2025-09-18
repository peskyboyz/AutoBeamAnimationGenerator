-- This Source Code Form is subject to the terms of the bCDDL, v. 1.1.
-- If a copy of the bCDDL was not distributed with this
-- file, You can obtain one at http://beamng.com/bCDDL-1.1.txt
local M = {}

local function onInit()
    electrics.values['clockh'] = 0
    electrics.values['clockmin'] = 0
end

local function reset()
    onInit()
end

local function updateGFX(dt)
    local time = os.date("*t", os.time())
    local hour = time.hour%12 + time.min/60
    electrics.values['clockh'] = hour*30
    electrics.values['clockmin'] = time.min*6
end

-- public interface
M.onInit    = onInit
M.onReset   = reset
M.updateGFX = updateGFX

return M