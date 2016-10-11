cd build\libs
del /Q *
cd ..\..
gradlew.bat setupCIWorkspace build --refresh-dependencies
cd build\libs
copy *universal.jar ..\..\Releases\1.10.2\12.18.1.2076\1.2.0\
pause