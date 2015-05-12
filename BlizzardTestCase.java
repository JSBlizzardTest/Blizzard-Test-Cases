import org.json.*;

import java.io.*;
import java.net.*;
	/**
 * @author John Brandon Salaz
 *
 */
public class BlizzardTestCase {
	final String ITEM_SET_BASE_URL = "http://us.battle.net/api/wow/item/set/";
	final String ITEM_BASE_URL = "http://us.battle.net/api/wow/item/";
	final int itemRangeBase = 18800;
	final int itemRangeMax = 18900;
	final int itemSetRangeBase = 1060;
	final int itemSetRangeMax = 1160;
	
	public String testItemSetHasMatchingThresholds() throws JSONException{
		//This test ensures that for a given Item Set, an item in that set has matching bonus threshold values.
		for(int id=itemSetRangeBase; id < itemSetRangeMax; id++){
			String itemSetAPIResponse = "";
			String itemAPIResponse = "";
			
			try { 
				itemSetAPIResponse = getItemSetAPIResponse(id);
			} catch (FileNotFoundException e) {
				break; //This specific id doesn't exist. Continuing on.
			} catch (IOException e){
				return "FAIL: IOException thrown.";
			}
			
			JSONObject itemSetJson = new JSONObject(itemSetAPIResponse); //Gathering the setBonuses from the retrieved item set.
			JSONArray itemSetChildren = new JSONArray(itemSetJson.get("items").toString());
			JSONArray itemSetBonuses = new JSONArray(itemSetJson.get("setBonuses").toString());
			
			try { 
				itemAPIResponse = getItemAPIResponse(itemSetChildren.getInt(0)); 
			} catch (FileNotFoundException e) {
				return "FAIL: Listed child item does not exist. ItemSet ID: " + id + "; Item ID: " + itemSetChildren.getInt(0);
			} catch (IOException e){
				return "FAIL: IOException thrown.";
			}
			
			JSONObject itemJson = new JSONObject(itemAPIResponse); //From the first Item ID listed in the item set, we will get the set bonuses listed there.
			JSONObject childItemSet = new JSONObject(itemJson.get("itemSet").toString());
			JSONArray childSetBonuses = new JSONArray(childItemSet.get("setBonuses").toString());
			
			
			//Fail if there aren't the same number of set bonuses.
			if(itemSetBonuses.length() != childSetBonuses.length()){
				return "FAIL: Unequal number of setBonus descriptions between itemSet ID: " + id + "and Item ID: " + itemSetChildren.getInt(0);
			}
			
			for(int i=0; i < itemSetBonuses.length(); i++){
				JSONObject itemSetBonusDescription = itemSetBonuses.getJSONObject(i);
				JSONObject childBonusDescription = childSetBonuses.getJSONObject(i);
				
				//If the thresholds in the Item Set setBonuses doesn't match the thresholds in the item setBonuses then we fail.
				if(!itemSetBonusDescription.get("threshold").equals(childBonusDescription.get("threshold"))){ 
					return "FAIL: Descriptions do not match. ItemSet ID: " + id + "; Item ID: " + itemSetChildren.getInt(0);
				}
			}
		}
		
		return "PASS";
	}
	
