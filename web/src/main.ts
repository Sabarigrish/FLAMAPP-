import { FrameViewer } from './FrameViewer.js';
import { ProcessingMode } from './types.js';

/**
 * Main entry point for the web viewer
 */

let viewer: FrameViewer;

function init(): void {
    console.log('Initializing Edge Detection Web Viewer...');
    
    try {
        viewer = new FrameViewer({
            canvasId: 'frameCanvas',
            statsContainerId: 'statsContainer',
            autoUpdate: true
        });
        
        // Load a sample frame on startup
        viewer.loadSampleFrame();
        
        // Hide loading indicator
        const loadingIndicator = document.getElementById('loadingIndicator');
        if (loadingIndicator) {
            loadingIndicator.style.display = 'none';
        }
        
        // Setup event listeners
        setupEventListeners();
        
        // Update stats every second
        setInterval(() => {
            updateStatsDisplay();
        }, 1000);
        
        console.log('Viewer initialized successfully');
    } catch (error) {
        console.error('Failed to initialize viewer:', error);
        showError('Failed to initialize viewer. Check console for details.');
    }
}

function setupEventListeners(): void {
    // Refresh button
    const refreshBtn = document.getElementById('refreshBtn');
    if (refreshBtn) {
        refreshBtn.addEventListener('click', () => {
            viewer.loadSampleFrame();
        });
    }
    
    // File upload
    const fileInput = document.getElementById('fileInput') as HTMLInputElement;
    if (fileInput) {
        fileInput.addEventListener('change', handleFileUpload);
    }
    
    // Mode selector
    const modeSelector = document.getElementById('modeSelector') as HTMLSelectElement;
    if (modeSelector) {
        modeSelector.addEventListener('change', () => {
            const mode = modeSelector.value as ProcessingMode;
            console.log('Mode changed to:', mode);
        });
    }
}

function handleFileUpload(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    
    if (!file) return;
    
    const reader = new FileReader();
    reader.onload = (e) => {
        const result = e.target?.result as string;
        if (result) {
            const img = new Image();
            img.onload = () => {
                viewer.loadBase64Frame(
                    result,
                    img.width,
                    img.height,
                    ProcessingMode.EDGE_DETECTION
                );
            };
            img.src = result;
        }
    };
    reader.readAsDataURL(file);
}

function updateStatsDisplay(): void {
    const stats = viewer.getStats();
    const timeSinceUpdate = Date.now() - stats.lastUpdate;
    
    const statsElement = document.getElementById('statsContainer');
    if (statsElement) {
        statsElement.innerHTML = `
            <div class="stat-item">
                <span class="stat-label">FPS:</span>
                <span class="stat-value">${stats.fps.toFixed(1)}</span>
            </div>
            <div class="stat-item">
                <span class="stat-label">Resolution:</span>
                <span class="stat-value">${stats.width}x${stats.height}</span>
            </div>
            <div class="stat-item">
                <span class="stat-label">Mode:</span>
                <span class="stat-value">${stats.mode}</span>
            </div>
            <div class="stat-item">
                <span class="stat-label">Last Update:</span>
                <span class="stat-value">${timeSinceUpdate}ms ago</span>
            </div>
        `;
    }
}

function showError(message: string): void {
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message';
    errorDiv.textContent = message;
    document.body.insertBefore(errorDiv, document.body.firstChild);
}

// Initialize when DOM is ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
} else {
    init();
}

// Export for potential external use
export { viewer, FrameViewer };

