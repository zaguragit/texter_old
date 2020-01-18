package posidon.texter.ui.view

import posidon.texter.AppInfo
import posidon.texter.Window
import posidon.texter.backend.TextFile
import posidon.texter.ui.Constants
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Insets
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.*
import javax.swing.text.StyledDocument
import javax.swing.undo.UndoManager

class FileTab(label: String, icon: ImageIcon, val file: TextFile, val document: StyledDocument) : JPanel() {

    private val labelView: JLabel
    private val iconView: JButton
    private val closeTabBtn: JButton
    private fun doesShowNumbers() = file.extension != "md"

    val undoManager = UndoManager()

    var active = false
        set(value) {
            field = value
            if (active) {
                if (Window.activeTab == null) Window.textArea.isVisible = true
                else Window.activeTab!!.active = false
                Window.textNumbers.isVisible = doesShowNumbers()
                background = Window.theme.uiHighlight
                Window.activeTab = null
                Window.textArea.styledDocument = document
                Window.activeTab = this@FileTab
                Window.title = AppInfo.NAME + " - " + file.name
            } else {
                background = Window.theme.uiBG
            }
        }

    fun updateTheme() {
        background = if (active) Window.theme.uiHighlight
        else Window.theme.uiBG
        file.colorAll(document)
        iconView.icon = file.icon
        closeTabBtn.icon = Window.theme.iconTheme.close_tab_hover
        closeTabBtn.disabledIcon = Window.theme.iconTheme.close_tab
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

        add(Button(icon = Window.theme.iconTheme.close_tab_hover).apply {
            isEnabled = false
            disabledIcon = Window.theme.iconTheme.close_tab
            margin = Insets(0, 0, 0, 0)
            isOpaque = false
            isContentAreaFilled = false
            isBorderPainted = false
            border = BorderFactory.createEmptyBorder(8, 8, 8, 8)
            addActionListener {
                if (Window.activeTab == this@FileTab) {
                    Window.activeTab = null
                    Window.textArea.isVisible = false
                    Window.title = AppInfo.NAME
                    Window.textNumbers.isVisible = false
                }
                Window.tabs.remove(this@FileTab)
                Window.jFrame.validate()
            }
            addMouseListener(object : MouseListener {
                override fun mouseReleased(p0: MouseEvent?) {}
                override fun mouseClicked(p0: MouseEvent?) {}
                override fun mousePressed(p0: MouseEvent?) {}
                override fun mouseEntered(p0: MouseEvent?) { isEnabled = true }
                override fun mouseExited(p0: MouseEvent?) { isEnabled = false }
            })
            closeTabBtn = this
        }, BorderLayout.EAST)

        addMouseListener(object : MouseListener {
            override fun mouseReleased(p0: MouseEvent?) {}
            override fun mouseEntered(p0: MouseEvent?) {}
            override fun mouseExited(p0: MouseEvent?) {}
            override fun mousePressed(p0: MouseEvent?) {}
            override fun mouseClicked(p0: MouseEvent?) { this@FileTab.active = true }
        })
    }
}