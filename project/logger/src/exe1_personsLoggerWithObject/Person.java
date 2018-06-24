package exe1_personsLoggerWithObject;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Person {
	private static Logger theLogger = Logger.getLogger("myLogger");

	private String name;

	public Person(String name) {
		this.name = name;

		FileHandler theHandler;
		try {
			theHandler = new FileHandler("person_" + name + ".txt");
			theHandler.setFilter(new ObjectFilter(this));
			theHandler.setFormatter(new MyFormatter());
			theLogger.addHandler(theHandler);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void goToTheSea() {
		theLogger.log(Level.INFO, name + " is going to the sea", this);
	}

	public void goToMovie() {
		theLogger.log(Level.INFO, name + " is going to the movie", this);
	}

	public void goToThePark() {
		theLogger.log(Level.INFO, name + " is going to the park", this);
	}
}
