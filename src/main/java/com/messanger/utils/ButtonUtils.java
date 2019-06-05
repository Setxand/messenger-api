package com.messanger.utils;

import com.messanger.Button;

public class ButtonUtils {

	public static Button getUrlButton(String title, String url) {
		Button button = new Button();
		button.setType("web_url");
		button.setTitle(title);
		button.setUrl(url);
		return button;
	}

}
