package com.messanger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.messanger.Requests.RequestObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MessengerProfileApi implements MesBod, RequestObject {
	@JsonProperty("get_started")
	private GetStarted getStarted;
	@JsonProperty("persistent_menu")
	private List<PersistentMenu> persistentMenu;
	private Platform platform;

	public MessengerProfileApi(GetStarted getStarted, List<PersistentMenu> persistentMenu) {
		this.getStarted = getStarted;
		this.persistentMenu = persistentMenu;
	}

	public MessengerProfileApi() {
	}


}
