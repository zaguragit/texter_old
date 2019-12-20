package posidon.texter.ui

import posidon.texter.Window
import java.awt.Color
import java.awt.SystemColor
import java.net.URL
import javax.swing.ImageIcon
import javax.swing.UIManager

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
}

data class IconTheme(
    val file_menu: URL = Window::class.java.getResource("/icons/actions/file_menu.png"),
    val close_tab: ImageIcon = ImageIcon(Window::class.java.getResource("/icons/misc/close_tab.png")),
    val close_tab_hover: ImageIcon = ImageIcon(Window::class.java.getResource("/icons/misc/close_tab_hover.png")),
    val folder: ImageIcon = ImageIcon(FileTree::class.java.getResource("/icons/files/folder.png")),
    val file: ImageIcon = ImageIcon(FileTree::class.java.getResource("/icons/files/file.png"))
)

private object IconThemes {
    val elementary = IconTheme(
        file_menu = Window::class.java.getResource("/icons/elementary/actions/file_menu.png"),
        close_tab = ImageIcon(Window::class.java.getResource("/icons/elementary/actions/close.png")),
        close_tab_hover = ImageIcon(Window::class.java.getResource("/icons/elementary/actions/close.png")),
        folder = ImageIcon(FileTree::class.java.getResource("/icons/elementary/files/folder.png")),
        file = ImageIcon(FileTree::class.java.getResource("/icons/elementary/files/file.png"))
    )
}