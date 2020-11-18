/**
 * Computer Department at Cinvestav CDMX.
 * Copyright (c) 2020
 * All rights reserved.
 */

package mx.cinvestav.cs.applacovid.utils;


import java.util.Base64;


public class ValidationUtils
{
	public ValidationUtils(int keyLengthBytes)
	{
		this.KEY_LENGTH_BYTES = keyLengthBytes;
	}

	public boolean isValidBase64Key(String value)
	{
		try
		{
			byte[] key = Base64.getDecoder().decode(value);

			if (key.length != KEY_LENGTH_BYTES)
			{
				return false;
			}

			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	private final int KEY_LENGTH_BYTES;
}
