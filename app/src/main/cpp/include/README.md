# OpenCV Headers Directory

## ⚠️ Headers Not Included

OpenCV C++ headers are not included in the repository due to size.

## 📥 Download and Setup

See: **[SETUP_OPENCV_HEADERS.md](../SETUP_OPENCV_HEADERS.md)** for complete instructions.

### Quick Steps:

1. Download: https://github.com/opencv/opencv/releases/download/4.8.0/opencv-4.8.0-android-sdk.zip
2. Extract the zip file
3. Copy from: `opencv-android-sdk/sdk/native/jni/include/`
4. Paste to: `app/src/main/cpp/include/` (this directory)

### Expected Structure:

```
include/
└── opencv2/
    ├── opencv.hpp
    ├── core/
    ├── imgproc/
    └── ... (other modules)
```

### Alternative:

If you don't want to download manually, see **[OPENCV_FIX_COMPLETE.md](../../../../OPENCV_FIX_COMPLETE.md)** for other options.

---

This is a **one-time setup**. Once headers are in place, the project will build successfully!

