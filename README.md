# Highly experimental Mandelbrot viewer in Kotlin Compose Multiplatform

As a way of experimenting with Kotlin Compose I decided to build Mandelbrot plotter. 
It works, but there are some issues, as expected:

- I can't get `Dispatchers.Main` to work in `MandelbrotViewerModel.kt` without crashing the iOS simulator
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
Remove the `Dispatchers.Main` in the `init` routine in `MandelbrotViewerModel.kt` or fix it and file a PR ;-)
Just run the `iosDeployIpadDebug` task in the `compose ios` group

