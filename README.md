# AutoBeam Animation Generator
Automation to BeamNG.drive animation description tool</br> Created by peskyboyz</br>  
## Description
This tool allows you to quickly generate the lines needed to animated basic props in cars exported from Automation to BeamNG.drive. 

The tool can currently generate the lines needed for the following animations:
 - Steering
 - Speedometer
 - Tachometer
 - Water temperature gauge (radiator)
 - Oil temperature gauge
 - Fuel gauge
 - Boost gauge
 - Engine load
 - Radiator fan
 - Engine pulleys
 - Parking brake
 - Throttle
 - Brake
 - Clutch
 - Gear selector for Auto, Manual and Auto Manual/DCT transmission (Sequential and paddles shifters not supported)
 - Turn Signal
 - Hazards
 - Headlight switch
 - Fog lights
 - Altitude gauge
 - Air speed
</br></br>
 - Other types of animations are possible with some creative use of the above functions (active aero, shift lights, etc.) 

Note that this tool can now generate animation for rotations and translations

Many other animations are possible and details can be found in the BeamNG.drive documentation </br>
https://documentation.beamng.com/modding/vehicle/vehicle_system/electrics/  
https://documentation.beamng.com/modding/vehicle/vehicle_system/controller/main/vehiclecontroller/  
https://documentation.beamng.com/modding/vehicle/sections/props/

## How to Use

The tool operates by allowing the user to enter general parameters of an animation and outputs the completed line to be 
added to Automation.
The correct format for the line is as follows: </br>
`~prop:[Fixture number],[Function],[Rotation X],[Rotation Y],[Rotation Z],[Translation X],[Translation Y],[Translation Z],[Min],[Max],[Offset],[Multiplier]~`
</br>  

Note that no prop can have two animations. Only the second will work in BeamNG, although it may function unusually, sometimes starting the animation in the wrong position.

Note that all prop move based on the origin of the fixture in Automation. So if you want to move a throttle pedal but the origin is not at the top,
you will have to add a translation to have an appropriate movement. </br> </br>
Here is a guide to the 6 movement options: </br>
### **Important**
** NOTE THE DIRECTION OF THE ARROWS WHEN CHOOSING A DIRECTION OF ROTATION ** </BR>
** IF THE ARROW IS FACING YOU, YOU MUST SELECT THE OPPOSITE DIRECTION OF ROTATION ** </BR> 
`~prop:[Fixture number],[Function],-1,0,0,0,0,0,[Min],[Max],[Offset],[Multiplier]~` [Rotation X] - rotation clockwise around red **arrow pointing away** (NOTE THAT THIS IS NEGATIVE)</br>
`~prop:[Fixture number],[Function],0,1,0,0,0,0,[Min],[Max],[Offset],[Multiplier]~` [Rotation Y] - rotation clockwise around green **arrow pointing away** </br>
`~prop:[Fixture number],[Function],0,0,1,0,0,0,[Min],[Max],[Offset],[Multiplier]~` [Rotation Z] - rotation clockwise around blue **arrow pointing away** </br>

`~prop:[Fixture number],[Function],0,0,0,-1,0,0,[Min],[Max],[Offset],[Multiplier]~` [Translation X] - translation moving positive red (NOTE THAT THIS IS NEGATIVE)</br>
`~prop:[Fixture number],[Function],0,0,0,0,1,0,[Min],[Max],[Offset],[Multiplier]~` [Translation Y] - translation moving positive blue </br>
`~prop:[Fixture number],[Function],0,0,0,0,0,1,[Min],[Max],[Offset],[Multiplier]~` [Translation Z] - translation moving positive green </br>

### Calculations
To manually calculate the values for the three Rotations and Translations, you must figure out how far the prop will move per unit
of the function selected. For example, a tachometer (RPM) has a total angle of rotation on the dial of 270°. 
The minimum RPM listed is 0 and the maximum RPM listed is 6000.  </br>

It is important to note that several function are based on specific units, such as speed being based on meters per second (m/s),
and as such will require conversion from MPH or KPH, before doing the calculations above.

The Prop ID can be found on the left side of the "Fixtures" tab as seen below. The prop ID for this steering wheel, which in this case is 98.   
<img src="/README%20Assets/Prop%20ID.png" alt="Screenshot of the location of Prop ID in Automation" width="150px">
</br>

