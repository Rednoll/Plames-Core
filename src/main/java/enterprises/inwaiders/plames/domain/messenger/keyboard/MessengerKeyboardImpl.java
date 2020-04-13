package enterprises.inwaiders.plames.domain.messenger.keyboard;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import enterprises.inwaiders.plames.api.messenger.keyboard.KeyboardButton;
import enterprises.inwaiders.plames.api.messenger.keyboard.MessengerKeyboard;
import enterprises.inwaiders.plames.dao.EntityLink;
import enterprises.inwaiders.plames.dao.messenger.keyboard.KeyboardHlRepository;
import enterprises.inwaiders.plames.domain.user.impl.UserImpl;
import enterprises.inwaiders.plames.spring.SpringUtils;

public class MessengerKeyboardImpl implements MessengerKeyboard {

	private Long id = null;
	
	private List<List<KeyboardButton>> matrix = new ArrayList<>();
	
	private boolean onetime = false;
	
	public MessengerKeyboardImpl() {
		
	}
	
	public MessengerKeyboardImpl(Long id) {
		
		this.id = id;
	}
	
	@Override
	public KeyboardButton getButtonByMark(String mark) {
		
		for(List<KeyboardButton> row : matrix) {
			
			for(KeyboardButton button : row) {
				
				if(button.getMark().equals(mark)) {
					
					return button;
				}
			}
		}
		
		return null;
	}
	
	@Override
	public KeyboardButton getButtonByLabel(String label) {
		
		for(List<KeyboardButton> row : matrix) {
			
			for(KeyboardButton button : row) {
				
				if(button.getLabel().equals(label)) {
					
					return button;
				}
			}
		}
		
		return null;
	}
	
	@Override
	public void setButton(int row, int column, KeyboardButton button) {
		
		List<KeyboardButton> buttons = null;
		
		if(button.getMark() == null || button.getMark().isEmpty()) {
			
			button.setMark(row+"-"+column);
		}
		
		if(this.matrix.size() <= row) {
			
			buttons = new ArrayList<>();
			this.matrix.add(row, buttons);
		}
		else {
			
			buttons = this.matrix.get(row);
		}
		
		if(buttons.size() <= column) {
			
			buttons.add(column, button);
		}
		else {
			
			buttons.set(column, button);
		}
	}

	@Override
	public List<List<KeyboardButton>> getButtonsMatrix() {
		
		return this.matrix;
	}
	
	public void setOnetime(boolean onetime) {
		
		this.onetime = onetime;
	}
	
	public boolean isOnetime() {
		
		return this.onetime;
	}
	
	public static class HighLevelRepository extends KeyboardHlRepository<MessengerKeyboardImpl> {
		
		public static ObjectMapper mapper = new ObjectMapper();
		
		private File mainDir = new File("./data/modules/core/profiles/keyboards");
		
		public HighLevelRepository() {
			
			if(!mainDir.exists()) {
				
				mainDir.mkdirs();
			}
			
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
		}
		
		@Override
		public EntityLink getLink(MessengerKeyboardImpl kb) {
			
			return new EntityLink(SpringUtils.getEntityName(MessengerKeyboardImpl.class), kb.getId());
		}
		
		@Override
		public void save(MessengerKeyboardImpl keyboard) {
			
			//TODO
		}
		
		@Override
		public MessengerKeyboardImpl getById(Long id) {
			
			return load(id);
		}
		
		@Override
		public void save(MessengerKeyboard keyboard, long id) {
			
			File file = new File(mainDir, id+".json");
		
			if(file.exists()) {
				
				file.delete();
			}
			
			List<List<KeyboardButton>> matrix = keyboard.getButtonsMatrix();
			
			ObjectNode node = mapper.createObjectNode();
				node.put("onetime", keyboard.isOnetime());
		
				ArrayNode jsonRows = mapper.createArrayNode();
				
					for(List<KeyboardButton> row : matrix) {
						
						ArrayNode jsonColumns = mapper.createArrayNode();
						
						for(KeyboardButton column : row) {
							
							if(column instanceof MessengerButtonBase) {
								
								jsonColumns.add(((MessengerButtonBase) column).toJson());
							}
						}
						
						jsonRows.add(jsonColumns);
					}
				
				node.put("buttons", jsonRows);
				
			try {
				
				Files.write(file.toPath(), mapper.writeValueAsBytes(node));
			}
			catch(JsonProcessingException e) {
				
				e.printStackTrace();
			}
			catch(IOException e) {
				
				e.printStackTrace();
			}
		}

		@Override
		public MessengerKeyboardImpl load(long id) {
			
			File file = new File(mainDir, id+".json");
			
			if(!file.exists()) {
				
				return null;
			}
			
			try {
			
				ObjectNode node = (ObjectNode) mapper.readTree(file);
				
				MessengerKeyboardImpl keyboard = new MessengerKeyboardImpl(id);
					keyboard.setOnetime(node.get("onetime").asBoolean());
					
					ArrayNode jsonButtonsRows = (ArrayNode) node.get("buttons");
						
						int rowsIterator = 0;
						for(JsonNode rawJsonButtonsColumns : jsonButtonsRows) {
							
							ArrayNode jsonButtonsColumns = (ArrayNode) rawJsonButtonsColumns;
						
							int columnsIterator = 0;
							for(JsonNode rawJsonButton : jsonButtonsColumns) {
								
								ObjectNode jsonButton = (ObjectNode) rawJsonButton;
								
								keyboard.setButton(rowsIterator, columnsIterator, MessengerButtonBase.fromJson(jsonButton));
							
								columnsIterator++;
							}
							
							rowsIterator++;
						}
					
				return keyboard;
			}
			catch (JsonProcessingException e) {
				
				e.printStackTrace();
			}
			catch (IOException e) {
				
				e.printStackTrace();
			}
			
			return null;
		}
	}
	
	public Long getId() {
		
		return this.id;
	}
}
