package bussinesLogic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class WarFormatter extends Formatter{
	
	private final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";

	@Override
	public String format(LogRecord record) {
		StringBuffer buf = new StringBuffer();
		
		buf.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
		buf.append("\n"+formatMessage(record)+"\n");
		
		
		return buf.toString();
	}

}
