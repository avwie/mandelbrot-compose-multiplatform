# Highly experimental Mandelbrot viewer in Kotlin Compose Multiplatform

https://user-images.githubusercontent.com/7676826/195795138-7c80a627-a747-4c6a-8b49-10cc6f742d81.mp4

As a way of experimenting with Kotlin Compose I decided to build Mandelbrot plotter. 
It works, but there are some issues, as expected:

- The Arm64 targets aren't working
- The JS canvas behaves weirdly on resize

However, the JB Compose Multiplatform is highly experimental, so that is expected.

## How to run

Use clicking for centering and use arrow up to zoom in and arrow down to zoom out.

### JVM

Just run the `run` task in the `compose desktop` group.

### JS
Just run the `jsBrowserDevelopmentRun` task in the `kotlin browser` group

### iPad
Just run the `iosDeployIpadDebug` task in the `compose ios` group

