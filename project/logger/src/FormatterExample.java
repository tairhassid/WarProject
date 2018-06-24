import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.XMLFormatter;

public class FormatterExample {

	private static Logger theLogger = Logger.getLogger("myLogger");
	private static FileHandler theFileHandler;

	public static void main(String[] args) throws SecurityException, IOException {

		theFileHandler = new FileHandler("myLogger.xml", true);
		
		theLogger.addHandler(theFileHandler);
		
		theFileHandler.setFormatter(new SimpleFormatter());
		theLogger.log(Level.INFO, "message with SimpleFormatter");
		
		theFileHandler.setFormatter(new XMLFormatter());
		theLogger.log(Level.INFO, "message with XMLFormatter");
	}
}
