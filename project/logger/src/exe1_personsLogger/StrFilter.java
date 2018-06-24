package exe1_personsLogger;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class StrFilter implements Filter {
	private String str;
	
	public StrFilter(String str) {
		this.str = str;
	}
	
	@Override
	public boolean isLoggable(LogRecord log) {
		return log.getMessage().contains(str);
	}
}
