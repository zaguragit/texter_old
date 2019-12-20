package posidon.texter.ui

import posidon.texter.Window
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.Insets
import java.io.File
import java.util.*
import javax.swing.*
import javax.swing.event.TreeSelectionEvent
import javax.swing.plaf.ScrollBarUI
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeCellRenderer
import javax.swing.tree.TreeNode

class FileTree(dir: File) : JPanel() {

    private val scrollPane = JScrollPane()
    private val tree = JTree(addNodes(null, dir))
    private val renderer = DefaultTreeCellRenderer()

    fun addNodes(curTop: DefaultMutableTreeNode?, dir: File): DefaultMutableTreeNode {
        val curPath = dir.path
        val curDir = DefaultMutableTreeNode(dir.name)
        curTop?.add(curDir)
        val ol = Vector<String>()
        val tmp = dir.list()
        if (tmp != null) for (s in tmp) ol.addElement(s)
        ol.sortWith(String.CASE_INSENSITIVE_ORDER)
        var f: File
        val files = Vector<String>()
        // Make two passes, one for Dirs and one for Files. This is #1.
        for (thisObject in ol) {
            val newPath: String = if (curPath == ".") thisObject else curPath + File.separator + thisObject
            if (File(newPath).also { f = it }.isDirectory) addNodes(curDir, f)
            else files.addElement(thisObject)
        }
        // Pass two: for files.
        for (fnum in files.indices) curDir.add(DefaultMutableTreeNode(files.elementAt(fnum)))
        return curDir
    }

    fun updateColors() {
        tree.background = Window.theme.uiBG
        renderer.backgroundSelectionColor = Window.theme.uiBG
        renderer.backgroundNonSelectionColor = Window.theme.uiBG
        renderer.borderSelectionColor = Window.theme.uiBG
        renderer.textSelectionColor = Window.theme.textSelected
        renderer.textNonSelectionColor = Window.theme.text
        renderer.closedIcon = Window.theme.iconTheme.folder
        renderer.openIcon = Window.theme.iconTheme.folder
        renderer.leafIcon = Window.theme.iconTheme.file
    }

    var horizontalScrollBarUI: ScrollBarUI
        get() = scrollPane.horizontalScrollBar.ui
        set(value) = scrollPane.horizontalScrollBar.setUI(value)

    var verticalScrollBarUI: ScrollBarUI
        get() = scrollPane.verticalScrollBar.ui
        set(value) = scrollPane.verticalScrollBar.setUI(value)

    override fun getMinimumSize(): Dimension { return Dimension(200, 400) }
    override fun getPreferredSize(): Dimension { return Dimension(200, 400) }

    init {
        layout = BorderLayout()
        tree.addTreeSelectionListener { e: TreeSelectionEvent ->
            val node = e.path.lastPathComponent as DefaultMutableTreeNode
            if (node.isLeaf) Window.openFile(e.path.path.joinToString("/"))
        }
        scrollPane.isOpaque = false
        scrollPane.border = null
        scrollPane.viewport.add(tree)
        add(BorderLayout.CENTER, scrollPane)
        renderer.border = BorderFactory.createEmptyBorder(4, 8, 4, 8)
        tree.cellRenderer = renderer
        tree.isRootVisible = false
        isOpaque = false
    }
}