/**
 * Computer Department at Cinvestav CDMX.
 * Copyright (c) 2020
 * All rights reserved.
 */

package mx.cinvestav.cs.applacovid.api;


import java.time.LocalDateTime;
import java.util.Date;


public interface CovidTest
{
	Long getId();

	void setId(Long id);

	String getUuid();

	void setUuid(String uuid);

	String getKd();

	void setKd(String kd);

	Boolean isExposed();

	void setIsExposed(Boolean isExposed);

	Long getDateKey();

	void setDateKey(Long dateKey);

	Date getKeyDate();

	void setKeyDate(Date keyDate);

	String getUserName();

	void setUserName(String userName);

	String getIdTest();

	void setIdTest(String idTest);

	LocalDateTime getRegisterDate();

	void setRegisterDate(LocalDateTime registerDate);

	LocalDateTime getLastUpdate();

	void setLastUpdate(LocalDateTime lastUpdate);

	/**
	 * Record ID. Identifies the record in the database. It must be unique.
	 */
	public static final String ID = "id";

	/**
	 * Kd. It is the user Kd in one specific day.
	 */
	public static final String KD = "kd";

	/**
	 * User device ID. Identifies the user device. It must be unique.
	 */
	public static final String UUID = "uuid";

	/**
	 * It determines if the record was already set as exposed or not.
	 */
	public static final String ISEXPOSED = "isExposed";

	/**
	 * The date when the key (kd) is sent to the backend to be registered.
	 */
	public static final String DATEKEY = "dateKey";

	/**
	 * Representation of date_key in YYYY-MM-DD HH:SS:MM.mmmmmm.
	 */
	public static final String KEYDATE = "keyDate";

	/**
	 * The user name of the person to take the covid-19 test.
	 */
	public static final String USERNAME = "userName";

	/**
	 * The expedient ID of the person to take the covid-19 test.
	 */
	public static final String IDTEST = "idTest";

	/**
	 * The date when the record was stored in the database.
	 */
	public static final String REGISTERDATE = "registerDate";

	/**
	 * The date when the record was updated in the database, commonly when the record is
	 * reported as exposed.
	 */
	public static final String LASTUPDATE = "lastUpdate";
}
