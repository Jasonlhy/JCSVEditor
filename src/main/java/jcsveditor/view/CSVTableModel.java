package jcsveditor.view;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.Arrays;

/* Title jcsveditor.table.CSVTableModel
 * Aurthor : Liu Ho Yin
 * Last Modified: 25-5-2012
 *
 * This class is the table model for shwoing csv
 * it have some advanced function such as inserting row ,removing row*/

public class CSVTableModel extends AbstractTableModel {

    private Object[][] cells;
    private String[] columnNames;

    /* no data providied */
    public CSVTableModel() {
        this(null, null);
    }

    public CSVTableModel(Object[][] cells) {
        this(cells, null);
    }

    public CSVTableModel(Object[][] cells, String[] columnNames) {
        // ask for default cells
        if (cells == null)
            this.cells = createCells();
        else
            this.cells = cells;

        this.columnNames = columnNames;
    }

    public Object[][] getCells() {
        return cells;
    }

    /*
         * return columnName for table header Inputted the columName ,use them
         * otherwise user A,B,C,D from abstractTableModel
         */
    public String getColumnName(int c) {
        return (columnNames != null) ? columnNames[c] : super.getColumnName(c);
    }

    public Class getColumnClass(int c) {
        return cells[0][c].getClass();
    }

    public int getColumnCount() {
        return cells[0].length;
    }

    public int getRowCount() {
        return cells.length;
    }

    public Object getValueAt(int r, int c) {
        return cells[r][c];
    }

    public void setValueAt(Object obj, int r, int c) {
        cells[r][c] = obj;
        fireTableCellUpdated(r, c);
    }

    public boolean isCellEditable(int r, int c) {
        return true;
    }

    /*
     * add a new Column into the exisiting table make a new cells copy the
     * origninal value, initiazlie data in new added addedColumns redirect the
     * object reference for cells data
     */
    public void addColumn(int firstNewIndex, int addCount) {
        // prepare to copy
        int noOfRow = getRowCount();
        int noOfColumn = getColumnCount();
        Object[][] newCells = new Object[noOfRow][noOfColumn + addCount];

        // this is a column pointer for the new created array
        // to help it from getting the value from the original cell
        // when the column index of the original cells bigger than the new index
        // that mean that value should be at the right side of the new added
        // column
        // and the data at the right side of the new added column should be
        // shifted at number of added column
        // and we shift the column pointer to right by number of added column
        int cP;
        for (int i = 0; i < noOfRow; i++) {
            for (int j = 0; j < noOfColumn; j++) {
                cP = (j >= firstNewIndex) ? j + addCount : j;
                newCells[i][cP] = cells[i][j];
            }
        }

        for (int i = 0; i < noOfRow; i++) {
            for (int j = 0; j < addCount; j++)
                newCells[i][firstNewIndex + j] = "";
        }

        // redirect the object reference
        this.cells = newCells;

        // tell view to update
        fireTableStructureChanged();
    }

    /**
     * Add row(s) before a row
     *
     * @param idx
     * @param count
     */
    public void addRowBefore(int idx, int count){
        if (idx < 0 || idx >= getRowCount()){
            throw new IllegalArgumentException("Please provide a valid index of row to be inserted before");
        }
        addRow(idx, count);
    }

    /**
     * Add row(s) after a row
     *
     * @param idx
     * @param count
     */
    public void addRowAfter(int idx, int count){
        if (idx < 0 || idx >= getRowCount()){
            throw new IllegalArgumentException("Please provide a valid index of row to be inserted after");
        }
        addRow(idx + 1, count);
    }

    /**
     * Generic addRow function implements addRowBefore and addRowAfter
     * Because addRowAfter(idx) = addRowBefore(idx + 1)
     * It assumes the data is valid
     *
     * @param beforeRow
     * @param addCount
     */
    private void addRow(int beforeRow, int addCount) {
        // 1. Create a new array
        // 2. Copy the existing content to the new array with skipping the "new row"
        int noRow = getRowCount();
        int noCol = getColumnCount();
        Object[][] newCells = new Object[noRow + addCount][noCol];

        int rP;
        for (int i = 0; i < noRow; i++) {
            rP = (i >= beforeRow) ? (i + addCount) : i;
            System.arraycopy(cells[i], 0, newCells[rP], 0, noCol);
        }

        // initialize the new added cells
        for (int j = 0; j < addCount; j++){
            Arrays.fill(newCells[beforeRow + j], "");
        }

        // redirect the object reference
        this.cells = newCells;

        // tell view to update
        fireTableStructureChanged();

    }

