import javax.swing.table.*;
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
		Object[][] cells = null;
		int noOfRow = 0;
		int maxColumn = 0;
		Vector<String[]> v = new Vector<String[]>();

		BufferedReader reader;
		String line;
		try {
			maxColumn = 0; // variable that tell the array ho wmuch it is
							// required
			reader = new BufferedReader(new FileReader(file));
			while ((line = reader.readLine()) != null) {
				String[] cellsInEachLine = line.split(",");
				if (cellsInEachLine.length > maxColumn)
					maxColumn = cellsInEachLine.length;

				System.out.println(cellsInEachLine.length);
				v.add(cellsInEachLine);
				noOfRow++;
			}

			// make the data as a 2d array
			cells = new Object[noOfRow][maxColumn];
			for (int i = 0; i < cells.length; i++) {
				String[] cellsInLine = v.get(i);
				for (int j = 0; j < maxColumn; j++) {
					if (j >= cellsInLine.length) // the string [] is going index
													// out of bound
						cells[i][j] = "";
					else
						cells[i][j] = cellsInLine[j];
				}
			}

			reader.close();
		} catch (Exception e) {
			System.out.println("input error");
			System.out.println("Please input a good csv fie");
		}
		return cells;
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
