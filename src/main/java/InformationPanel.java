import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

/* Title InformationPanel
 * Author Liu Ho Yin
 * Modified 25-5-2012
 * A tabbed Panel to show the list of file and the tree*/
public class InformationPanel extends JTabbedPane {
    private JTree fileTree;
    private DefaultTreeModel model;
    private JListPane listPane;
    private FileRecord record;


    public InformationPanel(FileRecord record) {
        super(JTabbedPane.TOP);
        this.record = record;

		/* File Tree */
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("My computer");

        DefaultMutableTreeNode node;
        File[] roots = File.listRoots(); // get the roots (drives)
        for (int k = 0; k < roots.length; k++) {
            node = new DefaultMutableTreeNode(new FileNode(roots[k]));
            top.add(node);
            // add a dummy node which will only load data when expand
            node.add(new DefaultMutableTreeNode((true)));
        }

        model = new DefaultTreeModel(top);
        fileTree = new JTree(model);

        // fileTree.putClientProperty("JTree.lineStyle", "Angled");
        fileTree.setShowsRootHandles(true);
        fileTree.setEditable(false);

        TreeCellRenderer renderer = new FileTreeCellRenderer();
        fileTree.setCellRenderer(renderer);

        fileTree.addTreeWillExpandListener(new TreeWillExpandListener() {
            public void treeWillExpand(TreeExpansionEvent event) {
                // expand the root -> get out
                TreePath path = event.getPath();
                if (path.getPathCount() == 1)
                    return;

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) (event.getPath().getLastPathComponent());
                FileNode fn = (FileNode) (node.getUserObject());
                fn.expand(node);

                model.reload(node);
            }

            public void treeWillCollapse(TreeExpansionEvent event) {
            }
        });

        // File list
        listPane = new JListPane(record);
        fileTree.addMouseListener(new FileTreeMouseListener());

		/* add all things into tab */
        addTab("File Explorer", new JScrollPane(fileTree));
        addTab("Opened File", new JScrollPane(listPane));
    }

    /**
     * getter and setter
     */
    public JListPane getListPane() {
        return listPane;
    }

    private class FileTreeMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            int[] rows = fileTree.getSelectionRows();

            if (rows.length > 0 &&  e.getClickCount() == 2) {
                // 1. Get the component that tree is holding
                // 2. Get the object in mutable node
                // 3. Get the file inside
                DefaultMutableTreeNode holdingNode = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
                FileNode fileNode = (FileNode) holdingNode.getUserObject();
                File file = fileNode.getFile();

                if (file.isFile()){
                    record.addExistFile(file);
                    fileTree.clearSelection();
                }
            }
        }
    }

}

class FileNode {
    private File f;

    public FileNode(File f) {
        this.f = f;
    }

    public File getFile() {
        return f;
    }

    public String toString() {
        return f.getName().length() > 0 ? f.getName() : f.getPath();
    }

    public boolean hasSubDirs() {
        File[] files = f.listFiles();
        if (files == null)
            return false;
        for (int k = 0; k < files.length; k++)
            if (files[k].isDirectory())
                return true;
        return false;
    }

    public void expand(DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode flag = (DefaultMutableTreeNode) parent.getFirstChild();

        if (flag == null)
            return;
        Object obj = flag.getUserObject();
        if (!(obj instanceof Boolean))
            return; // Already expanded
        parent.removeAllChildren(); // Remove Flag
        File[] files = f.listFiles();

        if (files == null)
            return;
        java.util.Arrays.sort(files);

        for (int k = 0; k < files.length; k++) {
            File f = files[k];
            if (!(FileNode.isSelectableFile(f) || f.isDirectory()))
                continue;

            FileNode nd = new FileNode(f);
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(nd);
            parent.add(node);

            if (nd.hasSubDirs())
                node.add(new DefaultMutableTreeNode(new Boolean(true)));
        }
    }

    public static boolean isSelectableFile(File file) {
        String fullFileName = file.getName();
        String extension;
        int mid = fullFileName.lastIndexOf(".");
        extension = fullFileName.substring(mid + 1, fullFileName.length());
        if (extension.equals("txt") || extension.equals("csv"))
            return true;
        else
            return false;

    }
}

class FileTreeCellRenderer extends DefaultTreeCellRenderer {
    public Component getTreeCellRendererComponent(JTree fileTree, Object value, boolean sel, boolean expanded,
                                                  boolean leaf, int row, boolean hasFocus) {

        super.getTreeCellRendererComponent(fileTree, value, sel, expanded, leaf, row, hasFocus);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Object obj = node.getUserObject();
        String fileName = obj.toString();
        setText(fileName);

        // check file type
        String extension;
        int mid = fileName.lastIndexOf(".");
        extension = fileName.substring(mid + 1, fileName.length());

        if (expanded)
            setIcon(new ImageIcon("../graph/expandedfolder.gif"));
        else
            setIcon(new ImageIcon("../graph/folder.gif"));

        if (obj instanceof Boolean)
            setText("Retrieving data...");

        // set beautiful file graph
        if (extension.equals("txt"))
            setIcon(new ImageIcon("../graph/txt.PNG"));
        else if (extension.equals("csv"))
            setIcon(new ImageIcon("../graph/csv.PNG"));

        return this;
    }

}
