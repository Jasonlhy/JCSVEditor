import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

/* Title InformationPanel
 * Author Liu Ho Yin
 * Modifired 25-5-2012
 * A tabbedpane to show the list of file and the tree*/
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
			node.add(new DefaultMutableTreeNode(new Boolean(true)));
		}

		model = new DefaultTreeModel(top);
		fileTree = new JTree(model);

		fileTree.putClientProperty("JTree.lineStyle", "Angled");
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

		// make jfilelist
		listPane = new JListPane(record);
		fileTree.addMouseListener(new FileTreeMouseListener());

		/* add all things into tab */
		addTab("File Expoler", new JScrollPane(fileTree));
		addTab("Opened File", new JScrollPane(listPane));
	}

	/** getter and setter */
	public JTree getFileTree() {
		return fileTree;
	}

	public void setFileTree(JTree fileTree) {
		this.fileTree = fileTree;
	}

	public JListPane getListPane() {
		return listPane;
	}

	public void setlistPane(JListPane listPane) {
		this.listPane = listPane;
	}

	private class FileTreeMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			// get the row index of mouse clicked
			int selRow = fileTree.getRowForLocation(e.getX(), e.getY());
			// get the selected row index
			int[] rows = fileTree.getSelectionRows();
			// selection event (in select,out of select) component

			if (rows != null && rows[0] != 0 && rows[0] == selRow && e.getClickCount() == 2) {
				// get the component that tree is holding -> get the object in
				// mutable node-> get the file in file noe
				DefaultMutableTreeNode holdingNode = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
				FileNode fileNode = (FileNode) holdingNode.getUserObject();
				File file = fileNode.getFile();

				if (file.isFile())
					record.addExistFile(file);
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
		String extension = "";
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
		String extension = "";
		int mid = fileName.lastIndexOf(".");
		extension = fileName.substring(mid + 1, fileName.length());

		if (expanded)
			setIcon(new ImageIcon("../graph/expandedfolder.gif"));
		else
			setIcon(new ImageIcon("../graph/folder.gif"));

		if (obj instanceof Boolean)
			setText("Retrieving data...");

		// set beuatiful file graph
		if (extension.equals("txt"))
			setIcon(new ImageIcon("../graph/txt.PNG"));
		else if (extension.equals("csv"))
			setIcon(new ImageIcon("../graph/csv.PNG"));

		return this;
	}

}
