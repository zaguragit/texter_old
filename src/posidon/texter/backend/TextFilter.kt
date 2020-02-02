package posidon.texter.backend

import posidon.texter.Window
import javax.swing.text.AttributeSet
import javax.swing.text.Document
import javax.swing.text.DocumentFilter
import javax.swing.text.Element

class TextFilter : DocumentFilter() {

    override fun insertString(fb: FilterBypass, offset: Int, string: String, a: AttributeSet?) {
        var str = string
        if (string == "\n") str = addWhiteSpace(fb.document, offset)
        fb.insertString(offset, str, a)
    }

    override fun replace(fb: FilterBypass, offset: Int, length: Int, string: String, a: AttributeSet?) {
        when {
            string == "\t" && Window.textArea.selectedText != null -> Window.activeTab!!.indentText(offset, length)
            string == "\n" -> fb.replace(offset, length, addWhiteSpace(fb.document, offset), a)
            else -> fb.replace(offset, length, string, a)
        }
    }

    override fun remove(fb: FilterBypass, offset: Int, length: Int) {
        fb.remove(offset, length)
    }

    private fun addWhiteSpace(doc: Document, offset: Int): String {
        val whiteSpace = StringBuilder("\n")
        val rootElement: Element = doc.defaultRootElement
        val line: Int = rootElement.getElementIndex(offset)
        var i: Int = rootElement.getElement(line).startOffset
        while (true) {
            val temp: String = doc.getText(i, 1)
            if (temp == " " || temp == "\t") {
                whiteSpace.append(temp)
                i++
            } else break
        }
        return whiteSpace.toString().replace("    ", "\t")
    }
}