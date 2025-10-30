package com.assessment.edgedetection.utils

/**
 * Simple FPS counter
 */
class FPSCounter {
    private var frameCount = 0
    private var lastTime = System.currentTimeMillis()
    private var fps = 0.0
    
    /**
     * Call this on each frame
     * @return Current FPS, or null if not yet calculated
     */
    fun tick(): Double? {
        frameCount++
        val currentTime = System.currentTimeMillis()
        val deltaTime = currentTime - lastTime
        
        // Update FPS every second
        if (deltaTime >= 1000) {
            fps = (frameCount * 1000.0) / deltaTime
            frameCount = 0
            lastTime = currentTime
            return fps
        }
        
        return if (fps > 0) fps else null
    }
    
    fun getFPS(): Double = fps
}

