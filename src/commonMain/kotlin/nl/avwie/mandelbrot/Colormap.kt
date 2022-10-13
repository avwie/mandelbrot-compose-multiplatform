import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

fun interface ColorMap {
    operator fun get(value: Float): Color

    companion object {
        val Plasma : ColorMap = SteppedColorMap(
            0.00f to Color(13, 8, 135),
            0.14f to Color(84, 2, 163),
            0.29f to Color(139, 10, 165),
            0.43f to Color(185, 50, 137),
            0.57f to Color(219, 92, 104),
            0.71f to Color(244, 136, 73),
            0.86f to Color(254, 188, 43),
            1.00f to Color(240, 249, 33)
        )

        val Viridis : ColorMap = SteppedColorMap(
            0.00f to Color(68, 1, 84),
            0.14f to Color(70, 50, 127),
            0.29f to Color(54, 92, 141),
            0.43f to Color(39, 127, 142),
            0.57f to Color(31, 161, 135),
            0.71f to Color(74, 194, 109),
            0.86f to Color(159, 218, 58),
            1.00f to Color(253, 231, 37)
        )

        val HSV = ColorMap {
            val boundedValue = it.coerceIn(0.0f, 1.0f)
            Color.hsv((boundedValue * 360.0).toFloat(), 1.0f, 1.0f)
        }
    }
}

class SteppedColorMap(
    private val steps : List<Pair<Float, Color>>
) : ColorMap {
    constructor(vararg steps: Pair<Float, Color>): this(steps.toList())

    init {
        require(steps.first().first == 0.0f)
        require(steps.map { it.first }.windowed(2).all { (a, b) -> a < b })
        require(steps.last().first == 1.0f)
    }

    override operator fun get(value: Float): Color {
        val boundedValue = value.coerceIn(0.0f, 1.0f)
        val (start, stop) = steps.windowed(2).first { (start, stop) -> start.first <= value && stop.first >= value  }
        val fraction = (boundedValue - start.first) / (stop.first - start.first)
        return lerp(start.second, stop.second, fraction)
    }
}