-- This Source Code Form is subject to the terms of the bCDDL, v. 1.1.
-- If a copy of the bCDDL was not distributed with this
-- file, You can obtain one at http://beamng.com/bCDDL-1.1.txt
-- Active Aerodynamics Controller - Spoiler affects downforce and drag
local M = {}

-- USER CONFIGURABLE VARIABLES
local DOWNFORCE_MULTIPLIER_LOW = 1.0      -- Downforce multiplier when spoiler is retracted (100%)
local DOWNFORCE_MULTIPLIER_NORMAL = 1.2   -- Downforce multiplier at normal position (120%)
local DOWNFORCE_MULTIPLIER_HIGH = 1.5     -- Downforce multiplier at high/airbrake position (150%)

local DRAG_MULTIPLIER_LOW = 1.0           -- Drag multiplier when spoiler is retracted (100%)
local DRAG_MULTIPLIER_NORMAL = 1.05       -- Drag multiplier at normal position (105%)
local DRAG_MULTIPLIER_HIGH = 1.3          -- Drag multiplier at high/airbrake position (130% - airbrake effect)

local REAR_BIAS = true                     -- If true, only affects rear downforce. If false, affects both front and rear
-- END USER CONFIGURABLE VARIABLES

local baseDownforceFront = 0
local baseDownforceRear = 0
local baseDragForward = 0
local isInitialized = false

local function onInit()
    isInitialized = false
    baseDownforceFront = 0
    baseDownforceRear = 0
    baseDragForward = 0
end

local function reset()
    onInit()
end

local function updateGFX(dt)
    -- Initialize base downforce values on first run
    if not isInitialized then
        -- Get the base downforce factors from the vehicle data
        if v.data and v.data.downforceModel then
            local targetSpeedKMH = v.data.downforceModel.targetSpeedKMH or 200
            local targetAirDensity = v.data.downforceModel.targetAirDensity or 1.2041
            local dfFrontN = v.data.downforceModel.downforceTargetFront or 0
            local dfRearN = v.data.downforceModel.downforceTargetRear or 0
            
            -- Calculate base factors using the same formula as cefaero
            if v.getFactorForDownforceAtKMH then
                baseDownforceFront = v.getFactorForDownforceAtKMH(dfFrontN, targetSpeedKMH, targetAirDensity)
                baseDownforceRear = v.getFactorForDownforceAtKMH(dfRearN, targetSpeedKMH, targetAirDensity)
                isInitialized = true
                print("Active Aero Initialized:")
                print("  Base front factor: " .. baseDownforceFront)
                print("  Base rear factor: " .. baseDownforceRear)
            end
        end
        
        -- Store the base drag force for reference
        if v.getDragForce then
            local dragForces = v.getDragForce()
            if dragForces and dragForces[2] then
                baseDragForward = dragForces[2]
            end
        end
    end
    
    if not isInitialized then
        return
    end
    
    -- Get current spoiler position (0-1)
    local spoilerPosition = electrics.values.spoiler_active or 0
    
    -- Calculate downforce multiplier based on spoiler position
    local downforceMultiplier = DOWNFORCE_MULTIPLIER_LOW
    local dragMultiplier = DRAG_MULTIPLIER_LOW
    
    if spoilerPosition >= 0.9 then
        -- High position (airbrake)
        downforceMultiplier = DOWNFORCE_MULTIPLIER_HIGH
        dragMultiplier = DRAG_MULTIPLIER_HIGH
    elseif spoilerPosition >= 0.4 then
        -- Normal position - interpolate between normal and high
        local normalToHighRange = (spoilerPosition - 0.4) / 0.5
        downforceMultiplier = DOWNFORCE_MULTIPLIER_NORMAL + (DOWNFORCE_MULTIPLIER_HIGH - DOWNFORCE_MULTIPLIER_NORMAL) * normalToHighRange
        dragMultiplier = DRAG_MULTIPLIER_NORMAL + (DRAG_MULTIPLIER_HIGH - DRAG_MULTIPLIER_NORMAL) * normalToHighRange
    else
        -- Low to normal - interpolate
        local lowToNormalRange = spoilerPosition / 0.4
        downforceMultiplier = DOWNFORCE_MULTIPLIER_LOW + (DOWNFORCE_MULTIPLIER_NORMAL - DOWNFORCE_MULTIPLIER_LOW) * lowToNormalRange
        dragMultiplier = DRAG_MULTIPLIER_LOW + (DRAG_MULTIPLIER_NORMAL - DRAG_MULTIPLIER_LOW) * lowToNormalRange
    end
    
    -- Apply the downforce multiplier
    if v.setDownforceFactorFront and v.setDownforceFactorRear then
        if REAR_BIAS then
            -- Only affect rear downforce
            v.setDownforceFactorFront(baseDownforceFront)
            v.setDownforceFactorRear(baseDownforceRear * downforceMultiplier)
        else
            -- Affect both front and rear
            v.setDownforceFactorFront(baseDownforceFront * downforceMultiplier)
            v.setDownforceFactorRear(baseDownforceRear * downforceMultiplier)
        end
    end
    
    -- Apply drag multiplier by modifying the vehicle's drag area
    -- This approach multiplies the effective frontal area which affects drag calculation
    if v.getVehicleArea and v.setVehicleArea then
        local baseArea = v.data.dragModel and v.data.dragModel.vehicleArea or v.getVehicleArea()
        v.setVehicleArea(baseArea * dragMultiplier)
    end
end

-- public interface
M.onInit    = onInit
M.reset     = reset
M.updateGFX = updateGFX

return M