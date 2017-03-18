import jcsveditor.view.CSVTableModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.StyledEditorKit;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.LinkedList;

/*TItle JasonEditr
 * Author Liu HO YIn
 * Last Modifired 25-5-2012
 * 
 * THis is the main class for gui 
 * and make each class to have comuunication*/
public class JCSVEditor extends JFrame {

    JFileChooser fc;
    File oldFile = null;
    FileNameExtensionFilter textFilter;
    FileNameExtensionFilter csvFilter;
    String newLine = System.getProperty("line.separator");
    AboutBox aboutBox;
    JMenuBar menuBar;
    JToolBar toolBar;
    JPopupMenu popUpMenu;
    JCheckBoxMenuItem textMenuItem;
    JSplitPane splitPane;
    ContentPanel contentPanel;
    InformationPanel informationPanel;
    boolean enable = false;
    FileRecord record;
    LinkedList<File> fileCache;

    public JCSVEditor() {
        super("JCSVEditor");

        fileCache = new LinkedList<File>();
        record = new FileRecord();
        InformationPanel informationPanel = new InformationPanel(record);
        contentPanel = new ContentPanel(record);

        // file chooser with file filter
        fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);

        textFilter = new FileNameExtensionFilter("plain text file(*.txt)", "txt");
        csvFilter = new FileNameExtensionFilter("common separeted value(*.csv)", "csv");

        fc.addChoosableFileFilter(textFilter);
        fc.addChoosableFileFilter(csvFilter);
        fc.setFileFilter(textFilter);

        // about dialog
        aboutBox = new AboutBox(this);

        /** Actions */
        /* file action */
        Action newTXTAction = new NewTXTAction("New txt", new ImageIcon("./graph/txt.png"));
        Action newCSVAction = new NewCSVAction("New csv", new ImageIcon("./graph/csv.png"));
        Action openAction = new OpenAction("Open", new ImageIcon("./graph/open.gif"), KeyEvent.VK_O);
        Action saveAction = new SaveAction("Save", new ImageIcon("./graph/save.gif"), KeyEvent.VK_S);
        Action saveAsAction = new SaveAsAction("Save As", new ImageIcon("./graph/saveAs.gif"), KeyEvent.VK_A);
        Action saveAllAction = new SaveAllAction("Save All", new ImageIcon("./graph/save_all.png"), KeyEvent.VK_A);
        Action closeAction = new CloseAction("Close", new ImageIcon("./graph/close.gif"), KeyEvent.VK_C);
        Action closeAllAction = new CloseAllAction("Close All", new ImageIcon("./graph/close_all.png"));

		/* help action */
        Action textAction = new TextAction("Show Text", new ImageIcon("./graph/text.gif"), KeyEvent.VK_T);
        Action aboutAction = new AboutAction("About", new ImageIcon("./graph/about.gif"), KeyEvent.VK_H);

		/* table action */
        // row action
        Action addUpperRowAction = new AddUpperRowAction("add upper row", new ImageIcon("./graph/upper_row.PNG"));
        Action addLowerRowAction = new AddLowerRowAction("add lower row", new ImageIcon("./graph/lower_row.PNG"));

        // column action
        Action addLeftColumnAction = new AddLeftColumnAction("add left column",
                new ImageIcon("./graph/left_column.PNG"));
        Action addRightColumnAction = new AddRightColumnAction("add right column",
                new ImageIcon("./graph/right_column.PNG"));

        // remove action
        Action removeRowAction = new RemoveRowAction("remove row", new ImageIcon("./graph/remove_row.PNG"));
        Action removeColumnAction = new RemoveColumnAction("remove column",
                new ImageIcon("./graph/remove_column.PNG"));

        // other aciotn
        Action clearCellAction = new ClearCellAction("Clear cell", new ImageIcon("./graph/clear_cell.png"));
        Action newCellAction = new NewCellAction("New cell", new ImageIcon("./graph/new_cell.png"));

