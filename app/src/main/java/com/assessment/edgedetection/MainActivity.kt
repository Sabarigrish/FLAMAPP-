package com.assessment.edgedetection

import android.Manifest
import android.content.pm.PackageManager
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.assessment.edgedetection.camera.CameraHandler
import com.assessment.edgedetection.databinding.ActivityMainBinding
import com.assessment.edgedetection.gl.GLRenderer
import com.assessment.edgedetection.jni.NativeProcessor
import com.assessment.edgedetection.utils.FPSCounter

/**
 * Main activity - integrates camera, OpenCV processing, and OpenGL rendering
 */
class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var glRenderer: GLRenderer
    private lateinit var cameraHandler: CameraHandler
    private lateinit var nativeProcessor: NativeProcessor
    private lateinit var fpsCounter: FPSCounter
    
    private var currentMode = NativeProcessor.ProcessingMode.EDGE_DETECTION
    private var processingBuffer: ByteArray? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startCamera()
        } else {
            Toast.makeText(this, "Camera permission required", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Initialize components
        nativeProcessor = NativeProcessor()
        fpsCounter = FPSCounter()
        cameraHandler = CameraHandler(this)
        
        // Setup OpenGL Surface View
        setupGLSurfaceView()
        
        // Setup UI controls
        setupControls()
        
        // Display OpenCV version
        try {
            val version = nativeProcessor.getOpenCVVersion()
            binding.tvInfo.text = "OpenCV: $version"
            Log.i(TAG, "OpenCV version: $version")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get OpenCV version", e)
            binding.tvInfo.text = "OpenCV: Error"
        }
        
        // Check camera permission
        if (cameraHandler.hasPermission()) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun setupGLSurfaceView() {
        binding.glSurfaceView.setEGLContextClientVersion(2)
        
        glRenderer = GLRenderer()
        glRenderer.onSurfaceCreatedListener = {
            Log.i(TAG, "OpenGL surface ready")
        }
        
        binding.glSurfaceView.setRenderer(glRenderer)
        binding.glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }

    private fun setupControls() {
        // Mode toggle button
        binding.btnToggleMode.setOnClickListener {
            currentMode = when (currentMode) {
                NativeProcessor.ProcessingMode.RAW -> {
                    binding.btnToggleMode.text = "Mode: Grayscale"
                    NativeProcessor.ProcessingMode.GRAYSCALE
                }
                NativeProcessor.ProcessingMode.GRAYSCALE -> {
                    binding.btnToggleMode.text = "Mode: Edge Detection"
                    NativeProcessor.ProcessingMode.EDGE_DETECTION
                }
                NativeProcessor.ProcessingMode.EDGE_DETECTION -> {
                    binding.btnToggleMode.text = "Mode: Raw"
                    NativeProcessor.ProcessingMode.RAW
                }
            }
            
            Toast.makeText(this, "Switched to ${currentMode.name}", Toast.LENGTH_SHORT).show()
        }
        
        binding.btnToggleMode.text = "Mode: Edge Detection"
    }

    private fun startCamera() {
        cameraHandler.onFrameAvailable = { data, width, height ->
            processFrame(data, width, height)
        }
        
        cameraHandler.startCamera(this) {
            Log.i(TAG, "Camera started")
            runOnUiThread {
                Toast.makeText(this, "Camera ready", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun processFrame(inputData: ByteArray, width: Int, height: Int) {
        try {
            // Allocate output buffer if needed
            if (processingBuffer == null || processingBuffer!!.size != inputData.size) {
                processingBuffer = ByteArray(inputData.size)
            }
            
            val outputBuffer = processingBuffer!!
            
            // Process frame using native code
            nativeProcessor.processFrame(
                inputData,
                width,
                height,
                outputBuffer,
                currentMode
            )
            
            // Update OpenGL texture
            glRenderer.updateFrame(outputBuffer, width, height)
            
            // Update FPS counter
            val fps = fpsCounter.tick()
            if (fps != null) {
                runOnUiThread {
                    binding.tvFps.text = String.format("FPS: %.1f | %dx%d | %s", 
                        fps, width, height, currentMode.name)
                }
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error processing frame", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraHandler.stopCamera()
    }

    override fun onPause() {
        super.onPause()
        binding.glSurfaceView.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.glSurfaceView.onResume()
    }
}

