package iful.edu.telbot.main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {

	private final static String BOT_NAME = "GogolBot";
	private final static String BOT_TOKEN = "558220375:AAGMFC8uBqDH-UzDoAGv1RG_1fPqlDWXXOo";
	private static String MAIN_CHAT;
	private static String ADMIN_CHAT;
	private String lastPicture;
	private int f_height;
	private int f_width;

	@Override
	public void onUpdateReceived(Update update) {
		Message message = update.getMessage();
		if (message.hasText() && message.getText().equals("/start")) {
			checkAndSetChat(message);
		} else if (message.hasText() && message.getText().equals("/skip") && message.getChatId().toString().equals(MAIN_CHAT)) {
			String member = message.getFrom().getFirstName() + " " + message.getFrom().getLastName();
			sendMsg(String.valueOf(ADMIN_CHAT), member);

		} else if (message.hasText() && message.getText().equals("/board") && message.getChatId().toString().equals(MAIN_CHAT)) {
			SendMessage smessage = new SendMessage() // Create a message object object
					.setChatId(message.getChatId().toString()).setText("Here is your keyboard");
			ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
			List<KeyboardRow> keyboard = new ArrayList<>();
			KeyboardRow row = new KeyboardRow();
			row.add("Good reason");
			row.add("Lazy ass");
			keyboard.add(row);
			keyboardMarkup.setKeyboard(keyboard);
			smessage.setReplyMarkup(keyboardMarkup);
			try {
				sendMessage(smessage);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}

		/*
		 * else if (message.hasText() && message.getText().equals("/pic")) { if
		 * (lastPicture != null) { sendPht(message); sendMsg(message, f_height + "\n" +
		 * f_width); } else { sendMsg(message, "No one picture were before"); }
		 * 
		 * } else if (message.hasPhoto()) { handlePhoto(message); sendMsg(message,
		 * f_height + "\n" + f_width); } else if (message.hasText() &&
		 * message.getText().equals("/Марік")) { sendMsg(message, "Не підр"); } else if
		 * (message.hasText() && message.getText().equals("/Коля")) { sendMsg(message,
		 * "Хороша людина"); } else { sendMsg(message, message.getText()); }
		 */
	}

	// private CurrentWeather getCurrentWeather(String city) throws Exception {
	// try {
	// JSONObject weatherObject = getWeatherObject("weather", city);
	// CurrentWeather weather = JsonUtil.toObject(weatherObject,
	// CurrentWeather.class);
	// if (weather == null) {
	// throw new Exception("Cannot parse weather");
	// }
	// return weather;
	// } catch (Exception e) {
	// logger.error("Cannot get weather data", e);
	// throw e;
	// }
	// }

	public synchronized void setButtons(SendMessage sendMessage) {
		// Создаем клавиуатуру
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		sendMessage.setReplyMarkup(replyKeyboardMarkup);
		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboard(false);

		// Создаем список строк клавиатуры
		List<KeyboardRow> keyboard = new ArrayList<>();

		// Первая строчка клавиатуры
		KeyboardRow keyboardFirstRow = new KeyboardRow();
		// Добавляем кнопки в первую строчку клавиатуры
		keyboardFirstRow.add(new KeyboardButton("Привет"));

		// Вторая строчка клавиатуры
		KeyboardRow keyboardSecondRow = new KeyboardRow();
		// Добавляем кнопки во вторую строчку клавиатуры
		keyboardSecondRow.add(new KeyboardButton("Помощь"));

		// Добавляем все строчки клавиатуры в список
		keyboard.add(keyboardFirstRow);
		keyboard.add(keyboardSecondRow);
		// и устанваливаем этот список нашей клавиатуре
		replyKeyboardMarkup.setKeyboard(keyboard);
	}

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