#### Rotation
Let's base the example rotation on the example tachometer described above. </br>
To find the total range we do `[Max] - [Min] = Total Range` which in this case is `6000 - 0 = 6000` </br>
Therefore, we can do `Angle of Rotation / Total Range = Step Value` which in this case is `270 / 6000 = 0.045`
Now we need to know if the needle will be moving clockwise or counter-clockwise.

By referring to the handy rules above we can figure out if the value should be positive or negative.

In the case of the tachometer, the dial rotates counter-clockwise about the green arrow pointing away, so the value will be negative in the [Rotation Y] field. </br>
With the values calculated we can write the final description string. </br>
`~prop:[Fixture number],rpmTacho,0,-0.045,0,0,0,0,0,6000,0,1~` </br>

#### Translations
Let's base the example translation on a speedometer with a [Min] of 0 and a [Max] of 200 kph. We will let it have a total horizontal movement of 9.3 cm found using the ruler in the Misc fixture tab. 
Note 1 cm is also equal to 1 unit in the automation coordinate system. We also have a scaling in the Z axis (green) of 1.2.</br>
We must first convert the kph to m/s which is required for all speeds in BeamNG. `200kph / 3.6 = 55.556` While this is not perfectly precise, it will suffice for these example calculations. </br>
To find the total range we do `[Max] - [Min] = Total Range` which in this case is `55.556 - 0 = 55.556` </br>
For all translation that are measured in cm, we must also divide the distance range by 10 to get the appropriate values for the distance range. `9.3 / 10 = 0.93` </br>
</br>
With this we can calculate the step value. We first need to divide the distance value by the scaling value. NOTE THAT THIS IS NOT DONE FOR THE TRANSLATION IN Z (GREEN). IN THAT CASE LEAVE THE SCALING VALUE AS 1. </br>
We can then divide the result of the distance and scaling division by the total range. `(distanceValue / scalingValue) / (Total Range) = Step Value` </br>
In this case, the step value calculation is `(0.93 / 1.2) / 55.556 = 0.013949...` Depending on the desired accuracy we can use more or less decimals for the animation.

For this speedometer, we want the gauge to move positive X (Red). By looking at the guide in the _How to Use_ section, we can see that to move positive X the step value must be negative. </br>
We now have all the information for the animation, except for the prop ID. </br>
`~prop:[Fixture number],wheelspeed,0.0,0.0,0.0,-0.01394,0.0,0.0,0.0,55.556,0.0,1~` </br>

### Adding Description

Now add all the lines you have generated to the description field in Automation and export the vehicle to BeamNG.drive.
</br><img src="/README%20Assets/Description%20Location.png" alt="Screenshot of Description location" width="550px">
</br><img src="/README%20Assets/Automation%20Description.png" alt="Screenshot of Description example" width="550px">

The vehicle with animations will now be present in BeamNG.drive

## Using the Tool

<img src="/README%20Assets/AppStart.png" alt="Screenshot of tool with all options" width="734">
- First select a function type in the dropdown. Depending on the function chosen, some of the values may be autofilled and locked. </br>
- Now enter your prop ID found in Automation on the left side of the Fixtures tab. </br>
- Now select the type of transformation desired (rotation or translation). </br>
- Select the direction of the transformation by referring to the guide above in the "Important" section . </br>
- If a rotation is selected, enter the range of rotation in degrees. </br>
- If a translation is selected, enter the distance that you want the prop to move in centimeters (use the ruler fixture under Misc).
- Enter the scaling in the selected axis. If you want movement in the X axis (red), enter the X axis (red) scaling value. </br>
- Enter the measurement unit (if available), the minimum, maximum and the offset value for the prop. These will be further described in their respective sections.</br>

When all fields have been entered select Calculate. If there are no issues, the string will appear below. 
Copy it and paste it in the description field in Automation

If you want to return to the description once an error is present or a completed , click the "Back to explanation" button.

If you want to display this Readme guide, click on the "Help" button.

