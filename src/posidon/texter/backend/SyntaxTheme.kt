package posidon.texter.backend

data class SyntaxTheme(
    val textAreaBG: Int = 0x111213,
    val textAreaFG: Int = 0xe1e2e3,
    val textAreaCaret: Int = 0xe1e2e3,
    val windowBG: Int = 0x111213,
    val scrollBarBG: Int = 0x111213,
    val scrollBarFG: Int = 0x1d1e1f,
    val uiBG: Int = 0x252627,
    val uiHighlight: Int = 0x313233,
    val text: Int = 0xe1e2e3
)

object SyntaxThemes {
    val midnight = SyntaxTheme(
        0x111321,
        0xafd2e0,
        0x89a4c1,
        0x0e101e,
        0x111321,
        0x1d1f30,
        0x222539,
        0x323549,
        0xafd2e0
    )
}