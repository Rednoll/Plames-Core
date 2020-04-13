package enterprises.inwaiders.plames.domain.messenger.keyboard;

import com.fasterxml.jackson.databind.node.ObjectNode;

import enterprises.inwaiders.plames.api.messenger.keyboard.KeyboardLinkButton;

public class MessengerLinkButtonImpl extends MessengerButtonBase implements KeyboardLinkButton {

	private String url = null;
	
	public ObjectNode toJson() {
		
		ObjectNode node = toJson();
			node.put("type", "link");
			node.put("url", url);
	
		return node;
	}
	
	public void loadFromJson(ObjectNode node) {
		super.loadFromJson(node);
		
		this.url = node.get("url").asText();
	}
	
	@Override
	public void setUrl(String url) {
		
		this.url = url;
	}

	@Override
	public String getUrl() {
		
		return this.url;
	}
}
