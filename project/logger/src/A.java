import java.util.logging.Level;
import java.util.logging.Logger;

public class A {
	private static Logger theLogger = Logger.getLogger("myLogger");
	
	public void foo() {
		theLogger.log(Level.INFO, "In A::foo");
	}
	
	public void goo() {
		theLogger.log(Level.INFO, "In A::goo");
	}
}
