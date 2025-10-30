/**
 * Type definitions for the web viewer
 */

export interface FrameData {
    width: number;
    height: number;
    data: string; // Base64 encoded image
    timestamp: number;
    processingMode: ProcessingMode;
}

export enum ProcessingMode {
    RAW = "RAW",
    GRAYSCALE = "GRAYSCALE",
    EDGE_DETECTION = "EDGE_DETECTION"
}

export interface FrameStats {
    fps: number;
    width: number;
    height: number;
    mode: ProcessingMode;
    lastUpdate: number;
}

export interface ViewerConfig {
    canvasId: string;
    statsContainerId: string;
    autoUpdate: boolean;
}

