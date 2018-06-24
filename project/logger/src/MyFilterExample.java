import java.util.logging.Logger;

public class MyFilterExample {

	private static Logger theLogger = Logger.getLogger("myLogger");

	public static void main(String[] args) {

		theLogger.setFilter(new MyFilter());
		
		A a = new A();
		a.foo();
		a.goo();
	}
}
