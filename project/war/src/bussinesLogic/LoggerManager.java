package bussinesLogic;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerManager {
	private final String WAR_LOGGER_FILE = "logFiles\\WarLogger.txt";
	private static Logger logger = Logger.getLogger("WarLog");
	
	public LoggerManager(){
		FileHandler handler;
		try {
			handler = new FileHandler(WAR_LOGGER_FILE);
			handler.setFormatter(new WarFormatter());
			logger.addHandler(handler);
			logger.setUseParentHandlers(false);
			logger.log(Level.INFO, "Let the war begin:");
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
