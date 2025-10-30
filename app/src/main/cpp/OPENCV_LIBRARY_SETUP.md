# OpenCV Library Setup

## Problem

CMake can't find the OpenCV library from Maven. We need the `.so` files.

## ✅ Quick Solution

### Option 1: Extract Library from Maven (Recommended)

The Maven dependency has downloaded the library. Let's extract it:

1. **Build the project once** (it will download dependencies)

2. **Find the library in Gradle cache:**
   ```
   C:\Users\varun\.gradle\caches\transforms-3\
   ```
   Search for: `libopencv_java4.so`

3. **Copy to project:**
   Create: `app/src/main/jniLibs/arm64-v8a/libopencv_java4.so`
   Create: `app/src/main/jniLibs/armeabi-v7a/libopencv_java4.so`

### Option 2: Download Pre-built Library (Faster)

1. **Download OpenCV Android SDK:**
   ```
   https://github.com/opencv/opencv/releases/download/4.8.0/opencv-4.8.0-android-sdk.zip
   ```

2. **Extract and copy libraries:**
   From: `opencv-android-sdk/sdk/native/libs/`
   To: `app/src/main/jniLibs/`

   Structure:
   ```
   app/src/main/jniLibs/
   ├── arm64-v8a/
   │   └── libopencv_java4.so
   ├── armeabi-v7a/
   │   └── libopencv_java4.so
   ├── x86/
   │   └── libopencv_java4.so (optional, for emulator)
   └── x86_64/
       └── libopencv_java4.so (optional, for emulator)
   ```

3. **Update CMakeLists.txt** (see below)

---

## Updated CMakeLists.txt

I'll update it to use the libraries from `jniLibs`.

