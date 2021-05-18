## ECalc - EPERM quick calculator for Android

ECalc is an EPERM quick calculator for the Android operating system. The project was begun sometime between 2013 and 2015, but never got off the ground due to a general lack of demand, among other factors.

Existing mobile options for making EPERM measurements are limited, so it's time to resurrect the project.

This code was written for the purpose of learning to code for Android, not for providing an official EPERM product. There are bugs. It is *not* production ready in its current state. If you use this code, you're on your own. Verify your results with another calculator (such as the online calculator at <a href="https://www.radelec.com/qc">www.radelec.com/qc</a>) before providing official results.

### What Does it Do?

- allows entry of start/end dates, electret voltages, background gamma, elevation, and produces results based on your EPERM configuration (SST, SLT, LLT, LST, HST, LLT-OO, LST-OO, LMT-OO). These results can be printed in either US or SI units.

### What It Does Not Do

- save any of your results
- produce any kind of official report
- keep track of your state or government QA/QC requirements (users wanting that should use the RRM database application)
- check for user input errors (double check your data-entry!)

### Generating an APK

The project uses Gradle as its build system. You may use the gradlew wrapper to generate a build from the command line, or open an IDE using Gradle such as Android Studio and run the build. Use adb to install the file.

`./gradlew assembleDebug`

`adb install -t ECalc/build/outputs/apk/debug/ECalc-debug.apk`