Here is an example of a completed animation list for a car. </br>
`~prop:7,rpmspin,0,1,0,0,0,0,-360,360,0,1~` </br>
`~prop:15,rpmTacho,0,-0.03,0,0,0,0,0.0,9000.0,0.0,1~` </br>
`~prop:16,wheelspeed,0,-3.35,0,0,0,0,0.0,80.5,0.0,1~` </br>
`~prop:17,watertemp,0,-0.98,0,0,0,0,60.0,121.11,-60.0,1~` </br>
`~prop:18,fuel,0,-60.0,0,0,0,0,0.0,1.0,0.0,1~` </br>
`~prop:19,oiltemp,0,0.98,0,0,0,0,60.0,121.11,-60.0,1~` </br>
`~prop:20,steering,0,0.8,0,0,0,0,-900.0,900.0,0.0,1~` </br>
`~prop:26,lowhighbeam,0,-60.0,0,-0.8,-1.4,0,0.0,1.0,0.0,1~` </br>
`~prop:27,lowhighbeam,0,60.0,0,0.8,-1.4,0,0.0,1.0,0.0,1~` </br>
`~prop:55,parkingbrake,-23,0.0,0,0,0,0,0.0,1.0,0.0,1~` </br>
`~prop:56,turnsignal,0,-18.0,0,0,0,0,-1.0,1.0,0.0,1~` </br>
`~prop:62,ignition,0,25.0,0,0,0,0,0.0,1.0,0.0,1~` </br>
`~prop:66,lights,0,0,0,0,0,-0.006,0.0,2.0,0.0,1~` </br>
`~prop:104,brake,22,0,0,0,0,0,0,1.0,0.0,1~` </br>
`~prop:105,throttle,22,0,0,0,0,0,0,240,0.0,1~` </br>
`~prop:119,rpmspin,0,1,0,0,0,0,-360,360,0,1~` </br>
`~prop:125,clutch,22,0,0,0,0,0,0,240,0.0,1~` </br>
`~prop:128,gearIndex,0,180,0,0,0,0,-1,1,0,1~` </br>
`~prop:129,gearIndex,0,180,0,0,0,0,-1,0,0,1~` </br>
`~prop:130,gearIndex,0,180,0,0,0,0,0,2,0,1~` </br>
`~prop:131,gearIndex,0,180,0,0,0,0,1,3,-1,1~` </br>
`~prop:132,gearIndex,0,180,0,0,0,0,2,4,-2,1~` </br>
`~prop:133,gearIndex,0,180,0,0,0,0,3,5,-3,1~` </br>

### Functions

All following descriptions that are based on a gauge cluster will include an example based on this car.  
<img src="/README%20Assets/Test%20Gauge%20Cluster.png" alt="Screenshot of gauge cluster in Automation">
#### Steering

- Enter your prop ID for the moving part of the steering wheel. (With several steering wheels in which the base and 
the wheel are one fixture, you will have to duplicate the fixture, set the wheel of one to be invisible via the material,
and set the other ones base to be invisible. This second one will be the moving fixture while the first one will remain still)
- Enter a range of rotation; It is recommended to leave this set to 900° which means 450° each way. Changing the value here will change the ratio between the physical prop and BeamNG's steering wheel, providing the appearance of the correct rotation for the steering wheel.
- The default steering rotation in BeamNG is 900°. If you desire this can be changed by selecting the "Advanced Tuning Parameters" in BeamNG's "Vehicle Config" and changing it under "Tuning". If you do change the steering angle in BeamNG, ensure that the step value for the rotation is set to 1
- The direction of rotation, the Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
- Select Calculate.
- Example: Using the default value of 900° we get 1.5 rotations of the wheel each way.
</br> `~prop:98,steering,0,1.0,0,0,0,0,-900.0,900.0,0.0,1~`
  
#### Speed

- Enter your prop ID for the speedometer needle
- Enter the range of rotation (This should be the angle of rotation of the speedometer dial)
- Select the direction of rotation
- The Min Value should be set to the minimum value on the speedometer. Note that this could be higher than 0 if the gauge start at a different value.
- The Max Value should be the highest speed that is available on the speedometer, not the top speed of the car
- The Offset should be left alone.
- Select Calculate.
- Example: Based on the test gauge cluster above the units are set to MPH, the startMax to 200, and the startOffset at 0
  </br> `~prop:40,wheelspeed,0,-3.02,0,0,0,0,0,89.4,0,1~` *NOTE: The values in the description are in m/s*

