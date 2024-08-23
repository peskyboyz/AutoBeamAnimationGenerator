# Automation to BeamNG Animation Description Tool 
Created by peskyboyz</br>  
## Description
This tool allows you to quickly generate the lines needed to animated basic props in cars exported from Automation to BeamNG.drive. 

The tool can currently generate the lines needed for the following animations:
 - Steering
 - Speedometer
 - Tachometer
 - Water temperature (radiator)
 - Fuel gauge
 - Boost gauge

Note that these animation are currently only for rotations. While linear animations are possible, they are not currently supported by this tool.

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

`~prop:135,rpmspin,1,0,0,0,0,0,-360,360,0,1~` clockwise around red arrow pointing away </br>
`~prop:136,rpmspin,0,1,0,0,0,0,-360,360,0,1~` clockwise around green arrow pointing away </br>
`~prop:137,rpmspin,0,0,1,0,0,0,-360,360,0,1~` clockwise around blue arrow pointing away </br>

`~prop:135,throttle,0,0,0,1,0,0,0,240,0.0,1~` moves positive red </br>
`~prop:136,throttle,0,0,0,0,1,0,0,240,0.0,1~` moves positive blue </br>
`~prop:137,throttle,0,0,0,0,0,1,0,240,0.0,1~` moves positive green </br>

### Calculations
To calculate the values for the three Rotations and Translations, you must figure out how far the prop will move per unit
of the function selected. For example, a tachometer (RPM) has a total angle of rotation on the dial of 270°. 
The minimum RPM listed is 0 and the maximum RPM listed is 6000.  </br>
To find the max range we do `[Max] - [Min] = Total Range` which in this case is `6000 - 0 = 6000` </br>
Therefore, we can do `Angle of Rotation / Total Range = Step Value` which in this case is `270 / 6000 = 0.045`
Now we need to know if the needle will be moving clockwise or counter-clockwise.

By referring to the following handy rules we can figure out if the value should be positive or negative: </br>
[Rotation X]: positive is clockwise around red arrow pointing away </br>
[Rotation Y]: positive is clockwise around green arrow pointing away </br>
[Rotation Z]: positive is clockwise around blue arrow pointing away </br>
[Translation X]: positive moves positive red </br>
[Translation Y]: positive moves positive blue </br>
[Translation Z]: positive moves positive green

In the case of the tachometer, the dial rotates counter-clockwise about the green arrow so the value will be negative in the [Rotation Y] field. </br>
With the values calculated we can write the final description string. </br>
`~prop:[Fixture number],rpmTacho,0,-0.045,0,0,0,0,0,6000,0,1~` </br>

It is important to note that several function are based on specific units, such as speed being based on meters per second (m/s),
and as such will require conversion from MPH or KPH, before doing the calculations above

### Using the Tool
Depending on the function chosen, the user will have to enter the prop ID, the direction of rotation, the range of 
rotation (in degrees), the measurement unit, the minimum, maximum and the offset value for the prop. These will be 
further described in their respective sections.
</br></br>
The Prop ID can be found on the left side of the "Fixtures" tab as seen below. The prop ID for this steering wheel, which in this case is 98.   
![Screenshot of the location of Prop ID in Automation](/README%20Assets/Prop%20ID.png)
</br>
When all fields have been entered select Calculate. If there are no issues, the string will appear below. 
Copy it and paste it in the description field in Automation

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

All following descriptions will include an example based on this gauge cluster  
![Screenshot of gauge cluster in Automation](/README%20Assets/Test%20Gauge%20Cluster.png)

### Steering

- Select the "Steering" type
- Enter your prop ID for the moving part of the steering wheel. (With several steering wheels in which the base and 
the wheel are one fixture, you will have to duplicate the fixture, set the wheel of one to be invisible via the material,
and set the other ones base to be invisible. This second one will be the moving fixture while the first one will remain still)
- Enter a range of rotation (It is recommended to leave this set to 900. This value can be set to a different value if desired)
- The direction of rotation, the Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
- Select Calculate.
- Example: Using the default value of 900° we get 1.5 rotations of the wheel each way.
</br>![Screenshot of Steering example](/README%20Assets/Steering%20Example.png)
</br> `~prop:98,steering,0,1.0,0,0,0,0,-900.0,900.0,0.0,1~`
  
### Speed

