-- This Source Code Form is subject to the terms of the bCDDL, v. 1.1.
-- If a copy of the bCDDL was not distributed with this
-- file, You can obtain one at http://beamng.com/bCDDL-1.1.txt
-- Clock with ignition control (for modern electric clocks)
local M = {}

local function onInit()
    electrics.values['clockh_elec'] = 0
    electrics.values['clockmin_elec'] = 0
end

local function reset()
    onInit()
end

local function updateGFX(dt)
    local ignitionLevel = electrics.values.ignitionLevel or 0
    
    -- Only update clock if ignition is on
    if ignitionLevel > 0 then
        local time = os.date("*t", os.time())
        local hour = time.hour % 12 + time.min / 60
        electrics.values['clockh_elec'] = hour * 30
        electrics.values['clockmin_elec'] = time.min * 6
    end
    -- When ignition is off, keep the last values (hands stay frozen)
end

-- public interface
M.onInit    = onInit
M.onReset   = reset
M.updateGFX = updateGFX

return M