import java.util.*;
import java.io.*;

/* Title: FIleRecord 
 * Author: Liu Ho Yin
 * Last Modifired: 25-5-2012
 * act as a model for jlist,editing view (RightOfEditor)*/

public class FileRecord extends Observable {
	// a data structure to store the file record
	private LinkedList<File> list = new LinkedList<File>();
	private int removedFileIndex = 0;
	private int changedFileIndex = 0;

	// status to indicate the change to this FileRecord Model
	public final static Integer ADD_NEW_FILE = 1234;
	public final static Integer ADD_EXIST_FILE = 12345;
	public final static Integer REMOVE_FILE = 1234567;
	public final static Integer CHANGE_FILE = 12345678;
	public final static Integer REMOVE_ALL = 123232984;

	// counter variable for countering the new file
	public static Integer newFileCounter = 0;

	public FileRecord() {
	}

	/* add a bank new file record which is not exist */
	public void addNewFile(String extension) {
		list.add(new File("New File " + newFileCounter + extension));

		newFileCounter++;

		// tell the views to update
		setChanged();
		notifyObservers(ADD_NEW_FILE);
	}

	/* add a exist file record , the parameter is passed from controller */
	public void addExistFile(File existFile) {
		list.add(existFile);

		// tell the views to update
		setChanged();
		notifyObservers(ADD_EXIST_FILE);
	}

	/*
	 * remove a file reference from this model, the index is passd from the
	 * controller, determine by the internalfram/tabbeed panel selected index
	 */
	public void removeFile(int index) {
		setRemovedFileIndex(index);
		File removedFile = list.remove(index);

		// tell the views to update
		setChanged();
		notifyObservers(REMOVE_FILE);
	}

	/* return a file at the particular index */
	public File getFileAt(int index) {
		return list.get(index);
	}

	/* return the new file reference just added into this model */
	public File getLastFile() {
		return list.getLast();
	}

	/* return the last remove file Index */
	public int getRemovedFileIndex() {
		return removedFileIndex;
	}

	/* set removedFileIndex */
	public void setRemovedFileIndex(int removedFileIndex) {
		this.removedFileIndex = removedFileIndex;
	}

	/* change the file Record */
	public void changeFileAt(int index, File file) {
		list.set(index, file);
		setChangedFileIndex(index);

		// tell the views to update
		setChanged();
		notifyObservers(CHANGE_FILE);
	}

	/* return changedFileIndex */
	public int getChangedFileIndex() {
		return changedFileIndex;
	}

	/* set changedFileIndex */
	public void setChangedFileIndex(int index) {
		changedFileIndex = index;
	}

	/* get all files in model */
	public File[] getAllFiles() {
		int size = list.size();
		File[] files = new File[size];
		return list.toArray(files);
	}

	/* remove all the files in the record */
	public void removeAllFiles() {
		list.clear();

		// update view
		setChanged();
		notifyObservers(REMOVE_ALL);
	}

	/* return the index of a partifuclar file record */
	public int indexOf(File file) {
		return list.indexOf(file);
	}

}
