package nl.avwie.mandelbrot

import ColorMap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class MandelbrotViewerModel(private val scope: CoroutineScope) {

    private var currentCalculationJob : Job = Job()

    private val viewPorts = MutableStateFlow(
        Viewport(
            width = 1,
            height = 1,
            x = -1.141,
            y = -0.2678,
            xScale = 0.1
        )
    )

    private val limits = MutableStateFlow(512)
    private val parallel = MutableStateFlow(true)
    private val colorMaps = MutableStateFlow(ColorMap.Plasma)
    private val mandelbrots = MutableStateFlow(MandelbrotMap.UNIT);

    private val options = combine(viewPorts, limits) { viewPort: Viewport, limit: Int ->
        MandelbrotMap.Options.fromViewport(viewPort, limit)
    }

    val bitmaps = combine(mandelbrots, colorMaps) { mandelbrot, colorMap -> mandelbrot.asBitmap(colorMap) }

    init {
        scope.launch(Dispatchers.Default) {
            combine(options, parallel) { options: MandelbrotMap.Options, parallel: Boolean ->
                calculateMandelbrotMaps(options, parallel, resolutions = listOf(64, 4, 1))
            }.collect()
        }
    }

    fun updateColorMap(colorMap: ColorMap) {
        this.colorMaps.update { colorMap }
    }

    fun updateSize(size: IntSize) {
        this.viewPorts.update { it.copy(width = size.width, height = size.height) }
    }

    fun updatePosition(offset: Offset) {
        this.viewPorts.update { viewPort ->
            val (x, y) = MandelbrotMap.Options
                .fromViewport(viewPort, limits.value)
                .convertScreenCoordinates(offset.x, offset.y)
            viewPort.copy(x = x, y = y)
        }
    }

    fun zoom(amount: Float) {
        this.viewPorts.update { viewPort -> viewPort.copy(xScale = viewPort.xScale * amount) }
    }

    fun setParallel(parallel: Boolean) {
        this.parallel.update { parallel }
    }

    private suspend fun calculateMandelbrotMaps(options: MandelbrotMap.Options, parallel: Boolean, resolutions: List<Int>) = coroutineScope {
        // this can probably be done nicer, but don't know how atm
        currentCalculationJob.cancel()
        currentCalculationJob = launch {
            resolutions.forEach { resolution ->
                mandelbrots.update {
                    MandelbrotMap.run(options.withResolution(resolution), parallel)
                }
            }
        }
    }
}