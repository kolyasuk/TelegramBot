package iful.edu.telbot.weatherobjects;

public class Temperature {
	private String value;
	private String max;
	private String min;
	private String unit;

	public Temperature(String value, String min, String max, String unit) {
		super();
		setValue(value);
		setMin(min);
		setMax(max);
		setUnit(unit);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public String toString() {
		String sign;
		if (getUnit().equals("metric")) {
			sign = "°C";
		} else if (getUnit().equals("imperial")) {
			sign = "°F";
		} else
			sign = "°K";
		return "Current=" + getValue() + sign + " Minimal=" + getMin() + sign + " Maximal=" + getMax() + sign;
	}

}
