package posidon.texter.ui

import posidon.texter.Window
import java.awt.Color
import java.awt.Font
import java.awt.Image
import javax.swing.ImageIcon

data class Theme(
    val iconTheme: IconTheme = IconTheme(),
    val textAreaBG: Color = Color(0x111213),
    val textAreaFG: Color = Color(0xe1e2e3),
    val textAreaCaret: Color = Color(0xe1e2e3),
    val windowBG: Color = Color(0x111213),
    val scrollBarBG: Color = Color(0x111213),
    val scrollBarFG: Color = Color(0x1d1e1f),
    val uiBG: Color = Color(0x252627),
    val uiHighlight: Color = Color(0x313233),
    val text: Color = Color(0xe1e2e3),
    val textSelected: Color = Color(0xF0F1F2)
)

object Constants {
    val codeFont = Font(Font.MONOSPACED, Font.PLAIN, 15)
    val uiFont = Font(Font.SANS_SERIF, Font.PLAIN, 15)
}

object Themes {
    val dark = Theme(
        iconTheme = IconTheme(),
        textAreaBG = Color(0x111213),
        textAreaFG = Color(0xe1e2e3),
        textAreaCaret = Color(0xe1e2e3),
        windowBG = Color(0x111213),
        scrollBarBG = Color(0x111213),
        scrollBarFG = Color(0x1d1e1f),
        uiBG = Color(0x252627),
        uiHighlight = Color(0x313233),
        text = Color(0xe1e2e3),
        textSelected = Color(0xF0F1F2)
    )
    val midnight = Theme(
        iconTheme = IconTheme(),
        textAreaBG = Color(0x111321),
        textAreaFG = Color(0xafd2e0),
        textAreaCaret = Color(0x89a4c1),
        windowBG = Color(0x0e101e),
        scrollBarBG = Color(0x111321),
        scrollBarFG = Color(0x1d1f30),
        uiBG = Color(0x222539),
        uiHighlight = Color(0x323549),
        text = Color(0xafd2e0),
        textSelected = Color(0xF0F1F2)
    )
    val elementary = Theme(
        iconTheme = IconThemes.elementary,
        textAreaBG = Color(0x303030),
        textAreaFG = Color(0xededed),
        textAreaCaret = Color(0xfafafa),
        windowBG = Color(0x404040),
        scrollBarBG = Color(0x303030),
        scrollBarFG = Color(0x383838),
        uiBG = Color(0x383838),
        uiHighlight = Color(0x414141),
        text = Color(0xc0c1bf),
        textSelected = Color(0xF0F1F2)
    )
    val material = Theme(
        iconTheme = IconThemes.elementary,
        textAreaBG = Color(0x1E272B),
        textAreaFG = Color(0xCFD8DC),
        textAreaCaret = Color(0xB0BEC5),
        windowBG = Color(0x212B30),
        scrollBarBG = Color(0x1E272B),
        scrollBarFG = Color(0x263238),
        uiBG = Color(0x263238),
        uiHighlight = Color(0x37474F),
        text = Color(0xCFD8DC),
        textSelected = Color(0xECEFF1)
    )
}

data class IconTheme(
    val file_menu: ImageIcon = ImageIcon(Themes::class.java.getResource("/icons/actions/file_menu.png")),
    val close_tab: ImageIcon = ImageIcon(Themes::class.java.getResource("/icons/misc/close_tab.png")),
    val close_tab_hover: ImageIcon = ImageIcon(Themes::class.java.getResource("/icons/misc/close_tab_hover.png")),
    val folder: ImageIcon = ImageIcon(Themes::class.java.getResource("/icons/files/folder.png")),
    val folder_open: ImageIcon = ImageIcon(Themes::class.java.getResource("/icons/files/folder_open.png")),
    val file_text: ImageIcon = ImageIcon(Themes::class.java.getResource("/icons/files/file.png")),
    val java: ImageIcon = ImageIcon(Themes::class.java.getResource("/icons/files/java.png")),
    val kotlin: ImageIcon = ImageIcon(Themes::class.java.getResource("/icons/files/kotlin.png")),
    val xml: ImageIcon = ImageIcon(Themes::class.java.getResource("/icons/files/xml.png")),
    val file_highlighter: ImageIcon = ImageIcon(Themes::class.java.getResource("/icons/files/highlighter.png")),
    val file_exec: ImageIcon = ImageIcon(Themes::class.java.getResource("/icons/files/exec.png")),
    val img: ImageIcon = ImageIcon(Themes::class.java.getResource("/icons/files/img.png")),
    val file: ImageIcon = ImageIcon(Themes::class.java.getResource("/icons/files/other.png"))
)

private object IconThemes {
    val elementary = IconTheme(
        file_menu = ImageIcon(Themes::class.java.getResource("/icons/elementary/actions/file_menu.png")),
        close_tab = ImageIcon(Themes::class.java.getResource("/icons/elementary/misc/close.png")),
        close_tab_hover = ImageIcon(Themes::class.java.getResource("/icons/elementary/misc/close.png")),
        folder = ImageIcon(Themes::class.java.getResource("/icons/elementary/files/folder.png")),
        folder_open = ImageIcon(Themes::class.java.getResource("/icons/elementary/files/folder_open.png")),
        file_text = ImageIcon(Themes::class.java.getResource("/icons/elementary/files/file.png")),
        java = ImageIcon(Themes::class.java.getResource("/icons/elementary/files/java.png")),
        kotlin = ImageIcon(Themes::class.java.getResource("/icons/elementary/files/kotlin.png")),
        xml = ImageIcon(Themes::class.java.getResource("/icons/elementary/files/xml.png")),
        file_highlighter = ImageIcon(Themes::class.java.getResource("/icons/elementary/files/highlighter.png")),
        file_exec = ImageIcon(Themes::class.java.getResource("/icons/elementary/files/exec.png")),
        img = ImageIcon(Themes::class.java.getResource("/icons/elementary/files/img.png")),
        file = ImageIcon(Themes::class.java.getResource("/icons/elementary/files/other.png"))
    )
}