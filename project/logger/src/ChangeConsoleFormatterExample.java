import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChangeConsoleFormatterExample {

	private static Logger theLogger = Logger.getLogger("myLogger");
	private static ConsoleHandler theConsoleHandler;

	public static void main(String[] args) {

		theLogger.setUseParentHandlers(false);
		
		theConsoleHandler = new ConsoleHandler();
		theLogger.addHandler(theConsoleHandler);
		
		theConsoleHandler.setFormatter(new MyFormatter()); 
		
		theLogger.log(Level.INFO, "info with customized formatter");
		theLogger.log(Level.WARNING, "warning with customized formatter");
	}
}
