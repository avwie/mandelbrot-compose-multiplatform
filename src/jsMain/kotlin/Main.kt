import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.Window
import kotlinx.browser.document
import kotlinx.browser.window
import nl.avwie.mandelbrot.MandelbrotViewer
import nl.avwie.mandelbrot.MandelbrotViewerModel
import org.jetbrains.skiko.wasm.onWasmReady
import org.w3c.dom.HTMLCanvasElement

fun main() {
    val canvas = document.getElementById("ComposeTarget") as HTMLCanvasElement
    window.addEventListener("resize", { resizeCanvas(canvas) }, false)
    resizeCanvas(canvas)

    onWasmReady {
        Window("Mandelbrot") {
            val scope = rememberCoroutineScope()
            MandelbrotViewer(model = MandelbrotViewerModel(scope))
        }
    }
}

fun resizeCanvas(canvas: HTMLCanvasElement) {
    canvas.width = window.innerWidth
    canvas.height = window.innerHeight
}