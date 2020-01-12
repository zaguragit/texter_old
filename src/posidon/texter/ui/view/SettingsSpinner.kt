package posidon.texter.ui.view

import posidon.texter.Window
import posidon.texter.backend.Settings
import java.awt.Dimension
import javax.swing.ComboBoxModel
import javax.swing.DefaultComboBoxModel
import javax.swing.JComboBox
import javax.swing.event.ListDataListener
import javax.swing.plaf.ComboBoxUI

class SettingsSpinner(
    label: String,
    key: String,
    items: Array<String>
) : SettingsView(label, key, Settings.Type.TEXT) {

    var selectionListener: (selection: String) -> Unit = {}

    init {
        add(JComboBox<String>(items).apply {
            selectedItem = Settings.getString(key)
            addActionListener {
                selectedItem?.let { save(it) }
                selectionListener.invoke(selectedItem as String)
            }
            maximumSize = Dimension(256, 32)
            background = Window.theme.uiBG
            foreground = Window.theme.text
        })
    }
}