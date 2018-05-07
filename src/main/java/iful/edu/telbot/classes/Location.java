package iful.edu.telbot.classes;

public class Location {
	private String city;
	private String country;
	private String unit = "metric";

	public Location(String city, String country) {
		setCity(city);
		setCountry(country);
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public String toString() {
		return "Location [City=" + city + ", Country=" + country + ", Unit=" + unit + "]";
	}

}
