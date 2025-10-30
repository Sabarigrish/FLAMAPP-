# ⚠️ IMPORTANT: Download OpenCV Android SDK

## Current Issue

You have downloaded the **OpenCV source distribution** (`opencv-4.12.0`), but you need the **OpenCV Android SDK** (pre-built for Android).

## ✅ Correct Download

### Step 1: Download OpenCV Android SDK

**Direct Link (OpenCV 4.8.0):**
```
https://github.com/opencv/opencv/releases/download/4.8.0/opencv-4.8.0-android-sdk.zip
```

**Or visit:** https://opencv.org/releases/

### Step 2: Extract and Place Files

1. Extract `opencv-4.8.0-android-sdk.zip`
2. You'll see a folder structure like:
   ```
   opencv-android-sdk/
   ├── apk/
   ├── samples/
   └── sdk/          ← This is what we need!
       ├── java/
       └── native/
   ```

3. Copy the `sdk` folder to `flamapp/opencv/`:
   ```
   flamapp/
   └── opencv/
       ├── sdk/              ← Place the sdk folder here
       │   ├── java/
       │   │   ├── src/
       │   │   └── res/
       │   └── native/
       │       ├── jni/
       │       └── libs/
       ├── build.gradle
       ├── AndroidManifest.xml
       └── README.md
   ```

### Step 3: Verify Structure

Check that these files exist:
```
opencv/sdk/native/jni/include/opencv2/opencv.hpp  ← Headers
opencv/sdk/native/libs/arm64-v8a/libopencv_java4.so  ← Native libs
opencv/sdk/java/src/org/opencv/  ← Java classes
```

### Step 4: Clean Project

In Android Studio:
1. **Build → Clean Project**
2. **File → Sync Project with Gradle Files**
3. **Build → Rebuild Project**

## What You Downloaded vs What You Need

| What You Have | What You Need |
|---------------|---------------|
| opencv-4.12.0/ (source code) | opencv-android-sdk/ (pre-built) |
| Needs compilation | Ready to use |
| ~500 MB | ~150 MB |
| Has CMakeLists.txt | Has pre-built .so files |

## Alternative: Use Maven (Easier)

If you can't get the SDK working, you can use OpenCV from Maven:

### Update app/build.gradle:

Replace:
```gradle
// OpenCV
implementation project(':opencv')
```

With:
```gradle
// OpenCV from Maven
implementation 'org.opencv:opencv:4.8.0'
```

### Update settings.gradle:

Remove:
```gradle
include ':opencv'
```

**Note:** Maven version is simpler but slightly larger APK size.

## Quick Test

After fixing, run this in Android Studio terminal:
```bash
gradlew clean
gradlew assembleDebug
```

Should build without errors!

---

**Need help?** See the main [opencv/README.md](README.md) for more details.

