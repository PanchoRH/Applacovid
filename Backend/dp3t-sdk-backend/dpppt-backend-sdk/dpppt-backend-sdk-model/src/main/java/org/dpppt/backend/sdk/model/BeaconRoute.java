package org.dpppt.backend.sdk.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;


public class BeaconRoute
{
	@JsonIgnore
	private Integer id;

	@NotNull
	private String appUuid;

	@NotNull
	private String beaconUuid;

	@NotNull
	private Integer major;

	@NotNull
	private Integer minor;

	@NotNull
	private Long timestamp;

	@NotNull
	private Integer rssi;

	@JsonIgnore
	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getAppUuid()
	{
		return appUuid;
	}

	public void setAppUuid(String appUuid)
	{
		this.appUuid = appUuid;
	}

	public String getBeaconUuid()
	{
		return beaconUuid;
	}

	public void setBeaconUuid(String beaconUuid)
	{
		this.beaconUuid = beaconUuid;
	}

	public Integer getMajor()
	{
		return major;
	}

	public void setMajor(Integer major)
	{
		this.major = major;
	}

	public Integer getMinor()
	{
		return minor;
	}

	public void setMinor(Integer minor)
	{
		this.minor = minor;
	}

	public Long getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(Long timestamp)
	{
		this.timestamp = timestamp;
	}

	public Integer getRssi()
	{
		return rssi;
	}

	public void setRssi(Integer rssi)
	{
		this.rssi = rssi;
	}
}
