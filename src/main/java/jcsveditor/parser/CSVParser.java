package jcsveditor.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVParser {
    private CSVTokenizer token;
    private String[][] results;

    public String[][] getResults() {
//        results[0][0] = "abc\ngggg";
        return results;
    }

    // this constructor is mainly used for testing
    public CSVParser(CSVTokenizer token) {
        this.token = token;
    }

    public CSVParser(String path) {
        this.token = new CSVTokenizer(path);
    }

    public CSVParser(File file) {
        this.token = new CSVTokenizer(file);
    }

    /**
     * Number of column depend on number of value field
     *
     * @return
     */
    public String[][] parse() throws IOException {
        List<List<String>> rowValues = new ArrayList<>();

        int numRow = 0;
        int numCol = 0;
        do {
            List<String> cellValues = parseLine();
            // some csv file always have line break at the very end .... just ignore it
            boolean isTrailingRow = cellValues.size() == 1 && (cellValues.get(0).charAt(0) == ((char) - 1));
            if (!isTrailingRow){
                rowValues.add(cellValues);
                if (cellValues.size() > numCol) {
                    numCol = cellValues.size();
                }
                numRow++;
            }
        } while (!token.isEOF());

        System.out.println("numRow: " + numRow);
        System.out.println("numCol: " + numCol);
        results = new String[numRow][numCol];

        int i = 0;
        for (List<String> rowValue : rowValues) {
            rowValue.toArray(results[i]);
            i++;
        }

        return results;
    }

    /**
     * Read a CSV line
     *
     * @return
     * @throws IOException
     */
    private List<String> parseLine() throws IOException {
        token.next();

        // init
        StringBuilder builder = new StringBuilder();

        // prepare
        List<String> cellValues = new ArrayList<>();
        char current;

        // edge case: first one is ,
        if (token.peek() == ','){
            cellValues.add("");
        }

        do {
            builder.setLength(0);
            current = token.peek();

            // ',' will be skip in method
            if (current == '"') {
                token.next();
                readEscapedValue(builder);
                cellValues.add(builder.toString());
            } else if (current == ',') {
                // , as delimiter
                // assume before it has a value
                // also eat continuous ,
                token.next();

                // for continuous ,  e.g. "aaa|,,|bbbb"
                while (!token.isEOL() && token.peek() == ','){
                    cellValues.add("");
                    token.next();
                }

                // edge case: last one is ,
                if (token.isEOL() || token.isEOF()){
                    cellValues.add("");
                }
            } else {
                readNonEscapedValue(builder);
                cellValues.add(builder.toString());
            }
        } while (!token.isEOL() && !token.isEOF());

        return cellValues;
    }

    /**
     * Read everything util , or EOL
     *
     * @param builder
     */
    private void readNonEscapedValue(StringBuilder builder) throws IOException {
        if (token.isEOL()) {
            // throw new CSVParseException("Unexpected End");
        }

        boolean foundComma;
        do {
            foundComma = false;

            char c = token.peek();
            if (c == ',') {
                foundComma = true;
            } else {
                builder.append(c);
                token.next();
            }
        } while (!token.isEOL() && !token.isEOF() && !foundComma);
    }

    /**
     * Read everything util ", or to "EOL
     *
     * @param builder
     */
    private void readEscapedValue(StringBuilder builder) throws IOException {
        boolean foundDoubleQuote;
        do {
            foundDoubleQuote = false;
            char c = token.peek();

            if (c == '"') {
                token.next();

                if (!token.isEOL() && token.peek() == '"') {
                    // 2double quote
                    builder.append('"');

                    // assume it is not end
                    token.next();
                } else {
                    // assume it is followed by ',' or EOL
                    if (!token.isEOL() && token.peek() != ',') {
//                        throw new CSVParseException("Expected , at columns: " + idx);
                    }

                    foundDoubleQuote = true;
                }
            } else {
                if (token.isEOL()){
                    builder.append("\n");
                } else {
                    builder.append(c);
                }
                token.next();
            }
        } while (!token.isEOF() && !foundDoubleQuote);
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

    public static void main(String[] args) throws IOException {
//        CSVParser parser = new CSVParser("C:\\Users\\jason\\Desktop\\single.csv");
//        parser.parseLine();
        CSVParser parser = new CSVParser("C:\\Users\\jason\\Desktop\\single_escape.csv");
        parser.parse();

        System.out.println("end of program");
    }
}