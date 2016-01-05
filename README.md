Beacon Locator
=======================

An Android application to interact with beacons.  
Implemented in nice material design using Mvvv pattern + data binding and could be used as a reference application for
Altbeacon android library 
[github of altbeacon library](http://altbeacon.github.io/android-beacon-library/).

## What does this application can do?

The application can scan and locate beacons (Eddystone, iBeacons or AltBeacons) and present detailed information 
about beacons properties.
The application allows you to define actions that will be triggered when a specified event occurs.

###These events are supported:
- Beacon enters a region
- Beacon leaves the region
- Beacon is near you
 
###For each event you can define unlimited number of actions :
- Open an application
- Open url
- Get Gps Location
- Broadcast intent action
- Set Normal / Silent mode
- Execute [tasker action] (http://tasker.dinglisch.net) 

For example, you may set a silent mode entering your bedroom...and set normal sound profile leaving it.

More features are coming soon in next release. 
Please, do hesitate to contact us if you have some ideas about new features or want to contribute.

## Limitations
Android 4.3 with Bluetooth 4.0 LE or newer is required.

## License

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


