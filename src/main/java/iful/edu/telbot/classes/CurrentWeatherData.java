package iful.edu.telbot.classes;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import iful.edu.telbot.weatherobjects.Clouds;
import iful.edu.telbot.weatherobjects.Speed;
import iful.edu.telbot.weatherobjects.Temperature;
import iful.edu.telbot.weatherobjects.Wind;

public class CurrentWeatherData {

	private Clouds clouds;
	private Wind wind;
	private Speed speed;
	private Temperature temperature;

	private void setAttribute(String currentTagName, Element rootElement) {
		NodeList currentList = rootElement.getElementsByTagName(currentTagName);
		Node currentNode = currentList.item(0);
		Element currentElement = (Element) currentNode;
		switch (currentTagName) {
		case "temperature":
			temperature = new Temperature(currentElement.getAttribute("value"), currentElement.getAttribute("min"), currentElement.getAttribute("max"), currentElement.getAttribute("unit"));
			break;
		case "wind":
			wind = new Wind();
			break;
		case "speed":
			Speed speed = new Speed(currentElement.getAttribute("value"), currentElement.getAttribute("name"));
			wind.setSpeed(speed);
			break;
		case "clouds":
			clouds = new Clouds(currentElement.getAttribute("value"), currentElement.getAttribute("name"));
			break;
		}

	}

	public void setAllWeatherDataByTagNames(Document doc) {
		NodeList list = doc.getElementsByTagName("current");
		Node rootNode = list.item(0);
		Element rootElement = (Element) rootNode;

		setAttribute("temperature", rootElement);
		setAttribute("wind", rootElement);
		setAttribute("speed", rootElement);
		setAttribute("clouds", rootElement);
	}

	@Override
	public String toString() {
		return "WeatherData [Clouds=" + getClouds() + ", Wind=" + getWind() + ", Temperature=" + getTemperature() + "]";
	}

	public Clouds getClouds() {
		return clouds;
	}

	public Wind getWind() {
		return wind;
	}

	public Speed getSpeed() {
		return speed;
	}

	public Temperature getTemperature() {
		return temperature;
	}

}
