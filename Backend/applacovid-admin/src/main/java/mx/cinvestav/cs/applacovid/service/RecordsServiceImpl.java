/**
 * Computer Department at Cinvestav CDMX.
 * Copyright (c) 2020
 * All rights reserved.
 */

package mx.cinvestav.cs.applacovid.service;


import mx.cinvestav.cs.applacovid.jpa.CovidTestImpl;
import mx.cinvestav.cs.applacovid.repository.CovidTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RecordsServiceImpl implements RecordsService
{
	@Override
	public List<CovidTestImpl> getNotExposedRecords()
	{
		return repository.findAllByIsExposedIsFalse();
	}

	@Override
	public List<CovidTestImpl> getExposedRecords()
	{
		return repository.findAllByIsExposedIsTrue();
	}

	@Override
	public CovidTestImpl getRecord(String kd)
	{
		return repository.findByKd(kd).orElse(null);
	}

	@Override
	public void deleteRecord(Long id)
	{
		repository.deleteById(id);
	}


	@Override
	public void addRecord(CovidTestImpl covidTest)
	{
		repository.save(covidTest);
	}


	@Override
	public void updateRecord(CovidTestImpl covidTest)
	{
		repository.save(covidTest);
	}


	@Autowired
	private CovidTestRepository repository;
}
