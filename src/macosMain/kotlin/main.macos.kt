import androidx.compose.ui.window.Window
import nl.avwie.mandelbrot.MandelbrotApp
import platform.AppKit.NSApp
import platform.AppKit.NSApplication

fun main() {
    NSApplication.sharedApplication()
    Window("Mandelbrot") {
        MandelbrotApp()
    }
    NSApp?.run()
}