package jcsveditor.parser;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class CSVParser {

    private CSVLineParser lineParser;
    private String[][] results;

    public String[][] getResults() {
        return results;
    }

    public CSVParser(){
        this.lineParser = new CSVLineParser();
    }

    public CSVParser(File file) {
        this.lineParser = new CSVLineParser();

        List<List<String>> rowValues = new LinkedList<>();
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                int numRow = 0;
                int numCol = 0;
                String line = br.readLine();

                while (line != null) {
                    lineParser.setLine(line);

                    List<String> cellValues = lineParser.parseLine();
                    rowValues.add(cellValues);
                    if (cellValues.size() > numCol) {
                        numCol = cellValues.size();
                    }

                    numRow++;

                    line = br.readLine();
                }

                System.out.println("numRow: " + numRow);
                System.out.println("numCol: " + numCol);
                results = new String[numRow][numCol];

                int i = 0;
                for (List<String> rowValue : rowValues) {
                    rowValue.toArray(results[i]);

                    i++;
                }
            }
        } catch (FileNotFoundException ex) {
            throw new CSVParseException("Cannot found the csv file" + ex);
        } catch (IOException ex) {
            throw new CSVParseException("Cannot read the csv file " + ex);
        }
    }

    /**
     * For debug, [----] is to distinguish the whitespace
     */
    public void showResult() {
        for (int i = 0; i < results.length; i++) {
            System.out.println("Row: " + i);
            for (int j = 0; j < results[i].length; j++) {
                System.out.println("Item " + j + " : [----]" + results[i][j] + "[----]");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        // File file = new File("C:\\Users\\jason\\Desktop\\a.txt");
        // CSVParser parser = new CSVParser(file);
        // parser.showResult();

        CSVLineParser lineParser = new CSVLineParser("123,");
        List<String> strs = lineParser.parseLine();

    }
}
