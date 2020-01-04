package posidon.texter.ui.view

import java.awt.Component
import javax.swing.JScrollPane
import kotlin.math.max
import kotlin.math.min

internal class HorizontalScrollPane(component: Component?) : JScrollPane(component) {
    init {
        val horizontalScrollBar = getHorizontalScrollBar()
        isWheelScrollingEnabled = false
        addMouseWheelListener {
            val newVal = horizontalScrollBar.value + horizontalScrollBar.blockIncrement * it.scrollAmount * it.wheelRotation
            if (it.wheelRotation > 0) horizontalScrollBar.value = min(newVal, horizontalScrollBar.maximum)
            else if (it.wheelRotation < 0) horizontalScrollBar.value = max(newVal, 0)
        }
    }
}