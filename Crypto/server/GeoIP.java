package com.zoubworld.Crypto.server;

public class GeoIP {
    public GeoIP(String ip, String cityName, String countryName, String postal, String LeastSpecificSubdivision) {
		this.ipAddress=ip;
		this.city=cityName;
		this.country=countryName;
		this.postal=postal;
		this.LeastSpecificSubdivision=LeastSpecificSubdivision;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public String getCity() {
		return city;
	}
	public String getCountry() {
		return country;
	}
	public String getPostal() {
		return postal;
	}
	public String getLeastSpecificSubdivision() {
		return LeastSpecificSubdivision;
	}
	private String ipAddress;
	private String city;
	private String country;
    private String postal;
    private String LeastSpecificSubdivision;
    // constructors, getters and setters... 
}