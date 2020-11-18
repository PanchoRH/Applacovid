package org.dpppt.backend.sdk.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;


public class Beacon
{
	@JsonIgnore
	private Integer id;

	@NotNull
	private Long latitude;

	@NotNull
	private Long longitude;

	@JsonIgnore
	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Long getLatitude()
	{
		return latitude;
	}

	public void setLatitude(Long latitude)
	{
		this.latitude = latitude;
	}

	public Long getLongitude()
	{
		return longitude;
	}

	public void setLongitude(Long longitude)
	{
		this.longitude = longitude;
	}
}
