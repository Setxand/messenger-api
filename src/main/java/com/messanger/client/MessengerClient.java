package com.messanger.client;

import com.messanger.*;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public abstract class MessengerClient {

	private final RestTemplate restTemplate;
	private final String accessToken;
	private final String serverUrl;
	private final String webhook;
	private Map<String, String> urlMap = new HashMap<>();

	public MessengerClient(String accessToken, String serverUrl, String webhook, String urlMap) {
		this.accessToken = accessToken;
		this.serverUrl = serverUrl;
		this.webhook = webhook;
		this.urlMap = processMap(urlMap);
		restTemplate = new RestTemplateBuilder().rootUri("https://graph.facebook.com").build();
	}

	public abstract void errorMessage(Long recipient);

	public abstract void sendSimpleQuestion(Long recipient, String text, String payload, String splitter);

	public void sendSimpleMessage(String text, Long recipient) {
		Message message = new Message(text);
		Messaging messaging = new Messaging(message, new Recipient(recipient));
		sendMessage(messaging);
	}

	public UserData sendFacebookRequest(Long recipient) {
		String dataFields = "?fields=first_name,last_name,profile_pic,locale&access_token=";
		return restTemplate.getForEntity("/v2.6/" + recipient + dataFields + accessToken, UserData.class).getBody();
	}

	public void sendButtons(List<Button> buttons, String text, Long recipient, String templateName,
							String attachmentType) {
		Attachment attachment = new Attachment();
		attachment.setType(attachmentType);
		Payload payload = new Payload();
		payload.setTemplateType(templateName);
		payload.setButtons(buttons);
		payload.setText(text);
		attachment.setPayload(payload);
		Message message = new Message();
		message.setAttachment(attachment);
		sendMessage(new Messaging(message, new Recipient(recipient)));
	}

	public void sendQuickReplies(List<QuickReply> quickReplies, String text, Long recipient) {
		Message message = new Message();
		message.setQuickReplies(quickReplies);
		message.setText(text);
		sendMessage(new Messaging(message, new Recipient(recipient)));
	}

	public void sendMessage(MesBod messaging) {
		makeRequest("/v2.6/me/messages?access_token=", "SEND_MESSAGE", messaging);
	}

	protected void makeRequest(String url, String requestType, Object request) {
		try {
			switch (requestType) {

				case "SEND_MESSAGE":
					restTemplate.postForEntity(url + accessToken, request, Void.class);
					break;

				case "SET_WEBHOOK":
					restTemplate.postForEntity(url, request, Void.class);
					break;

				default:
					throw new RuntimeException("Invalid request type");
			}
		} catch (HttpClientErrorException ex) {
			throw new RuntimeException("Messenger request error : " + ex.getResponseBodyAsString());
		}
	}


	public void setWebHooks() {
		String[] webhooks = webhook.split(",");
		for (int i = 0; i < webhooks.length; i++) {
			String webhook = webhooks[i];
			List<String> strings = new ArrayList<>(urlMap.values());
			setWebHook(webhook, strings.get(i));
		}
	}

	protected void setWebHook(String webhook, String url) {
		Shell setMessengerWebHook = new Shell();
		setMessengerWebHook.setCallbackUrl(serverUrl + webhook);
		setMessengerWebHook.setVerToken("VerTok");
		setMessengerWebHook.setObject("page");
		setMessengerWebHook.setFields(new String[]{"messages", "messaging_postbacks"});


		makeRequest(url, "SET_WEBHOOK", setMessengerWebHook);
	}

	private static Map<String, String> processMap(String urlMap) {
		String[] urlss = urlMap.split(",");
		Map<String, String> map = new LinkedHashMap<>();
		for (int i = 0; i < urlss.length - 1; i += 2) map.put(urlss[i], urlss[i + 1]);
		return map;
	}

}