#### RPM

- Enter your prop ID for the tachometer needle
- Enter the range of rotation (This should be the angle of rotation of the tachometer dial)
- Select the direction of rotation
- It is recommended that the Min Value is left at 0. If the tachometer starts higher than 0, set it here.
- The Max Value should be the highest RPM that is available at the end of the tachometer not the startMax RPM's of the engine
- The Offset should be left alone unless the needle needs to drop below the startMin when off
- Select Calculate.
- Example: Based on the test gauge cluster above the Min is left alone, the Max to 9000 and the Offset at 0
  </br> `~prop:41,rpmTacho,0,-0.03,0,0,0,0,0,9000,0,1~` *NOTE: The rpmTacho function shows a smoothed RPM. If you want it to be instantaneous replace the `rpmTacho` with `rpm`*

#### Fuel

- Enter your prop ID for the fuel gauge needle
- Enter the range of rotation (This should be the angle of rotation of the fuel gauge dial)
- Select the direction of rotation
- The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
- Select Calculate.
  </br> `~prop:43,fuel,0,-110.0,0,0,0,0,0.0,1.0,0.0,1~` 

#### Boost

- Enter your prop ID for the speedometer needle
- Enter the range of rotation (This should be the angle of rotation of the boost gauge dial)
- Select the direction of rotation
- Select the appropriate unit for boost (bar, psi, kPa)
- The Min Value is set to the lowest value that can be shown on the gauge
- The Max Value should be the highest value that can be shown on the gauge
- The Offset should be the opposite value of the startMin to compensate and move the starting point of the dial to 0 bar instead of -1 bar
- Select Calculate.
- Example: Based on the test gauge cluster above the units are set to "bar", the Min to -1, the Max to 2, 
and the Offset at 1 to compensate for the -1 for the startMin
  </br>`~prop:44,turboBoost,0,-6.205,0,0,0,0,-14.5038,29.0076,14.5038,1~` *NOTE: The values in the description are in psi*

#### Water Temp

- Enter your prop ID for the water temperature needle
- Enter the range of rotation (This should be the angle of rotation of the water temperature dial)
- Select the direction of rotation
- Select the appropriate unit for temperature (°C, °F)
- The Min Value is set to the lowest value that should be shown on the gauge 
(**IMPORTANT:** This value depends on the lowest you want to show. Most cars cannot show values from freezing)
- The Max Value should be the highest value that should be shown on the gauge
(**IMPORTANT:** This value depends on the highest you want to show. Most cars do not show values past overheating. In BeamNG this occurs at 120°C)
- The Offset should be the opposite value of the startMin to compensate and move the starting point of the dial to the startMin
- Select Calculate.
- Example: Based on the test gauge cluster above the units are set to "°C", the Min to 60, the Max to 120,
  and the Offset at -60 to compensate for the 60 for the startMin. This results in the middle of the gauge being at 90°C
  </br> `~prop:45,watertemp,0.0,-1.83,0.0,0,0,0,60.0,120.0,-60.0,1~` *NOTE: The values in the description are in °C*

#### Oil Temp

- Enter your prop ID for the oil temperature needle
- Enter the range of rotation (This should be the angle of rotation of the oil temperature dial)
- Select the direction of rotation
- Select the appropriate unit for temperature (°C, °F)
- The Min Value is set to the lowest value that should be shown on the gauge
  (**IMPORTANT:** This value depends on the lowest you want to show. Most cars cannot show values from freezing)
- The Max Value should be the highest value that should be shown on the gauge
  (**IMPORTANT:** This value depends on the highest you want to show. Most cars do not show values past overheating. In BeamNG this occurs at 120°C)
- The Offset should be the opposite value of the startMin to compensate and move the starting point of the dial to the startMin
- Select Calculate.
- Example: With the oil gauge the units are set to "°C", the Min to 80, the Max to 120,
  and the Offset at -80 to compensate for the 80 for the startMin. This results in the middle of the gauge being at 100°C
  </br> `~prop:1,oiltemp,0.0,2.75,0.0,0,0,0,80.0,120.0,-80.0,1~` *NOTE: The values in the description are in °C

#### Engine Load (Torque)

