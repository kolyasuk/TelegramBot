package iful.edu.telbot.weatherobjects;

public class Wind {
	private Speed speed;

	public Wind(Speed speed) {
		super();
		setSpeed(speed);
	}

	public Wind() {

	}

	public Speed getSpeed() {
		return speed;
	}

	public void setSpeed(Speed speed) {
		this.speed = speed;
	}

	@Override
	public String toString() {
		return getSpeed().toString();
	}

}
