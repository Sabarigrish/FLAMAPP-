#include <jni.h>
#include <string>
#include <android/log.h>
#include <opencv2/opencv.hpp>
#include <opencv2/imgproc.hpp>
#include "edge_detector.h"

#define LOG_TAG "NativeProcessor"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

using namespace cv;

extern "C" {

/**
 * Process image frame with Canny edge detection
 * @param env JNI environment
 * @param thiz Java object
 * @param inputData Input image data (RGBA)
 * @param width Image width
 * @param height Image height
 * @param outputData Output buffer (RGBA)
 * @param processingMode 0=raw, 1=grayscale, 2=edges
 */
JNIEXPORT void JNICALL
Java_com_assessment_edgedetection_jni_NativeProcessor_processFrame(
        JNIEnv *env,
        jobject thiz,
        jbyteArray inputData,
        jint width,
        jint height,
        jbyteArray outputData,
        jint processingMode) {

    try {
        // Get input data
        jbyte *inputBytes = env->GetByteArrayElements(inputData, nullptr);
        jbyte *outputBytes = env->GetByteArrayElements(outputData, nullptr);

        if (!inputBytes || !outputBytes) {
            LOGE("Failed to get byte array elements");
            return;
        }

        // Create OpenCV Mat from input (RGBA)
        Mat inputMat(height, width, CV_8UC4, (unsigned char *) inputBytes);
        Mat outputMat(height, width, CV_8UC4, (unsigned char *) outputBytes);

        // Process based on mode
        switch (processingMode) {
            case 0: // Raw - just copy
                inputMat.copyTo(outputMat);
                break;

            case 1: { // Grayscale
                Mat gray;
                cvtColor(inputMat, gray, COLOR_RGBA2GRAY);
                cvtColor(gray, outputMat, COLOR_GRAY2RGBA);
                break;
            }

            case 2: { // Edge detection
                EdgeDetector detector;
                Mat processed = detector.detectEdges(inputMat, 50, 150);
                cvtColor(processed, outputMat, COLOR_GRAY2RGBA);
                break;
            }

            default:
                inputMat.copyTo(outputMat);
                break;
        }

        // Release arrays
        env->ReleaseByteArrayElements(inputData, inputBytes, JNI_ABORT);
        env->ReleaseByteArrayElements(outputData, outputBytes, 0);

    } catch (const cv::Exception &e) {
        LOGE("OpenCV error: %s", e.what());
    } catch (...) {
        LOGE("Unknown error in processFrame");
    }
}

/**
 * Apply Canny edge detection
 */
JNIEXPORT void JNICALL
Java_com_assessment_edgedetection_jni_NativeProcessor_applyCannyEdgeDetection(
        JNIEnv *env,
        jobject thiz,
        jbyteArray inputData,
        jint width,
        jint height,
        jbyteArray outputData,
        jdouble threshold1,
        jdouble threshold2) {

    try {
        jbyte *inputBytes = env->GetByteArrayElements(inputData, nullptr);
        jbyte *outputBytes = env->GetByteArrayElements(outputData, nullptr);

        Mat inputMat(height, width, CV_8UC4, (unsigned char *) inputBytes);
        Mat outputMat(height, width, CV_8UC4, (unsigned char *) outputBytes);

        EdgeDetector detector;
        Mat edges = detector.detectEdges(inputMat, threshold1, threshold2);
        cvtColor(edges, outputMat, COLOR_GRAY2RGBA);

        env->ReleaseByteArrayElements(inputData, inputBytes, JNI_ABORT);
        env->ReleaseByteArrayElements(outputData, outputBytes, 0);

    } catch (const cv::Exception &e) {
        LOGE("OpenCV error in Canny: %s", e.what());
    }
}

/**
 * Get OpenCV version
 */
JNIEXPORT jstring JNICALL
Java_com_assessment_edgedetection_jni_NativeProcessor_getOpenCVVersion(
        JNIEnv *env,
        jobject thiz) {
    return env->NewStringUTF(CV_VERSION);
}

} // extern "C"