- Engine load percentage,based on current torque compared to the maximum amount of torque the engine can produce.
- Enter the prop id
- The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
- Enter the direction and range of rotation.
- NOTE: Doesn't seem to work as described in documentation. Seems to show load percentage,based on current torque compared 
to the maximum amount of torque the engine can produce AT THE CURRENT RPM.
</br> `~prop:1,engineLoad,0.0,270.0,0.0,0,0,0,0.0,1.0,0.0,1~`

#### Electric Radiator Fan

- Spin function for the radiator fan,which activates when the radiator fan turns on. Used for props.
- Note that this only turns on when coolant reaches 105°C
- The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
- Recommended to leave the range of rotation to 360°.
</br> `~prop:1,radiatorFanSpin,0.0,1.0,0.0,0,0,0,0.0,360.0,0.0,1~`

#### RPM Spin (Pulleys)

- Spin function based on engine RPM. Used for spinning pulley props or crank driven fans.
- The Min Value, the Max Value, and the Offset are locked as a change will result in significant changes for the animation.
- For rpmspin, the prop will move the amount stated in the [Max] field (with the opposite in [Min] as the engine rotates, then will reset to the starting position.)
- Therefore, if you enter 180 instead of the default 360, the prop will rotate 180° at the same rate as the engine, then stop and wait for the engine to complete its revolution before resetting to the starting point.
  The value set for the rotation controls both the speed of rotation and the max angle of movement before it resets.
- For example if the value is set to 0.5, instead of the default 1, the prop will rotate at half the normal speed.
  However, since the position resets once the engine completes a revolution, the prop will only have time to rotate 180° in the time allotted. If it was set to 0.25, the prop would only rotate 90° before reset.
</br> `~prop:1,rpmspin,0.0,1.0,0.0,0,0,0,-360.0,360.0,0.0,1~`

#### Parking/Hand Brake

- Parking brake input.
- The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
</br> `~prop:1,parkingbrake,0.0,-30.0,0.0,0,0,0,0.0,1.0,0.0,1~`

#### Throttle

- Throttle percentage. Affected by things like rev matching and throttle cut in the transmission shift logic.
- The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
  </br> `~prop:1,throttle,0.0,-30.0,0.0,0,0,0,0.0,1.0,0.0,1~`

#### Brake

- Brake input percentage.
- The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
  </br> `~prop:1,brake,0.0,30.0,0.0,0,0,0,0.0,1.0,0.0,1~`

#### Clutch

- Clutch input percentage.
- The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
  </br> `~prop:1,clutch,0.0,30.0,0.0,0,0,0,0.0,1.0,0.0,1~`

#### Automatic Gearbox

- For creating Automatic gearbox shifters and dash gear indicators
- Default position is park; Auto goes P, R, N, D, 2, 1
- The Min will be 0 which is Park, and the max will be 1.
- This sort of works for Auto with manual. It will move through the auto portion, then will also move for the Sport 'gear' and the manual gears following resulting in very small steps or very long movement. If you want to only move through the modes, use the Mode Shifter function.
- Enter the range of movement as the entire movement range.
- `~prop:135,gear_A,0,-25,0,0,0,0,0,1,0,1~` For column shifter
- `~prop:143,gear_A,0,0,0,-0.9,0,0,0,1,0,1~` For dash indicator

#### Manual Gearbox

