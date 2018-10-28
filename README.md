Beacon Locator
==============

An Android application to interact with beacons.  
Implemented in nice material design using Mvvv pattern + data binding and could be used as a reference application for
Altbeacon android library [github of altbeacon library](http://altbeacon.github.io/android-beacon-library/).

<a href="https://f-droid.org/packages/com.samebits.beacon.locator" target="_blank">
<img src="https://f-droid.org/badge/get-it-on.png" alt="Get it on F-Droid" height="100"/></a>
<a href="https://play.google.com/store/apps/details?id=com.samebits.beacon.locator" target="_blank">
<img src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" alt="Get it on Google Play" height="100"/></a>

## What can this application do?

The application can scan and locate beacons (Eddystone, iBeacons or AltBeacons) and present detailed information 
about beacons properties.
The application allows you to define actions that will be triggered when a specified event occurs.

### Supported events

- Beacon enters a region
- Beacon leaves the region
- Beacon is near you
 
### For each event you can define an unlimited number of actions

- Open an application
- Open url
- Get Gps Location
- Broadcast intent action
- Set Normal / Silent mode
- Execute [tasker action] (http://tasker.dinglisch.net) 

For example, you may set a silent mode entering your bedroom...and set normal sound profile leaving it.

More features are coming soon in next release. 
Please, do hesitate to contact us if you have some ideas about new features or want to contribute.

## Screenshots

<img src="https://cloud.githubusercontent.com/assets/415304/12170836/d8b1c9f2-b544-11e5-9e05-98a850a6a998.png" width="256"><img src="https://cloud.githubusercontent.com/assets/415304/12170835/d8b10616-b544-11e5-8abc-95dad8295b6e.png" width="256">
<img src="https://cloud.githubusercontent.com/assets/415304/12170832/d8ae2e78-b544-11e5-9376-c8759d08480e.png" width="256"><img src="https://cloud.githubusercontent.com/assets/415304/12170837/d8b27262-b544-11e5-9ae7-ce7e91c6abe9.png" width="256">
<img src="https://cloud.githubusercontent.com/assets/415304/12170834/d8afe682-b544-11e5-97d4-681f9a045a86.png" width="256"><img src="https://cloud.githubusercontent.com/assets/415304/12170833/d8af3836-b544-11e5-8112-8eda824b22ba.png" width="256">
<img src="https://cloud.githubusercontent.com/assets/415304/12170838/d8c3fb22-b544-11e5-8ecd-f56ed1f51097.png" width="256">


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


