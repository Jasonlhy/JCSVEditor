package jcsveditor.parser;

/***
 * All CSV Parse Error as Runtime error, including parser, File IO
 * 
 * @author jason
 *
 */
public class CSVParseException extends RuntimeException {
	public CSVParseException(String message){
		super(message);
	}
	
	public CSVParseException(String message, Throwable cause){
		super(message, cause);
	}
}
