#include "edge_detector.h"
#include <android/log.h>

#define LOG_TAG "EdgeDetector"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

using namespace cv;

EdgeDetector::EdgeDetector() {
    LOGI("EdgeDetector initialized");
}

EdgeDetector::~EdgeDetector() {
}

Mat EdgeDetector::detectEdges(const Mat &input, double threshold1, double threshold2) {
    Mat gray = toGrayscale(input);
    Mat blurred = applyGaussianBlur(gray, 5);
    
    Mat edges;
    Canny(blurred, edges, threshold1, threshold2);
    
    return edges;
}

Mat EdgeDetector::applyGaussianBlur(const Mat &input, int kernelSize) {
    Mat blurred;
    GaussianBlur(input, blurred, Size(kernelSize, kernelSize), 0);
    return blurred;
}

Mat EdgeDetector::toGrayscale(const Mat &input) {
    Mat gray;
    if (input.channels() == 4) {
        cvtColor(input, gray, COLOR_RGBA2GRAY);
    } else if (input.channels() == 3) {
        cvtColor(input, gray, COLOR_RGB2GRAY);
    } else {
        gray = input.clone();
    }
    return gray;
}

