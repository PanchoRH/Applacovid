/*
 * Copyright (c) 2020 Ubique Innovation AG <https://www.ubique.ch>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * SPDX-License-Identifier: MPL-2.0
 */

package org.dpppt.backend.sdk.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.dpppt.backend.sdk.model.BeaconRoute;
import org.dpppt.backend.sdk.model.CovidCode;
import org.dpppt.backend.sdk.model.Exposee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

public class JDBCDPPPTDataServiceImpl implements DPPPTDataService {

	private static final Logger logger = LoggerFactory.getLogger(JDBCDPPPTDataServiceImpl.class);
	private static final String PGSQL = "pgsql";
	private final String dbType;
	private final NamedParameterJdbcTemplate jt;

	public JDBCDPPPTDataServiceImpl(String dbType, DataSource dataSource) {
		this.dbType = dbType;
		this.jt = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	@Transactional(readOnly = false)
	public void upsertExposee(Exposee exposee, String appSource) {
		String sql = null;
		if (dbType.equals(PGSQL)) {
			sql = "insert into t_exposed (key, key_date, app_source) values (:key, :key_date, :app_source)"
					+ " on conflict on constraint key do nothing";
		} else {
			sql = "merge into t_exposed using (values(cast(:key as varchar(10000)), cast(:key_date as date), cast(:app_source as varchar(50))))"
					+ " as vals(key, key_date, app_source) on t_exposed.key = vals.key"
					+ " when not matched then insert (key, key_date, app_source) values (vals.key, vals.key_date, vals.app_source)";
		}
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("key", exposee.getKey());
		params.addValue("app_source", appSource);
		params.addValue("key_date", new Date(exposee.getKeyDate()));
		jt.update(sql, params);
	}
	@Override
	@Transactional(readOnly = false)
	public void upsertExposees(List<Exposee> exposees, String appSource) {
		String sql = null;
		if (dbType.equals(PGSQL)) {
			sql = "insert into t_exposed (key, key_date, app_source) values (:key, :key_date, :app_source)"
					+ " on conflict on constraint key do nothing";
		} else {
			sql = "merge into t_exposed using (values(cast(:key as varchar(10000)), cast(:key_date as date), cast(:app_source as varchar(50))))"
					+ " as vals(key, key_date, app_source) on t_exposed.key = vals.key"
					+ " when not matched then insert (key, key_date, app_source) values (vals.key, vals.key_date, vals.app_source)";
		}
		var parameterList = new ArrayList<MapSqlParameterSource>();
		for(var exposee : exposees) {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("key", exposee.getKey());
			params.addValue("app_source", appSource);
			params.addValue("key_date", new Date(exposee.getKeyDate()));
			parameterList.add(params);
		}
		jt.batchUpdate(sql, parameterList.toArray(new MapSqlParameterSource[0]));
	}

	@Override
	@Transactional(readOnly = true)
	public int getMaxExposedIdForBatchReleaseTime(long batchReleaseTime, long releaseBucketDuration) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("batchReleaseTime", Date.from(Instant.ofEpochMilli(batchReleaseTime)));
		params.addValue("startBatch", Date.from(Instant.ofEpochMilli(batchReleaseTime - releaseBucketDuration)));
		String sql = "select max(pk_exposed_id) from t_exposed where received_at >= :startBatch and received_at < :batchReleaseTime";
		Integer maxId = jt.queryForObject(sql, params, Integer.class);
		if (maxId == null) {
			return 0;
		} else {
			return maxId;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Exposee> getSortedExposedForBatchReleaseTime(long batchReleaseTime, long releaseBucketDuration, int retentionDays) {
		String sql = "select pk_exposed_id, key, key_date from t_exposed where received_at >= :startBatch and received_at < :batchReleaseTime order by pk_exposed_id desc";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("batchReleaseTime", Date.from(Instant.ofEpochMilli(batchReleaseTime)));
		params.addValue("startBatch", Date.from(Instant.ofEpochMilli(batchReleaseTime - releaseBucketDuration)));

		List<Exposee> exposeeList = jt.query(sql, params, new ExposeeRowMapper());

		sql = "select id as pk_exposed_id, key, key_date from covid_test " +
			"where is_exposed = true and key_date >= :allowedKeys ";
		params = new MapSqlParameterSource();
		params.addValue("allowedKeys", Date.from(Instant.ofEpochMilli(batchReleaseTime - (retentionDays * 86400000))));

		List<Exposee> betyList = jt.query(sql, params, new ExposeeRowMapper());

		for (Exposee exposee : betyList)
		{
			Exposee e = exposeeList.stream().filter(x -> x.getKey().equals(exposee.getKey())).findAny().orElse(null);

			if (e == null)
			{
				exposeeList.add(exposee);
			}
		}

		logger.debug("exposeeList..\n");
		for (Exposee e : exposeeList)
		{
			logger.debug(" - " + e.getId() + " - " + e.getKey() + " - " + e.getKeyDate());
		}

		return exposeeList;
	}

	@Override
	@Transactional(readOnly = false)
	public void cleanDB(Duration retentionPeriod) {
		OffsetDateTime retentionTime = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC).minus(retentionPeriod);
		logger.info("Cleanup DB entries before: " + retentionTime);
		MapSqlParameterSource params = new MapSqlParameterSource("retention_time", Date.from(retentionTime.toInstant()));
		String sqlExposed = "delete from t_exposed where received_at < :retention_time";
		jt.update(sqlExposed, params);
	}

	@Override
	@Transactional(readOnly = false)
	public void addBeacon(BeaconRoute route)
	{
		logger.debug("Adding beacon path: " + route.toString());

		String sql = "insert into beacon_route (app_uuid, beacon_uuid, major, minor, timestamp, rssi) " +
			"values (:appUuid, :beaconUuid, :major, :minor, :timestamp, :rssi)"
			+ " on conflict on constraint key do nothing";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("appUuid", route.getAppUuid());
		params.addValue("beaconUuid", route.getBeaconUuid());
		params.addValue("major", route.getMajor());
		params.addValue("minor", route.getMinor());
		params.addValue("timestamp", new Date(route.getTimestamp()));
		params.addValue("rssi", route.getRssi());
		jt.update(sql, params);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isValidCovidCode(String code, int codeValidDays)
	{
		boolean value = false;

		String sql = "select * from covid_code where code = :code and used = false and created_at >= :time";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("code", code);
		params.addValue("time",
			Date.from(Instant.ofEpochMilli(System.currentTimeMillis() - (codeValidDays * 86400000))));
		CovidCode covidCode = jt.queryForObject(sql, params, new CovidCodeMapper());

		if (covidCode != null)
		{
			sql = "update covid_code set used = :used where id = :id";

			params = new MapSqlParameterSource();
			params.addValue("used", true);
			params.addValue("id", covidCode.getId());

			value = true;
		}

		return value;
	}

	private static final class CovidCodeMapper implements RowMapper<CovidCode>
	{
		public CovidCode mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			if (!rs.first())
			{
				return null;
			}

			CovidCode code = new CovidCode();
			code.setId(rs.getInt("id"));
			code.setCode(rs.getString("code"));
			return code;
		}
	}
}
