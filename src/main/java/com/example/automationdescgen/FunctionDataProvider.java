package com.example.automationdescgen;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.stream.Collectors;

import java.util.List;

public class FunctionDataProvider {
    public static final String ALL_CATEGORIES = "All";
    public static ObservableList<String> getCategories() {
        ObservableList<Function> functions = getFunctions();
        List<String> categories = functions.stream()
                .map(Function::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        return FXCollections.observableArrayList(categories);
    }

    public static ObservableList<Function> getFunctionsByCategory(String category) {
        ObservableList<Function> allFunctions = getFunctions();
        if (ALL_CATEGORIES.equals(category)) {
            return allFunctions;
        }
        List<Function> filteredFunctions = allFunctions.stream()
                .filter(function -> category.equals(function.getCategory()))
                .collect(Collectors.toList());
        return FXCollections.observableArrayList(filteredFunctions);
    }
    public static ObservableList<Function> getFunctions() {
        return FXCollections.observableArrayList(
                /*
                 Special value of 0 - Enable all options with link offset not selected
                 Special value of 1 - Enable all options with link offset selected
                 Special value of 2 - Disable value spinners
                 Special value of 3 - Disable all options
                 Special value of 4 - Disable value spinners and translations

                 List of categories:
                 - Control Inputs
                 - Gauges
                 - Shifters
                 - Lighting
                 - Gear Display
                 - Engine/Mechanical
                 - Special/Utility
                 */
                new Function(
                        "Steering (Wheel)",
                        "steering",
                        900.0,
                        -900.0,
                        900.0,
                        0.0,
                        -1000,
                        1000,
                        0,
                        false,
                        1,
                        List.of("degrees"),
                        """
                                - Enter your prop ID for the moving part of the steering wheel. (With several steering wheels in which the base and the wheel are one fixture, you will have to duplicate the fixture, set the wheel of one to be invisible via the material, and set the other ones base to be invisible. This second one will be the moving fixture while the first one will remain still)
                                - IMPORTANT: The steering rotation is always clockwise if the green arrow is pointing from the steering column to the face of the driver. The option has been left enabled to allow other uses.
                                - Enter a range of rotation; It is recommended to leave this set to 900° which means 450° each way. Changing the value here will change the ratio between the physical prop and BeamNG's steering wheel, providing the appearance of the correct rotation for the steering wheel.)
                                - The default steering rotation in BeamNG is 900°. If you desire this can be changed by selecting the "Advanced Tuning Parameters" in BeamNG's "Vehicle Config" and changing it under "Tuning".
                                - The Min Value, the Max Value, and the Offset are locked as a change will have no effect or result in errors for the animation.""",
                        1,
                        2,
                        "Control Inputs"

                ),
                new Function(
                        "RPM",
                        "rpmTacho",
                        270,
                        0,
                        9000,
                        0,
                        -100,
                        20000,
                        20000,
                        false,
                        4,
                        List.of("RPM"),
                        """
                                - Smoothed engine speed value for use with tachometers
                                - It is recommended that the Min Value is left at 0. If the tachometer starts higher than 0, set it here.
                                - The Max Value should be the highest RPM that is available at the end of the tachometer not the startMax RPM's of the engine.
                                - The Offset should be left alone unless the Min has been raised above 0, and the needle needs to drop back to the start position. In this case the Offset should be equal and opposite of the Min""",
                        0,
                        1,
                        "Gauges"
                ),
                new Function(
                        "Wheel Speed",
                        "wheelspeed",
                        270,
                        0,
                        200,
                        0,
                        0,
                        1000,
                        1000,
                        false,
                        3,
                        List.of("KPH", "MPH"),
                        """
                                - Speed of the car measured at the wheels.
                                - It is recommended that the Min Value is left at 0. If the speedometer starts higher than 0, set it here.
                                - The Max Value should be the highest speed that is available at the end of the speedometer not the max speed of the car.
                                - The Offset should be left alone unless the Min has been increased from 0. If the Min has been changed, set the opposite value here (Ex: Min set to 20; offset should be set to -20.""",
                        0,
                        1,
                        "Gauges"
                ),
                new Function(
                        "Coolant Temp",
                        "watertemp",
                        110,
                        60,
                        120,
                        -60,
                        -1000,
                        1000,
                        80,
                        false,
                        2,
                        List.of("°C", "°F"),
                        """
                                - Temperature of the radiator water (used as the main engine temp on water cooled cars).
                                - The Min Value is set to the lowest value that should be shown on the gauge (IMPORTANT: This value depends on the lowest you want to show. Most cars cannot show values from freezing)
                                - The Max Value should be the highest value that should be shown on the gauge (IMPORTANT: This value depends on the highest you want to show. Most cars do not show values past overheating. In BeamNG this occurs at 120°C)
                                - The Offset should be the opposite value of the Min to compensate and move the starting point of the dial to the Min""",
                        0,
                        1,
                        "Gauges"
                ),
                new Function(
                        "Oil Temp",
                        "oiltemp",
                        110,
                        80,
                        120,
                        -80,
                        -1000,
                        1000,
                        80,
                        false,
                        2,
                        List.of("°C", "°F"),
                        """
                                - Temperature of the engine oil (used as the main engine temp on air cooled cars).
                                - The Min Value is set to the lowest value that should be shown on the gauge (IMPORTANT: This value depends on the lowest you want to show. Most cars cannot show values from freezing)
                                - The Max Value should be the highest value that should be shown on the gauge (IMPORTANT: This value depends on the highest you want to show. Most cars do not show values past overheating. In BeamNG this occurs at over 140°C)
                                - Note that the normal operating temperature for the oil is 100°C so it is recommended to have 100 be the center of the gauge.
                                - The Offset should be the opposite value of the Min to compensate and move the starting point of the dial to the Min""",
                        0,
                        1,
                        "Gauges"
                ),
                new Function(
                        "Fuel",
                        "fuel",
                        110,
                        0,
                        1,
                        0,
                        0,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - Percentage of fuel left in the tank.
                                - The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
                                """,
                        0,
                        2,
                        "Gauges"
                ),
                new Function(
                        "Boost",
                        "turboBoost",
                        110,
                        -15,
                        30,
                        15,
                        -20,
                        100,
                        50,
                        true,
                        4,
                        List.of("psi", "bar", "kPa"),
                        """
                                - Select the appropriate unit for boost (bar, psi, kPa)
                                - The Min Value is set to the lowest value that can be shown on the gauge
                                - The Max Value should be the highest value that can be shown on the gauge
                                - The Offset should be the opposite value of the Min to compensate and move the starting point of the dial to 0""",
                        0,
                        1,
                        "Gauges"
                ),
                new Function(
                        "Engine Load",
                        "engineLoad",
                        110,
                        0,
                        1,
                        0,
                        0,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - Engine load percentage,based on current torque compared to the maximum amount of torque the engine can produce.
                                - NOTE: Doesn't seem to work as described in documentation. Seems to show load percentage,based on current torque compared to the maximum amount of torque the engine can produce AT THE CURRENT RPM.
                                This means that if you are at 30% throttle a gauge will show 30% of max load.
                                - The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
                                """,
                        0,
                        2,
                        "Gauges"
                ),
                new Function(
                        "Radiator Fan",
                        "radiatorFanSpin",
                        360,
                        0,
                        360,
                        0,
                        0,
                        360,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - Spin function for the radiator fan,which activates when the radiator fan turns on. Used for props.
                                - Note that this only turns on when coolant reaches 95°C
                                - The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
                                - Recommended to leave the range of rotation to 360°.
                                """,
                        1,
                        2,
                        "Engine/Mechanical"
                ),
                new Function(
                        "RPM Spin (pulleys)",
                        "rpmspin",
                        720,
                        -360,
                        360,
                        0,
                        -360,
                        360,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - Spin function based on engine RPM. Used for spinning pulley props or crank driven fans.
                                - For rpmspin, the prop will move the amount stated in the [Max] field (with the opposite in [Min] as the engine rotates, then will reset to the starting position.
                                - Therefore, if you enter 180 instead of the default 360, the prop will rotate 180° at the same rate as the engine, then stop and wait for the engine to complete its revolution before resetting to the starting point.
                                The value set for the rotation controls both the speed of rotation and the max angle of movement before it resets.
                                - For example if the value is set to 0.5, instead of the default 1, the prop will rotate at half the normal speed.
                                However, since the position resets once the engine completes a revolution, the prop will only have time to rotate 180° in the time allotted. If it was set to 0.25, the prop would only rotate 90° before reset.
                                - Note that this doesn't work for exhausts""",
                        0,
                        2,
                        "Engine/Mechanical"
                ),
                new Function(
                        "Parking Brake",
                        "parkingbrake_input",
                        30,
                        0,
                        1,
                        0,
                        0,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - Parking brake input
                                - The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
                                """,
                        0,
                        2,
                        "Control Inputs"
                ),
                new Function(
                        "Throttle",
                        "throttle_input",
                        30,
                        0,
                        1,
                        0,
                        0,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - Raw throttle input percentage. NOT affected by things like rev matching and throttle cut in the transmission shift logic.
                                - Used for pedals.
                                - The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
                                """,
                        0,
                        2,
                        "Control Inputs"
                ),
                new Function(
                        "Brake",
                        "brake_input",
                        30,
                        0,
                        1,
                        0,
                        0,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - Brake input percentage.
                                - Used for pedals.
                                - The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
                                """,
                        0,
                        2,
                        "Control Inputs"
                ),
                new Function(
                        "Clutch",
                        "clutch_input",
                        30,
                        0,
                        1,
                        0,
                        0,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - Clutch input percentage.
                                - Used for pedals.
                                - The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
                                """,
                        0,
                        2,
                        "Control Inputs"
                ),
                new Function(
                        "Throttle Output",
                        "throttle",
                        30,
                        0,
                        1,
                        0,
                        0,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - Throttle output percentage. Affected by things like rev matching and throttle cut in the transmission shift logic.
                                - Can be used for throttle body plate.
                                - The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
                                """,
                        0,
                        2,
                        "Control Inputs"
                ),
                new Function(
                        "Ignition (On/Off+Starter)",
                        "ignitionLevel",
                        30,
                        0,
                        3,
                        0,
                        0,
                        3,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - Ignition state.
                                - 0 = ignition off, 1 = accessory only, 2 = ignition on (engine running or not), 3 = starter running.
                                - The Min Value, the Max Value, and the Offset are unlocked to allow specialty uses like 
                                showing/hiding a screen when the car is at least in accessory (min = 0, max = 1, offset = 0), 
                                or creating a push start button (min = 2, max = 3, offset = -2).
                                - If you want a normal key with all the positions, leave the min, max, offset alone.
                                """,
                        0,
                        1,
                        "Control Inputs"
                ),
                new Function(
                        "Ignition (On/Off)",
                        "ignition",
                        30,
                        0,
                        1,
                        0,
                        0,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - Ignition state (on or off). 0 = ignition off, 1 = ignition on (engine running or not)
                                - The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
                                """,
                        0,
                        2,
                        "Control Inputs"
                ),
                new Function(
                        "Automatic Shifter",
                        "gear_A",
                        30,
                        0,
                        1,
                        0,
                        0,
                        15,
                        0,
                        false,
                        2,
                        List.of("Gears"),
                        """
                                - For creating Automatic gearbox shifters and dash gear indicators
                                - Default position is park; Auto goes P, R, N, D, 2, 1
                                - The Min will be 0 which is Park, and the max will be 1.
                                - Enter the range of movement as the entire movement range
                                """,
                        0,
                        2,
                        "Shifters"
                ),
                new Function(
                        "Manual Shifter",
                        "gearIndex",
                        360,
                        -1,
                        1,
                        0,
                        -1,
                        11,
                        10,
                        false,
                        2,
                        List.of("Gear Positions"),
                        """
                                - For creating manual gearbox shifters ie: H-pattern
                                - IMPORTANT: READ THROUGH THE README OR THE ENTIRE EXPLANATION FOR THIS ANIMATION BY CLICKING THE HELP BUTTON. REFERENCE SCREENSHOT AVAILABLE IN README.
                                - This tool will only create one line of the gearbox per use. Each gear follows a pattern where Neutral has a min and max of -1 and 1, First has a min and max of 0 and 2, Second is 1 and 3 but with a offset of -1, Third, is 2 and 4 with and offset of -2 and so on going up. Reverse must have a min and max of -1 and 0 with an offset of 0.
                                - Example for floor shifters
                                `~prop:0,gearIndex,0,180,0,0,0,0,-1,1,0,1~` Neutral
                                `~prop:1,gearIndex,0,180,0,0,0,0,0,2,0,1~`  1st gear
                                `~prop:2,gearIndex,0,180,0,0,0,0,1,3,-1,1~` 2nd gear
                                `~prop:3,gearIndex,0,180,0,0,0,0,2,4,-2,1~` 3rd gear
                                `~prop:4,gearIndex,0,180,0,0,0,0,3,5,-3,1~` 4th gear
                                `~prop:5,gearIndex,0,180,0,0,0,0,4,6,-4,1~` 5th gear
                                `~prop:6,gearIndex,0,180,0,0,0,0,5,7,-5,1~` 6th gear
                                `~prop:7,gearIndex,0,180,0,0,0,0,6,8,-6,1~` 7th gear
                                `~prop:8,gearIndex,0,180,0,0,0,0,7,9,-7,1~` 8th gear
                                `~prop:9,gearIndex,0,180,0,0,0,0,-1,0,0,1~` Reverse gear
                                - Note that for this to work, you will need to have a separate gear stick for each gear plus neutral.
                                - FOR FLOOR SHIFTERS: Set every copy of the shifter to its final position (Neutral shifter to center, 1st gear shifter to 1st gear location, etc...), then, EXCEPT FOR NEUTRAL, rotate them 180° from the final position, so they are pointing down and are hidden under the center console. As you move through the gears, each shifter will rotate up to its set position then drop down to be replaced by the neutral shifter, before moving the commanded gear's shifter.
                                - FOR COLUMN SHIFTERS: Similar to floor shifters, set each to their final position, then move all but neutral linearly behind the dash. Will be tricky to do, but it must be hidden both front and backwards after moving the same distance from final position. For example in the seat and dash.
                                - IMPORTANT - For each gear that has a difference of 2 between the [Min] and [Max] (everyone but reverse and final gear) ensure that 360° is entered for range of rotation because the gear must move 180° to its active position then another 180° to be hidden again. Or for column move a set amount into position and the same amount to be hidden again.
                                """,
                        0,
                        0,
                        "Shifters"
                ),
                new Function(
                        "Mode Shifter (Adv Auto/DCT)",
                        "gearModeIndex",
                        180,
                        -1,
                        5,
                        -1,
                        -1,
                        5,
                        1,
                        true,
                        2,
                        List.of("Unit"),
                        """
                                - For creating DCT gearbox shifters and dash gear indicators for a DCT transmission
                                - Default position is park; Auto portion goes P, R, N, D, S
                                """,
                        0,
                        2,
                        "Shifters"
                ),
                new Function(
                        "Mode Shifter (Auto Manual)",
                        "gearModeIndex",
                        180,
                        -1,
                        4,
                        -1,
                        -1,
                        4,
                        1,
                        true,
                        2,
                        List.of("Unit"),
                        """
                                - For creating Auto Manual gearbox shifters and dash gear indicators
                                - Default position is reverse; Auto portion goes R, N, D, S
                                """,
                        0,
                        2,
                        "Shifters"
                ),
                new Function(
                        "CVT Shifter",
                        "gearModeIndex",
                        180,
                        -1,
                        4,
                        -1,
                        -1,
                        4,
                        1,
                        true,
                        2,
                        List.of("Unit"),
                        """
                                - For creating CVT shifters and dash gear indicators
                                - Default position is park; Pattern goes P, R, N, D
                                """,
                        0,
                        2,
                        "Shifters"
                ),
                new Function(
                        "Sequential Shifter",
                        "sequentialLeverY",
                        30,
                        -1,
                        1,
                        0,
                        -1,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - For creating sequential gearbox shifters. Will work for lever shifters. May work for paddle shifters if they don't turn with some messing around.
                                - IMPORTANT NOTE: As of September 11th, 2025 this function will not work without a minor change to the jbeam.
                                - You must add the following to the controller in the file \\car_file_name\\vehicles\\car_name\\car_id\\car_id_main.jbeam from your export, where the car name will be what you exported from Automation and the car_id will be a set of numbers and letters like 8d8a3.
                                                                
                                ["propAnimation/sequentialLever", {"name":"sequentialLever"}], (line to be added) 
                                
                                Which goes here:
                                		
                                	"controller": [
                                	        ["fileName"],
                                	        ["vehicleController", {}],
                                	        ["cefaero"],
                                		["propAnimation/sequentialLever", {"name":"sequentialLever"}],
                                	    ],
                                - You can also add this just below the controller to add sound effects when shifting.
                                
                                    "sequentialLever": {
                                            "shiftSoundNode:":["int_shft"],
                                            "shiftSoundEventSequentialGearUp": "event:>Vehicle>Interior>Gearshift>sequential_03_out",
                                            "shiftSoundEventSequentialGearDown": "event:>Vehicle>Interior>Gearshift>sequential_03_in",
                                            "shiftSoundVolumeSequentialGearUp": 0.5,
                                            "shiftSoundVolumeSequentialGearDown": 0.5,
                                            }
                                """,
                        0,
                        0,
                        "Shifters"
                ),
                new Function(
                        "Turn Signal (2 way)",
                        "turnsignal",
                        30,
                        -1,
                        1,
                        0,
                        -1,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - Used for turn signal stalk
                                - Varies depending on which turn signal is selected (right is 1, left is -1)
                                - The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
                                - Set the prop to the center (idle) position.
                                - Select the rotation direction that will move it to the "active turning right" position
                                - Choose a range of rotation that will move it from center to either active position
                                """,
                        0,
                        2,
                        "Control Inputs"
                ),
                new Function(
                        "Hazards Switch",
                        "hazard_enabled",
                        30,
                        0,
                        1,
                        0,
                        0,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - Indicates the hazards have been turned on
                                - The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
                                """,
                        0,
                        2,
                        "Control Inputs"
                ),
                new Function(
                        "Hazards Flashing",
                        "hazard",
                        30,
                        0,
                        1,
                        0,
                        0,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - Flashing function when hazards are on.
                                - The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
                                """,
                        0,
                        2,
                        "Lighting"

                ),
                new Function(
                        "Headlights (off/on)",
                        "lowhighbeam",
                        30,
                        0,
                        1,
                        0,
                        0,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - Activates if either the low or high beams are turned on.
                                - Only 2 setting positions (off/on)
                                - Useful for pop-up headlights or hidden headlight covers
                                - The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
                                """,
                        0,
                        2,
                        "Lighting"

                ),
                new Function(
                        "Headlights (3 way)",
                        "lights",
                        30,
                        0,
                        2,
                        0,
                        0,
                        2,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - Index of the current light mode (0 is off, 1 is lowbeams, 2 is highbeams)
                                - Useful for light switches that have 3 positions (off, low, high)
                                - The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
                                - Use a range that will encompass the whole range of motion from off to highbeams
                                """,
                        0,
                        2,
                        "Lighting"
                ),
                new Function(
                        "Fog Lights",
                        "fog",
                        30,
                        0,
                        1,
                        0,
                        0,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - Indicates the fog lights are turned on.
                                - Can be used for special function since fog light cannot currently export
                                - The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
                                """,
                        0,
                        2,
                        "Lighting"
                ),
                new Function(
                        "Altitude",
                        "altitude",
                        30,
                        0,
                        100,
                        0,
                        -500,
                        5000,
                        5000,
                        false,
                        5,
                        List.of("Meters", "Feet"),
                        """
                                - Altitude of the vehicle
                                - Set [Min] to the lowest value on the gauge; Set [Max] to the highest value on the gauge; Set [Offset] to the opposite value of the [Min] to bring the start to 0;
                                - Set the needle to the lowest position on the gauge.
                                """,
                        0,
                        1,
                        "Gauges"
                ),
                new Function(
                        "Air Speed",
                        "airspeed",
                        30,
                        0,
                        200,
                        0,
                        0,
                        1000,
                        1000,
                        false,
                        3,
                        List.of("KPH", "MPH"),
                        """
                                - Speed of the vehicle compared to the world.
                                - It is recommended that the Min Value is left at 0. If the speedometer starts higher than 0, set it here.
                                - The Max Value should be the highest speed that is available at the end of the speedometer not the max speed of the car.
                                - The Offset should be left alone unless the Min has been increased from 0. If the Min has been changed, set the opposite value here (Ex: Min set to 20; offset should be set to -20.
                                """,
                        0,
                        1,
                        "Gauges"
                ),
                new Function(
                        "Air Speed + Wind",
                        "airflowspeed",
                        30,
                        0,
                        200,
                        0,
                        0,
                        1000,
                        1000,
                        false,
                        3,
                        List.of("KPH", "MPH"),
                        """
                                - Speed of the vehicle compared to the airflow, taking into account wind.
                                - It is recommended that the Min Value is left at 0. If the speedometer starts higher than 0, set it here.
                                - The Max Value should be the highest speed that is available at the end of the speedometer not the max speed of the car.
                                - The Offset should be left alone unless the Min has been increased from 0. If the Min has been changed, set the opposite value here (Ex: Min set to 20; offset should be set to -20.""\",
                                """,
                        0,
                        1,
                        "Gauges"
                ),
                new Function(
                        "Steering (Percentage)",
                        "steering_input",
                        10,
                        -1,
                        1,
                        0,
                        -1,
                        1,
                        1,
                        true,
                        2,
                        List.of("Unit"),
                        """
                                - Steering input percentage.
                                """,
                        0,
                        1,
                        "Control Inputs"
                ),
                new Function(
                        "Clock Hour",
                        "clockh",
                        10,
                        0,
                        360,
                        -360,
                        0,
                        360,
                        360,
                        true,
                        2,
                        List.of("Unit"),
                        """
                                - Clock hour position
                                - IMPORTANT NOTE: This function relies on you coping a lua file to your exported car. 
                                - Place the clock.lua from BeamNG into the folder \\car_file_name\\vehicles\\car_name\\lua
                                """,
                        0,
                        2,
                        "Special/Utility"
                ),
                new Function(
                        "Clock Minute",
                        "clockmin",
                        360,
                        0,
                        360,
                        -360,
                        0,
                        360,
                        360,
                        true,
                        2,
                        List.of("Unit"),
                        """
                                - Clock minute position
                                - IMPORTANT NOTE: This function relies on you coping a lua file to your exported car. 
                                - Place the clock.lua from BeamNG into the folder \\car_file_name\\vehicles\\car_name\\lua
                                """,
                        0,
                        2,
                        "Special/Utility"
                ),
                new Function(
                        "Blank",
                        "dummy",
                        360,
                        0,
                        1,
                        0,
                        -10000,
                        20000,
                        2000,
                        true,
                        3,
                        List.of("Default", "Meters", "Feet", "KPH", "MPH", "psi", "bar", "kPa", "°C", "°F"),
                        """
                                - A blank function for use where one of the others is too restricted
                                - Be sure to replace the "dummy" with the desired function type
                                """,
                        0,
                        1,
                        "Special/Utility"
                ),
                new Function(
                        "Display Park (Auto)",
                        "disp_P",
                        10,
                        0,
                        1,
                        0,
                        -1,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - For displaying park gear indicator with an automatic transmission
                                """,
                        0,
                        2,
                        "Gear Display"
                ),
                new Function(
                        "Display Reverse (Manual)",
                        "disp_R",
                        10,
                        0,
                        1,
                        0,
                        -1,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - For displaying reverse gear indicator with a manual transmission
                                """,
                        0,
                        2,
                        "Gear Display"
                ),
                new Function(
                        "Display Reverse (Auto)",
                        "disp_Ra",
                        10,
                        0,
                        1,
                        0,
                        -1,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - For displaying reverse gear indicator with an automatic transmission
                                """,
                        0,
                        2,
                        "Gear Display"
                ),
                new Function(
                        "Display Neutral (Manual)",
                        "disp_N",
                        10,
                        0,
                        1,
                        0,
                        -1,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - For displaying neutral gear indicator with a manual transmission
                                """,
                        0,
                        2,
                        "Gear Display"
                ),
                new Function(
                        "Display Neutral (Auto)",
                        "disp_Na",
                        10,
                        0,
                        1,
                        0,
                        -1,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - For displaying neutral gear indicator with a automatic transmission
                                """,
                        0,
                        2,
                        "Gear Display"
                ),
                new Function(
                        "Display Drive (Auto)",
                        "disp_D",
                        10,
                        0,
                        1,
                        0,
                        -1,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - For displaying drive gear indicator with a automatic transmission
                                """,
                        0,
                        2,
                        "Gear Display"
                ),
                new Function(
                        "Display Sport (Auto)",
                        "disp_S",
                        10,
                        0,
                        1,
                        0,
                        -1,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - For displaying Sport gear indicator with a automatic transmission
                                """,
                        0,
                        2,
                        "Gear Display"
                ),
                new Function(
                        "Display First",
                        "disp_1",
                        10,
                        0,
                        1,
                        0,
                        -1,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - For displaying first gear indicator
                                """,
                        0,
                        2,
                        "Gear Display"
                ),
                new Function(
                        "Display Second",
                        "disp_2",
                        10,
                        0,
                        1,
                        0,
                        -1,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - For displaying second gear indicator
                                """,
                        0,
                        2,
                        "Gear Display"
                ),
                new Function(
                        "Display Third",
                        "disp_3",
                        10,
                        0,
                        1,
                        0,
                        -1,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - For displaying third gear indicator
                                """,
                        0,
                        2,
                        "Gear Display"
                ),
                new Function(
                        "Display Fourth",
                        "disp_4",
                        10,
                        0,
                        1,
                        0,
                        -1,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - For displaying fourth gear indicator
                                """,
                        0,
                        2,
                        "Gear Display"
                ),
                new Function(
                        "Display Fifth",
                        "disp_5",
                        10,
                        0,
                        1,
                        0,
                        -1,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - For displaying fifth gear indicator
                                """,
                        0,
                        2,
                        "Gear Display"
                ),
                new Function(
                        "Display Sixth",
                        "disp_6",
                        10,
                        0,
                        1,
                        0,
                        -1,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - For displaying sixth gear indicator
                                """,
                        0,
                        2,
                        "Gear Display"
                ),
                new Function(
                        "Display Seventh",
                        "disp_7",
                        10,
                        0,
                        1,
                        0,
                        -1,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - For displaying seventh gear indicator
                                """,
                        0,
                        2,
                        "Gear Display"
                ),
                new Function(
                        "Display Eighth",
                        "disp_8",
                        10,
                        0,
                        1,
                        0,
                        -1,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - For displaying eighth gear indicator
                                """,
                        0,
                        2,
                        "Gear Display"
                ),
                new Function(
                        "Display Ninth",
                        "disp_9",
                        10,
                        0,
                        1,
                        0,
                        -1,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - For displaying ninth gear indicator
                                """,
                        0,
                        2,
                        "Gear Display"
                ),
                new Function(
                        "Display Tenth",
                        "disp_10",
                        10,
                        0,
                        1,
                        0,
                        -1,
                        1,
                        0,
                        false,
                        2,
                        List.of("Unit"),
                        """
                                - For displaying tenth gear indicator
                                """,
                        0,
                        2,
                        "Gear Display"
                )
        );
    }
}
