package posidon.texter.ui.view

import posidon.texter.backend.Settings
import java.awt.Dimension
import javax.swing.JComboBox

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
                save(selectedItem)
                selectionListener.invoke(selectedItem as String)
            }
            preferredSize = Dimension(128, 32)
        })
    }
}