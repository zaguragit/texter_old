package posidon.texter.ui

import posidon.texter.AppInfo
import posidon.texter.Window
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JFrame
import javax.swing.JPanel

class SettingsWindow(jFrame: JFrame) {

    init {
        val actualTitle = jFrame.title
        jFrame.title = AppInfo.NAME + " - Settings"
        val panel = JPanel()
        val mainPane = jFrame.contentPane
        panel.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                jFrame.contentPane = mainPane
                jFrame.title = actualTitle
                jFrame.validate()
            }
        })
        panel.background = Window.theme.windowBG
        jFrame.contentPane = panel
        jFrame.validate()
    }
}