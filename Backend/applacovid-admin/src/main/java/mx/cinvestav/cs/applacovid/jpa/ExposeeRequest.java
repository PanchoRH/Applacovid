/**
 * Computer Department at Cinvestav CDMX.
 * Copyright (c) 2020
 * All rights reserved.
 */

package mx.cinvestav.cs.applacovid.jpa;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class ExposeeRequest
{
	public ExposeeRequest()
	{
	}


	public String getKey()
	{
		return key;
	}


	public void setKey(String key)
	{
		this.key = key;
	}


	public ExposeeAuthData getAuthData()
	{
		return authData;
	}


	public void setAuthData(ExposeeAuthData authData)
	{
		this.authData = authData;
	}


	public long getKeyDate()
	{
		return keyDate;
	}


	public void setKeyDate(long keyDate)
	{
		this.keyDate = keyDate;
	}


	public Integer isFake()
	{
		return fake;
	}


	public void setIsFake(Integer fake)
	{
		this.fake = fake;
	}


	private Integer fake = 0;

	@NotNull
	@Size(min = 24, max = 44)
	private String key;

	@NotNull
	private long keyDate;

	private ExposeeAuthData authData;
}
