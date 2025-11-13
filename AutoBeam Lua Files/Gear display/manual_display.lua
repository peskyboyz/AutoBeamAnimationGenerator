-- This Source Code Form is subject to the terms of the bCDDL, v. 1.1.
-- If a copy of the bCDDL was not distributed with this
-- file, You can obtain one at http://beamng.com/bCDDL-1.1.txt
-- Modified by peskyboyz - added ignition level check

local M = {}

local function onInit()
    -- Initialize numbered gear displays
    for i = 1, 10 do
        electrics.values['disp_' .. i] = 0
    end
end

local function reset()
    onInit()
end

local function updateGFX(dt)
    local gearIndex = electrics.values.gearIndex or 0
    local ignitionLevel = electrics.values.ignitionLevel or 0
        
    -- Only display gear indicators if ignition is on (> 0)
    if ignitionLevel > 0 then
		-- Use gearIndex for manual transmission gears
		-- Reverse Manual
		if gearIndex < -0.5 then
			electrics.values['disp_R'] = 1
		else
			electrics.values['disp_R'] = 0
		end
		
		-- Neutral
		if gearIndex >= -0.5 and gearIndex < 0.5 then
			electrics.values['disp_N'] = 1
		else
			electrics.values['disp_N'] = 0
		end
		
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
    else
        -- Ignition is off, turn off all displays
        electrics.values['disp_R'] = 0
        electrics.values['disp_N'] = 0
        
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