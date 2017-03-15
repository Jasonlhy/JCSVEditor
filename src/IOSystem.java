import javax.swing.table.*;

import jcsveditor.parser.CSVParser;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;

/*Title IOSystem
 * Author: Liu Ho Yin
 * Last Modifired: 25-5-2012
 * 
 * This class is just used to output and input with static method*/
public class IOSystem {
	static String line = System.getProperty("line.separator");

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

			// since the table size may be bigger than the value contain in the
			// file
			// we need to check out what value needed to be outputed
			// the counter is about the exact max row and column index we need
			// to output
			int maxColumnIndex = 0;
			int maxRowIndex = 0;

			// still have a word in a line && column index is bigger than the
			// counter-> expand the coulumn
			// still have a word in a line && row index is bigger than the
			// counter -> expand the row couter
			Object[][] copyOfCells = new Object[noOfRow][noOfColumn];
			for (int i = 0; i < noOfRow; i++) {
				for (int j = 0; j < noOfColumn; j++) {
					// get the cell value
					Object value = model.getValueAt(i, j);
					// increase counter if possible
					if (value != "" && j > maxColumnIndex)
						maxColumnIndex = j;
					if (value != "" && i > maxRowIndex)
						maxRowIndex = i;
				}
			}

			// by retrive the size of table, get their value
			String output = "";
			for (int i = 0; i <= maxRowIndex; i++) {
				for (int j = 0; j <= maxColumnIndex; j++) {
					if (j == maxColumnIndex)// last item
						output += model.getValueAt(i, j);
					else
						output += model.getValueAt(i, j) + ",";
				}
				output += line;
			}

			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				writer.write(output);
				writer.close();
			} catch (Exception ex) {
				System.out.println("out put error ");
			}

		}

	}

}
