package nl.avwie.mandelbrot

val EMPTY_BITMAP = buildBitmap(1, 1)

data class Viewport(
    val width: Int,
    val height: Int,
    val x: Double,
    val y: Double,
    val xScale: Double
)