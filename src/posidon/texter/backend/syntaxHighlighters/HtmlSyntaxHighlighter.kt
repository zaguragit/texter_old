package posidon.texter.backend.syntaxHighlighters

import posidon.texter.Window
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants
import javax.swing.text.StyledDocument
import kotlin.math.max
import kotlin.math.min

class HtmlSyntaxHighlighter : SyntaxHighlighter() {

    private enum class TagState {
        IN_STRING_DOUBLE,
        IN_STRING_SINGLE,
        IN_TAG,
        IN_TAG_PARAMS,
        TEXT
    }

    val tagColor = Window.theme.light_blue
    val tagParamsColor = Window.theme.yellow

    override fun colorLine(doc: StyledDocument, lineStart: Int, line: String, lineI: Int) {
        doc.setCharacterAttributes(lineStart, line.length, defaultTextStyle(), false)
        if (line.isNotEmpty()) {
            var startPos = lineStart
            var state = TagState.TEXT
            var length = 0
            for (i in 0..line.lastIndex) {
                length++
                when (line[i]) {
                    '<' -> if (state == TagState.TEXT) {
                        state = TagState.IN_TAG
                        startPos = i + lineStart
                        length = 1
                    }
                    '>' -> if (state == TagState.IN_TAG) {
                        doc.setCharacterAttributes(startPos, length, SimpleAttributeSet().apply {
                            StyleConstants.setForeground(this, tagColor)
                        }, false)
                        state = TagState.TEXT
                        startPos = i + lineStart + 1
                        length = 0
                    } else if (state == TagState.IN_TAG_PARAMS) {
                        doc.setCharacterAttributes(startPos, length - 1, SimpleAttributeSet().apply {
                            StyleConstants.setForeground(this, tagParamsColor)
                        }, false)
                        if (line[i-1] == '/') doc.setCharacterAttributes(lineStart + i - 1, 2, SimpleAttributeSet().apply {
                            StyleConstants.setForeground(this, tagColor)
                        }, false)
                        else doc.setCharacterAttributes(lineStart + i, 1, SimpleAttributeSet().apply {
                            StyleConstants.setForeground(this, tagColor)
                        }, false)
                        state = TagState.TEXT
                        startPos = i + lineStart + 1
                        length = 0
                    }
                    '"' -> if (state == TagState.IN_STRING_DOUBLE) {
                        doc.setCharacterAttributes(startPos, length, SimpleAttributeSet().apply {
                            StyleConstants.setForeground(this, Window.theme.green)
                        }, false)
                        state = TagState.IN_TAG_PARAMS
                        startPos = i + lineStart + 1
                        length = 0
                    } else if (state == TagState.IN_TAG_PARAMS) {
                        doc.setCharacterAttributes(startPos, length, SimpleAttributeSet().apply {
                            StyleConstants.setForeground(this, tagParamsColor)
                        }, false)
                        state = TagState.IN_STRING_DOUBLE
                        startPos = i + lineStart
                        length = 1
                    }
                    '\'' -> if (state == TagState.IN_STRING_SINGLE) {
                        doc.setCharacterAttributes(startPos, length, SimpleAttributeSet().apply {
                            StyleConstants.setForeground(this, Window.theme.green)
                        }, false)
                        state = TagState.IN_TAG_PARAMS
                        startPos = i + lineStart + 1
                        length = 0
                    } else if (state == TagState.IN_TAG_PARAMS) {
                        doc.setCharacterAttributes(startPos, length, SimpleAttributeSet().apply {
                            StyleConstants.setForeground(this, tagParamsColor)
                        }, false)
                        state = TagState.IN_STRING_SINGLE
                        startPos = i + lineStart
                        length = 1
                    }
                    ' ' -> if (state == TagState.IN_TAG) {
                        doc.setCharacterAttributes(startPos, length, SimpleAttributeSet().apply {
                            StyleConstants.setForeground(this, tagColor)
                        }, false)
                        state = TagState.IN_TAG_PARAMS
                        startPos = i + lineStart + 1
                        length = 0
                    }
                }
            }
            line.indexOf("<!DOCTYPE").let {
                if (it != -1) {
                    doc.setCharacterAttributes(lineStart + it, line.indexOf('>', it) - it + 1, SimpleAttributeSet().apply {
                        StyleConstants.setForeground(this, Window.theme.blue_gray)
                    }, false)
                }
            }
        }
    }
}