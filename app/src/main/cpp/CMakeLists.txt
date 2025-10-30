cmake_minimum_required(VERSION 3.22.1)

project("edgedetection")

set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wall")

# OpenCV headers (local copy)
include_directories(${CMAKE_SOURCE_DIR}/include)

# Find system libraries
find_library(log-lib log)
find_library(android-lib android)
find_library(GLESv2-lib GLESv2)
find_library(EGL-lib EGL)

# OpenCV library - look in jniLibs directory
set(OPENCV_LIB_DIR ${CMAKE_SOURCE_DIR}/../jniLibs/${ANDROID_ABI})

# Import OpenCV as a pre-built library
add_library(opencv_java4 SHARED IMPORTED)
set_target_properties(opencv_java4 PROPERTIES
    IMPORTED_LOCATION ${OPENCV_LIB_DIR}/libopencv_java4.so
)

# Source files
file(GLOB_RECURSE CPP_SOURCES
    "native_processor.cpp"
    "edge_detector.cpp"
)

# Create native library
add_library(${CMAKE_PROJECT_NAME} SHARED ${CPP_SOURCES})

# Link libraries
target_link_libraries(${CMAKE_PROJECT_NAME}
    opencv_java4
    ${log-lib}
    ${android-lib}
    ${GLESv2-lib}
    ${EGL-lib}
)
