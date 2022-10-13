package nl.avwie.mandelbrot

import ColorMap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.jetbrains.skia.Bitmap
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

class MandelbrotViewerModel(private val scope: CoroutineScope) {

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

    @OptIn(FlowPreview::class)
    private val maps = combine(viewPorts, limits, parallel) { viewPort: Viewport, limit: Int, parallel: Boolean ->
        calculateMandelbrotMap(MandelbrotMap.Options.fromViewport(viewPort).copy(limit = limit), parallel = parallel)
    }.flattenConcat()

    private val _bitmaps = MutableStateFlow(EMPTY_BITMAP)
    val bitmaps : StateFlow<Bitmap> = _bitmaps

    init {
        scope.launch(Dispatchers.Default) {
            combine(maps, colorMaps) { map, colorMap ->
                _bitmaps.update {
                    map.asBitmap(colorMap)
                }
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
                .fromViewport(viewPort)
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

    private suspend fun calculateMandelbrotMap(options: MandelbrotMap.Options, parallel: Boolean): Flow<MandelbrotMap> = flow {
        listOf(64, 16, 4, 1).forEach { resolution ->
            emit(MandelbrotMap.run(options.withResolution(resolution), parallel = parallel))
        }
    }
}