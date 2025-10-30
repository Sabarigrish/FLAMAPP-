# Setup OpenCV Headers

## You Need To Do This Once

The CMakeLists.txt is configured to look for OpenCV headers in:
```
app/src/main/cpp/include/opencv2/
```

## Quick Setup (5 minutes)

### Step 1: Download OpenCV Android SDK
Download this file:
```
https://github.com/opencv/opencv/releases/download/4.8.0/opencv-4.8.0-android-sdk.zip
```
(~150 MB download)

### Step 2: Extract the ZIP file
You'll get a folder called `opencv-android-sdk`

### Step 3: Copy the headers
Navigate to:
```
opencv-android-sdk/sdk/native/jni/include/
```

Copy the entire `include` folder contents to:
```
flamapp/app/src/main/cpp/include/
```

### Step 4: Verify Structure
After copying, you should have:
```
app/src/main/cpp/
├── include/
│   └── opencv2/
│       ├── opencv.hpp          ← Main header
│       ├── core/
│       ├── imgproc/
│       ├── features2d/
│       └── ... (other modules)
├── native_processor.cpp
├── edge_detector.cpp
├── edge_detector.h
├── CMakeLists.txt
└── SETUP_OPENCV_HEADERS.md (this file)
```

### Step 5: Sync Gradle
In Android Studio:
1. **File → Sync Project with Gradle Files**
2. **Build → Clean Project**
3. **Build → Rebuild Project**

Should work now!

## What If It Still Doesn't Work?

If you continue to have issues, see **[OPENCV_FIX_COMPLETE.md](../../../../OPENCV_FIX_COMPLETE.md)** in the root directory for alternative solutions.

## Why Do We Need This?

- **Maven dependency** provides the compiled `.so` libraries (which is good!)
- But **native compilation** needs the C++ header files
- Headers define the OpenCV API that our C++ code uses
- This is a one-time setup step

## Alternative: Disable Native Build Temporarily

If you want to test the Gradle configuration first, you can temporarily disable native compilation:

In `app/build.gradle`, comment out lines 39-44:
```gradle
// externalNativeBuild {
//     cmake {
//         path file('src/main/cpp/CMakeLists.txt')
//         version '3.22.1'
//     }
// }
```

This lets you build the app (native processing won't work, but you can test the setup).

---

**After this setup, your project will build successfully!**