        /** menu bar **/
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu tableMenu = new JMenu("Table");
        JMenu helpMenu = new JMenu("Help");
        menuBar.add(fileMenu);
        menuBar.add(tableMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

		/* file menu */
        JMenu newMenu = new JMenu("New");
        newMenu.setIcon(new ImageIcon("./graph/new.png"));
        JMenuItem openMenuItem = new JMenuItem(openAction);
        JMenuItem saveMenuItem = new JMenuItem(saveAction);
        JMenuItem saveAsMenuItem = new JMenuItem(saveAsAction);
        JMenuItem saveAllMenuItem = new JMenuItem(saveAllAction);
        JMenuItem closeMenuItem = new JMenuItem(closeAction);
        JMenuItem closeAllMenuItem = new JMenuItem(closeAllAction);

        fileMenu.add(newMenu);
        fileMenu.add(openMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.add(saveAllMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(closeMenuItem);
        fileMenu.add(closeAllMenuItem);

        // nests menu in filemenu
        JMenuItem newTXTMenuItem = new JMenuItem(newTXTAction);
        JMenuItem newCSVMenuItem = new JMenuItem(newCSVAction);

        newMenu.add(newTXTMenuItem);
        newMenu.add(newCSVMenuItem);

		/* Table menu */
        JMenu rowMenu = new JMenu("Row");
        JMenu columnMenu = new JMenu("Column");
        JMenuItem clearCellMenuItem = new JMenuItem(clearCellAction);
        JMenuItem newCellMenuItem = new JMenuItem(newCellAction);
        JMenuItem refreshMenuItem = new JMenuItem("Refresh");
        refreshMenuItem.addActionListener(e -> {
                record.refreshFile();
            }
        );

        tableMenu.add(rowMenu);
        tableMenu.add(columnMenu);
        tableMenu.addSeparator();
        tableMenu.add(clearCellMenuItem);
        tableMenu.add(newCellMenuItem);
        tableMenu.add(refreshMenuItem);

        // nest rowMenu
        rowMenu.setIcon(new ImageIcon("./graph/row.jpg"));
        JMenuItem addUpperRowMenuItem = new JMenuItem(addUpperRowAction);
        JMenuItem addLowerRowMenuItem = new JMenuItem(addLowerRowAction);
        JMenuItem removeRowMenuItem = new JMenuItem(removeRowAction);

        rowMenu.add(addUpperRowMenuItem);
        rowMenu.add(addLowerRowMenuItem);
        rowMenu.addSeparator();
        rowMenu.add(removeRowMenuItem);

        // nest columnMenu
        columnMenu.setIcon(new ImageIcon("./graph/column.jpg"));
        JMenuItem addLeftColumnMenuItem = new JMenuItem(addLeftColumnAction);
        JMenuItem addRightColumnMenuItem = new JMenuItem(addRightColumnAction);
        JMenuItem removeColumnMenuItem = new JMenuItem(removeColumnAction);

        columnMenu.add(addLeftColumnMenuItem);
        columnMenu.add(addRightColumnMenuItem);
        columnMenu.addSeparator();
        columnMenu.add(removeColumnMenuItem);

        // help menu
        textMenuItem = new JCheckBoxMenuItem(textAction);
        JMenuItem aboutMenuItem = new JMenuItem(aboutAction);
        helpMenu.add(textMenuItem);
        helpMenu.addSeparator();
        helpMenu.add(aboutMenuItem);

		/* Tool Bar */
        toolBar = new JToolBar();
        Integer[] sizeArray = {6, 8, 12, 18, 24, 36, 48, 60, 80};
        String[] viewArray = {ContentPanel.TAB_VIEW, ContentPanel.INTERAL_FRAME_VIEW};

        // file button
        JButton newCSV = new JButton(newCSVAction);
        JButton newTXT = new JButton(newTXTAction);
        JButton open = new JButton(openAction);
        JButton save = new JButton(saveAction);
        JButton saveAs = new JButton(saveAsAction);
        JButton close = new JButton(closeAction);

        // document button
        JComboBox size = new JComboBox(sizeArray);
        size.setSelectedIndex(2); // default size 12
        JToggleButton bold = new JToggleButton(new ImageIcon("./graph/bold.png"));
        JToggleButton italic = new JToggleButton(new ImageIcon("./graph/italic.png"));
        JToggleButton underlind = new JToggleButton(new ImageIcon("./graph/underline.png"));
        JComboBox view = new JComboBox(viewArray);

        // table button
        JButton addUpperRow = new JButton(addUpperRowAction);
        JButton addLowerRow = new JButton(addLowerRowAction);
        JButton addLeftColumn = new JButton(addLeftColumnAction);
        JButton addRightColumn = new JButton(addRightColumnAction);
        JButton removeRow = new JButton(removeRowAction);
        JButton removeColumn = new JButton(removeColumnAction);

        // add file button
        toolBar.add(newCSV);
        toolBar.add(newTXT);
        toolBar.add(open);
        toolBar.add(save);
        toolBar.add(saveAs);
        toolBar.add(close);

        // add document button
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.add(size);
        toolBar.add(bold);
        toolBar.add(italic);
        toolBar.add(underlind);

        // add table button
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.add(addUpperRow);
        toolBar.add(addLowerRow);
        toolBar.add(removeRow);
        toolBar.add(addLeftColumn);
        toolBar.add(addRightColumn);
        toolBar.add(removeColumn);

        // change the view
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.add(view);

        /** add everything */

        // add into spile pane
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, informationPanel, contentPanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(0.3);

        add(toolBar, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setSize(800, 640);

        // hide the texts by default
        showAndHideText();

		/* add register and make some connection between classes */
        view.addItemListener(new ViewListener());

        // selection on list -> select the view
        informationPanel.getListPane().getJList().addListSelectionListener(new MyListSelectionListener());

        // set document style action
        size.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox chooseNumber = (JComboBox) e.getSource();
                Integer number = (Integer) chooseNumber.getSelectedItem();
                String name = String.valueOf(number);

                // actively involve actionperformed by passing the event to
                // sizeAction
                Action sizeAction = new StyledEditorKit.FontSizeAction(name, number);
                sizeAction.actionPerformed(e);
            }
        });

        bold.addActionListener(new StyledEditorKit.BoldAction());
        italic.addActionListener(new StyledEditorKit.ItalicAction());
        underlind.addActionListener(new StyledEditorKit.UnderlineAction());
    }

