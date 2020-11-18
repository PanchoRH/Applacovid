/**
 * Computer Department at Cinvestav CDMX.
 * Copyright (c) 2020
 * All rights reserved.
 */

package mx.cinvestav.cs.applacovid.jpa;


import mx.cinvestav.cs.applacovid.api.CovidTest;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Entity(name = "CovidTestImpl")
@Table(name = "CovidTest")
public class CovidTestImpl implements CovidTest
{
	public CovidTestImpl()
	{
	}


	@Override
	public Long getId()
	{
		return id;
	}


	@Override
	public void setId(Long id)
	{
		this.id = id;
	}


	@Override
	public String getKd()
	{
		return kd;
	}


	@Override
	public void setKd(String kd)
	{
		this.kd = kd;
	}


	@Override
	public String getUuid()
	{
		return uuid;
	}


	@Override
	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}


	@Override
	public Boolean isExposed()
	{
		return isExposed;
	}


	@Override
	public void setIsExposed(Boolean isExposed)
	{
		this.isExposed = isExposed;
	}


	@Override
	public Long getDateKey()
	{
		return dateKey;
	}


	@Override
	public void setDateKey(Long dateKey)
	{
		this.dateKey = dateKey;
	}


	@Override
	public Date getKeyDate()
	{
		return keyDate;
	}


	@Override
	public void setKeyDate(Date keyDate)
	{
		this.keyDate = keyDate;
	}


	@Override
	public String getUserName()
	{
		return userName;
	}


	@Override
	public void setUserName(String userName)
	{
		this.userName = userName;
	}


	@Override
	public String getIdTest()
	{
		return idTest;
	}


	@Override
	public void setIdTest(String idTest)
	{
		this.idTest = idTest;
	}


	@Override
	public LocalDateTime getRegisterDate()
	{
		return registerDate;
	}


	@Override
	public void setRegisterDate(LocalDateTime registerDate)
	{
		this.registerDate = registerDate;
	}


	@Override
	public LocalDateTime getLastUpdate()
	{
		return lastUpdate;
	}


	@Override
	public void setLastUpdate(LocalDateTime lastUpdate)
	{
		this.lastUpdate = lastUpdate;
	}


	public void setAttribute(String name, Object value)
	{
		if (name.equalsIgnoreCase(CovidTest.ID))
		{
			try
			{
				this.id = (Long) value;
			}
			catch (ClassCastException e)
			{
				this.id = Long.valueOf((Integer) value);
			}
		}
		else if (name.equalsIgnoreCase(CovidTest.KD))
		{
			this.kd = (String) value;
		}
		else if (name.equalsIgnoreCase(CovidTest.UUID))
		{
			this.uuid = (String) value;
		}
		else if (name.equalsIgnoreCase(CovidTest.ISEXPOSED))
		{
			this.isExposed = (Boolean) value;
		}
		else if (name.equalsIgnoreCase(CovidTest.DATEKEY))
		{
			try
			{
				this.dateKey = (Long) value;
			}
			catch (ClassCastException e)
			{
				this.dateKey = Long.valueOf((Integer) value);
			}
		}
		else if (name.equalsIgnoreCase(CovidTest.KEYDATE))
		{
			this.keyDate = (Date) value;
		}
		else if (name.equalsIgnoreCase(CovidTest.USERNAME))
		{
			this.userName = (String) value;
		}
		else if (name.equalsIgnoreCase(CovidTest.IDTEST))
		{
			this.idTest = (String) value;
		}
		else if (name.equalsIgnoreCase(CovidTest.REGISTERDATE))
		{
			this.registerDate = (LocalDateTime) value;
		}
		else if (name.equalsIgnoreCase(CovidTest.LASTUPDATE))
		{
			this.lastUpdate = (LocalDateTime) value;
		}
	}


	/**
	 * Unique identifier for the record in the database.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic
	@Column(name = "id")
	private Long id;

	/**
	 * Key day, is the seed of the DP3T algorithm. Retrieved by the QR screen in the
	 * mobile application.
	 */
	@Basic
	@Column(name = "key", nullable = false, unique = true)
	@NotBlank(message = "Key is mandatory")
	private String kd;

	/**
	 * UUID of the mobile application. Is an identifier to match the Admin Console
	 * functionality with Beacons solution.
	 */
	@Basic
	@Column(name = "uuid", nullable = false, unique = true)
	@NotBlank(message = "UUID is mandatory")
	private String uuid;

	/**
	 * Tells if a record was already reported as 'positive' or not to Applacovid backend.
	 */
	@Basic
	@Column(name = "isExposed", nullable = false)
	private Boolean isExposed;

	/**
	 * The user name of the person who get a COVID-19 test.
	 */
	@Basic
	@Column(name = "username", nullable = false)
	@NotBlank(message = "User name is mandatory")
	private String userName;

	/**
	 * The physic expedient of the person who get a COVID-19 test.
	 */
	@Basic
	@Column(name = "idtest")
	private String idTest;

	/**
	 * Timestamp associated to kd (seed) of the DP3T algorithm. In epoch milliseconds.
	 */
	@Basic
	@Column(name = "dateKey", nullable = false)
	@NotNull(message = "Date key is mandatory")
	private Long dateKey;

	/**
	 * Representation of date_key in YYYY-MM-DD hh:ss:mm.ffffff
	 * Using Date instead of LocalDateTime because of compatibility with dpppt-back-end.
	 * Do not change to LocalDateTime.
	 */
	@Basic
	@Column(name = "keyDate", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date keyDate;

	/**
	 * Timestamp when the record was reported 'positive' to Applacovid backend.
	 */
	@Basic
	@Column(name = "lastUpdate")
	// From JDK 1.8 not needed to specify this annotation
	//@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime lastUpdate;

	/**
	 * Timestamp when the record was registerd in the admin console database.
	 */
	@Basic
	@Column(name = "registerDate")
	private LocalDateTime registerDate;
}
