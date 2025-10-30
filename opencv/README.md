# OpenCV Module

This directory contains the OpenCV Android SDK.

## Setup Instructions

1. **Download OpenCV Android SDK**
   - Visit: https://opencv.org/releases/
   - Download the Android SDK (e.g., opencv-4.8.0-android-sdk.zip)
   - Current recommended version: 4.8.0 or later

2. **Extract SDK**
   - Unzip the downloaded file
   - You should see a folder structure like:
     ```
     opencv-android-sdk/
     ├── apk/
     ├── samples/
     └── sdk/
     ```

3. **Copy SDK Files**
   - Copy the `sdk/` folder into this directory (`opencv/`)
   - Final structure should be:
     ```
     opencv/
     ├── sdk/
     │   ├── java/
     │   │   ├── src/
     │   │   └── res/
     │   └── native/
     │       ├── jni/
     │       │   └── include/
     │       └── libs/
     │           ├── arm64-v8a/
     │           ├── armeabi-v7a/
     │           ├── x86/
     │           └── x86_64/
     ├── build.gradle
     ├── AndroidManifest.xml
     └── README.md (this file)
     ```

4. **Verify Installation**
   - Check that `opencv/sdk/native/jni/include/opencv2/opencv.hpp` exists
   - Check that `opencv/sdk/native/libs/arm64-v8a/libopencv_java4.so` exists
   - Check that `opencv/sdk/java/src/org/opencv/` has Java source files

5. **Sync Project**
   - In Android Studio: File → Sync Project with Gradle Files
   - Ensure no errors in the Build output

## Troubleshooting

### SDK Not Found
If you see errors about OpenCV not being found:
- Ensure the `sdk/` folder is directly inside `opencv/`
- Check that native libraries (.so files) are present
- Verify the directory structure matches above

### Wrong OpenCV Version
- This project is tested with OpenCV 4.x
- Download version 4.8.0 or later for best compatibility

### Native Libraries Missing
If native build fails:
- Ensure all ABI folders exist in `sdk/native/libs/`
- Required: arm64-v8a, armeabi-v7a
- Optional but recommended: x86, x86_64 (for emulator)

## Important Notes

- The OpenCV SDK files are **not included** in the Git repository due to size
- Each developer must download and install OpenCV separately
- The `.gitignore` excludes `opencv/sdk/` to prevent committing large binary files
- Only configuration files (`build.gradle`, `AndroidManifest.xml`) are tracked

## Download Link

📥 **Direct Download**: https://github.com/opencv/opencv/releases

Example for OpenCV 4.8.0:
```
https://github.com/opencv/opencv/releases/download/4.8.0/opencv-4.8.0-android-sdk.zip
```

## Version Information

- **Recommended**: OpenCV 4.8.0+
- **Minimum**: OpenCV 4.5.0
- **Architecture**: Android SDK (Java + Native libs)

---

After completing these steps, proceed to build the main Android app.