- For creating manual gearbox shifters ie: H-pattern
- IMPORTANT: READ THROUGH THE README OR THE ENTIRE EXPLANATION FOR THIS ANIMATION BY CLICKING THE HELP BUTTON. REFERENCE SCREENSHOT AVAILABLE IN README.
- This tool will only create one line of the gearbox per use. Each gear follows a pattern where Neutral has a min and max of -1 and 1, First has a min and max of 0 and 2, Second is 1 and 3 but with a offset of -1, Third, is 2 and 4 with and offset of -2 and so on going up. Reverse must have a min and max of -1 and 0 with an offset of 0.
`~prop:9,gearIndex,0,180,0,0,0,0,-1,1,0,1~` Neutral </br>
`~prop:10,gearIndex,0,180,0,0,0,0,0,2,0,1~` 1st gear </br>
`~prop:11,gearIndex,0,180,0,0,0,0,1,3,-1,1~` 2nd gear </br>
`~prop:12,gearIndex,0,180,0,0,0,0,2,4,-2,1~` 3rd gear </br>
`~prop:13,gearIndex,0,180,0,0,0,0,3,5,-3,1~` 4th gear </br>
`~prop:14,gearIndex,0,180,0,0,0,0,-1,0,0,1~` Reverse gear </br>
- Note that for this to work, you will need to have a separate gear stick for each gear plus neutral.
- Set every copy of the shifter to its final position (Neutral shifter to center, 1st gear shifter to 1st gear location, etc...), then, EXCEPT FOR NEUTRAL, rotate them 180° from the final position, so they are pointing down and are hidden under the center console. As you move through the gears, each shifter will rotate up to its set position then drop down to be replaced by the neutral shifter, before moving the commanded gear's shifter.
- IMPORTANT - For each gear that has a difference of 2 between the [Min] and [Max] (everyone but reverse and final gear) ensure that 360° is entered for range of rotation because the gear must move 180° to its active position then another 180° to be hidden again.
  </br> <img src="/README%20Assets/Manual%20shifter%20example.png" alt="Screenshot of an example manual shifter setup in Automation" width="500px">

#### Mode Shifter (Auto Manual/DCT)

- For creating Automatic gearbox shifters and dash gear indicators when the gearbox can be manually shifted like a DCT transmission
- Default position is park; Auto goes P, R, N, D, S
- The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
- `~prop:1,gearModeIndex,0.0,45.0,0.0,0,0,0,-1.0,5.0,-1.0,1~`

#### Turn Signal (2 way)

- Used for turn signal stalk
- Varies depending on which turn signal is selected (right is 1, left is -1)
- The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
- Set the prop to the center (idle) position.
- Select the rotation direction that will move it to the "active turning right" position
- Choose a range of rotation that will move it from center to either active position
- `~prop:56,turnsignal,0,-18.0,0,0,0,0,-1.0,1.0,0.0,1~`

#### Hazards Switch

- Indicates the hazards have been turned on
- The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
- `~prop:1,hazard_enabled,0.0,0.0,0.0,0,0,0.00500,0.0,1.0,0.0,1~`

#### Hazards Flashing

- Flashing function when hazards are on.
- The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
- `~prop:1,hazard,0.0,0.0,0.0,0,0,0.00500,0.0,1.0,0.0,1~`

#### Headlights (off/on)

- Activates if either the low or high beams are turned on.
- Only 2 setting positions (off/on).
- Useful for pop-up headlights or hidden headlight covers.
- The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
- `~prop:1,lowhighbeam,0.0,30.0,0.0,0,0,0,0.0,1.0,0.0,1~`

#### Headlights (3 way)

- Index of the current light mode (0 is off, 1 is lowbeams, 2 is highbeams).
- Useful for light switches that have 3 positions (off, low, high).
- The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
- Use a range that will encompass the whole range of motion from off to highbeams.
- `~prop:1,lights,0.0,15.0,0.0,0,0,0,0.0,2.0,0.0,1~`

#### Fog Lights

- Indicates the fog lights are turned on.
- Can be used for special function since fog light cannot currently export
- The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
- `~prop:1,fog,0.0,30.0,0.0,0,0,0,0.0,1.0,0.0,1~`

#### Altitude

- Altitude of the vehicle in meters.
- Set [Min] to the lowest value on the gauge; Set [Max] to the highest value on the gauge; Set [Offset] to the opposite value of the [Min] to bring the start to 0;
- Set the needle to the lowest position on the gauge.
- `~prop:1,altitude,0.0,0.36,0.0,0,0,0,0.0,1000.0,0.0,1~`

#### Air Speed

