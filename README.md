# WakeOnLan

This Android app allows the user to start a computer remotely via Wake-on-Lan.
You can also find the app in the Google Play Store:
https://play.google.com/store/apps/details?id=de.eickemeyer.wake.on.lan

Features:
---------
* Scanning for online network devices
* Save devices in a favorite list
* Manually wake devices
* New: Choose your preferred Wake-on-Lan port and the CIRD to adapt the subnet scan size
* Supports different layouts for different devices to improve usability (see Screenshot folder)
* Material Design with modern elements
* Data Binding 
* No memory leaks (LeakCanary tested) except of a known  

TODOs:
------
* Replace `RetainedScanTaskFragment` by a more modern way to handle orientation changes aware background tasks
* More capabilities, e.g., Widget, grouping etc.
</br></br>
<img src="https://github.com/eickeee/WakeOnLan/blob/master/Screenshots/nexus5_menu.png" alt="Nexus5_Menu" width="250"/>
<img src="https://github.com/eickeee/WakeOnLan/blob/master/Screenshots/nexus5_scan.png" alt="Nexus5_Scan" width="250"/>
<img src="https://github.com/eickeee/WakeOnLan/blob/master/Screenshots/nexus5_wake.png" alt="Nexus5_Wake" width="250"/>
</br>
<img src="https://github.com/eickeee/WakeOnLan/blob/master/Screenshots/nexus7_land.png" alt="Nexus7_Landscape" width="1000"/>


License
-------

    Copyright 2016 Chris Eickemeyer

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
