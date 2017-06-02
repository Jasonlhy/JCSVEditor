package test;

import jcsveditor.parser.CSVParser;
import jcsveditor.parser.CSVTokenizer;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TestCSVLIneParser {
    private static String[][] parseString(String s) throws java.io.IOException{
        BufferedReader reader = new BufferedReader(new StringReader(s));
        CSVTokenizer tokenizer = new CSVTokenizer((reader));
        CSVParser parser = new CSVParser(tokenizer);
        return parser.parse();
    }

    @Test
    public void testNonEscapeNoSpace() throws java.io.IOException{
        String [][] results = parseString("aaa,bbb");

        assertEquals(1, results.length);
        String[] actual = results[0];
        String[] expected = new String[]{"aaa", "bbb"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testNonEscapeWithSpace() throws java.io.IOException {
        String [][] results = parseString("  aaa  ,  bbb  ");
        String[] actual = results[0];

        String[] expected = new String[]{"  aaa  ","  bbb  "};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testEscapedNoSpace() throws java.io.IOException {
        String [][] results = parseString("\"aaa\",\"bbb\"");
        String[] actual = results[0];

        String[] expected = new String[]{"aaa","bbb"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testEscapedWithSpace() throws java.io.IOException {
        String [][] results = parseString("\"aaa   \",\"  bbb  \"");
        String[] actual = results[0];

        String[] expected = new String[]{"aaa   ","  bbb  "};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testNestedEscapedNoCommas() throws java.io.IOException {
        String [][] results = parseString("\"aaa \"\" nested \"\" \",\"bbb\"");
        String[] actual = results[0];

        String[] expected = new String[]{"aaa \" nested \" ","bbb"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testNestedEscapedWithCommas() throws java.io.IOException {
        String [][] results = parseString("\"aaa \"\" nested , string \"\" \",\"bbb\"");
        String[] actual = results[0];

        String[] expected = new String[]{"aaa \" nested , string \" ", "bbb"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testLastThreeColumnsEmpty() throws java.io.IOException {
        String [][] results = parseString("aaa,,,");
        String[] actual = results[0];

        String[] expected = new String[]{"aaa","","",""};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testMiddleColumsEmpty() throws java.io.IOException {
        String [][] results = parseString("aaa,,,bbb");
        String[] actual = results[0];

        String[] expected = new String[]{"aaa","","","bbb"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testRandomColumsEmpty() throws java.io.IOException {
        String [][] results = parseString("aaa,,bbb, ,ccc");
        String[] actual = results[0];

        String[] expected = new String[]{"aaa","","bbb"," ","ccc"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testRandomColumsEscapedEmpty() throws java.io.IOException {
        String [][] results = parseString("aaa,,\"bbb\", ,\" \"\"nestedstring\"\"\",,\"ccc\", ddddd");
        String[] actual = results[0];

        String[] expected = new String[]{"aaa", "", "bbb", " ", " \"nestedstring\"", "", "ccc", " ddddd"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testAllColumnsEmpty() throws java.io.IOException {
        String [][] results = parseString(",,,");
        String[] actual = results[0];

        String[] expected = new String[]{"", "", "", ""};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testTwoConsecutiveCommas() throws java.io.IOException {
        String [][] results = parseString(",,22");
        String[] actual = results[0];

        String[] expected = new String[]{"", "", "22"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testNonEscapedLastColumnEmpty() throws java.io.IOException {
        String [][] results = parseString("123,");
        String[] actual = results[0];

        String[] expected = new String[]{"123", ""};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testEscapedLastColumnEmpty() throws java.io.IOException {
        String [][] results = parseString("\"123\",");
        String[] actual = results[0];

        String[] expected = new String[]{"123", ""};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSingleNonEscapedValue() throws java.io.IOException {
        String [][] results = parseString("123");
        String[] actual = results[0];

        String[] expected = new String[]{"123"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSingleEscapedValue() throws java.io.IOException {
        String [][] results = parseString("\"123\"");
        String[] actual = results[0];

        String[] expected = new String[]{"123"};
        assertArrayEquals(expected, actual);
    }
}
