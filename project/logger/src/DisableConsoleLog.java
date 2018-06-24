import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DisableConsoleLog {

private static Logger theLogger = Logger.getLogger("myLogger");
	
	public static void main(String[] args) throws SecurityException, IOException {
		
		theLogger.addHandler(new FileHandler("myLogger.xml"));
		
		theLogger.setUseParentHandlers(false);
		
		theLogger.log(Level.INFO, "Writing info from the 'main'");
	}
}
