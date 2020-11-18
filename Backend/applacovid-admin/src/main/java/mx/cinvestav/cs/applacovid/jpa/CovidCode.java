/**
 * Computer Department at Cinvestav CDMX.
 * Copyright (c) 2020
 * All rights reserved.
 */

package mx.cinvestav.cs.applacovid.jpa;


import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Entity(name = "CovidCode")
@Table(name = "CovidCode")
public class CovidCode
{
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

	public Date getCreatedAt()
	{
		return createdAt;
	}

	public void setCreatedAt(Date createdAt)
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

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic
	@Column(name = "pk_covid_code_id")
	private Integer id;

	@Basic
	@Column(name = "code", nullable = false, unique = true)
	@NotBlank(message = "Covid code is mandatory")
	private String code;

	@Basic
	@Column(name = "createdAt", nullable = false)
	@NotNull(message = "Created at is mandatory")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Basic
	@Column(name = "used", nullable = false)
	private boolean used;
}
