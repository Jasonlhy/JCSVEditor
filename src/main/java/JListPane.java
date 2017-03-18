import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

/**
 * Title JListPane
 * Author Liu Ho Yin
 * Last Modified 25-5-2012
 * This class is a view associated with FIleRecord model which stores the file record
 * It owns a JList which shows the file name according to the FileRecord model
 */
public class JListPane extends JPanel implements Observer {
    private JList list;
    private FileRecord record;

    public JListPane(FileRecord record) {
        setLayout(new BorderLayout());
        DefaultListModel model = new DefaultListModel();
        list = new JList(model);
        add(new JScrollPane(list), BorderLayout.CENTER);

        this.record = record;

        // make association between view and model
        record.addObserver(this);
    }

    // update the view when FileRecord have been manipuldated
    public void update(Observable o, Object status) {

        if (status == FileChangeOption.ADD_NEW_FILE) {
            System.out.println("NewFile is create");

            // get the file just added from model and add its name into the list
            File file = record.getLastFile();
            DefaultListModel model = (DefaultListModel) list.getModel();
            model.addElement(file.getName());
        } else if (status == FileChangeOption.ADD_EXIST_FILE) {
            // get the file just added from model and add its name into the list
            File file = record.getLastFile();
            DefaultListModel model = (DefaultListModel) list.getModel();
            model.addElement(file.getName());
        } else if (status == FileChangeOption.REMOVE_FILE) {
            // get the removed file index which is just removed and remove the
            // list item according to the index
            int removedFileIndex = record.getRemovedFileIndex();
            DefaultListModel model = (DefaultListModel) list.getModel();
            model.remove(removedFileIndex);

        } else if (status == FileChangeOption.CHANGE_FILE) {
            // change the file content -> refresh it (get it based on index)
            DefaultListModel model = (DefaultListModel) list.getModel();
            int index = record.getChangedFileIndex();
            model.set(index, record.getFileAt(index).getName());
        } else if (status == FileChangeOption.REMOVE_ALL) {
            DefaultListModel model = (DefaultListModel) list.getModel();
            model.clear();
        }
    }

    /* return the JList */
    public JList getJList() {
        return list;
    }

}
