package exe1_personsLogger;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class MyFormatter extends Formatter {

	@Override
	public String format(LogRecord rec) {
		return rec.getMessage();
	}
}