    /* just save as a file */
    public void saveAs(File file, Component c, int index) {
        // check file type -> set appropricate filter
        String fileName = file.getName();
        if (fileName.indexOf(".txt") != -1)
            fc.setFileFilter(textFilter);
        else
            fc.setFileFilter(csvFilter);

        int value = fc.showSaveDialog(null);
        if (value == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
            file = manipulateFilePath(file);
            record.changeFileAt(index, file);

            IOSystem.write(file, c);

            // a cache is need because java.exists() for a new file is not
            // worakble
            // sine the properties of NTFS
            fileCache.add(file);

        }
    }

    // just save a file
    public void saveFile(File file, Component c, int index) {
        if (file.exists() || fileCache.contains(file)) {
            IOSystem.write(file, c);
        } else
            saveAs(file, c, index);

    }

    // change the path of the file for saving with a particular extension
    public File manipulateFilePath(File file) {
        String path = file.getPath();
        javax.swing.filechooser.FileFilter fileFilter = fc.getFileFilter();

        // no input any extension
        if (path.indexOf(".") == -1) {
            // make a new file path with extension
            if (fileFilter == textFilter)
                file = new File(path + ".txt");
            else if (fileFilter == csvFilter)
                file = new File(path + ".csv");

        }

        return file; // return a new/old file path
    }

    /* shwo and hide the toolbar text */
    public void showAndHideText() {
        Component[] component = toolBar.getComponents();

        for (int i = 0; i < component.length; i++) {
            if (component[i] instanceof JButton) {
                JButton target = (JButton) component[i];
                if (enable)
                    target.setText((String) (target.getAction()).getValue(Action.NAME));
                else
                    target.setText(null);
            }
        }

    }

    // private class for contentpanel to change the view
    private class ViewListener implements ItemListener {
        public void itemStateChanged(ItemEvent e) {
            JComboBox comboBox = (JComboBox) e.getSource();
            String comingView = (String) comboBox.getSelectedItem();
            contentPanel.changeView(comingView);
        }
    }

    /*
     * get the source (JList) , change in selection -> get the index ,based on
     * it to select a file
     */
    private class MyListSelectionListener implements ListSelectionListener {
        public MyListSelectionListener() {
        }

        public void valueChanged(ListSelectionEvent e) {
            // get the index of selection in list
            JList list = (JList) e.getSource();
            int index = list.getSelectedIndex();

            if (index >= 0) {
                contentPanel.setSelectedAt(index);
            }
        }
    }

