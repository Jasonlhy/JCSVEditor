import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

// recursive descending + predictive(1) for nested ""
// know issue "sxxx""xxxx",
class CSVLineParser {
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
	public List<Object> parseLine() {
		// init
		StringBuilder builder = new StringBuilder();
		idx = 0;

		// prepare
		List<Object> cellValues = new LinkedList<Object>();
		char current = peek();

		do {
			builder.setLength(0);
			current = peek();

			// ',' will be skip in method
			if (current == '"') {
				next();
				readEscapedValue(builder, 1);
				cellValues.add(builder.toString());
			} else {
				readValue(builder);
				cellValues.add(builder.toString());
			}
		} while (!isEnd());

		return cellValues;
	}

	// read everything until , or EOL
	private void readValue(StringBuilder builder) {
		if (isEnd()) {
			System.out.println("FUCK!! end");
		}

		char c = 0;
		do {
			c = peek();
			if (c == ',') {
				next();
				break;
			}

			if (c == '"') {
				builder.append('"');
				next();
				readEscapedValue(builder, 1);
			} else {
				builder.append(c);
				next();
			}
		} while (!isEnd());
	}

	// read everything until " or EOL
	private void readEscapedValue(StringBuilder builder, int quouted) {
		if (isEnd()) {
			System.out.println("FUCK!! end");
		}

		char c = 0;
		do {

			c = peek();
			System.out.println("idx: " + idx + ", c: " + peek());

			if (c == '"') {
				quouted++;
				next();

				if (isEnd() || (peek() == ',')) {
					char peek1 = 0;
					if (!isEnd()) {
						peek1 = peek();
					}

					next();

					if ((quouted % 2 == 0)) {
						break;
					} else {
						builder.append('"');
						System.out.println("peek: " + peek());
						if (!isEnd() && peek1 == ',') {
							builder.append(',');
						}
					}
				} else {
					builder.append('"');
				}
			} else {
				builder.append(c);
				next();
			}
		} while (!isEnd());
	}
}

public class CSVParser {

	private CSVLineParser lineParser;
	public Object[][] results;

	public CSVParser() {
		this.lineParser = new CSVLineParser();
	}

	public CSVParser(File file) throws IOException {

		this.lineParser = new CSVLineParser();

		int numRow = 0;
		int numCol = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = br.readLine();
			lineParser.setLine(line);
			
			List<Object> cellValues = lineParser.parseLine();
			if (cellValues.size() > numCol){
				numCol = cellValues.size();
			}
			
			numRow++;
		}
	}

	public void showResult() {
//		for (int i = 0; i < results.length; i++) {
//			for (int j = 0; j < results[i].length; j++){
//				System.out.println(results[i][j] + "[EOL]");
//			}
//			System.out.println();
//		}
	}

	// " + 1 = && pair = even = break, pair > 2 = inner, or
	public static void main(String[] args) {
		// CSVLineParser line = new CSVLineParser("aaa,bbb");
		// CSVLineParser line = new CSVLineParser(" aaa , bbb ");
		// CSVLineParser line = new CSVLineParser("\"aaa \"ad\", , ada\",bbb");
		// CSVLineParser line = new CSVLineParser("\"aaa \"ad\" ada\", bbb");
		// CSVLineParser line = new CSVLineParser("\"123 \"456\",789\",bbb");
		CSVLineParser line = new CSVLineParser("111 \"234\"678 222,333");
		List<Object> objects = line.parseLine();
		for (int i = 0; i < objects.size(); i++) {
			System.out.print(i + ": ");
			System.out.println("[SOL]" + objects.get(i) + "[EOL]");
		}

//		File file = new File("C:\\Users\\jason\\Desktop\\a.txt");
//		try {
//			CSVParser parser = new CSVParser(file);
//			parser.showResult();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}
