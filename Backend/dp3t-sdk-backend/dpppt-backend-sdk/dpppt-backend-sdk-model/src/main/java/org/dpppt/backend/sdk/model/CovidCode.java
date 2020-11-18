package org.dpppt.backend.sdk.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;


public class CovidCode
{
	@JsonIgnore
	private Integer id;

	@NotNull
	private String code;

	@NotNull
	private Long createdAt;

	@NotNull
	private boolean used;

	@JsonIgnore
	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public Long getCreatedAt()
	{
		return createdAt;
	}

	public void setCreatedAt(Long createdAt)
	{
		this.createdAt = createdAt;
	}

	public boolean isUsed()
	{
		return used;
	}

	public void setUsed(boolean used)
	{
		this.used = used;
	}
}
