package posidon.texter

import posidon.texter.ui.ScrollBar
import posidon.texter.ui.Theme
import java.awt.*
import javax.swing.*
import javax.swing.border.Border
import javax.swing.text.StyledDocument


class Window {

    companion object {
        private val jFrame = JFrame(appInfo.NAME)
        private var textArea = JTextPane()
        private lateinit var document: StyledDocument
        var theme: Theme = Theme()
            set(value) {
                field = value
                textArea.foreground = Color(theme.textAreaFG)
                textArea.caretColor = Color(theme.textAreaCaret)
                jFrame.background = Color(theme.textAreaBG)
                jFrame.contentPane.background = Color(theme.textAreaBG)
            }
    }

    init {
        jFrame.size = Dimension(appInfo.INIT_WIDTH, appInfo.INIT_HEIGHT)
        jFrame.minimumSize = Dimension(appInfo.MIN_WIDTH, appInfo.MIN_HEIGHT)
        jFrame.isResizable = true
        jFrame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        jFrame.setLocationRelativeTo(null)

        val monospace = Font(Font.MONOSPACED, Font.PLAIN, 15)

        textArea.isEditable = true
        textArea.font = monospace
        textArea.isOpaque = false
        textArea.border = BorderFactory.createEmptyBorder(12, 12, 12, 12)
        document = textArea.styledDocument

        val noWrapPanel = JPanel(BorderLayout())
        noWrapPanel.add(textArea)
        noWrapPanel.isOpaque = false
        val scroll = JScrollPane(noWrapPanel)
        scroll.border = null
        scroll.isOpaque = false
        scroll.viewport.isOpaque = false
        scroll.verticalScrollBar.setUI(ScrollBar())
        scroll.horizontalScrollBar.setUI(ScrollBar())
        scroll.verticalScrollBar.unitIncrement = 10
        scroll.horizontalScrollBar.unitIncrement = 10


        //jFrame.add(input, BorderLayout.NORTH);
        jFrame.add(scroll, BorderLayout.CENTER)

        theme = Theme()

        jFrame.isVisible = true
    }

    var title: String
    get() = jFrame.title
    set(value) { jFrame.title = value }
}