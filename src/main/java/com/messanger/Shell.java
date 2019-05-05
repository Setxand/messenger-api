package com.messanger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.messanger.Requests.RequestObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.annotation.Annotation;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
@Setter
@NoArgsConstructor
public class Shell implements RequestObject {
	@JsonProperty("whitelisted_domains")
	private List<String> whiteListedDomains;
	private String object;
	@JsonProperty("callback_url")
	private String callbackUrl;
	private String[] fields;
	@JsonProperty("verify_token")
	private String verToken;
	@JsonProperty("include_values")
	private Boolean includeValues;
	@JsonProperty("access_token")
	private String accessToken;
	private Platform platform;

}
