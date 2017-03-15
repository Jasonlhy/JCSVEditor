package jcsveditor.parser.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import jcsveditor.parser.CSVLineParser;

public class TestCSVLIneParser {

	@Test
	public void testNonEscapeNoSpace() {
		CSVLineParser parser = new CSVLineParser("aaa,bbb");
		List<String> tokens = parser.parseLine();
		String [] actual = new String[tokens.size()];
		tokens.toArray(actual);
		
		String [] expected = new String[] {"aaa", "bbb"};
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testNonEscapeWithSpace() {
		CSVLineParser parser = new CSVLineParser("  aaa  ,  bbb  ");
		List<String> tokens = parser.parseLine();
		String [] actual = new String[tokens.size()];
		tokens.toArray(actual);
		
		String [] expected = new String[] {"  aaa  ", "  bbb  "};
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testEscapedNoSpace() {
		CSVLineParser parser = new CSVLineParser("\"aaa\",\"bbb\"");
		List<String> tokens = parser.parseLine();
		String [] actual = new String[tokens.size()];
		tokens.toArray(actual);
		
		String [] expected = new String[] {"aaa", "bbb"};
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testEscapedWithSpace() {
		CSVLineParser parser = new CSVLineParser("\"aaa   \",\"  bbb  \"");
		List<String> tokens = parser.parseLine();
		String [] actual = new String[tokens.size()];
		tokens.toArray(actual);
		
		String [] expected = new String[] {"aaa   ", "  bbb  "};
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testNestedEscapedNoCommas() {
		CSVLineParser parser = new CSVLineParser("\"aaa \"\" nested \"\" \",\"bbb\"");
		List<String> tokens = parser.parseLine();
		String [] actual = new String[tokens.size()];
		tokens.toArray(actual);
		
		String [] expected = new String[] {"aaa \" nested \" ", "bbb"};
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testNestedEscapedWithCommas() {
		CSVLineParser parser = new CSVLineParser("\"aaa \"\" nested , string \"\" \",\"bbb\"");
		List<String> tokens = parser.parseLine();
		String [] actual = new String[tokens.size()];
		tokens.toArray(actual);
		
		String [] expected = new String[] {"aaa \" nested , string \" ", "bbb"};
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testLastThreeColumnsEmpty() {
		CSVLineParser parser = new CSVLineParser("aaa,,,");
		List<String> tokens = parser.parseLine();
		String [] actual = new String[tokens.size()];
		tokens.toArray(actual);
		
		String [] expected = new String[] {"aaa","","",""};
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testMiddleColumsEmpty() {
		CSVLineParser parser = new CSVLineParser("aaa,,,bbb");
		List<String> tokens = parser.parseLine();
		String [] actual = new String[tokens.size()];
		tokens.toArray(actual);
		
		String [] expected = new String[] {"aaa","","","bbb"};
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testRandomColumsEmpty() {
		CSVLineParser parser = new CSVLineParser("aaa,,bbb, ,ccc");
		List<String> tokens = parser.parseLine();
		String [] actual = new String[tokens.size()];
		tokens.toArray(actual);
		
		String [] expected = new String[] {"aaa","","bbb"," ","ccc"};
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testRandomColumsEscapedEmpty() {
		CSVLineParser parser = new CSVLineParser("aaa,,\"bbb\", ,\" \"\"nestedstring\"\"\",,\"ccc\", ddddd");
		List<String> tokens = parser.parseLine();
		String [] actual = new String[tokens.size()];
		tokens.toArray(actual);
		
		String [] expected = new String[] {"aaa","","bbb"," ", " \"nestedstring\"", "", "ccc", " ddddd"};
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testAllColumnsEmpty() {
		CSVLineParser parser = new CSVLineParser(",,,");
		List<String> tokens = parser.parseLine();
		String [] actual = new String[tokens.size()];
		tokens.toArray(actual);
		
		String [] expected = new String[] {"","",""};
		assertArrayEquals(expected, actual);
	}

}
