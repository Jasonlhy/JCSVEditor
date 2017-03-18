import java.io.File;

/**
 * Created by jason on 19/3/2017.
 */
public class FileChangeEvent {
    private FileChangeOption option;
    private File file;

    public FileChangeOption getOption(){
        return option;
    }

    public FileChangeEvent(FileChangeOption option, File file){
        this.option = option;
        this.file = file;
    }

}
