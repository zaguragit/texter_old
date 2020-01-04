package posidon.texter.ui

import java.awt.Color
import java.awt.Font
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
    val textSelected: Color = Color(0xF0F1F2),
    val textSelectionBG: Color = Color(0x152942),

    val red: Color = Color(0xFF001C),
    val coral: Color = Color(0xF46A65),
    val orange: Color = Color(0xff8833),
    val yellow: Color = Color(0xFFB714),
    val lime: Color = Color(0x9CDC45),
    val green: Color = Color(0x49984D),
    val teal: Color = Color(0x0EFFD6),
    val cyan: Color = Color(0x69D3F5),
    val light_blue: Color = Color(0x7599FF),
    val blue: Color = Color(0x5564FF),
    val purple: Color = Color(0x8D6CFF),
    val pink: Color = Color(0xFF4BB7),
    val gray: Color = Color(0x7C7C7C),
    val blue_gray: Color = Color(0x6E87B4)
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
        textSelected = Color(0xF0F1F2),
        textSelectionBG = Color(0x152942),

        red = Color(0xFF001C),
        coral = Color(0xF46A65),
        orange = Color(0xff8833),
        yellow = Color(0xFFB714),
        lime = Color(0x9CDC45),
        green = Color(0x49984D),
        teal = Color(0x0EFFD6),
        cyan = Color(0x69D3F5),
        light_blue = Color(0x7599FF),
        blue = Color(0x5C7CFF),
        purple = Color(0x8D6CFF),
        pink = Color(0xFF4BB7),
        gray = Color(0x7C7C7C),
        blue_gray = Color(0x6E87B4)
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
        textSelected = Color(0xF0F1F2),
        textSelectionBG = Color(0x0A3850),

        red = Color(0xFF001C),
        coral = Color(0xF46A65),
        orange = Color(0xff8833),
        yellow = Color(0xFFDB23),
        lime = Color(0x80DC42),
        green = Color(0x46B167),
        teal = Color(0x0EFFD6),
        cyan = Color(0x69D3F5),
        light_blue = Color(0x73A6FF),
        blue = Color(0x5C7CFF),
        purple = Color(0x8D6CFF),
        pink = Color(0xFF4BB7),
        gray = Color(0x7C7C7C),
        blue_gray = Color(0x6E87B4)
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
        textSelected = Color(0xF0F1F2),
        textSelectionBG = Color(0x1A397A),

        red = Color(0xFF001C),
        coral = Color(0xF46A65),
        orange = Color(0xff8833),
        yellow = Color(0xFFB714),
        lime = Color(0x9CDC45),
        green = Color(0x49984D),
        teal = Color(0x45FFCA),
        cyan = Color(0x69D3F5),
        light_blue = Color(0x7599FF),
        blue = Color(0x5564FF),
        purple = Color(0x8D6CFF),
        pink = Color(0xFF4BB7),
        gray = Color(0x7C7C7C),
        blue_gray = Color(0x6E87B4)
    )
    val material = Theme(
        iconTheme = IconThemes.material,
        textAreaBG = Color(0x1E272B),
        textAreaFG = Color(0xCFD8DC),
        textAreaCaret = Color(0xB0BEC5),
        windowBG = Color(0x212B30),
        scrollBarBG = Color(0x1E272B),
        scrollBarFG = Color(0x263238),
        uiBG = Color(0x263238),
        uiHighlight = Color(0x37474F),
        text = Color(0xCFD8DC),
        textSelected = Color(0xECEFF1),
        textSelectionBG = Color(0x0D47A1),

        red = Color(0xD32F2F),
        coral = Color(0xFF3D00),
        orange = Color(0xFB8C00),
        yellow = Color(0xFFEB3B),
        lime = Color(0x8BC34A),
        green = Color(0x388E3C),
        teal = Color(0x1DE9B6),
        cyan = Color(0x00E5FF),
        light_blue = Color(0x00B0FF),
        blue = Color(0x2962FF),
        purple = Color(0xAA00FF),
        pink = Color(0xFF4081),
        gray = Color(0x757575),
        blue_gray = Color(0x607D8B)
    )
    val ubuntu = Theme(
        iconTheme = IconThemes.ubuntu,
        textAreaBG = Color(0x2C001E),
        textAreaFG = Color(0xD6D3CF),
        textAreaCaret = Color(0xE95420),
        windowBG = Color(0x411934),
        scrollBarBG = Color(0x2C001E),
        scrollBarFG = Color(0x5E2750),
        uiBG = Color(0x772953),
        uiHighlight = Color(0x843E64),
        text = Color(0xDEDBD8),
        textSelected = Color(0xE2E0DD),
        textSelectionBG = Color(0x5E2750),

        red = Color(0xc6262e),
        coral = Color(0xF46A65),
        orange = Color(0xE95420),
        yellow = Color(0xf9c440),
        lime = Color(0x9bdb4d),
        green = Color(0x50823E),
        teal = Color(0x1DE9B6),
        cyan = Color(0x7EBFD3),
        light_blue = Color(0x899EC8),
        blue = Color(0x747CDA),
        purple = Color(0xA52CB0),
        pink = Color(0xCD529C),
        gray = Color(0xAEA79F),
        blue_gray = Color(0x667885)
    )
}