- Speed of the vehicle compared to the world.
- It is recommended that the Min Value is left at 0. If the speedometer starts higher than 0, set it here.
- The Max Value should be the highest speed that is available at the end of the speedometer not the max speed of the car.
- The Offset should be left alone unless the Min has been increased from 0. If the Min has been changed, set the opposite value here (Ex: Min set to 20; offset should be set to -20.
- `~prop:1,airspeed,0.0,4.86,0.0,0,0,0,0.0,55.556,0.0,1~`

#### Air Speed + Wind

- Speed of the vehicle compared to the airflow, taking into account wind.
- It is recommended that the Min Value is left at 0. If the speedometer starts higher than 0, set it here.
- The Max Value should be the highest speed that is available at the end of the speedometer not the max speed of the car.
- The Offset should be left alone unless the Min has been increased from 0. If the Min has been changed, set the opposite value here (Ex: Min set to 20; offset should be set to -20.
- `~prop:1,airflowspeed,0.0,4.86,0.0,0,0,0,0.0,55.556,0.0,1~`

#### Steering (Percentage)

- Steering input percentage.
- Can be used for steering or special functions like flaps that actuate when you turn.
- For example, the below string move a prop 30° starting at 20% right and ending at 30% right.
- `~prop:1,steering_input,0.0,300.0,0.0,0,0,0,0.2,0.3,-0.2,1~`

### Special uses

#### Shift Lights
- By using the RPM function with a difference between the [Min] and [Max] of 1, you can have the prop move as soon as the RPM is reached.
- You should add two fixture for each shift light. One for the on light and one for the off light.
- Example of a car with 9 shift lights. The unlit lights are visible in the front and the lit ones are hidden in the dash until the RPM is reached.
</br> <img src="/README%20Assets/Shift%20Lights%20Example.png" alt="Screenshot of an example shift light setup Automation" width="300px">
</br> Final Appearance 
</br> <img src="/README%20Assets/Shift%20Lights%20Final.png" alt="Screenshot of an example shift light setup Automation" width="300px">

- Lit up prop moving to "On" position </br>
`~prop:48,rpmTacho,0.0,0.0,0.0,0.0,0.0,0.008,7249,7250,-7249,1~` </br>
`~prop:49,rpmTacho,0.0,0.0,0.0,0.0,0.0,0.008,7499,7500,-7499,1~` </br>
`~prop:50,rpmTacho,0.0,0.0,0.0,0.0,0.0,0.008,7749,7750,-7749,1~` </br>
`~prop:51,rpmTacho,0.0,0.0,0.0,0.0,0.0,0.008,7999,8000,-7999,1~` </br>
`~prop:52,rpmTacho,0.0,0.0,0.0,0.0,0.0,0.008,8249,8250,-8249,1~` </br>
`~prop:53,rpmTacho,0.0,0.0,0.0,0.0,0.0,0.008,8499,8500,-8499,1~` </br>
`~prop:54,rpmTacho,0.0,0.0,0.0,0.0,0.0,0.008,8749,8750,-8749,1~` </br>
`~prop:55,rpmTacho,0.0,0.0,0.0,0.0,0.0,0.008,8999,9000,-8999,1~` </br>
`~prop:56,rpmTacho,0.0,0.0,0.0,0.0,0.0,0.008,9249,9250,-9249,1~` </br>
- Not lit up prop moving back </br>
`~prop:62,rpmTacho,0.0,0.0,0.0,0.0,0.0,-0.008,7249,7250,-7249,1~` </br>
`~prop:63,rpmTacho,0.0,0.0,0.0,0.0,0.0,-0.008,7499,7500,-7499,1~` </br>
`~prop:64,rpmTacho,0.0,0.0,0.0,0.0,0.0,-0.008,7749,7750,-7749,1~` </br>
`~prop:65,rpmTacho,0.0,0.0,0.0,0.0,0.0,-0.008,7999,8000,-7999,1~` </br>
`~prop:66,rpmTacho,0.0,0.0,0.0,0.0,0.0,-0.008,8249,8250,-8249,1~` </br>
`~prop:67,rpmTacho,0.0,0.0,0.0,0.0,0.0,-0.008,8499,8500,-8499,1~` </br>
`~prop:68,rpmTacho,0.0,0.0,0.0,0.0,0.0,-0.008,8749,8750,-8749,1~` </br>
`~prop:69,rpmTacho,0.0,0.0,0.0,0.0,0.0,-0.008,8999,9000,-8999,1~` </br>
`~prop:70,rpmTacho,0.0,0.0,0.0,0.0,0.0,-0.008,9249,9250,-9249,1~` </br>
</br>
- Video of above example </br>



https://github.com/user-attachments/assets/fe46213d-6d00-4713-a0a2-ca27ae250acb



#### Active Aero

- Many types of active aero can be created. 
  - By using the steering percentage function you can create flaps that activate when you turn the wheel a certain amount. (See Pagani Huayra)
    - Right side: `~prop:71,steering_input,-100.0,0.0,0.0,0.0,0.0,0.0,0.2,0.4,-0.2,1~` 
    - Left side: `~prop:72,steering_input,100.0,0.0,0.0,0.0,0.0,0.0,-0.4,-0.2,0.2,1~`
  - By using the air speed you can have a wing flip up or down based on your speed. (See McLaren P1 or F1)
    - `~prop:61,airspeed,-7.19,0.0,0.0,0.0,0.0,0.0,36.11,38.89,-36.11,1~`
  - By using the brake function you can create a wing that flips up under heavy braking. (See Bugatti Veyron)
    - `~prop:61,brake,-80,0.0,0.0,0.0,0.0,0.0,0.75,0.90,-0.75,1~`

#### Throttle body Butterfly Value

- Use the throttle function to move the fixture 90°. Just be sure it rotates about the centre of the fixture

#### Cooling vents that open when the car is overheating

- Use the radiator fan function to move it when it activates

#### Special functions by using the fog function

- Since the fog lights are not exported, this function can be used for anything where you want the prop to have two positions.

#### Others are possible - Think creatively 

- There are other functions not added to this tool such as the isSportActive that have not been tested. Refer to the BeamNG documentation and try it out.

## Updates
Version 0.7.1 - 21 September 2024
- Fixed settings for Manual Gearbox which would prevent setting any offset
- Fixed the logic for rotation about X being backwards

Version 0.7.0 - 10 September 2024
- Added translation animations
- Fixed the minimum propID from 1 to 0
- Fixed the default values not being set for the first function choice
- Finished adding validation for every field
- Added many new function types
- Added the ability to check for an update on launch and offer the update to the user
- Updated README.md

Version 0.6.1 - 29 August 2024
- Fixed Back to Explanation button appearing when the calculate button was clicked before selecting a function
- Added logic to prevent unwanted values in the spinners.
- Fixed an issue where null values would not be caught

Version 0.6 - 28 August 2024
- First public release
- Add new name to app "AutoBeam Animation Generator"
- Added all current functions to README.md
- Removed old screenshots from README assets
- Added icons for app
- Added function for turnsignal, lowhighbeam, lights, gear_A, gearIndex

Version 0.5 - 25 August 2024
- Rewrote AddNewController to handle more types of function types with one set of methods for all types. Now uses a single ComboBox instead of radio buttons.
- Added ability to make animation for X, Y, and/or Z rotations via changes to AddNew.fxml and AddNewController.java.
- Created Function.java to create the class for all functions.
- Created FunctionDataProvider.java to generate all function types according to Function class.
- Created ConversionProvider.java to provide conversion factors for all unit types (exception is °F due to subtraction needed to convert to °C).
- Set minimum window size for application.
- Added button to show the explanation of the chosen function.
- Fixed backwards logic for rotation direction
- Added function for oiltemp, engineLoad, radiatorFanSpin, rpmSpin, parkingbrake,throttle, brake, clutch
- Updated README.md for new version

Version 0.4.2 - 23 August 2024
- Added information to README.md regarding other types of animation

Version 0.4.1 - 18 August 2024
- Corrected rounding issue where rotationY could be inaccurate for certain RPM dials
- Added information to README.md regarding other types of animation

Version 0.4 - 1 August 2024
- Moved project to JDK 21 along with dependencies
- Hid Start Menu page (Is currently incomplete). Now goes directly to tool
- Added Help button which will open README.md in browser via GitHub page
- Renamed several files

Version 0.3.1 - 1 August 2024
- Corrected README.md formatting
- Added missing Description Location.png for README.md

Version 0.3 - 1 August 2024
- Improvement to error message and generated description (moved space and dash to error message title, cleared title from generated string)
- Fixed default spinner values by setting a default during initialization
- Added a default value to the Angle of rotation spinner
- Improved/corrected values to allow different steering angles
- Replaced RPM output from "rpm" to "rpmTacho" to have smoothed rpm values typical of a tachometer
- Created README.md guide and added images for guide

Version 0.2 - 30 July 2024
- Bare bones functional program that allows the user to generate the descriptions that allow Automation car animation to work in BeamNG.drive.
- Currently missing help screen, ability to save list of generations, and other details