    /* action for new a csv file */
    private class NewCSVAction extends AbstractAction {
        public NewCSVAction(String text, ImageIcon icon) {
            super(text, icon);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            record.addNewFile(".csv");
        }

    }

    /* action for new a txt file */
    private class NewTXTAction extends AbstractAction {
        public NewTXTAction(String text, ImageIcon icon) {
            super(text, icon);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            record.addNewFile(".txt");
        }

    }

    /**
     * Action for refreshing current selected document
     */
    private class RefreshAction extends AbstractAction {
        public RefreshAction(String text, ImageIcon icon) {
            super(text, icon);
        }

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    /* action for open a exisitng file */
    private class OpenAction extends AbstractAction {
        public OpenAction(String text, ImageIcon icon, int mnemonic) {
            super(text, icon);
            putValue(NAME, text);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int value = fc.showOpenDialog(null);
            if (value == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                if (record.contains(file)){
                    JOptionPane.showMessageDialog(null, "The file already exists ");
                } else {
                    record.addExistFile(file);
                }
            }
        }
    }

    /* action to save a file */
    private class SaveAction extends AbstractAction {
        public SaveAction(String text, ImageIcon icon, int mnemonic) {
            super(text, icon);
            putValue(NAME, text);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // prepare to saveFIle
            int index = contentPanel.getSelectedIndex();
            if (index >= 0) { // have content
                File file = record.getFileAt(index);
                Component c = contentPanel.getSelectedComponent();

                saveFile(file, c, index);
            }
        }
    }

    /* action to save as a file */
    private class SaveAsAction extends AbstractAction {
        public SaveAsAction(String text, ImageIcon icon, int mnemonic) {
            super(text, icon);
            putValue(NAME, text);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // prepare to saveAsFIle
            int index = contentPanel.getSelectedIndex();
            if (index >= 0) { // have content
                File file = record.getFileAt(index);
                Component c = contentPanel.getSelectedComponent();

                saveAs(file, c, index);
            }
        }
    }

    /* action to close a file */
    private class CloseAction extends AbstractAction {
        public CloseAction(String text, ImageIcon icon, int mnemonic) {
            super(text, icon);
            putValue(NAME, text);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int index = contentPanel.getSelectedIndex();
            if (index >= 0) {// have content
                // for safety
                int value = JOptionPane.showConfirmDialog(null, "Want to save the file before leave?",
                        "Want to save the file first?", JOptionPane.YES_OPTION);
                if (value == JOptionPane.YES_OPTION) {
                    // prepare more to saveFIle
                    File file = record.getFileAt(index);
                    Component c = contentPanel.getSelectedComponent();

                    saveFile(file, c, index);
                }

                // finally remove file
                record.removeFile(index);
            }
        }
    }

    /* action to close all the file */
    private class SaveAllAction extends AbstractAction {
        public SaveAllAction(String text, ImageIcon icon, int mnemonic) {
            super(text, icon);
            putValue(NAME, text);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Component[] components = contentPanel.getAllComponents();
            File[] files = record.getAllFiles();

            // check each component and their file
            for (int i = 0; i < components.length; i++) {
                File file = files[i];
                Component c = components[i];
                contentPanel.setSelectedAt(i);

                saveFile(file, c, i);
            }
        }
    }

    // inner class for hiding text
    private class TextAction extends AbstractAction {
        public TextAction(String text, ImageIcon icon, int mnemonic) {
            super(text, icon);
            putValue(NAME, text);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // change the boolean value for showing text
            enable = !enable;
            textMenuItem.setSelected(enable);
            showAndHideText();

        }
    }

    // inner class for about
    private class AboutAction extends AbstractAction {
        public AboutAction(String text, ImageIcon icon, int mnemonic) {
            super(text, icon);
            putValue(NAME, text);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            aboutBox.setVisible(true);
        }
    }

    /* a action to add left column */
    private class AddLeftColumnAction extends AbstractAction {

        public AddLeftColumnAction(String text, ImageIcon icon) {
            super(text, icon);
        }

