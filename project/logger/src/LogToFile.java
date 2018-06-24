import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogToFile {

	private static Logger theLogger = Logger.getLogger("myLogger");
	
	public static void main(String[] args) throws SecurityException, IOException {
		
		theLogger.addHandler(new FileHandler("myLogger.xml"));
		theLogger.log(Level.INFO, "Writing info from the 'main'");
		
		foo();
	}

	private static void foo() {
		theLogger.log(Level.WARNING, "Writing warning from 'foo'");
	}
}
