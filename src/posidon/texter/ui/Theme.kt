package posidon.texter.ui

import java.awt.Color
import java.awt.SystemColor
import javax.swing.UIManager

data class Theme(
    val textAreaBG: Color = Color(0x111213),
    val textAreaFG: Color = Color(0xe1e2e3),
    val textAreaCaret: Color = Color(0xe1e2e3),
    val windowBG: Color = Color(0x111213),
    val scrollBarBG: Color = Color(0x111213),
    val scrollBarFG: Color = Color(0x1d1e1f),
    val uiBG: Color = Color(0x252627),
    val uiHighlight: Color = Color(0x313233),
    val text: Color = Color(0xe1e2e3)
)

object Themes {
    val dark = Theme(
        textAreaBG = Color(0x111213),
        textAreaFG = Color(0xe1e2e3),
        textAreaCaret = Color(0xe1e2e3),
        windowBG = Color(0x111213),
        scrollBarBG = Color(0x111213),
        scrollBarFG = Color(0x1d1e1f),
        uiBG = Color(0x252627),
        uiHighlight = Color(0x313233),
        text = Color(0xe1e2e3)
    )
    val midnight = Theme(
        textAreaBG = Color(0x111321),
        textAreaFG = Color(0xafd2e0),
        textAreaCaret = Color(0x89a4c1),
        windowBG = Color(0x0e101e),
        scrollBarBG = Color(0x111321),
        scrollBarFG = Color(0x1d1f30),
        uiBG = Color(0x222539),
        uiHighlight = Color(0x323549),
        text = Color(0xafd2e0)
    )
}