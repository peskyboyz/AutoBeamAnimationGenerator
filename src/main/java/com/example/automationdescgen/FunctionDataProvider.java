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
                                - Enter a range of rotation (It is recommended to leave this set to 900° which means 450° each way. This value can be set to a different value if desired)
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
                                - The Offset should be left alone unless the needle needs to drop below the startMin when off.""",
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
                                - The Offset should be left alone unless the min has been increased from 0. If the Min has been changed, set the opposite value here (Ex: Min set to 20; offset should be set to -20.""",
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
                                - The Offset should be the opposite value of the startMin to compensate and move the starting point of the dial to the startMin""",
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
                                - The Max Value should be the highest value that should be shown on the gauge (IMPORTANT: This value depends on the highest you want to show. Most cars do not show values past overheating. In BeamNG this occurs at 120°C)
                                - The Offset should be the opposite value of the startMin to compensate and move the starting point of the dial to the startMin""",
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
                                - The Offset should be the opposite value of the startMin to compensate and move the starting point of the dial to 0""",
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
                                However, since the position resets once the engine completes a revolution, the prop will only have time to rotate 180° in the time alloted. If it was set to 0.25, the prop would only rotate 90° before reset.
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
                                - Throttle percentage. Affected by things like revmatching and throttle cut in the transmission shift logic.
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
                )


                // Add more functions here...
        );
    }
}
