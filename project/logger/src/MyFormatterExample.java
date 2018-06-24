import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyFormatterExample {
	private static Logger theLogger = Logger.getLogger("myLogger");
	private static FileHandler theFileHandler;

	public static void main(String[] args) throws SecurityException, IOException {

		theFileHandler = new FileHandler("myLogger.xml", true);
		theLogger.addHandler(theFileHandler);
		
		theFileHandler.setFormatter(new MyFormatter()); 
		
		theLogger.log(Level.INFO, "info with customized formatter");
		theLogger.log(Level.WARNING, "warning with customized formatter");
	}
}
