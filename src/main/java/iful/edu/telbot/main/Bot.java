package iful.edu.telbot.main;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Comparator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.w3c.dom.Document;

import com.vdurmont.emoji.EmojiParser;

import iful.edu.telbot.classes.CurrentWeatherData;
import iful.edu.telbot.classes.Location;

public class Bot extends TelegramLongPollingBot {

	private final static String BOT_NAME = "GogolBot";
	private final static String BOT_TOKEN = "558220375:AAGMFC8uBqDH-UzDoAGv1RG_1fPqlDWXXOo";
	private static String MAIN_CHAT;
	private static String ADMIN_CHAT;
	private String lastPicture;
	private int f_height;
	private int f_width;
	private CurrentWeatherData weatherData = new CurrentWeatherData();

	@Override
	public void onUpdateReceived(Update update) {
		Message message = update.getMessage();
		if (message.hasText() && message.getText().equals("/start")) {
			checkAndSetChat(message);
		} else if (message.hasText() && message.getText().equals("/weather") && message.getChatId().toString().equals(MAIN_CHAT)) {
			try {
				getCurrentWeather(new Location("Ivano-Frankivsk", "UA"));
				String text = EmojiParser
						.parseToUnicode(":thermometer: Temperature: " + weatherData.getTemperature().toString() + "\n:" + "cloud: Sky: " + weatherData.getClouds().getValue() + "\n" + ":dash: Wind: " + weatherData.getWind().toString());
				sendMsg(message.getChatId().toString(), text);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} /*
			 * else if (message.hasText() && message.getText().equals("/skip") &&
			 * message.getChatId().toString().equals(MAIN_CHAT)) { setButtons(message); }
			 */
	}

	private CurrentWeatherData getCurrentWeather(Location location) throws Exception {
		String city = location.getCity();
		String country = location.getCountry();

		try {
			URL url;
			String appID = "671d3c8f8c6729fd1eeb91756513e170";
			url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + city + "," + country + "&units=" + location.getUnit() + "&mode=xml&APPID=" + appID);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.connect();
			InputStream in = con.getInputStream();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(in);
			weatherData.setAllWeatherDataByTagNames(doc);

		} catch (Exception e) {
			System.out.println("Weather parsing error. please try again or contact app developer.");
			e.printStackTrace();
		}
		return weatherData;
	}

	/*
	 * public synchronized void setButtons(Message message) { SendMessage smessage =
	 * new SendMessage() // Create a message object object
	 * .setChatId(message.getChatId()).setText("Here is your keyboard"); // Create
	 * ReplyKeyboardMarkup object ReplyKeyboardMarkup keyboardMarkup = new
	 * ReplyKeyboardMarkup(); // Create the keyboard (list of keyboard rows)
	 * List<KeyboardRow> keyboard = new ArrayList<>(); // Create a keyboard row
	 * KeyboardRow row = new KeyboardRow(); // Set each button, you can also use
	 * KeyboardButton objects if you need // something else than text
	 * row.add("Row 1 Button 1"); row.add("Row 1 Button 2"); // Add the first row to
	 * the keyboard keyboard.add(row);
	 * 
	 * // Set the keyboard to the markup keyboardMarkup.setKeyboard(keyboard); //
	 * Add it to the message smessage.setReplyMarkup(keyboardMarkup); String member
	 * = message.getFrom().getFirstName() + " " + message.getFrom().getLastName();
	 * SendMessage nsmessage = new SendMessage() // Create a message object object
	 * .setChatId(ADMIN_CHAT).setText(member); try { sendMessage(nsmessage); //
	 * Sending our message object to user } catch (TelegramApiException e) {
	 * e.printStackTrace(); } }
	 */
	private void handlePhoto(Message message) {
		List<PhotoSize> photos = message.getPhoto();
		f_height = photos.stream().sorted(Comparator.comparing(PhotoSize::getFileSize).reversed()).findFirst().orElse(null).getHeight();
		f_width = photos.stream().sorted(Comparator.comparing(PhotoSize::getFileSize).reversed()).findFirst().orElse(null).getWidth();
		lastPicture = photos.stream().sorted(Comparator.comparing(PhotoSize::getFileSize).reversed()).findFirst().orElse(null).getFileId();
	}

	private void sendMsg(String chatId, String text) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(chatId);
		sendMessage.setText(text);
		try {
			sendMessage(sendMessage);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	private void sendPht(Message message) {
		SendPhoto sendPhoto = new SendPhoto();
		sendPhoto.setChatId(message.getChatId().toString());
		sendPhoto.setPhoto(lastPicture);
		try {
			sendPhoto(sendPhoto);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	private void checkAndSetChat(Message message) {
		if (ADMIN_CHAT == null) {
			ADMIN_CHAT = message.getChatId().toString();
			sendMsg(String.valueOf(ADMIN_CHAT), "You setted admin chat");
		} else if (ADMIN_CHAT != null && message.getChatId().toString().equals(ADMIN_CHAT)) {
			sendMsg(String.valueOf(ADMIN_CHAT), "You have already setted admin chat");
		} else if (MAIN_CHAT == null) {
			MAIN_CHAT = message.getChatId().toString();
			sendMsg(String.valueOf(MAIN_CHAT), "Congratulations! You started using AntiDekan");
		} else {
			sendMsg(String.valueOf(MAIN_CHAT), "You have already setted chat");
		}
	}

	@Override
	public String getBotUsername() {
		return BOT_NAME;
	}

	@Override
	public String getBotToken() {
		return BOT_TOKEN;
	}

}
