import java.util.logging.Level;
import java.util.logging.Logger;

public class FirstLogExample {
	
	private static Logger theLogger = Logger.getLogger("myLogger");
	
	public static void main(String[] args) {
		
		theLogger.log(Level.INFO, "This is info message");
		theLogger.log(Level.WARNING, "This is a warning message");
		theLogger.log(Level.SEVERE, "This is a severe message");
		
		foo();
		
		theLogger.setLevel(Level.SEVERE);
		theLogger.log(Level.SEVERE, "Setting log level to SEVERE");
		theLogger.log(Level.INFO, "Invisible info message");
	}
	
	public static void foo() {
		theLogger.info("Info message from the method 'foo'");
	}
} // main
