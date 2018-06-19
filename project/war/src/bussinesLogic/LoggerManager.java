package bussinesLogic;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerManager {
	private final String WAR_LOGGER_FILE = "logFiles\\WarLogger.txt";
	private static Logger logger = Logger.getLogger("WarLog");
	
	public LoggerManager(){ // To avoid .lck files, we need to iterate all handles and close them --> https://stackoverflow.com/questions/2723280/why-is-my-program-creating-empty-lck-files
		FileHandler handler;
		try {
			handler = new FileHandler(WAR_LOGGER_FILE);
			handler.setFormatter(new WarFormatter());
			logger.addHandler(handler);
			logger.setUseParentHandlers(false);
			logger.log(Level.INFO, "Let the war begin:\n");
		} catch (SecurityException | IOException e) {
		} 
		
	}
	
	public static void addHandler(FileHandler handler){
		logger.addHandler(handler);
	}
	
	public static Logger getLogger(){
		return logger;
	}

}
