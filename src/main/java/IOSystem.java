import jcsveditor.parser.CSVParser;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.*;

/*Title IOSystem
 * Author: Liu Ho Yin
 * V1: Last Modified: 25-5-2012
 * V2 Support Double Quote: Last Modified: 16-3-2012
 * This class is just used to output and input with static method*/
public class IOSystem {
    private static String LINE_SEPARATOR = System.getProperty("line.separator");

    // read for txt
    public static String readTXT(File file) {
        String fileData = "";
        String line = "";
        BufferedReader reader;
        if (file.exists()) {
            try {
                reader = new BufferedReader(new FileReader(file));
                while ((line = reader.readLine()) != null)
                    fileData += line + "\n";

                reader.close();
            } catch (Exception e) {
                System.out.println("input error");
            }
        }
        return fileData;
    }

    // read for csv
    public static Object[][] readCSV(File file) {
        CSVParser parser = new CSVParser(file);
        try {
            parser.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parser.getResults();
    }

    // write file and base on component type
    public static void write(File file, Component c) {
        if (c instanceof JTextPane) {
            JTextPane pane = (JTextPane) c;
            String text = pane.getText();
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(text);
                writer.close();
            } catch (Exception ex) {
                System.out.println("out put error ");
            }
        } else if (c instanceof JTable) {
            JTable table = (JTable) c;
            TableModel model = table.getModel();

            int noOfRow = model.getRowCount();
            int noOfColumn = model.getColumnCount();

            // find the last row and column contains record
            // trim the rows and columns in saving to a file
            int maxColumnIndex = 0;
            int maxRowIndex = 0;

            for (int i = 0; i < noOfRow; i++) {
                for (int j = 0; j < noOfColumn; j++) {
                    // get the cell value
                    Object value = model.getValueAt(i, j);
                    // increase counter if possible
                    if (value != null && !value.equals("") && j > maxColumnIndex)
                        maxColumnIndex = j;
                    if (value != null && !value.equals("") && i > maxRowIndex)
                        maxRowIndex = i;
                }
            }

            // by retrieve the size of table, get their value
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i <= maxRowIndex; i++) {
                for (int j = 0; j <= maxColumnIndex; j++) {
                    Object value = model.getValueAt(i, j);
                    String cellContent = (String) value;
                    if (cellContent != null) {
                        boolean containDoubleQuote = cellContent.contains("\"");
                        boolean containCommas = cellContent.contains(",");
                        boolean mustEscape = containDoubleQuote || containCommas;

                        if (containDoubleQuote) {
                            cellContent = cellContent.replaceAll("\"", "\"\"");
                        }

                        if (mustEscape) {
                            builder.append('"');
                            builder.append(cellContent);
                            builder.append('"');
                        } else {
                            builder.append(value);
                        }
                    } else {
                        builder.append(value);
                    }

                    if (j != maxColumnIndex) {
                        builder.append(',');
                    }
                }

                builder.append(LINE_SEPARATOR);
            }

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.append(builder);
                writer.close();
            } catch (Exception ex) {
                System.out.println("out put error ");
            }

        }

    }

}
