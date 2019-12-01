package posidon.texter.ui

data class Theme(
    val textAreaBG: Int = 0x0,
    val textAreaFG: Int = 0xe1e2e3,
    val textAreaCaret: Int = 0xe1e2e3,
    val scrollBarBG: Int = 0x111213,
    val scrollBarFG: Int = 0x1d1e1f
)

object Themes {
    val midnight = Theme(
        0x111321,
        0xafd2e0,
        0x1e9acc,
        0x1d1f30,
        0x323549
    )
}