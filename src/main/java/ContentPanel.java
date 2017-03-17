import jcsveditor.view.CSVTableModel;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/* Title: ContePanel
* Aurthor : Liu Ho Yin
* Last Modified : 25-5-2012
*
* This class is a view assoicated with the FIleRecord mdeol which stores file records
* It owns JTabbedPane, JDesktopPane. Both of them are used to show the file content.
* The view between them can be swtiched
* Both of them will respone to the change in FIleRecord model
* such as createing new fil, opening a file, savves as file
* Some content may be added, removed, renamed in responed to the change
* */

public class ContentPanel extends JPanel implements Observer {
    private JPanel cardPanel; // holding tabbedPane and desktopPane ith between
    // two view
    private JTabbedPane tabbedPane;
    private JDesktopPane desktopPane;
    private String view;
    private FileRecord record;

    // control value for view
    public static String TAB_VIEW = "Tab View";
    public static String INTERNALFRAME_VIEW = "InternalFrame View";

    // for arraging internalFrame
    public static int x = 0;
    public static int y = 0;

    // data sture to remember the order of insertion of jinternalframe
    private ArrayList<JInternalFrame> orderOfFrame;

    public ContentPanel(FileRecord record) {
        /* contruction */
        cardPanel = new JPanel(new CardLayout());
        tabbedPane = new JTabbedPane();
        desktopPane = new JDesktopPane();
        new JComboBox();
        orderOfFrame = new ArrayList<JInternalFrame>();
        this.record = record;

        // make assoication between view and model
        record.addObserver(this);

        // add pane into card panel
        cardPanel.add(tabbedPane, TAB_VIEW);
        cardPanel.add(desktopPane, INTERNALFRAME_VIEW);

        // show tab view by default
        this.view = TAB_VIEW;

		/* add everythings in to the main panel */
        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);

    }

    /* return tabbedPane */
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    /* return desktopPane */
    public JDesktopPane getJDesktopPane() {
        return desktopPane;
    }

    /* return its view */
    public String getView() {
        return view;
    }

    /* new content(file) */
    public void addContent(File file) {
        // two component waiting to be installed
        Component newContent1;
        Component newContent2;

        // check file type
        String fullFileName = file.getName();
        String extension = "";
        int mid = fullFileName.lastIndexOf(".");
        extension = fullFileName.substring(mid + 1, fullFileName.length());

        if (extension.equals("txt")) {// text file
            String fileData = IOSystem.readTXT(file);
            DefaultStyledDocument document = new DefaultStyledDocument();

            try {
                // Add the text to the document
                document.insertString(0, fileData, null);

            } catch (Exception e) {
            }

            newContent1 = new JTextPane(document);
            newContent2 = new JTextPane(document);

        } else { // csv
            // prepare file data
            Object[][] cells;
            if (file.exists())
                cells = IOSystem.readCSV(file);
            else
                cells = null;

            // two "identical" tables
            CSVTableModel model = new CSVTableModel(cells);
            JTable table1 = new JTable(model);
            JTable table2 = new JTable(model);

            // for cell selection
            table1.setRowSelectionAllowed(true);
            table1.setColumnSelectionAllowed(true);
            table2.setRowSelectionAllowed(true);
            table2.setColumnSelectionAllowed(true);

            // for not draggable
            table1.setDragEnabled(false);
            table2.setDragEnabled(false);

            // for scroolling
            table1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            newContent1 = table1;
            newContent2 = table2;
        }

		/* view for internal frame */
        JInternalFrame internalFrame = new JInternalFrame(file.getName());
        desktopPane.add(internalFrame);
        internalFrame.add(new JScrollPane(newContent1));
        internalFrame.setSize(400, 400);
        internalFrame.setLocation(x, y);
        x += 20;
        y += 20;
        if (x > desktopPane.getWidth())
            x = 0;
        if (y > desktopPane.getWidth())
            y = 0;
        internalFrame.setVisible(true);
        orderOfFrame.add(internalFrame);

		/* view for tab view */
        tabbedPane.addTab(file.getName(), new JScrollPane(newContent2));
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);

    }

    /* to change the view */
    public void changeView(String viewChoice) {
        // remeber the selected index by the old view first
        int selectedIndex = getSelectedIndex();

        // change the view
        this.view = viewChoice;
        CardLayout layout = (CardLayout) cardPanel.getLayout();
        layout.show(cardPanel, view);

        // set the index for new view
        if (selectedIndex != -1)
            setSelectedAt(selectedIndex);
    }

    /* to set the view */
    public void setView(String view) {
        this.view = view;
    }

    /* update when file record has changed */
    public void update(Observable o, Object status) {
        if (status == FileRecord.ADD_NEW_FILE) {
            // frecth the file reference from the model and add contents into
            // view
            // use the file reference to make context
            File file = record.getLastFile();
            addContent(file);
        } else if (status == FileRecord.ADD_EXIST_FILE) {
            // get the file just added from model and add its name into the list
            File file = record.getLastFile();
            addContent(file);
        } else if (status == FileRecord.REMOVE_FILE) { // the model remove a
            // file , and the index
            // not supposed to be
            // here is going to be
            // removed

            tabbedPane.removeTabAt(record.getRemovedFileIndex());

            JInternalFrame frame = orderOfFrame.remove(record.getRemovedFileIndex());
            desktopPane.remove(frame);
            desktopPane.updateUI(); // prevent a bug because of unknow reason

        } else if (status == FileRecord.CHANGE_FILE) { // the model remove a
            // file , change the
            // index title
            // get the new title
            int index = record.getChangedFileIndex();
            String name = record.getFileAt(index).getName();

            tabbedPane.setTitleAt(index, name);

            JInternalFrame frame = orderOfFrame.get(index);
            frame.setTitle(name);
            desktopPane.updateUI(); // prevent a bug because of unknow reason

        } else if (status == FileRecord.REMOVE_ALL) { // fileRecord remove all
            // file
            tabbedPane.removeAll();

            desktopPane.removeAll();
            desktopPane.updateUI();

            orderOfFrame.clear();
        }

    }

    /*
     * return a seleced index based on the view using , this index is basd on
     * the order of file creation therefore the index is the same for the "same"
     * doucment in different view
     */
    public int getSelectedIndex() {
        int index = -1;
        if (getView().equals(TAB_VIEW))
            index = getTabbedPane().getSelectedIndex();
        else {
            // find the order with the selected frame
            JInternalFrame frame = desktopPane.getSelectedFrame();
            index = orderOfFrame.indexOf(frame);
        }
        return index;
    }

    /* return the order for inputting the internal frame */
    public ArrayList<JInternalFrame> getOrderOfFrame() {
        return orderOfFrame;
    }

    /* get content componened in selected tabbedPane/internalFrame */
    public Component getSelectedComponent() {
        JScrollPane selectedScrollPane = selectedScrollPane = (JScrollPane) tabbedPane.getSelectedComponent();
        if (view.equals(INTERNALFRAME_VIEW)) {
            JInternalFrame frame = (JInternalFrame) desktopPane.getSelectedFrame();
            Container container = frame.getContentPane();
            selectedScrollPane = (JScrollPane) container.getComponent(0);
        }

        // no selection
        if (selectedScrollPane == null)
            return null;

        JViewport viewport = (JViewport) selectedScrollPane.getComponent(0);
        Component c = viewport.getComponent(0);
        return c;
    }

    /*
     * get all the component for editing we only return the component in the
     * jtabbedpane because the content in the jtabbedpane and jinternal frame is
     * the sasme but it is much more easy to get the component from jtabbedpane
     */
    public Component[] getAllComponents() {
        // return JScrollPane array
        Component[] scrollPanes = tabbedPane.getComponents();
        Component[] contents = new Component[scrollPanes.length];

        // get the content inside scrollpane
        JViewport viewport;
        for (int i = 0; i < scrollPanes.length; i++) {
            JScrollPane pane = (JScrollPane) scrollPanes[i];
            viewport = (JViewport) pane.getComponent(0);
            contents[i] = viewport.getComponent(0);
        }

        return contents;
    }

    /* set all view selected at a particular index */
    public void setSelectedAt(int index) {
        // get the frame -> select it
        JInternalFrame frame = orderOfFrame.get(index);
        desktopPane.setSelectedFrame(frame);
        desktopPane.moveToFront(frame);
        frame.setLocation(0, 0);
        desktopPane.updateUI();

        // directly set the selected index
        tabbedPane.setSelectedIndex(index);
    }

}
