package posidon.texter.ui

import posidon.texter.Window
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.io.File
import java.util.*
import javax.swing.*
import javax.swing.event.TreeSelectionEvent
import javax.swing.plaf.ScrollBarUI
import javax.swing.plaf.TreeUI
import javax.swing.plaf.basic.BasicTreeUI
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeCellRenderer
import javax.swing.tree.TreePath


class FileTree(dir: File) : JPanel() {

    private val scrollPane = JScrollPane()
    private val tree = JTree(addNodes(null, dir))
    private val renderer = Renderer()
    private val treeUI = DecentTreeUI()

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

        for (thisObject in ol) {
            val newPath: String = if (curPath == ".") thisObject else curPath + File.separator + thisObject
            if (File(newPath).also { f = it }.isDirectory) addNodes(curDir, f)
            else files.addElement(thisObject)
        }

        for (fnum in files.indices) curDir.add(DefaultMutableTreeNode(files.elementAt(fnum)))
        return curDir
    }

    fun updateColors() {
        tree.background = Window.theme.uiBG
        renderer.backgroundSelectionColor = Window.theme.uiHighlight
        renderer.backgroundNonSelectionColor = Window.theme.uiBG
        renderer.borderSelectionColor = Window.theme.uiBG
        renderer.textSelectionColor = Window.theme.textSelected
        renderer.textNonSelectionColor = Window.theme.text
        renderer.closedIcon = Window.theme.iconTheme.folder
        renderer.openIcon = Window.theme.iconTheme.folder_open
        treeUI.collapsedIcon = null
        treeUI.expandedIcon = null
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
        tree.toggleClickCount = 2
        scrollPane.isOpaque = false
        scrollPane.border = null
        scrollPane.viewport.add(tree)
        add(BorderLayout.CENTER, scrollPane)
        renderer.border = BorderFactory.createEmptyBorder(4, 8, 4, 8)
        tree.cellRenderer = renderer
        tree.isRootVisible = false
        tree.setUI(treeUI)
        isOpaque = false
        tree.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                val row = tree.getRowForLocation(e.x, e.y)
                if (row != -1) {
                    val path: TreePath? = tree.getPathForLocation(e.x, e.y)
                    if (e.clickCount == 2 && (path?.lastPathComponent as DefaultMutableTreeNode).isLeaf) {
                        itemDoubleClickListener?.invoke(path.path.joinToString("/"))
                    }
                }
            }
        })
    }

    private var itemDoubleClickListener: ((path: String) -> Unit)? = null

    fun setLeafDoubleClickListener(listener: (path: String) -> Unit) {
        itemDoubleClickListener = listener
    }

    fun addSelectionListener(listener: (e: TreeSelectionEvent) -> Unit) {
        tree.addTreeSelectionListener(listener)
    }

    private class Renderer : DefaultTreeCellRenderer() {
        override fun getTreeCellRendererComponent(
            tree: JTree?,
            value: Any?,
            selected: Boolean,
            expanded: Boolean,
            isLeaf: Boolean,
            rowIndex: Int,
            hasFocus: Boolean
        ): Component {
            if (isLeaf) try {
                val v = value.toString()
                leafIcon = when {
                    v.endsWith(".kt") -> Window.theme.iconTheme.kotlin
                    v.endsWith(".java") -> Window.theme.iconTheme.java
                    v.endsWith(".class") ||
                    v.endsWith(".elf") ||
                    v.endsWith(".exe") ||
                    v.endsWith(".efi") ||
                    v.endsWith(".so") ||
                    v.endsWith(".o") ||
                    v.endsWith(".sh") -> Window.theme.iconTheme.file_exec
                    v.endsWith(".txt") -> Window.theme.iconTheme.file_text
                    v.endsWith(".highlighter") -> Window.theme.iconTheme.file_highlighter
                    else -> Window.theme.iconTheme.file
                }
            } catch (e: Exception) {}
            return super.getTreeCellRendererComponent(tree, value, selected, expanded, isLeaf, rowIndex, hasFocus)
        }
    }

    private class DecentTreeUI : BasicTreeUI() {
        override fun paintVerticalLine(g: Graphics?, c: JComponent?, x: Int, top: Int, bottom: Int) {}
        override fun paintVerticalPartOfLeg(g: Graphics?, clipBounds: Rectangle?, insets: Insets?, path: TreePath?) {}
        override fun paintHorizontalPartOfLeg(g: Graphics?, clipBounds: Rectangle?, insets: Insets?, bounds: Rectangle?,
                                              path: TreePath?, row: Int, isExpanded: Boolean, hasBeenExpanded: Boolean, isLeaf: Boolean) {}
    }
}