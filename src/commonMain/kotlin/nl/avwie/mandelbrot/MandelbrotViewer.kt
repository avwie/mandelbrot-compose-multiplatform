package nl.avwie.mandelbrot

import ColorMap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable fun MandelbrotViewer(model: MandelbrotViewerModel) {
    val requester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        requester.requestFocus()
    }

    Box {
        MandelbrotPlot(requester, model)

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            MandelbrotControls(requester, model)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable fun MandelbrotPlot(
    requester: FocusRequester,
    model: MandelbrotViewerModel
) {
    val bitmap by model.bitmaps.collectAsState(EMPTY_BITMAP)

    Canvas(modifier = Modifier
        .fillMaxSize()
        .focusRequester(requester)
        .focusable()
        .onPointerEvent(PointerEventType.Release) {
            val position = it.changes.first().position
            requester.requestFocus()
            model.updatePosition(position)
        }
        .onKeyEvent {
            when {
                it.type != KeyEventType.KeyUp -> false
                it.key == Key.DirectionUp -> {
                    model.zoom(0.5f); true
                }
                it.key == Key.DirectionDown -> {
                    model.zoom(2.0f); true
                }
                else -> false
            }
        }
        .onSizeChanged { size -> model.updateSize(size) }
    ) {
        drawImage(
            image = bitmap.asComposeImageBitmap(),
            dstSize = IntSize(size.width.toInt(), size.height.toInt())
        )
    }
}

@Composable fun MandelbrotControls(
    requester: FocusRequester,
    model: MandelbrotViewerModel
) {
    Surface(shape = RoundedCornerShape(4.dp), color = Color.White.copy(alpha = 0.7f) ,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            RunModeSelector(onChange = {
                model.setParallel(it)
                requester.requestFocus()
            })

            Spacer(modifier = Modifier.weight(1.0f))
            ColorMapSelector(onSelectColorMap = { model.updateColorMap(it) })
        }
    }
}

@Composable fun RunModeSelector(
    onChange: (parallel: Boolean) -> Unit = {}
) {
    var parallel by remember { mutableStateOf(true) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Switch(
            checked = parallel,
            onCheckedChange = { value ->
                parallel = value
                onChange(value)
            }
        )
        Text(if (parallel) "Parallel processing" else "Single threaded processing")
    }
}

@Composable fun ColorMapSelector(
    onSelectColorMap: (ColorMap) -> Unit = {}
) {
    var selectedColorMap by remember { mutableStateOf(ColorMap.Plasma) }

    val colorMaps = listOf(
        "Plasma" to ColorMap.Plasma,
        "Viridis" to ColorMap.Viridis,
        "HSV" to ColorMap.HSV
    )

    fun updateColorMap(colorMap: ColorMap) {
        selectedColorMap = colorMap
        onSelectColorMap(colorMap)
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        colorMaps.forEach { (label, colorMap) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = (colorMap == selectedColorMap),
                    onClick = { updateColorMap(colorMap) }
                )
                Text(label)
            }
        }
    }
}