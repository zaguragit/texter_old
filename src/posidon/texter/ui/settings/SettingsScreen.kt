package posidon.texter.ui.settings

import posidon.texter.AppInfo
import posidon.texter.Window
import posidon.texter.backend.Settings
import posidon.texter.ui.Constants
import posidon.texter.ui.view.SettingsSpinner
import java.awt.BorderLayout
import java.awt.Container
import java.awt.Dimension
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*

class SettingsScreen(private val jFrame: JFrame) {

    private val previousTitle: String = jFrame.title
    private val previousContentPane: Container = jFrame.contentPane

    init {
        jFrame.title = AppInfo.NAME + " - Settings"
        val panel = JPanel()
        panel.background = Window.theme.windowBG
        jFrame.contentPane = panel
        jFrame.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        panel.add(JPanel().apply {
            layout = BorderLayout()
            val backBtn: JButton
            add(JButton(Window.theme.iconTheme.action_back).apply {
                addActionListener { exit() }
                isOpaque = false
                isBorderPainted = false
                isContentAreaFilled = false
                backBtn = this
            }, BorderLayout.WEST)
            backBtn.maximumSize = Dimension(Int.MAX_VALUE, minimumSize.width)
            add(JLabel("Settings").apply {
                font = Constants.uiFont.deriveFont(48f)
                foreground = Window.theme.text
                horizontalAlignment = SwingConstants.CENTER
                border = BorderFactory.createEmptyBorder(0, 0, 0, backBtn.minimumSize.width)
            }, BorderLayout.CENTER)
            maximumSize = Dimension(Int.MAX_VALUE, 128)
            isOpaque = false
        })

        panel.add(SettingsSpinner("Theme", Settings.THEME, arrayOf("dark", "midnight", "elementary", "material", "ubuntu")).apply {
            selectionListener = {
                Window.updateTheme(it)
                previousContentPane.background = Window.theme.windowBG
            }
        })

        jFrame.validate()
    }

    fun exit() {
        jFrame.contentPane = previousContentPane
        jFrame.title = previousTitle
        jFrame.validate()
    }
}