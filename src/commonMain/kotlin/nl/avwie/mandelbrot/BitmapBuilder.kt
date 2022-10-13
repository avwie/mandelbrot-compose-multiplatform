package nl.avwie.mandelbrot

import androidx.compose.ui.graphics.Color
import org.jetbrains.skia.*

interface BitmapBuilderScope {
    operator fun set(index: Int, color: Color)
    operator fun set(x: Int, y: Int, color: Color)
}

fun buildBitmap(width: Int, height: Int, block: BitmapBuilderScope.() -> Unit = {}): Bitmap {
    val builder = BitmapBuilder(width, height)
    block(builder)
    return builder.build()
}

class BitmapBuilder(private val width: Int, private val height: Int): BitmapBuilderScope {
    private val bytes = ByteArray(width * height * ColorType.RGBA_8888.bytesPerPixel)

    override fun set(index: Int, color: Color) {
        bytes[index * ColorType.RGBA_8888.bytesPerPixel + 0] = (color.red * 255).toInt().toByte()
        bytes[index * ColorType.RGBA_8888.bytesPerPixel + 1] = (color.green * 255).toInt().toByte()
        bytes[index * ColorType.RGBA_8888.bytesPerPixel + 2] = (color.blue * 255).toInt().toByte()
        bytes[index * ColorType.RGBA_8888.bytesPerPixel + 3] = (255).toByte()
    }

    override operator fun set(x: Int, y: Int, color: Color) {
        require(x in 0 until width)
        require(y in 0 until height)
        val index = y * width + x
        set(index, color)
    }

    fun build(): Bitmap {
        val bitmap = Bitmap()
        val info = ImageInfo(
            colorInfo = ColorInfo(
                colorType = ColorType.RGBA_8888,
                alphaType = ColorAlphaType.PREMUL,
                colorSpace = ColorSpace.sRGB
            ),
            width = width,
            height = height
        )
        bitmap.allocPixels(info)
        bitmap.installPixels(bytes)
        return bitmap
    }
}