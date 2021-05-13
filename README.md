Ecalc - EPERM quick calculator for Android

ECalc is an EPERM quick calculator for the Android operating system. The project was begun somewhere between 2013 and 2015, but never got off the ground due to a general lack of demand, among other factors.

Given that the existing mobile options for EPERM measurements consist of outdated Casio calculator models, or the online calculator running on a laptop or smartphone, its time to resurrect the project.

This code was written primarily for the purpose of learning to code for Android, not for providing any official Rad Elec product. There are bugs. It is *not* production ready in its current state; in other words, if you use this code, you're on your own. Test your readings in another calculator (such as the online calculator at <a href="https://www.radelec.com/qc">www.radelec.com/qc</a>) before providing official results.

What Does it Do?
- allows entry of start/end dates, electret voltages, background gamma, elevation, and produces results based on your EPERM configuration (SST, SLT, LLT, LST, HST, LLT-OO, LST-OO, LMT-OO). These results can be printed in either US or SI units of measure.

What Doesn't It Do?
- save any of your results
- produce any kind of official report
- keep track of your state or government QA/QC requirements (users wanting that should use the RRM database application)
- check for user input errors (double check your data-entry!)


Generating an APK
This app was developed during the Android 4-5 era, and as of this date has not been updated to the latest build tools (Android Studio and Gradle). Therefore, you will need to use an older version of the Android SDK (at least the /tools part that contains ant), to build this project.

1. Install and configure paths for Android SDK
2. Find the SDK tools version r25.2.5 and extract the tools folder to your Android SDK (back up your likely-newer tools folder first!)
3. Navigate to this project in a command shell
4. Input `android update project --path .`
5. Input `ant debug`
6. Install the resulting apk onto your device using ADB

You may have to install older API versions in your Android SDK. There should be no dependency on any deprecated Android feature, so updating the app for a later API should be a breeze.

Note About the Main Menu
There is a main menu item which makes reference to the Recon CRM, but there are no plans to support the Recon CRM with this app. Please search the Play Store for Recon Mobile if you would like to use your Recon CRM with Android.

