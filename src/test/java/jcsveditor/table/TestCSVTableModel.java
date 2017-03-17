package jcsveditor.table;

import org.junit.Before;
import org.junit.Test;

import javax.swing.table.AbstractTableModel;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

/**
 * Created by jason on 18/3/2017.
 */
public class TestCSVTableModel {
    private CSVTableModel model;

    @Before
    public void init(){
        String [][] data = new String[3][3];
        data[0][0] = "aaa1";
        data[0][1] =  "bbb1";
        data[0][2] = "ccc1";

        data[1][0] = "aaa2";
        data[1][1] =  "bbb2";
        data[1][2] = "ccc2";

        data[2][0] = "aaa3";
        data[2][1] =  "bbb3";
        data[2][2] = "ccc3";

        model = new CSVTableModel(data);
    }

    @Test
    public void testAddRow(){
        model.addRow(0, 1);

        Object [][] actual = model.getCells();

        Object [][] expected  = new String[4][3];
        expected[0][0] = "";
        expected[0][1] =  "";
        expected[0][2] = "";

        expected[1][0] = "aaa1";
        expected[1][1] =  "bbb1";
        expected[1][2] = "ccc1";

        expected[2][0] = "aaa2";
        expected[2][1] =  "bbb2";
        expected[2][2] = "ccc2";

        expected[3][0] = "aaa3";
        expected[3][1] =  "bbb3";
        expected[3][2] = "ccc3";

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testAddColumn(){
        model.addColumn(0, 1);

        Object [][] actual = model.getCells();

        Object [][] expected  = new String[3][4];
        expected[0][0] = "";
        expected[0][1] = "aaa1";
        expected[0][2] =  "bbb1";
        expected[0][3] = "ccc1";

        expected[1][0] = "";
        expected[1][1] =  "aaa2";
        expected[1][2] = "bbb2";
        expected[1][3] = "ccc2";

        expected[2][0] = "";
        expected[2][1] =  "aaa3";
        expected[2][2] = "bbb3";
        expected[2][3] = "ccc3";

        assertArrayEquals(expected, actual);
    }
}
