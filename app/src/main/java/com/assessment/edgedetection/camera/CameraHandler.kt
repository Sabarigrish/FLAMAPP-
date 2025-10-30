package com.assessment.edgedetection.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.util.Log
import android.util.Size
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Camera handler using CameraX API
 */
class CameraHandler(private val context: Context) {

    companion object {
        private const val TAG = "CameraHandler"
        const val CAMERA_PERMISSION = Manifest.permission.CAMERA
    }

    private var cameraProvider: ProcessCameraProvider? = null
    private var camera: Camera? = null
    private var imageAnalysis: ImageAnalysis? = null
    private val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    
    var onFrameAvailable: ((ByteArray, Int, Int) -> Unit)? = null

    /**
     * Check if camera permission is granted
     */
    fun hasPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            CAMERA_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Start camera with preview and analysis
     */
    fun startCamera(lifecycleOwner: LifecycleOwner, onReady: () -> Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindCamera(lifecycleOwner)
            onReady()
        }, ContextCompat.getMainExecutor(context))
    }

    private fun bindCamera(lifecycleOwner: LifecycleOwner) {
        val cameraProvider = cameraProvider ?: return

        // Preview use case (not displayed directly, just for camera lifecycle)
        val preview = Preview.Builder()
            .build()

        // Image analysis use case - this is where we get frames
        imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(640, 480))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .build()
            .also {
                it.setAnalyzer(cameraExecutor, FrameAnalyzer())
            }

        // Select back camera
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            // Unbind all use cases before rebinding
            cameraProvider.unbindAll()

            // Bind use cases to camera
            camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalysis
            )

            Log.i(TAG, "Camera bound successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Camera binding failed", e)
        }
    }

    /**
     * Stop camera and release resources
     */
    fun stopCamera() {
        cameraProvider?.unbindAll()
        cameraExecutor.shutdown()
    }

    /**
     * Frame analyzer that converts ImageProxy to byte array
     */
    private inner class FrameAnalyzer : ImageAnalysis.Analyzer {
        override fun analyze(imageProxy: ImageProxy) {
            try {
                // Convert ImageProxy to RGBA byte array
                val buffer = imageProxy.planes[0].buffer
                val data = ByteArray(buffer.remaining())
                buffer.get(data)
                
                val width = imageProxy.width
                val height = imageProxy.height
                
                // Deliver frame to callback
                onFrameAvailable?.invoke(data, width, height)
                
            } catch (e: Exception) {
                Log.e(TAG, "Error analyzing frame", e)
            } finally {
                imageProxy.close()
            }
        }
    }
}

