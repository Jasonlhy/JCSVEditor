import java.io.File;
import java.util.LinkedList;
import java.util.Observable;

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
        notifyObservers(FileChangeOption.ADD_NEW_FILE);
    }

    /**
     * Is the file added into the editor
     *
     * @param file
     * @return True if the file added into the editor, false if the added is not added into the editor
     */
    public boolean contains(File file) {
        if (file == null){
            throw  new IllegalArgumentException("file cannot be null");
        }

        return list.stream().anyMatch( f->
                f.getAbsolutePath().equals(file.getAbsolutePath())
        );
    }

    /* add a exist file record , the parameter is passed from controller */
    public void addExistFile(File existFile) {
        list.add(existFile);

        // tell the views to update
        setChanged();
        notifyObservers(FileChangeOption.ADD_EXIST_FILE);
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
        notifyObservers(FileChangeOption.REMOVE_FILE);
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
        notifyObservers(FileChangeOption.CHANGE_FILE);
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
        notifyObservers(FileChangeOption.REMOVE_ALL);
    }

    public void refreshFile() {
        // update view
        setChanged();
        notifyObservers(FileChangeOption.REFRESH_FILE);
    }
}
