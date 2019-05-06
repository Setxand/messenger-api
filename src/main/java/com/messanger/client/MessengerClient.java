package com.messanger.client;

import com.messanger.*;
import com.messanger.Requests.RequestObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class MessengerClient {

	private final RestTemplate restTemplate;
	private final Map<String, String> accessTokenMap;
	private final String serverUrl;
	private final String webhook;
	private final Map<String, String> urlMap;

	public MessengerClient(String accessTokenMap, String serverUrl, String webhook, String urlMap) {
		this.serverUrl = serverUrl;
		this.webhook = webhook;
		this.urlMap = processMap(urlMap);
		this.accessTokenMap = processMap(accessTokenMap);

		restTemplate = new RestTemplateBuilder().rootUri("https://graph.facebook.com").build();
	}

	public abstract void errorMessage(Messaging messaging);

	public abstract void sendSimpleQuestion(Long recipient, String text, String payload, String splitter);

	public void sendSimpleMessage(String text, Messaging messaging) {
		messaging.setRecipient(new Recipient(messaging.getSender().getId()));
		messaging.setMessage(new Message(text));
		sendRequest(messaging);
	}

	public UserData getFacebookUserInfo(Long recipient, Platform platform) {
		String dataFields = "?fields=first_name,last_name,profile_pic,locale&access_token=";
		return restTemplate.getForEntity("/v2.6/" + recipient + dataFields + accessTokenMap.get(platform.name()),
				UserData.class).getBody();
	}

	public void sendButtons(List<Button> buttons, String text, Messaging messaging, String templateName,
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
		messaging.setMessage(message);
		messaging.setRecipient(new Recipient(messaging.getSender().getId()));
		sendRequest(messaging);
	}

	public void sendQuickReplies(List<QuickReply> quickReplies, String text, Long recipient) {
		Message message = new Message();
		message.setQuickReplies(quickReplies);
		message.setText(text);
		sendRequest(new Messaging(message, new Recipient(recipient)));
	}

	public void sendRequest(RequestObject request) {
		makeRequest("/v2.6/me/messages?access_token=" + accessTokenMap.get(request.getPlatform().name()), request);
	}

	public void sendRequest(RequestObject request, String type) {
		Platform platform = request.getPlatform();
		request.setPlatform(null);
		makeRequest("/v2.6/me/" + type + "?access_token=" + accessTokenMap.get(platform.name()), request);
	}

	public void setWebHooks() {
		String[] webhooks = webhook.split(",");
		for (int i = 0; i < webhooks.length; i++) {
			String webhook = webhooks[i];
			List<String> strings = new ArrayList<>(urlMap.values());
			setWebHook(webhook, strings.get(i));
		}
	}

	protected void makeRequest(String url, Object request) {
		try {
			restTemplate.postForEntity(url, request, Void.class);

		} catch (HttpClientErrorException ex) {
			throw new RuntimeException("Messenger request error : " + ex.getResponseBodyAsString());
		}
	}

	protected void setWebHook(String webhook, String url) {
		Shell setMessengerWebHook = new Shell();
		setMessengerWebHook.setCallbackUrl(serverUrl + webhook);
		setMessengerWebHook.setVerToken("VerTok");
		setMessengerWebHook.setObject("page");
		setMessengerWebHook.setFields(new String[]{"messages", "messaging_postbacks"});


		makeRequest(url, setMessengerWebHook);
	}

	private static Map<String, String> processMap(String urlMap) {
		String[] urlss = urlMap.split(",");
		Map<String, String> map = new LinkedHashMap<>();
		for (int i = 0; i < urlss.length - 1; i += 2) map.put(urlss[i], urlss[i + 1]);
		return map;
	}

}//get Access-tok
//https://graph.facebook.com/oauth/access_token?client_id=asdasdasd&client_secret=fdsfdsf&grant_type=client_credentials