- Select the "Speed" type
- Enter your prop ID for the speedometer needle
- Enter the range of rotation (This should be the angle of rotation of the speedometer dial)
- Select the direction of rotation
- The Min Value is locked
- The Max Value should be the highest speed that is available on the speedometer, not the top speed of the car
- The Offset should be left alone unless the needle needs to drop below the min when off
- Select Calculate.
- Example: Based on the test gauge cluster above the units are set to MPH, the max to 200, and the offset at 0 
since the gauge starts at 0.</br>![Screenshot of Speed example](/README%20Assets/Speed%20Example.png)  
`~prop:40,wheelspeed,0,-3.02,0,0,0,0,0,89.4,0,1~` *NOTE: The values in the description are in m/s*

### RPM

- Select the "RPM" type
- Enter your prop ID for the tachometer needle
- Enter the range of rotation (This should be the angle of rotation of the tachometer dial)
- Select the direction of rotation
- It is recommended that the Min Value is left at 0. If the tachometer starts higher than 0, set it here.
- The Max Value should be the highest RPM that is available at the end of the tachometer not the max RPM's of the engine
- The Offset should be left alone unless the needle needs to drop below the min when off
- Select Calculate.
- Example: Based on the test gauge cluster above the min is left alone, the max to 9000 and the offset at 0
since the gauge starts at 0.</br>![Screenshot of RPM example](/README%20Assets/RPM%20Example.png)
  </br>
  `~prop:41,rpmTacho,0,-0.03,0,0,0,0,0,9000,0,1~` *NOTE: The rpmTacho function shows a smoothed RPM. If you want it to be instantaneous replace the rpmTacho with rpm*

### Fuel

- Select the "Fuel" type
- Enter your prop ID for the fuel gauge needle
- Enter the range of rotation (This should be the angle of rotation of the fuel gauge dial)
- Select the direction of rotation
- The Min Value, the Max Value, and the Offset are locked as a change will result in errors for the animation.
- Select Calculate.
- Example: </br>![Screenshot of Fuel example](/README%20Assets/Fuel%20Example.png)
  </br>
  `~prop:43,fuel,0,-110.0,0,0,0,0,0.0,1.0,0.0,1~` 

### Boost

- Select the "Speed" type
- Enter your prop ID for the speedometer needle
- Enter the range of rotation (This should be the angle of rotation of the boost gauge dial)
- Select the direction of rotation
- Select the appropriate unit for boost (bar, psi, kPa)
- The Min Value is set to the lowest value that can be shown on the gauge
- The Max Value should be the highest value that can be shown on the gauge
- The Offset should be the opposite value of the min to compensate and move the starting point of the dial to 0 bar instead of -1 bar
- Select Calculate.
- Example: Based on the test gauge cluster above the units are set to "bar", the min to -1, the max to 2, 
and the offset at 1 to compensate for the -1 for the min
  </br>![Screenshot of Boost example](/README%20Assets/Boost%20Example.png)
  </br>
  `~prop:44,turboBoost,0,-6.205,0,0,0,0,-14.5038,29.0076,14.5038,1~` *NOTE: The values in the description are in psi*

### Water Temp

- Select the "Water Temp" type
- Enter your prop ID for the water temperature needle
- Enter the range of rotation (This should be the angle of rotation of the water temperature dial)
- Select the direction of rotation
- Select the appropriate unit for temperature (°C, °F)
- The Min Value is set to the lowest value that should be shown on the gauge 
(**IMPORTANT:** This value depends on the lowest you want to show. Most cars cannot show values from freezing)
- The Max Value should be the highest value that should be shown on the gauge
(**IMPORTANT:** This value depends on the highest you want to show. Most cars do not show values past overheating. In BeamNG this occurs at 120°C)
- The Offset should be the opposite value of the min to compensate and move the starting point of the dial to the min
- Select Calculate.
- Example: Based on the test gauge cluster above the units are set to "°C", the min to 60, the max to 120,
  and the offset at -60 to compensate for the 60 for the min. This results in the middle of the gauge being at 90°C
  </br>![Screenshot of Boost example](/README%20Assets/Water%20Example.png)
  </br>
  `~prop:44,turboBoost,0,-6.205,0,0,0,0,-14.5038,29.0076,14.5038,1~` *NOTE: The values in the description are in psi*

### Adding Description

Now add all the lines you have generated to the description field in Automation and export the vehicle to BeamNG.drive.
</br>![Screenshot of Description location](/README%20Assets/Description%20Location.png)
</br>![Screenshot of Description example](/README%20Assets/Automation%20Description.png)

The vehicle with animations will now be present in BeamNG.drive

## List of Possible Animations

Animations that I have successfully used. When looking at the examples, the important parts to look at are the [Function],
[Min], [Max], [Offset].
- steering
  - `~prop:98,steering,0,1.0,0,0,0,0,-900.0,900.0,0.0,1~`
- rpmTacho (and rpm)
  - `~prop:41,rpmTacho,0,-0.03,0,0,0,0,0.0,9000.0,0.0,1~`
