package posidon.texter.ui.view

import posidon.texter.Window
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Rectangle
import javax.swing.*
import javax.swing.plaf.basic.BasicScrollBarUI


class MinimalScrollBarUI : BasicScrollBarUI() {

    override fun configureScrollBarColors() {
        thumbColor = Window.theme.uiHighlight
        scrollBarWidth = 5
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
            g.fillRect(0, 0, thumbBounds.width, thumbBounds.height)
        }
    }

    override fun paintTrack(g: Graphics?, c: JComponent?, bounds: Rectangle?) {}

    private fun createZeroButton(): JButton? {
        val button = JButton()
        val zeroDim = Dimension(0, 0)
        button.preferredSize = zeroDim
        button.minimumSize = zeroDim
        button.maximumSize = zeroDim
        return button
    }
}