import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class MyFilter implements Filter {

	@Override
	public boolean isLoggable(LogRecord rec) {
		if (rec.getSourceMethodName().equals("goo"))
			return false;
		else
			return true;
	}
}
