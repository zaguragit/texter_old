package posidon.texter.ui.view

import posidon.texter.backend.syntaxHighlighters.SyntaxHighlighter
import posidon.texter.ui.Constants
import java.awt.datatransfer.DataFlavor
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.BorderFactory
import javax.swing.JTextPane
import javax.swing.TransferHandler
import javax.swing.text.*

class TextArea : JTextPane() {

    init {
        font = Constants.codeFont
        isEditable = true
        border = BorderFactory.createEmptyBorder(12, 12, 12, 12)

        addKeyListener(object : KeyListener {

            var shiftPressed = false

            override fun keyTyped(e: KeyEvent) {
                if (e.keyChar == '\t' && shiftPressed)
                    unindentText(selectionStart, selectionEnd - selectionStart)
            }

            override fun keyPressed(e: KeyEvent) {
                if (e.keyCode == 16) shiftPressed = true
            }

            override fun keyReleased(e: KeyEvent) {
                if (e.keyCode == 16) shiftPressed = false
            }
        })

        transferHandler = object : TransferHandler() {
            override fun importData(info: TransferSupport): Boolean = try {
                val data = info.transferable.getTransferData(DataFlavor.stringFlavor).toString()
                replaceSelection(data)
                true
            } catch (ignore: Exception) { false }

            override fun canImport(info: TransferSupport) =
                info.isDataFlavorSupported(DataFlavor.stringFlavor)
        }
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
    }

    fun indentText(offset: Int, length: Int) {
        val rootElement: Element = document.defaultRootElement
        val firstLine: Int = rootElement.getElementIndex(offset)
        val lastLine: Int = rootElement.getElementIndex(offset + length)
        for (i in firstLine..lastLine)
            document.insertString(rootElement.getElement(i).startOffset, "\t", SyntaxHighlighter.defaultTextStyle())
    }

    var indentSize: Int = 16
        set(value) {
            field = value
            val fm = getFontMetrics(font)
            val charWidth = fm.charWidth(' ')
            val tabWidth = charWidth * value
            val tabs = arrayOfNulls<TabStop>(5)
            for (j in tabs.indices) {
                val tab = j + 1
                tabs[j] = TabStop((tab * tabWidth).toFloat())
            }
            val tabSet = TabSet(tabs)
            val attributes = SimpleAttributeSet()
            StyleConstants.setTabSet(attributes, tabSet)
            val length = document.length
            styledDocument.setParagraphAttributes(0, length, attributes, false)
        }
}