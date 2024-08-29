package com.example.automationdescgen;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class FunctionDataProvider {
    public static ObservableList<Function> getFunctions() {
        return FXCollections.observableArrayList(
                /*
                 Special value of 0 - Enable all options
                 Special value of 1 - Disable all options except rotation
                 Special value of 2 - Disable value spinners
                 Special value of 3 - Disable all options
                 */
                new Function(
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
                                - IMPORTANT: The steering rotation is always counter clockwise if the green arrow is pointing from the steering column to the face of the driver. The option has been left enabled to allow other uses.
                                - Enter a range of rotation; It is recommended to leave this set to 900° which means 450° each way. Changing the value here will change the ratio between the physical prop and BeamNG's steering wheel, providing the appearance of the correct rotation for the steering wheel.)
                                - The default steering rotation in BeamNG is 900°. If you desire this can be changed by selecting the "Advanced Tuning Parameters" in BeamNG's "Vehicle Config" and changing it under "Tuning".
                                - The Min Value, the Max Value, and the Offset are locked as a change will have no effect or result in errors for the animation.""",
                        1,
                        2

                ),
                new Function(
                        "rpmTacho",
                        270,
                        0,
                        9000,
                        0,
                        -100,
                        20000,
                        3000,
                        false,
                        4,
                        List.of("RPM"),
                        """
                                - Smoothed engine speed value for use with tachometers
                                - It is recommended that the Min Value is left at 0. If the tachometer starts higher than 0, set it here.
                                - The Max Value should be the highest RPM that is available at the end of the tachometer not the startMax RPM's of the engine.
                                - The Offset should be left alone unless the Min has been raised above 0, and the needle needs to drop back to the start position. In this case the Offset should be equal and opposite of the Min""",
                        0,
                        0
                ),
                new Function(
                        "wheelspeed",
                        270,
                        0,
                        200,
                        0,
                        0,
                        1000,
                        100,
                        false,
                        2,
                        List.of("KPH", "MPH"),
                        """
                                - Speed of the car measured at the wheels.
                                - It is recommended that the Min Value is left at 0. If the speedometer starts higher than 0, set it here.
                                - The Max Value should be the highest RPM that is available at the end of the tachometer not the startMax RPM's of the engine.
                                - The Offset should be left alone unless the Min has been increased from 0. If the Min has been changed, set the opposite value here (Ex: Min set to 20; offset should be set to -20.""",
                        0,
                        0
                ),
                new Function(
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
                        0
                ),
                new Function(
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
                        0
                ),
                new Function(
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
                        2
                ),
                new Function(
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
                        0
                ),
                new Function(
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
                        2
                ),
                new Function(
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
                                - Note that this only turns on when coolant reaches 105°C
                                - The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
                                - Recommended to leave the range of rotation to 360°.
                                """,
                        1,
                        2
                ),
                new Function(
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
                                """,
                        0,
                        2
                ),
                new Function(
                        "parkingbrake",
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
                        2
                ),
                new Function(
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
                                - Throttle percentage. Affected by things like rev matching and throttle cut in the transmission shift logic.
                                - The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
                                """,
                        0,
                        2
                ),
                new Function(
                        "brake",
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
                                - The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
                                """,
                        0,
                        2
                ),
                new Function(
                        "clutch",
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
                                - The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
                                """,
                        0,
                        2
                ),
                new Function(
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
                        2
                ),
                new Function(
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
                        2
                ),
                new Function(
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
                        2
                ),
                new Function(
                        "gear_A",
                        30,
                        0,
                        5,
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
                                - Set the Min to 0 which will be Park, then set the max to the amount of gears after P. For example with a typical "P, R, N, D, 2, 1" there are 5 gears after park.
                                - This sort of works for Auto with manual. It will move through the auto portion, then will also move for the Sport 'gear' and the manual gears following resulting in very small steps or very long movement.
                                - Note that the range of rotation is not divided by the number of available positions. This is intended and necessary for the gear_A function only.
                                """,
                        0,
                        0
                ),
                new Function(
                        "gearIndex",
                        180,
                        -1,
                        1,
                        0,
                        -1,
                        11,
                        0,
                        false,
                        2,
                        List.of("Gear Positions"),
                        """
                                - For creating manual gearbox shifters ie: H-pattern
                                - IMPORTANT: READ THROUGH THE README OR THE ENTIRE EXPLANATION FOR THIS ANIMATION BY CLICKING THE HELP BUTTON. REFERENCE SCREENSHOT AVAILABLE IN README.
                                - This tool will only create one line of the gearbox per use. Each gear follows a pattern where Neutral has a min and max of -1 and 1,
                                First has a min and max of 0 and 2, Second is 1 and 3 but with a offset of -1, Third, is 2 and 4 with and offset of -2 and so on going up.
                                Reverse must have a min and max of -1 and 0 with an offset of 0.
                                `~prop:9,gearIndex,0,180,0,0,0,0,-1,1,0,1~` Neutral </br>
                                `~prop:10,gearIndex,0,180,0,0,0,0,0,2,0,1~` 1st gear </br>
                                `~prop:11,gearIndex,0,180,0,0,0,0,1,3,-1,1~` 2nd gear </br>
                                `~prop:12,gearIndex,0,180,0,0,0,0,2,4,-2,1~` 3rd gear </br>
                                `~prop:13,gearIndex,0,180,0,0,0,0,3,5,-3,1~` 4th gear </br>
                                `~prop:14,gearIndex,0,180,0,0,0,0,-1,0,0,1~` Reverse gear </br>
                                - Note that for this to work, you will need to have a separate gear stick for each gear plus neutral.
                                - Set every copy of the shifter to its final position (Neutral shifter to center, 1st gear shifter to 1st gear location, etc...), then, EXCEPT FOR NEUTRAL, rotate them 180° from the final position, so they are pointing down and are hidden under the center console.
                                  As you move through the gears, each shifter will rotate up to its set position then drop down to be replaced by the neutral shifter, before moving the commanded gear's shifter.
                                - IMPORTANT - For each gear that has a difference of 2 between the [Min] and [Max] (everyone but reverse and final gear) ensure that 360° is entered for range of rotation because the gear must move 180° to its active position then another 180° to be hidden again.
                                """,
                        0,
                        0
                )


                // Add more functions here...
        );
    }
}
