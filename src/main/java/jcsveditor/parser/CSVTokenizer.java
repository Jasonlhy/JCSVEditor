package jcsveditor.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public class CSVTokenizer {
    private boolean eol;
    private BufferedReader reader;
    private int currentChar = 0;

    private static int CARRIAGE_RETURN = 13;
    private static int NEW_LINE = 10;


    public boolean isEOF(){
        return currentChar == -1;
    }

    public boolean isEOL(){
        return eol;
    }

    public char peek(){
        return (char) currentChar;
    }

    public CSVTokenizer(String path){
        try {
            reader = Files.newBufferedReader(Paths.get(path));
        } catch (InvalidPathException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CSVTokenizer(File file){
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (InvalidPathException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        if (reader != null){
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Read the next char in stream but exclude the line break character, including -1 for EOL
     *
     * @return
     * @throws IOException
     */
    public int nextNonLB() throws IOException{
        int c1 = next();
        int result = c1;

        do {
            if (c1 == CARRIAGE_RETURN){
                int c2 = next();
                if (c2 == NEW_LINE){
                    c1 = next();
                } else {
                    result = c2;
                }
            } else if (c1 == NEW_LINE){
                c1 = next();
            } else {
                result = c1;
            }
        } while (result == 10 || result == 13);

        return result;
    }

    /**
     * Read the next char in stream regredless of the line
     *
     *
     * @return
     * @throws IOException if end of line or error in reading the file
     */
    public int next() throws IOException {
        int previousChar = currentChar;
        currentChar = reader.read();
        if (eol && previousChar == CARRIAGE_RETURN && currentChar == NEW_LINE){
            currentChar = reader.read();
        }
        eol = (currentChar == CARRIAGE_RETURN) || (currentChar == NEW_LINE);

        return currentChar;
    }
}