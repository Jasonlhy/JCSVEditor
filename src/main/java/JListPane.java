import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;

/* Title JListPane
 * Author Liu Ho Yin
 * Last Modifired 25-5-2012
 * This class is a view assoicate with FIleRecord model which stores the file record
 * It owns a JList which shows the file name according to the FileRecord model*/

public class JListPane extends JPanel implements Observer {
	private JList list;
	private FileRecord record;

	public JListPane(FileRecord record) {
		setLayout(new BorderLayout());
		DefaultListModel model = new DefaultListModel();
		list = new JList(model);
		add(new JScrollPane(list), BorderLayout.CENTER);

		this.record = record;

		// make assoication between view and model
		record.addObserver(this);
	}

	// update the view when FileRecord have been manipuldated
	public void update(Observable o, Object status) {

		if (status == FileRecord.ADD_NEW_FILE) {
			System.out.println("NewFile is create");

			// get the file just added from model and add its name into the list
			File file = record.getLastFile();
			DefaultListModel model = (DefaultListModel) list.getModel();
			model.addElement(file.getName());
		} else if (status == FileRecord.ADD_EXIST_FILE) {
			// get the file just added from model and add its name into the list
			File file = record.getLastFile();
			DefaultListModel model = (DefaultListModel) list.getModel();
			model.addElement(file.getName());
		} else if (status == FileRecord.REMOVE_FILE) {
			// get the removed file index which is just removed and remove the
			// list item according to the index
			int removedFileIndex = record.getRemovedFileIndex();
			DefaultListModel model = (DefaultListModel) list.getModel();
			model.remove(removedFileIndex);

		} else if (status == FileRecord.CHANGE_FILE) {
			// change the file content -> refresh it (get it based on index)
			DefaultListModel model = (DefaultListModel) list.getModel();
			int index = record.getChangedFileIndex();
			model.set(index, record.getFileAt(index).getName());
		} else if (status == FileRecord.REMOVE_ALL) {
			DefaultListModel model = (DefaultListModel) list.getModel();
			model.clear();
		}
	}

	/* retun the jlist */
	public JList getJList() {
		return list;
	}
}
