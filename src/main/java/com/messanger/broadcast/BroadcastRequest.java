package com.messanger.broadcast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.messanger.Message;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
@Setter
@NoArgsConstructor
public class BroadcastRequest {
	@JsonProperty("broadcast_id")
	private Long broadcastId;
	private List<Message> messages;
	private List<Data> data;
	@JsonProperty("message_creative_id")
	private Long messageCreativeId;
	@JsonProperty("custom_label_id")
	private Long customLabelId;
	@JsonProperty("reach_estimation_id")
	private String reachEstimationId;

}
