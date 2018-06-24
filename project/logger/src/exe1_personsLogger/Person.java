package exe1_personsLogger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Person {
	private static Logger theLogger = Logger.getLogger("myLogger");
	
	private String name;
	
	public Person(String name) {
		this.name = name;
		
		FileHandler theHandler;
		try {
			theHandler = new FileHandler("person_" + name + ".txt");
			theHandler.setFilter(new StrFilter(name));
			theHandler.setFormatter(new MyFormatter());
			theLogger.addHandler(theHandler);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void goToTheSea() {
		theLogger.info(name + " is going to the sea");
	}
	
	public void goToMovie() {
		theLogger.info(name + " is going to the movie");
	}
	
	public void goToThePark() {
		theLogger.info(name + " is going to the park");
	}
}