        public void actionPerformed(ActionEvent e) {
            Component c = contentPanel.getSelectedComponent();
            if (c == null || c instanceof JTextPane)
                JOptionPane.showMessageDialog(null, "not ok for this type");
            else {
                JTable table = (JTable) c;
                CSVTableModel model = (CSVTableModel) table.getModel();

                int firstSelectedColumn = table.getSelectedColumn(); // a flag
                // to
                // determine
                // if
                // there
                // is
                // any
                // selection
                if (firstSelectedColumn == -1)
                    model.addColumn(0, 1);
                else {
                    int[] columns = table.getSelectedColumns();
                    int firstNewIndex = firstSelectedColumn;
                    int addCount = columns.length;
                    model.addColumn(firstNewIndex, addCount);
                }

            }

        }
    }

    /* action to add right column */
    private class AddRightColumnAction extends AbstractAction {

        public AddRightColumnAction(String text, ImageIcon icon) {
            super(text, icon);
        }

        public void actionPerformed(ActionEvent e) {
            Component c = contentPanel.getSelectedComponent();
            if (c == null || c instanceof JTextPane)
                JOptionPane.showMessageDialog(null, "not ok for this type");
            else {
                JTable table = (JTable) c;
                CSVTableModel model = (CSVTableModel) table.getModel();

                int firstSelectedColumn = table.getSelectedColumn(); // a flag
                // to
                // determine
                // if
                // there
                // is
                // any
                // selection
                if (firstSelectedColumn == -1) { // no selection , add to the
                    // righest side
                    // find the last table colum index , and the new indx to be
                    // added to the right side will be increase by 1
                    int newIndex = table.getColumnCount() - 1 + 1;
                    model.addColumn(newIndex, 1);
                } else {
                    int[] columns = table.getSelectedColumns();
                    // find the last selected table index , and the new index to
                    // be added to its right side will be increase by 1
                    int firstNewIndex = columns[columns.length - 1] + 1;
                    int addCount = columns.length;
                    model.addColumn(firstNewIndex, addCount);
                }

            }

        }
    }

    /* add upper row into current table */
    private class AddUpperRowAction extends AbstractAction {

        public AddUpperRowAction(String text, ImageIcon icon) {
            super(text, icon);
        }

        public void actionPerformed(ActionEvent e) {
            Component c = contentPanel.getSelectedComponent();
            if (c == null || c instanceof JTextPane)
                JOptionPane.showMessageDialog(null, "not ok for this type");
            else {
                JTable table = (JTable) c;
                CSVTableModel model = (CSVTableModel) table.getModel();

                // no selection -> default add to the top
                int[] rows = table.getSelectedRows();
                boolean isSelected = (rows.length > 0);
                int idx = (isSelected) ? rows[0] : 0;
                int count = (isSelected) ? rows.length : 1;

                model.addRowBefore(idx, count);
            }

        }
    }

    /* add lower row into current table */
    private class AddLowerRowAction extends AbstractAction {

        public AddLowerRowAction(String text, ImageIcon icon) {
            super(text, icon);
        }

        public void actionPerformed(ActionEvent e) {
            Component c = contentPanel.getSelectedComponent();
            if (c == null || c instanceof JTextPane)
                JOptionPane.showMessageDialog(null, "not ok for this type");
            else {
                JTable table = (JTable) c;
                CSVTableModel model = (CSVTableModel) table.getModel();

                // no selection -> default add to the bottom
                int[] rows = table.getSelectedRows();
                boolean isSelected = (rows.length > 0);
                int idx = (isSelected) ? rows[rows.length - 1] : (table.getRowCount() - 1);
                int count = (isSelected) ? rows.length : 1;

                model.addRowAfter(idx, count);
            }
        }
    }

    /* removeROw */
    private class RemoveRowAction extends AbstractAction {

        public RemoveRowAction(String text, ImageIcon icon) {
            super(text, icon);
        }

        public void actionPerformed(ActionEvent e) {
            Component c = contentPanel.getSelectedComponent();
            if (c == null || c instanceof JTextPane)
                JOptionPane.showMessageDialog(null, "not ok for this type");
            else {
                JTable table = (JTable) c;
                CSVTableModel model = (CSVTableModel) table.getModel();

                // special case
                if (model.getRowCount() == table.getSelectedRows().length)
                    JOptionPane.showMessageDialog(null, "Cannot remove all rows");

                    // remove them if there is a ok selection
                else {
                    int firstSelectedRow = table.getSelectedRow(); // a flag to
                    // determine
                    // if there
                    // is any
                    // selection
                    if (firstSelectedRow == -1) // no selection , alarm
                        JOptionPane.showMessageDialog(null, "No selection");

                    else {
                        int[] rows = table.getSelectedRows();
                        // find the first del index , and how man we want to del
                        int delCount = rows.length;
                        int firstDelIndex = rows[0];
                        System.out.println(firstDelIndex);
                        System.out.println(delCount);
                        model.removeRow(firstDelIndex, delCount);
                    }

                }

            }

        }
    }