    /* remove a row from the table model */
    public void removeRow(int firstDelIndex, int delCount) {
        // prepare to copy
        int noOfRow = getRowCount();
        int noOfColumn = getColumnCount();
        Object[][] newCells = new Object[noOfRow - delCount][noOfColumn];

        // we need to make the rows that below the removed row move upforward
        // so we use a rP to point to a particular index of the original table
        // cells
        // copy "correct" data to our new cells
        // normally the rP is same as the row inedx where the row is higher than
        // the removed row
        // but after the new cells pointer reach the first remove index , rP
        // will be increase by one to shift to the correct "data "
        // if we want to remove the 3rd row in the original cells -> then we
        // shift 1
        // the 1st rows in new cell is equals to the 1st row in original cells
        // the 2nd rows in new cell is equals to the 2nd row in original cells
        // the 3rd rows in new cell is equals to the 4th row in original cells
        // then 4thn rows in new cell is equals to the 5th row in original cells
        // ....
        //
        // if we want to remove the 3rd and 4th row in the original cells ->
        // then we shift 2
        // the 1st rows in new cell is equals to the 1st row in original cells
        // the 2nd rows in new cell is equals to the 2nd row in original cells
        // the 3rd rows in new cell is equals to the 5th row in original cells
        // then 5throws in new cell is equals to the 7th row in original cells
        // ....

        int rP;
        for (int i = 0; i < newCells.length; i++) {
            rP = (i >= firstDelIndex) ? i + delCount : i;
            System.out.println("i" + i);
            System.out.println("rP" + rP);
            for (int j = 0; j < newCells[0].length; j++) {
                System.out.println("j" + j);
                newCells[i][j] = cells[rP][j];
            }
        }

        // redirect object reference
        this.cells = newCells;

        // tell view to update
        fireTableStructureChanged();
    }

    /* remove a column from the table model */
    public void removeColumn(int firstDelIndex, int delCount) {
        // prepare to copy
        int noOfRow = getRowCount();
        int noOfColumn = getColumnCount();
        Object[][] newCells = new Object[noOfRow][noOfColumn - delCount];

        int cP;
        for (int i = 0; i < newCells.length; i++) {
            for (int j = 0; j < newCells[0].length; j++) {
                cP = (j >= firstDelIndex) ? j + delCount : j;
                newCells[i][j] = cells[i][cP];
            }
        }

        // redirect object reference
        this.cells = newCells;

        // tell view to update
        fireTableStructureChanged();

    }

    /* clear the cell of a range in the model */
    public void clearCell(int topLeftRow, int topLeftColumn, int buttonRightRow, int buttonRightColumn) {
        for (int i = topLeftRow; i <= buttonRightRow; i++) {
            for (int j = topLeftColumn; j <= buttonRightColumn; j++) {
                cells[i][j] = "";
            }
        }

        fireTableDataChanged();
    }

    /* create "" cells for tabal data */
    public Object[][] createCells() {
        boolean isSuccess = true;

        String stringOfRows = JOptionPane.showInputDialog(null, "How many rows do you want ?",
                "Please enter the number of row", JOptionPane.QUESTION_MESSAGE);
        String stringOfColumns = JOptionPane.showInputDialog(null, "How many columns do you want ?",
                "Please enter the number of column", JOptionPane.QUESTION_MESSAGE);

        int rows = 0;
        int columns = 0;
        try {
            rows = Integer.parseInt(stringOfRows);
            columns = Integer.parseInt(stringOfColumns);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "wrong input ! cells is set to 4*4");
            isSuccess = false;
        }

        Object[][] cells = (isSuccess) ? new Object[rows][columns] : new Object[4][4];

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++)
                cells[i][j] = "";
        }

        return cells;
    }

    /* set the cells of this model using */
    public void setCells(Object[][] cells) {
        this.cells = cells;
        fireTableDataChanged();
        fireTableStructureChanged();
    }
}
