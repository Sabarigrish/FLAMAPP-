# JNI Libraries Directory

## ⚠️ Required: OpenCV Native Libraries

You need to copy the OpenCV `.so` files here.

### Quick Steps:

1. **You already have the OpenCV SDK downloaded** (you copied the headers earlier)

2. **Navigate to the extracted folder:**
   ```
   opencv-android-sdk/sdk/native/libs/
   ```

3. **Copy these folders to here (`app/src/main/jniLibs/`):**
   - `arm64-v8a/` (entire folder with libopencv_java4.so)
   - `armeabi-v7a/` (entire folder with libopencv_java4.so)

### Expected Structure:

```
app/src/main/jniLibs/
├── arm64-v8a/
│   └── libopencv_java4.so
└── armeabi-v7a/
    └── libopencv_java4.so
```

### After Copying:

1. File → Sync Project with Gradle Files
2. Build → Clean Project
3. Build → Rebuild Project

Should work! ✅

---

**Note:** These are large files (~30 MB each), so they're excluded from Git via `.gitignore`.

