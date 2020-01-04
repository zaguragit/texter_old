package posidon.texter.ui.view

import posidon.texter.Window
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Rectangle
import javax.swing.*
import javax.swing.plaf.basic.BasicScrollBarUI


class ScrollBarUI : BasicScrollBarUI() {
    override fun configureScrollBarColors() {
        this.thumbColor = Window.theme.scrollBarFG
        this.trackColor = Window.theme.scrollBarBG
        this.scrollBarWidth = 10
    }

    override fun installComponents() {
        incrButton = createZeroButton()
        decrButton = createZeroButton()
        scrollbar.isEnabled = scrollbar.isEnabled
    }

    override fun paintThumb(g: Graphics, c: JComponent, thumbBounds: Rectangle) {
        if (!thumbBounds.isEmpty && scrollbar.isEnabled) {
            g.translate(thumbBounds.x, thumbBounds.y)
            g.color = thumbColor
            g.fillRect(0, 0, thumbBounds.width - 1, thumbBounds.height - 1)
        }
    }

    private fun createZeroButton(): JButton? {
        val button = JButton()
        val zeroDim = Dimension(0, 0)
        button.preferredSize = zeroDim
        button.minimumSize = zeroDim
        button.maximumSize = zeroDim
        return button
    }
}