	public String testItemSetHasMatchingSetBonusText() throws JSONException{
		//This test ensures that a given Item Set and an Item within the set have matching bonus descriptions.
		for(int id=itemSetRangeBase; id < itemSetRangeMax; id++){
			String itemSetAPIResponse = "";
			String itemAPIResponse = "";
			
			try { 
				itemSetAPIResponse = getItemSetAPIResponse(id);
			} catch (FileNotFoundException e) {
				break; //This specific id doesn't exist. Continuing on.
			} catch (IOException e){
				return "FAIL: IOException thrown.";
			}
			
			JSONObject itemSetJson = new JSONObject(itemSetAPIResponse); //Gathering the setBonuses from the retrieved item set.
			JSONArray itemSetChildren = new JSONArray(itemSetJson.get("items").toString());
			JSONArray itemSetBonuses = new JSONArray(itemSetJson.get("setBonuses").toString());
			
			try { 
				itemAPIResponse = getItemAPIResponse(itemSetChildren.getInt(0)); 
			} catch (FileNotFoundException e) {
				return "FAIL: Listed child item does not exist. ItemSet ID: " + id + "; Item ID: " + itemSetChildren.getInt(0);
			} catch (IOException e){
				return "FAIL: IOException thrown.";
			}
			
			JSONObject itemJson = new JSONObject(itemAPIResponse); //From the first Item ID listed in the item set, we will get the set bonuses listed there.
			JSONObject childItemSet = new JSONObject(itemJson.get("itemSet").toString());
			JSONArray childSetBonuses = new JSONArray(childItemSet.get("setBonuses").toString());
			
			
			//Fail if there aren't the same number of set bonuses.
			if(itemSetBonuses.length() != childSetBonuses.length()){
				return "FAIL: Unequal number of setBonus descriptions between itemSet ID: " + id + "and Item ID: " + itemSetChildren.getInt(0);
			}
			
			for(int i=0; i < itemSetBonuses.length(); i++){
				JSONObject itemSetBonusDescription = itemSetBonuses.getJSONObject(i);
				JSONObject childBonusDescription = childSetBonuses.getJSONObject(i);
				
				//If the setBonus descriptions in the item set do not match the setBonus descriptions in the item then fail.
				if(!itemSetBonusDescription.get("description").equals(childBonusDescription.get("description"))){ 
					return "FAIL: Descriptions do not match. ItemSet ID: " + id + "; Item ID: " + itemSetChildren.getInt(0);
				}
			}
		}
		
		return "PASS";
	}
	
	
	public String testItemSetHasValidChild() throws MalformedURLException, JSONException{
		//This test ensures that each item set has an items array that is longer than length zero.
		for(int id= itemSetRangeBase; id < itemSetRangeMax; id++){
			String itemSetAPIResponse = "";

			try { 
				itemSetAPIResponse = getItemSetAPIResponse(id);
			} catch (FileNotFoundException e) {
				break; //This specific id doesn't exist. Continuing on.
			} catch (IOException e) {
				return "FAIL: IOException thrown.";
			}
			
			JSONObject itemSetJson = new JSONObject(itemSetAPIResponse);
			JSONArray itemsList = new JSONArray(itemSetJson.get("items").toString()); //Gathering the items sub-array.
			
			if(itemsList.length() == 0){ //If the given item set has no children items then fail.
				return "FAIL: Item set found with no children items. ID: " + id;
			} 
		}
		return " PASS";
}
	
	public String testItemHasDescription() throws MalformedURLException, JSONException{
		//This test ensures that each item has a description field within the JSON text.
		for(int id=itemRangeBase; id < itemRangeMax; id++){
			String itemAPIResponse = "";
			
			try {
				itemAPIResponse = getItemAPIResponse(id); //Requesting JSON from API.
			} catch (FileNotFoundException e){
				break;
			} catch (IOException e) {
				return "FAIL: IOException thrown.";
			}
			
		     JSONObject itemJson = new JSONObject(itemAPIResponse);
		     
		     if(!itemJson.has("description")){ //Testing to ensure that the returned object has a description field.
		    	 System.out.println("FAIL: Description of item " + id + " is blank");
		    	 return "FAIL: Description of item " + id + " is blank.";
		     }
		}
		
		     return " PASS";
	}
	

	public String testInvalidItemSet() {
		//This test attempts to access an invalid item set page. When creating the buffer, this should fail because the http request will return an error code. 
		int badId = -1;
		
		URLConnection connection = null;
		
		
	 try{
		 	connection = new URL(ITEM_SET_BASE_URL + badId).openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		} catch (FileNotFoundException e){
			return "PASS"; //The page we requested was invalid and returned an HTTP Error. Passed.
		}
		catch (IOException e) {
			return "FAIL: Invalid item set threw an unexpected error. ";
		}
		return "FAIL: Invalid Item Set returned an actual object.";
	}
	
	public String testInvalidItem() {
		//This test attempts to access an invalid item page. When creating the buffer, this should fail because the http request will return an error code. 
		int badId = -1;
		
		URLConnection connection = null;
		
	 try{
		 	connection = new URL(ITEM_BASE_URL + badId).openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		} catch (FileNotFoundException e){
			return "PASS"; //The page we requested was invalid and returned an HTTP Error. Passed.
		}
		catch (IOException e) {
			return "FAIL: Invalid item test threw an unexpected error. ";
		}
	     
		 	return "FAIL: Invalid item test returned an actual object.";
	}
	
	public String getItemAPIResponse(int id) throws IOException{
		URLConnection connection = null;
		String itemAPIResponse = "";
		
		BufferedReader in = null;
		connection = new URL(ITEM_BASE_URL + id).openConnection();
		
		try { //If a specific ID is blank, we don't worry about it. 
		in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		} catch (FileNotFoundException e){
			throw e; //We want this exception to move up to the caller. 
		}
		
		 String line;
	     while ((line = in.readLine()) != null) {
	    	 itemAPIResponse += line;
	     }
	     in.close();
		
	     return itemAPIResponse;
	}
	
	public String getItemSetAPIResponse(int id) throws IOException{
		URLConnection connection = null;
		String itemSetAPIResponse = "";
		
		BufferedReader in = null;
		connection = new URL(ITEM_SET_BASE_URL + id).openConnection();
		
		try { 
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		} catch (FileNotFoundException e) {
			throw e; //We want this exception to move up to the caller. 
		}
		
		String line;
		while((line = in.readLine()) != null){
			itemSetAPIResponse += line;
		}
		in.close();
		
		return itemSetAPIResponse;
	}
		
		
}


