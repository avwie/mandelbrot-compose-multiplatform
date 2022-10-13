package nl.avwie.mandelbrot

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication

fun main() = singleWindowApplication(
    title = "Mandelbrot Viewer",
    state = WindowState(size = DpSize(1280.dp, 800.dp))
) {
    MandelbrotApp()
}