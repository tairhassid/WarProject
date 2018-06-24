import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class VaArgsAndRelectionExample {

	public static void main(String[] args) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		
		B b = new B();

		Method func = B.class.getMethod("goo", new Class[] { int.class, int.class });
		func.invoke(b, 8, 9);
		
		Object[] arr = {1,2};
		func.invoke(b, arr);
	}
}
