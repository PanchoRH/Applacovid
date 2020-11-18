/**
 * Computer Department at Cinvestav CDMX.
 * Copyright (c) 2020
 * All rights reserved.
 */

package mx.cinvestav.cs.applacovid.configuration;


import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


/**
 * This class set up the pattern for {@link LocalDateTime} data types.
 */
public class CustomDateTimeFormatter implements Formatter<LocalDateTime>
{
	public CustomDateTimeFormatter()
	{
		super();
	}

	@Override
	public LocalDateTime parse(String text, Locale locale) throws ParseException
	{
		final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MMMMM/yyyy HH:mm:ss");
		return (LocalDateTime) dtf.parse(text);
	}

	@Override
	public String print(LocalDateTime localDateTime, Locale locale)
	{
		final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd / MMMM / yyyy HH:mm");
		return dtf.format(localDateTime);
	}
}
