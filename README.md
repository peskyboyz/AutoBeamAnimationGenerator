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

Many other animations are possible and details can be found in the BeamNG.drive documentation 
https://documentation.beamng.com/modding/vehicle/vehicle_system/electrics/  
https://documentation.beamng.com/modding/vehicle/vehicle_system/controller/main/vehiclecontroller/
https://documentation.beamng.com/modding/vehicle/sections/props/

## How to Use

The tool operates by allowing the user to enter general parameters of an animation and outputs the completed line to be 
added to Automation.
The correct format for the line is as follows: </br>
`~prop:[Fixture number],[Function],[Rotation X],[Rotation Y],[Rotation Z],[Translation X],[Translation Y],[Translation Z],[Min],[Max],[Offset],[Multiplier]~`
</br>  
Depending on the function chosen, the user will have to enter the prop ID, the direction of rotation, the range of 
rotation (in degrees), the measurement unit, the minimum, maximum and the offset value for the prop. These will be 
further described in their respective sections.
</br></br>
The Prop ID can be found on the left side of the "Fixtures" tab as seen below. The prop ID for this steering wheel, which in this case is 98.   
![Screenshot of the location of Prop ID in Automation](/README%20Assets/Prop%20ID.png)
</br>
When all fields have been entered select Calculate. If there are no issues, the string will appear below. 
Copy it and paste it in the description field in Automation

All below descriptions will include an example based on this gauge cluster  
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

## Updates  
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
- Barebones functional program that allows the user to generate the descriptions that allow Automation car animation to work in BeamNG.drive.
- Currently missing help screen, ability to save list of generations, and other details