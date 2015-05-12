import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;


public class BlizzardDriver {
		
	public static void main(String[] args) throws MalformedURLException, IOException, JSONException
	{
			BlizzardTestCase driver = new BlizzardTestCase();
			
			System.out.print("Testing an invalid item... ");
			System.out.println(driver.testInvalidItem()); //Invoking TC #1 listed in Test Document
			
			System.out.print("Testing an invalid item set... ");
			System.out.println(driver.testInvalidItemSet()); //TC #2 
			
			System.out.print("Testing valid item has description field... ");
			System.out.println(driver.testItemHasDescription()); //TC #3
			
			System.out.print("Testing valid item set has children items... ");
			System.out.println(driver.testItemSetHasValidChild()); //TC #4
			
			System.out.print("Testing valid item set setBonuses description matches child item setBonuses description... ");
			System.out.println(driver.testItemSetHasMatchingSetBonusText()); //TC #5
			
			System.out.print("Testing valid item set setBonuses thresholds matches child item setBonuses thresholds... ");
			System.out.println(driver.testItemSetHasMatchingThresholds()); //TC #6
			
	}
}
