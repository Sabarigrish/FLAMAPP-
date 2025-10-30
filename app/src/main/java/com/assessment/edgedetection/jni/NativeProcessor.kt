package com.assessment.edgedetection.jni

import android.util.Log

/**
 * JNI bridge to native C++ OpenCV processing
 */
class NativeProcessor {

    companion object {
        private const val TAG = "NativeProcessor"
        
        init {
            try {
                System.loadLibrary("edgedetection")
                Log.i(TAG, "Native library loaded successfully")
            } catch (e: UnsatisfiedLinkError) {
                Log.e(TAG, "Failed to load native library", e)
            }
        }
    }

    /**
     * Processing modes
     */
    enum class ProcessingMode(val value: Int) {
        RAW(0),
        GRAYSCALE(1),
        EDGE_DETECTION(2)
    }

    /**
     * Process frame with specified mode
     * @param inputData Input RGBA data
     * @param width Image width
     * @param height Image height
     * @param outputData Output RGBA buffer
     * @param mode Processing mode
     */
    external fun processFrame(
        inputData: ByteArray,
        width: Int,
        height: Int,
        outputData: ByteArray,
        mode: Int
    )

    /**
     * Apply Canny edge detection with custom thresholds
     */
    external fun applyCannyEdgeDetection(
        inputData: ByteArray,
        width: Int,
        height: Int,
        outputData: ByteArray,
        threshold1: Double,
        threshold2: Double
    )

    /**
     * Get OpenCV version string
     */
    external fun getOpenCVVersion(): String

    /**
     * Process frame with mode enum
     */
    fun processFrame(
        inputData: ByteArray,
        width: Int,
        height: Int,
        outputData: ByteArray,
        mode: ProcessingMode
    ) {
        processFrame(inputData, width, height, outputData, mode.value)
    }
}

