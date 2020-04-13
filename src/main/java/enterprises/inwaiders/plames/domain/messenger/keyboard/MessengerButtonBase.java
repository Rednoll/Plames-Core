package enterprises.inwaiders.plames.domain.messenger.keyboard;

import com.fasterxml.jackson.databind.node.ObjectNode;

import enterprises.inwaiders.plames.api.messenger.keyboard.KeyboardButton;
import enterprises.inwaiders.plames.api.messenger.profile.UserProfile;

public abstract class MessengerButtonBase implements KeyboardButton {
	
	private String label = null;
	private Priority priority = Priority.SECONDARY;
	private String mark = null;
	
	public void action(UserProfile profile) {
		
	}
	
	public ObjectNode toJson() {
		
		ObjectNode node = MessengerKeyboardImpl.HighLevelRepository.mapper.createObjectNode();
			node.put("type", "command");
			node.put("priority", priority.toString());
			node.put("label", label);
			node.put("mark", mark);
			
		return node;
	}
	
	public void loadFromJson(ObjectNode node) {
		
		this.label = node.get("label").asText();
		this.priority = Priority.valueOf(node.get("priority").asText());
		this.mark = node.get("mark").asText();
	}
	
	@Override
	public void setMark(String marker) {
		
		this.mark = marker;
	}
	
	@Override
	public String getMark() {
		
		return this.mark;
	}
	
	@Override
	public void setLabel(String label) {
		
		this.label = label;
	}

	@Override
	public String getLabel() {
		
		return this.label;
	}
	
	@Override
	public void setPriority(Priority priority) {
		
		this.priority = priority;
	}
	
	@Override
	public Priority getPriority() {
		
		return this.priority;
	}
	
	public static MessengerButtonBase fromJson(ObjectNode node) {
		
		String type = node.get("type").asText();
	
		MessengerButtonBase button = null;
		
		if(type.equals("command")) {
			
			button = new MessengerCommandButtonImpl();
		}
		
		if(type.equals("link")) {
		
			button = new MessengerLinkButtonImpl();
		}
		
		button.setMark(node.get("mark").asText());
		button.setPriority(Priority.valueOf(node.get("priority").asText()));
		button.setLabel(node.get("label").asText());
		
		if(button != null) {
			
			button.loadFromJson(node);
		}
		
		return button;
	}
}