data class IconTheme(
    val file_menu: ImageIcon = ImageIcon(Themes::class.java.getResource("/icons/posidon/actions/file_menu.png")),
    val close_tab: ImageIcon = ImageIcon(Themes::class.java.getResource("/icons/posidon/misc/close_tab.png")),
    val close_tab_hover: ImageIcon = ImageIcon(Themes::class.java.getResource("/icons/posidon/misc/close_tab_hover.png")),
    val folder: ImageIcon = ImageIcon(Themes::class.java.getResource("/icons/posidon/files/folder.png")),
    val folder_open: ImageIcon = ImageIcon(Themes::class.java.getResource("/icons/posidon/files/folder_open.png")),
    val file_text: ImageIcon = ImageIcon(Themes::class.java.getResource("/icons/posidon/files/file_text.png")),
    val java: ImageIcon = ImageIcon(Themes::class.java.getResource("/icons/posidon/files/java.png")),
    val kotlin: ImageIcon = ImageIcon(Themes::class.java.getResource("/icons/posidon/files/kotlin.png")),
    val xml: ImageIcon = ImageIcon(Themes::class.java.getResource("/icons/posidon/files/xml.png")),
    val file_highlighter: ImageIcon = ImageIcon(Themes::class.java.getResource("/icons/posidon/files/highlighter.png")),
    val file_exec: ImageIcon = ImageIcon(Themes::class.java.getResource("/icons/posidon/files/exec.png")),
    val img: ImageIcon = ImageIcon(Themes::class.java.getResource("/icons/posidon/files/img.png")),
    val file: ImageIcon = ImageIcon(Themes::class.java.getResource("/icons/posidon/files/file.png"))
)

private object IconThemes {
    val elementary = IconTheme(
        file_menu = ImageIcon(Themes::class.java.getResource("/icons/elementary/actions/file_menu.png")),
        close_tab = ImageIcon(Themes::class.java.getResource("/icons/elementary/misc/close_tab.png")),
        close_tab_hover = ImageIcon(Themes::class.java.getResource("/icons/elementary/misc/close_tab.png")),
        folder = ImageIcon(Themes::class.java.getResource("/icons/elementary/files/folder.png")),
        folder_open = ImageIcon(Themes::class.java.getResource("/icons/elementary/files/folder_open.png")),
        file_text = ImageIcon(Themes::class.java.getResource("/icons/elementary/files/file_text.png")),
        java = ImageIcon(Themes::class.java.getResource("/icons/elementary/files/java.png")),
        kotlin = ImageIcon(Themes::class.java.getResource("/icons/elementary/files/kotlin.png")),
        xml = ImageIcon(Themes::class.java.getResource("/icons/elementary/files/xml.png")),
        file_highlighter = ImageIcon(Themes::class.java.getResource("/icons/elementary/files/highlighter.png")),
        file_exec = ImageIcon(Themes::class.java.getResource("/icons/elementary/files/exec.png")),
        img = ImageIcon(Themes::class.java.getResource("/icons/elementary/files/img.png")),
        file = ImageIcon(Themes::class.java.getResource("/icons/elementary/files/file.png"))
    )
    val material = IconTheme(
        file_menu = ImageIcon(Themes::class.java.getResource("/icons/material/actions/file_menu.png")),
        close_tab = ImageIcon(Themes::class.java.getResource("/icons/material/misc/close_tab.png")),
        close_tab_hover = ImageIcon(Themes::class.java.getResource("/icons/material/misc/close_tab_hover.png")),
        folder = ImageIcon(Themes::class.java.getResource("/icons/material/files/folder.png")),
        folder_open = ImageIcon(Themes::class.java.getResource("/icons/material/files/folder_open.png")),
        file_text = ImageIcon(Themes::class.java.getResource("/icons/material/files/file_text.png")),
        java = ImageIcon(Themes::class.java.getResource("/icons/material/files/java.png")),
        kotlin = ImageIcon(Themes::class.java.getResource("/icons/material/files/kotlin.png")),
        xml = ImageIcon(Themes::class.java.getResource("/icons/material/files/xml.png")),
        file_highlighter = ImageIcon(Themes::class.java.getResource("/icons/material/files/highlighter.png")),
        file_exec = ImageIcon(Themes::class.java.getResource("/icons/material/files/exec.png")),
        img = ImageIcon(Themes::class.java.getResource("/icons/material/files/img.png")),
        file = ImageIcon(Themes::class.java.getResource("/icons/material/files/file.png"))
    )
    val ubuntu = IconTheme(
        file_menu = ImageIcon(Themes::class.java.getResource("/icons/ubuntu/actions/file_menu.png")),
        close_tab = ImageIcon(Themes::class.java.getResource("/icons/ubuntu/misc/close_tab.png")),
        close_tab_hover = ImageIcon(Themes::class.java.getResource("/icons/ubuntu/misc/close_tab_hover.png")),
        folder = ImageIcon(Themes::class.java.getResource("/icons/ubuntu/files/folder.png")),
        folder_open = ImageIcon(Themes::class.java.getResource("/icons/ubuntu/files/folder_open.png")),
        file_text = ImageIcon(Themes::class.java.getResource("/icons/ubuntu/files/file_text.png")),
        java = ImageIcon(Themes::class.java.getResource("/icons/ubuntu/files/java.png")),
        kotlin = ImageIcon(Themes::class.java.getResource("/icons/ubuntu/files/kotlin.png")),
        xml = ImageIcon(Themes::class.java.getResource("/icons/ubuntu/files/xml.png")),
        file_highlighter = ImageIcon(Themes::class.java.getResource("/icons/ubuntu/files/highlighter.png")),
        file_exec = ImageIcon(Themes::class.java.getResource("/icons/ubuntu/files/exec.png")),
        img = ImageIcon(Themes::class.java.getResource("/icons/ubuntu/files/img.png")),
        file = ImageIcon(Themes::class.java.getResource("/icons/ubuntu/files/file.png"))
    )
}