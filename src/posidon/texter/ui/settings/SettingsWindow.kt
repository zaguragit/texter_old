package posidon.texter.ui.settings

import posidon.texter.AppInfo
import posidon.texter.Window
import posidon.texter.backend.Settings
import posidon.texter.ui.view.SettingsSpinner
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.BoxLayout
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

        jFrame.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.add(SettingsSpinner("Theme", Settings.THEME, arrayOf("dark", "midnight", "elementary", "material", "ubuntu")).apply {
            selectionListener = {
                Window.updateTheme(it)
            }
        })

        panel.add(SettingsSpinner("Theme", Settings.THEME, arrayOf("dark", "midnight", "elementary", "material", "ubuntu")).apply {
            selectionListener = {
                Window.updateTheme(it)
            }
        })

        panel.add(SettingsSpinner("Theme", Settings.THEME, arrayOf("dark", "midnight", "elementary", "material", "ubuntu")).apply {
            selectionListener = {
                Window.updateTheme(it)
            }
        })

        jFrame.validate()
    }
}