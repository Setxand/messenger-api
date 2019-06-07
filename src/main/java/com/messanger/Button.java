package com.messanger;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Button {

	private String type = "postback";
	private String url;
	private String title;
	private String payload;
	@JsonProperty("webview_height_ratio")
	private String heightRatio;
	@JsonProperty("messenger_extensions")
	private Boolean mesExtentions;

	public Button() {
	}

	public Button(String title, String payload) {
		this.payload = payload;
		this.title = title;
	}

	public Button(String type, String title, String payload) {
		this.type = type;
		this.title = title;
		this.payload = payload;
	}

	public Button(String title) {
		this.title = title;
	}

	public Button urlButton(String url){
		this.type = "web_url";
		this.url = url;
		return this;
	}

	public Button webView() {
		this.mesExtentions = true;
		this.heightRatio = "full";
		return this;
	}
}