- wheelspeed (airspeed and airflowspeed functions the same way)
  - `~prop:40,wheelspeed,0,-3.02,0,0,0,0,0.0,89.4,0.0,1~`
- watertemp (oiltemp` functions the same way)
  - `~prop:44,watertemp,0,-1.83,0,0,0,0,60.0,120.0,-60.0,1~`
- fuel
  - `~prop:43,fuel,0,-110.0,0,0,0,0,0.0,1.0,0.0,1~`
- turboBoost
  - `~prop:44,turboBoost,0,-6.205,0,0,0,0,-14.5038,29.0076,14.5038,1~`
- radiatorFanSpin (Note that this only turns on when coolant reaches 105°C)
  - `~prop:17,radiatorFanSpin,0,1,0,0,0,0,0,360,0,1~`
- rpmspin (for pulleys, crank driven fans, or other rpm dependent constantly spinning while the engine is on)
  - `~prop:23,rpmspin,0,1,0,0,0,0,-360,360,0,1~`
  - For rpmspin, the prop will move the amount stated in the [Max] field as the engine rotates, then will reset to the starting position. Therefore, if you enter 180 instead of the default 360, the prop will rotate 180° at the same rate as the engine, then stop and wait for the engine to complete its revolution before resetting to the starting point.
    The value set for the rotation controls both the speed of rotation and the max angle of movement before it resets. For example if the value is set to 0.5, instead of the default 1, the prop will rotate at half the normal speed. However, since the position resets once the engine completes a revolution, the prop will only have time to rotate 180° in the time alloted. If it was set to 0.25, the prop would only rotate 90° before reset.
    The [Multiplier] field does not affect the rotation at all.
- throttle, brake, clutch
  - `~prop:114,throttle,22,0,0,0,0,0,0,240,0.0,1~`
- lowhighbeam (is on for both low and high beam lights)
  - `~prop:30,lowhighbeam,0,60.0,0,0.8,-1.4,0,0.0,1.0,0.0,1~`
- lights (three setting positions; off, low, high)
  - `~prop:73,lights,0,0,0,0,0,-0.006,0.0,2.0,0.0,1~`
- turnsignal (set to middle as the value will go positive and negative)
  - `~prop:63,turnsignal,0,-18.0,0,0,0,0,-1.0,1.0,0.0,1~`
- ignition (off and on; no start setting)
  - `~prop:69,ignition,0,25.0,0,0,0,0,0.0,1.0,0.0,1~`
- parkingbrake
  - `~prop:62,parkingbrake,-23,0.0,0,0,0,0,0.0,1.0,0.0,1~`
- gear_A (Automatic gearbox)
  - `~prop:135,gear_A,0,-25,0,0,0,0,-2,3,0,1~` For column shifter </br>
    `~prop:143,gear_A,0,0,0,-0.9,0,0,-2,3,0,1~` For dash indicator
  - Default position is park; Auto goes P, R, N, D, 2, 1
  - This sort of works for Auto with manual. It will move through the auto portion, then will also move for the Sport 'gear'
    and the manual gears following resulting in a very long linear movement.
- gearIndex (for creating manual gearbox shifters ie: H-pattern)
  - `~prop:9,gearIndex,0,180,0,0,0,0,-1,1,0,1~` Neutral </br>
    `~prop:10,gearIndex,0,180,0,0,0,0,0,2,0,1~` 1st gear </br>
    `~prop:11,gearIndex,0,180,0,0,0,0,1,3,-1,1~` 2nd gear </br>
    `~prop:12,gearIndex,0,180,0,0,0,0,2,4,-2,1~` 3rd gear </br>
    `~prop:13,gearIndex,0,180,0,0,0,0,3,5,-3,1~` 4th gear </br>
    `~prop:14,gearIndex,0,180,0,0,0,0,-1,0,0,1~` Reverse gear </br>
  - Note that for this to work, you will need to have a separate gear stick for each gear plus neutral.
    Set every copy of the shifter to its final position (Neutral shifter to center, 1st gear shifter to 1st gear location, etc...),
    then, EXCEPT FOR NEUTRAL, rotate them 180° from the final position, so they are pointing down and are hidden under the center console.
    As you move through the gears, each shifter will rotate up to its set position then drop down to be replaced by the neutral shifter,
    before moving the commanded gear's shifter. </br>
    ![Screenshot of an example manual shifter setup in Automation](/README%20Assets/Manual%20shifter%20example.png)

## Updates  
Version 0.42 - 23 August 2024
- Added information to README.md regarding other types of animation

Version 0.41 - 18 August 2024
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