    /* removeRow */
    private class RemoveColumnAction extends AbstractAction {

        public RemoveColumnAction(String text, ImageIcon icon) {
            super(text, icon);
        }

        public void actionPerformed(ActionEvent e) {
            Component c = contentPanel.getSelectedComponent();
            if (c == null || c instanceof JTextPane)
                JOptionPane.showMessageDialog(null, "not ok for this type");
            else {
                JTable table = (JTable) c;
                CSVTableModel model = (CSVTableModel) table.getModel();

                if (model.getColumnCount() == table.getSelectedColumns().length)
                    JOptionPane.showMessageDialog(null, "Cannot remove all column");
                else {
                    int firstSelectedColumn = table.getSelectedColumn(); // a
                    // flag
                    // to
                    // determine
                    // if
                    // there
                    // is
                    // any
                    // selection
                    if (firstSelectedColumn == -1) // no selection-> default add
                        // to the toppest
                        System.out.println("You are kai");
                    else {
                        int[] columns = table.getSelectedColumns();
                        // find the first del index , and how man we want to del
                        int delCount = columns.length;
                        int firstDelIndex = columns[0];
                        model.removeColumn(firstDelIndex, delCount);
                    }
                }

            }

        }
    }

    /* action for clear cell */
    private class ClearCellAction extends AbstractAction {

        public ClearCellAction(String text, ImageIcon icon) {
            super(text, icon);
        }

        public void actionPerformed(ActionEvent e) {
            Component c = contentPanel.getSelectedComponent();
            if (c == null || c instanceof JTextPane)
                JOptionPane.showMessageDialog(null, "not ok for this type");
            else {
                JTable table = (JTable) c;
                CSVTableModel model = (CSVTableModel) table.getModel();
                int firstSelectedRow = table.getSelectedRow(); // a flag to
                // determine if
                // there is any
                // selection
                if (firstSelectedRow == -1)
                    JOptionPane.showMessageDialog(null, "please select some cells to clear");
                else {
                    // find the range to clear
                    int[] rows = table.getSelectedRows();
                    int[] columns = table.getSelectedColumns();

                    int topLeftRow = rows[0];
                    int topLeftColumn = columns[0];
                    int buttonRightRow = rows[rows.length - 1];
                    int buttonRightColumn = columns[columns.length - 1];

                    model.clearCell(topLeftRow, topLeftColumn, buttonRightRow, buttonRightColumn);

                }

            }

        }
    }

    /* action for create new cells in customize */
    private class NewCellAction extends AbstractAction {

        public NewCellAction(String text, ImageIcon icon) {
            super(text, icon);
        }

        public void actionPerformed(ActionEvent e) {
            Component c = contentPanel.getSelectedComponent();
            if (c == null || c instanceof JTextPane)
                JOptionPane.showMessageDialog(null, "not ok for this type");
            else {
                JTable table = (JTable) c;
                CSVTableModel model = (CSVTableModel) table.getModel();
                Object[][] cells = model.createCells();
                model.setCells(cells);
            }

        }
    }

    private class CloseAllAction extends AbstractAction {
        public CloseAllAction(String text, ImageIcon icon) {
            super(text, icon);
        }

        public void actionPerformed(ActionEvent e) {
            int value = JOptionPane.showConfirmDialog(null, "Want to save the file ?", "Want to save all the file?",
                    JOptionPane.YES_OPTION);
            if (value == JOptionPane.YES_OPTION) {
                Action saveAllAction = new SaveAllAction("Save All", new ImageIcon("./graph/saveAs.gif"),
                        KeyEvent.VK_A);
                saveAllAction.actionPerformed(e);
            }
            record.removeAllFiles();
        }
    }

    public static void main(String [] args){
        JCSVEditor editor = new JCSVEditor();
        editor.setVisible(true);
    }
}
