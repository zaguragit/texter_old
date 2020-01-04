package posidon.texter.ui.view

import posidon.texter.Window
import posidon.texter.ui.Constants
import java.awt.Insets
import javax.swing.BorderFactory
import javax.swing.Icon
import javax.swing.JButton

class Button(text: String? = null, icon: Icon? = null) : JButton(text, icon) {
    init {
        background = Window.theme.uiBG
        margin = Insets(0, 0, 0, 0)
        border = BorderFactory.createEmptyBorder(8, 10, 8, 10)
        foreground = Window.theme.text
        font = Constants.uiFont
        isFocusable = false
    }
}