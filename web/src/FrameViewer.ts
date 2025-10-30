import { FrameData, FrameStats, ProcessingMode, ViewerConfig } from './types.js';

/**
 * Frame viewer class - displays processed frames and stats
 */
export class FrameViewer {
    private canvas: HTMLCanvasElement;
    private ctx: CanvasRenderingContext2D;
    private statsContainer: HTMLElement;
    private config: ViewerConfig;
    
    private currentFrame: FrameData | null = null;
    private stats: FrameStats;
    private frameCount = 0;
    private lastFpsUpdate = 0;

    constructor(config: ViewerConfig) {
        this.config = config;
        
        const canvas = document.getElementById(config.canvasId) as HTMLCanvasElement;
        if (!canvas) {
            throw new Error(`Canvas element with id '${config.canvasId}' not found`);
        }
        this.canvas = canvas;
        
        const ctx = canvas.getContext('2d');
        if (!ctx) {
            throw new Error('Failed to get 2D context');
        }
        this.ctx = ctx;
        
        const statsContainer = document.getElementById(config.statsContainerId);
        if (!statsContainer) {
            throw new Error(`Stats container with id '${config.statsContainerId}' not found`);
        }
        this.statsContainer = statsContainer;
        
        this.stats = {
            fps: 0,
            width: 0,
            height: 0,
            mode: ProcessingMode.RAW,
            lastUpdate: Date.now()
        };
        
        console.log('FrameViewer initialized');
    }

    /**
     * Update frame data and render
     */
    public updateFrame(frameData: FrameData): void {
        this.currentFrame = frameData;
        this.stats.width = frameData.width;
        this.stats.height = frameData.height;
        this.stats.mode = frameData.processingMode;
        this.stats.lastUpdate = Date.now();
        
        this.renderFrame();
        this.updateStats();
        this.calculateFPS();
    }

    /**
     * Load frame from base64 string
     */
    public loadBase64Frame(base64Data: string, width: number, height: number, mode: ProcessingMode): void {
        const frameData: FrameData = {
            width,
            height,
            data: base64Data,
            timestamp: Date.now(),
            processingMode: mode
        };
        
        this.updateFrame(frameData);
    }

    /**
     * Load sample/demo frame
     */
    public loadSampleFrame(): void {
        // Create a sample gradient image with edge-like patterns
        const width = 640;
        const height = 480;
        const tempCanvas = document.createElement('canvas');
        tempCanvas.width = width;
        tempCanvas.height = height;
        const tempCtx = tempCanvas.getContext('2d');
        
        if (!tempCtx) return;
        
        // Draw edge detection pattern
        tempCtx.fillStyle = '#000000';
        tempCtx.fillRect(0, 0, width, height);
        
        // Draw white edges
        tempCtx.strokeStyle = '#FFFFFF';
        tempCtx.lineWidth = 2;
        
        // Draw some sample edges
        for (let i = 0; i < 20; i++) {
            tempCtx.beginPath();
            tempCtx.moveTo(Math.random() * width, Math.random() * height);
            tempCtx.lineTo(Math.random() * width, Math.random() * height);
            tempCtx.stroke();
        }
        
        // Draw circles
        for (let i = 0; i < 5; i++) {
            tempCtx.beginPath();
            tempCtx.arc(
                Math.random() * width,
                Math.random() * height,
                20 + Math.random() * 50,
                0,
                Math.PI * 2
            );
            tempCtx.stroke();
        }
        
        const base64 = tempCanvas.toDataURL('image/png');
        this.loadBase64Frame(base64, width, height, ProcessingMode.EDGE_DETECTION);
        
        console.log('Sample frame loaded');
    }

    /**
     * Render current frame to canvas
     */
    private renderFrame(): void {
        if (!this.currentFrame) return;
        
        const img = new Image();
        img.onload = () => {
            // Resize canvas if needed
            if (this.canvas.width !== this.currentFrame!.width || 
                this.canvas.height !== this.currentFrame!.height) {
                this.canvas.width = this.currentFrame!.width;
                this.canvas.height = this.currentFrame!.height;
            }
            
            // Clear and draw
            this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
            this.ctx.drawImage(img, 0, 0);
        };
        img.src = this.currentFrame.data;
    }

    /**
     * Update stats display
     */
    private updateStats(): void {
        const timeSinceUpdate = Date.now() - this.stats.lastUpdate;
        
        this.statsContainer.innerHTML = `
            <div class="stat-item">
                <span class="stat-label">FPS:</span>
                <span class="stat-value">${this.stats.fps.toFixed(1)}</span>
            </div>
            <div class="stat-item">
                <span class="stat-label">Resolution:</span>
                <span class="stat-value">${this.stats.width}x${this.stats.height}</span>
            </div>
            <div class="stat-item">
                <span class="stat-label">Mode:</span>
                <span class="stat-value">${this.stats.mode}</span>
            </div>
            <div class="stat-item">
                <span class="stat-label">Last Update:</span>
                <span class="stat-value">${timeSinceUpdate}ms ago</span>
            </div>
        `;
    }

    /**
     * Calculate FPS
     */
    private calculateFPS(): void {
        this.frameCount++;
        const now = Date.now();
        const delta = now - this.lastFpsUpdate;
        
        if (delta >= 1000) {
            this.stats.fps = (this.frameCount * 1000) / delta;
            this.frameCount = 0;
            this.lastFpsUpdate = now;
        }
    }

    /**
     * Get current stats
     */
    public getStats(): FrameStats {
        return { ...this.stats };
    }
}

