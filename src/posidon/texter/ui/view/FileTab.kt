package posidon.texter.ui.view

import posidon.texter.Window
import posidon.texter.backend.TextFile
import posidon.texter.ui.Constants
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Insets
import javax.swing.*
import javax.swing.text.StyledDocument

class FileTab(label: String, icon: ImageIcon, val file: TextFile, val document: StyledDocument) : JPanel() {

    private val labelView: JLabel
    private val iconView: JButton

    var active = false
        set(value) {
            field = value
            background = if (active) Window.theme.uiHighlight
            else Window.theme.uiBG
        }

    fun updateTheme() {
        background = if (active) Window.theme.uiHighlight
        else Window.theme.uiBG
        file.colorAll(document)
        iconView.icon = file.icon
    }

    init {
        preferredSize = Dimension(192, 32)
        size = Dimension(192, 32)
        background = Window.theme.uiBG
        isFocusable = false
        layout = BorderLayout()
        border = BorderFactory.createEmptyBorder(0, 0, 0, 0)

        add(JButton(icon).apply {
            margin = Insets(0, 0, 0, 0)
            isOpaque = false
            isContentAreaFilled = false
            isBorderPainted = false
            isFocusable = false
            border = BorderFactory.createEmptyBorder(6, 6, 6, 6)
            iconView = this
        }, BorderLayout.WEST)

        add(JLabel(label).apply{
            font = Constants.uiFont
            foreground = Window.theme.text
            isOpaque = false
            labelView = this
        }, BorderLayout.CENTER)
    }
}