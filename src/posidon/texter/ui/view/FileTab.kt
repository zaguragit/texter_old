package posidon.texter.ui.view

import posidon.texter.AppInfo
import posidon.texter.Window
import posidon.texter.backend.TextFile
import posidon.texter.backend.syntaxHighlighters.SyntaxHighlighter
import posidon.texter.ui.Constants
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Insets
import java.awt.Point
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.*
import javax.swing.text.*
import javax.swing.undo.UndoManager

class FileTab(label: String, icon: ImageIcon, val file: TextFile, private val document: StyledDocument) : JPanel() {

    private val labelView: JLabel
    private val iconView: JButton
    private val closeTabBtn: JButton
    private var caretPos = 0
    private var scrollPosition = Point(0, 0)

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
                Window.textArea.caretPosition = caretPos
                Window.scroll.viewport.viewPosition = scrollPosition
                Window.textArea.indentSize = 4
                Window.activeTab = this@FileTab
                Window.title = AppInfo.NAME + " - " + file.name
            } else {
                scrollPosition = Window.scroll.viewport.viewPosition
                caretPos = Window.textArea.caretPosition
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

    /*private fun setTabs(textPane: JTextPane, charactersPerTab: Int) {
        val fm = textPane.getFontMetrics(textPane.font)
        val charWidth = fm.charWidth(' ')
        val tabWidth = charWidth * charactersPerTab
        val tabs = arrayOfNulls<TabStop>(5)
        for (j in tabs.indices) {
            val tab = j + 1
            tabs[j] = TabStop((tab * tabWidth).toFloat())
        }
        val tabSet = TabSet(tabs)
        val attributes = SimpleAttributeSet()
        StyleConstants.setTabSet(attributes, tabSet)
        val length = textPane.document.length
        textPane.styledDocument.setParagraphAttributes(0, length, attributes, false)
    }

    fun indentText(offset: Int, length: Int) {
        val rootElement: Element = document.defaultRootElement
        val firstLine: Int = rootElement.getElementIndex(offset)
        val lastLine: Int = rootElement.getElementIndex(offset + length)
        for (i in firstLine..lastLine)
            document.insertString(rootElement.getElement(i).startOffset, "\t", SyntaxHighlighter.defaultTextStyle())
    }

    fun unindentText(offset: Int, length: Int) {
        val rootElement: Element = document.defaultRootElement
        val firstLine: Int = rootElement.getElementIndex(offset)
        val lastLine: Int = rootElement.getElementIndex(offset + length)
        for (i in firstLine..lastLine) {
            when {
                document.getText(rootElement.getElement(i).startOffset, 1) == "\t" -> document.remove(rootElement.getElement(i).startOffset, 1)
                document.getText(rootElement.getElement(i).startOffset, 4) == "    " -> document.remove(rootElement.getElement(i).startOffset, 4)
                document.getText(rootElement.getElement(i).startOffset, 3) == "   " -> document.remove(rootElement.getElement(i).startOffset, 3)
                document.getText(rootElement.getElement(i).startOffset, 2) == "  " -> document.remove(rootElement.getElement(i).startOffset, 2)
                document.getText(rootElement.getElement(i).startOffset, 1) == " " -> document.remove(rootElement.getElement(i).startOffset, 1)
            }
        }
    }*/
}