package posidon.texter.backend

import javax.swing.text.*

class NewLineFilter : DocumentFilter() {

    override fun insertString(fb: FilterBypass, offset: Int, string: String, a: AttributeSet?) {
        var str = string
        if (str == "\n") str = addWhiteSpace(fb.document, offset)
        super.insertString(fb, offset, str, a)
    }

    override fun replace(fb: FilterBypass, offset: Int, length: Int, string: String, a: AttributeSet?) {
        var str = string
        if (str == "\n") str = addWhiteSpace(fb.document, offset)
        super.replace(fb, offset, length, str, a)
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
        return whiteSpace.toString().replace("        ", "\t")
    }
}