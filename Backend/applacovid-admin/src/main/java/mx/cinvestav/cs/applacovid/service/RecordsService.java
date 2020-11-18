/**
 * Computer Department at Cinvestav CDMX.
 * Copyright (c) 2020
 * All rights reserved.
 */

package mx.cinvestav.cs.applacovid.service;


import mx.cinvestav.cs.applacovid.jpa.CovidTestImpl;

import java.util.List;


public interface RecordsService
{
	List<CovidTestImpl> getNotExposedRecords();

	List<CovidTestImpl> getExposedRecords();

	CovidTestImpl getRecord(String kd);

	void addRecord(CovidTestImpl covidTest);

	void updateRecord(CovidTestImpl covidTest);

	void deleteRecord(Long id);
}
