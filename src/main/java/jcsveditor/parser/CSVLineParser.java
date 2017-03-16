package jcsveditor.parser;

import java.util.LinkedList;
import java.util.List;

// Follow this standard: https://tools.ietf.org/html/rfc4180
// 1. , " must be escaped inside ""
// 2. Field value can be escaped inside "" or non-escaped followed by a delimiter
public class CSVLineParser {
	private int idx;
	private String line;

	public CSVLineParser() {

	}

	public CSVLineParser(String line) {
		this.line = line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public char peek() {
		return line.charAt(idx);
	}

	/**
	 * Increment the index and check the character it is the expected character
	 * or not
	 * 
	 * @param expectedChar
	 */
	public void next(char expectedChar) {
		if (peek() != expectedChar) {
			// run time exceptions
			// throw new JSONParseException("Expected " + expectedChar + " at "
			// + idx + " but " + peek() + " is found");
		}

		idx++;
	}

	/**
	 * Increment the index without checking the validity of the syntax
	 */
	public void next() {
		idx++;
	}

	public boolean isEnd() {
		return idx >= line.length();
	}

	/**
	 * Number of column depend on number of value field
	 * 
	 * @return
	 */
	public List<String> parseLine() {
		// init
		StringBuilder builder = new StringBuilder();
		idx = 0;

		// prepare
		List<String> cellValues = new LinkedList<String>();
		char current = peek();

		do {
			builder.setLength(0);
			current = peek();

			// ',' will be skip in method
			if (current == '"') {
				next();
				readEscapedValue(builder);
				cellValues.add(builder.toString());
			} else if (current == ','){
				// delimiter
				next();
				// ,,
				if (isEnd() || peek() == ','){
					cellValues.add("");
				}
			} else {
				readNonEscapedValue(builder);
				cellValues.add(builder.toString());
			}
		} while (!isEnd());

		return cellValues;
	}

	/**
	 * Read everything until , or EOL
	 * 
	 * @param builder
	 */
	private void readNonEscapedValue(StringBuilder builder) {
		if (isEnd()) {
			throw new CSVParseException("Unexpected End");
		}
 
		
		boolean foundComma;
		do {
			foundComma = false;
			
			char c = peek();
			if (c == ',') {
				foundComma = true;
			} else {
				builder.append(c);
				next();
			}
		} while (!isEnd() && !foundComma);
	}

	/**
	 * Read everything to " before , or to " before EOL
	 * 
	 * @param builder
	 */
	private void readEscapedValue(StringBuilder builder) {
		if (isEnd()) {
			throw new CSVParseException("Unexpected End");
		}

		boolean foundDoubleQuote;
		do {
			foundDoubleQuote = false;
			char c = peek();

			if (c == '"') {
				next();
				
				if (!isEnd() && peek() == '"'){
					// 2double quote
					builder.append('"');
					
					// assume it is not end
					next(); 
				} else {
					// assume it is followed by ',' or EOL
					if (!isEnd() && peek() != ','){
						throw new CSVParseException("Expected , at columns: " + idx);
					}
					foundDoubleQuote = true;
				}
			} else {
				builder.append(c);
				next();
			}
		} while (!isEnd() && !foundDoubleQuote);
	}
}