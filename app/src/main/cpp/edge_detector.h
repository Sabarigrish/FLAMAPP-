#ifndef EDGEDETECTION_EDGE_DETECTOR_H
#define EDGEDETECTION_EDGE_DETECTOR_H

#include <opencv2/opencv.hpp>

/**
 * Edge detection processor using OpenCV
 */
class EdgeDetector {
public:
    EdgeDetector();
    ~EdgeDetector();

    /**
     * Detect edges using Canny algorithm
     * @param input Input image (RGBA)
     * @param threshold1 First threshold for hysteresis
     * @param threshold2 Second threshold for hysteresis
     * @return Grayscale edge image
     */
    cv::Mat detectEdges(const cv::Mat &input, double threshold1, double threshold2);

    /**
     * Apply Gaussian blur for preprocessing
     */
    cv::Mat applyGaussianBlur(const cv::Mat &input, int kernelSize = 5);

    /**
     * Convert to grayscale
     */
    cv::Mat toGrayscale(const cv::Mat &input);

private:
    cv::Mat workingMat;
};

#endif //EDGEDETECTION_EDGE_DETECTOR_H

