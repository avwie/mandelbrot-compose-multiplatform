package nl.avwie.mandelbrot

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope

@Composable fun MandelbrotApp() {
    val scope = rememberCoroutineScope()
    val model = remember { MandelbrotViewerModel(scope) }
    MaterialTheme {
        MandelbrotViewer(model = model)
    }
}