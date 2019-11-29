package posidon.texter

import java.awt.Dimension
import javax.swing.JFrame


class Window {
    private val jFrame = JFrame(appInfo.NAME)

    init {
        jFrame.size = Dimension(appInfo.INIT_WIDTH, appInfo.INIT_HEIGHT)
        jFrame.minimumSize = Dimension(appInfo.MIN_WIDTH, appInfo.MIN_HEIGHT)

        jFrame.isResizable = true
        jFrame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        jFrame.setLocationRelativeTo(null)
        jFrame.isVisible = true
    }

    fun setTitle(string: String) {
        jFrame.title = string
    }

    fun getTitle(string: String): String {
        return jFrame.title
    }
}