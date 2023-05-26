package tests;
import java.lang.annotation.Target;

import org.junit.*;

import settings.*;
import types.*;
import types.Buildings.*;
import types.Zones.*;
public class Tests {
    // Test case for creating a storage object and checking initial values
    @Test
    public void create() {
        // Set player and city names
        String playerName = "Player", cityName = "City";
        // Create a storage object
        Storage s = new Storage(playerName, cityName);
        // Assert statements to check initial values
        Assert.assertEquals(s.getPlayerName(), playerName);
        Assert.assertEquals(s.getCityName(), cityName);
        Assert.assertEquals(s.getDate(), 0);
        Assert.assertEquals(s.getMoney(), 10000);
        Assert.assertEquals(s.getSatisfaction(), 100);
        Assert.assertEquals(s.getZones().size(), 0);
        Assert.assertEquals(s.getBuildings().size(), 0);
    }

    // Test case for adding a building successfully
    @Test
    public void addBuildingSuccess() {
        // Set player and city names
        String playerName = "Player", cityName = "City";
        // Create a storage object
        Storage s = new Storage(playerName, cityName);
        // Create a building object
        Fire f = new Fire(1, 1);
        // Add the building to the storage object
        Boolean status = s.addBuilding(f);
        // Assert statements to check the status and building list
        Assert.assertTrue(status);
        Assert.assertEquals(s.getBuildings().size(), 1);
        Assert.assertEquals(s.getBuildings().get(0), f);
    }

    // Test case for failing to add a building due to collision
    @Test
    public void addBuildingFailure() {
        // Set player and city names
        String playerName = "Player", cityName = "City";
        // Create a storage object
        Storage s = new Storage(playerName, cityName);
        // Create buildings for testing
        Fire f1 = new Fire(1, 1);           // will be added
        Forest f2 = new Forest(1, 1);       // will NOT be added
        // Add the first building successfully
        Boolean status1 = s.addBuilding(f1);    // success (no collision)
        // Try to add the second building, which should fail due to collision
        Boolean status2 = s.addBuilding(f2);    // failure (collision w/ f1)
        
        // Assert statements to check the status and building list
        Assert.assertTrue(status1);
        Assert.assertFalse(status2);
        Assert.assertEquals(s.getBuildings().size(), 1);
        Assert.assertEquals(s.getBuildings().get(0), f1);
    }

    // Test case for removing a building successfully
    @Test
    public void removeBuildingSuccess() {
        // Set player and city names
        String playerName = "Player", cityName = "City";
        // Create a storage object
        Storage s = new Storage(playerName, cityName);
        // Add a building to the storage object
        s.addBuilding(new Forest(1, 1));
        // Remove the building
        Boolean status = s.removeBuilding(1, 1);
        // Assert statement to check the status
        Assert.assertTrue(status);
    }

    // Test case for failing to remove a building
    @Test
    public void removeBuildingFailure() {
        // Set player and city names
        String playerName = "Player", cityName = "City";
        // Create a storage object
        Storage s = new Storage(playerName, cityName);
        // Try to remove a building that doesn't exist
        Boolean status = s.removeBuilding(1, 1);
        // Assert statement to check the status
        Assert.assertFalse(status); // as there is no building at (1, 1)
        
        // Create a new storage object and set pause to true
        Storage s2 = new Storage(playerName, cityName);
        s.setPause(true);
        // Try to remove a building with pause enabled
        Boolean status2 = s.removeBuilding(1, 1);
        // Assert statement to check the status
        Assert.assertFalse(status);
    }

    // Test case for checking collision check
    @Test
    public void collisionCheck() {
        // Set player and city names
        String playerName = "Player", cityName = "City";
        // Create a storage object
        Storage s = new Storage(playerName, cityName);

        // Add a forest building at (1, 1)
        s.addBuilding(new Forest(1, 1));
        // Check if the building index is correct
        Assert.assertEquals(s.getBuildingIndex(1, 1, 1), 0);

        // Add a fire building at (2, 2)
        s.addBuilding(new Fire(2, 2));
        // Check if the building index is correct
        Assert.assertEquals(s.getBuildingIndex(2, 2, 1), 1);
    }

    // Test case for testing the time functionality
    @Test
    public void testTime() {
        // Set player and city names
        String playerName = "Player", cityName = "City";
        // Create a storage object
        Storage s = new Storage(playerName, cityName);

        // Set the date to 30 (1 month)
        s.setDate(30);
        // Assert statements to check the date and formatted date string
        Assert.assertEquals(s.getDate(), 30);
        Assert.assertEquals(s.getStringDate(), "01/02/1850");

        // Set the date to 60 (2 months)
        s.setDate(60);
        // Assert statements to check the date and formatted date string
        Assert.assertEquals(s.getDate(), 60);
        Assert.assertEquals(s.getStringDate(), "01/03/1850");

        // Set the date to 2 years (24 months)
        s.setDate(30 * 12 * 2);
        // Assert statements to check the date and formatted date string
        Assert.assertEquals(s.getDate(), 30 * 12 * 2);
        Assert.assertEquals(s.getStringDate(), "01/01/1852");

        // Set the date to 2 years and 11 months (35 months)
        s.setDate(30 * 12 * 2 + 30 * 11);
        // Assert statements to check the date and formatted date string
        Assert.assertEquals(s.getDate(), 30 * 12 * 2 + 30 * 11);
        Assert.assertEquals(s.getStringDate(), "01/12/1852");
    }

    // Test case for testing the Timer object
    @Test
    public void testTimerObj() {
        // Set player and city names
        String playerName = "Player", cityName = "City";
        // Create a storage object
        Storage s = new Storage(playerName, cityName);
        // Assert statement to check the delay of the Timer object
        Assert.assertEquals(s.getTimer().getDelay(), 1000);
    }

    // Test case for testing the speed functionality
    @Test
    public void testSpeed() {
        // Set player and city names
        String playerName = "Player", cityName = "City";
        // Create a storage object
        Storage s = new Storage(playerName, cityName);
        // Assert statement to check the default speed value
        Assert.assertEquals(s.getSpeed(), 1);

        // Set the speed to 2
        s.setSpeed(2);
        // Assert statement to check the updated speed value
        Assert.assertEquals(s.getSpeed(), 2);
    }

    // Test case for testing the speed functionality on the date
    @Test
    public void testSpeedOnDate() {
        // Set player and city names
        String playerName = "Player", cityName = "City";
        // Create a storage object
        Storage s = new Storage(playerName, cityName);

        // Set the time to 0 and then set the speed to 2
        s.setDate(0);
        s.setSpeed(2);

        // Wait for 1 second (extra 500ms for safety)
        try {
            Thread.sleep(1500); 
        } 
        catch (InterruptedException e) {}

        // Check if the time is 2
        Assert.assertEquals(s.getDate(), 2);
    }

    // Test case for testing the save functionality
    @Test
    public void testSave() {
        // Set player and city names
        String playerName = "Player", cityName = "City";
        // Create the first storage object
        Storage s1 = new Storage(playerName, cityName);
        // Add buildings to the storage object
        for (int i = 0; i < 10; i++) {
            s1.addBuilding(new Forest(i, i));
        }
        // Set speed and date
        s1.setSpeed(2);
        s1.setDate(1000);
        // Save the storage object
        s1.save();

        // Create the second storage object
        Storage s2 = new Storage("", "");
        // Load the saved file into the second storage object
        s2.load("saves/" + playerName + "_" + cityName + ".sav");
        // Assert statements to check the values in the second storage object
        Assert.assertEquals(s2.getPlayerName(), s1.getPlayerName());
        Assert.assertEquals(s2.getCityName(), s1.getCityName());
        Assert.assertEquals(s2.getMoney(), s1.getMoney());
        Assert.assertEquals(s2.getSpeed(), s1.getSpeed());
        Assert.assertEquals(s2.getSatisfaction(), s1.getSatisfaction());
        Assert.assertEquals(s2.getZones().size(), s1.getZones().size());
        Assert.assertEquals(s2.getBuildings().size(), s1.getBuildings().size());
        // Save the second storage object
        s2.save();

        // Create the third storage object
        Storage s3 = new Storage("", "");
        // Try to load an invalid file
        s3.load("invalid.file");
        // Assert statements to check the values in the third storage object
        Assert.assertEquals(s3.getPlayerName(), "");
        Assert.assertEquals(s3.getCityName(), "");
    }

    // Test case for testing the addZone functionality
    @Test
    public void testAddZone() {
        // Set player and city names
        String playerName = "Player", cityName = "City";
        // Create a storage object
        Storage s = new Storage(playerName, cityName);
        // Add a zone to the storage object
        s.addZone(new Residential(1, 1));
        // Assert statements to check the size and coordinates of the zone list
        Assert.assertEquals(s.getZones().size(), 1);
        Assert.assertEquals(s.getZones().get(0).getX(), 1);
        Assert.assertEquals(s.getZones().get(0).getY(), 1);
    }

    // Test case for failing to add a zone due to collisions
    @Test
    public void testAddZoneFailure() {
        // Set player and city names
        String playerName = "Player", cityName = "City";
        // Create a storage object
        Storage s = new Storage(playerName, cityName);
        // Add zones and buildings for testing
        s.addZone(new Residential(1, 1));   // will be added
        s.addZone(new Residential(1, 1));   // will not be added

        s.addBuilding(new Forest(3, 3));    // will be added
        s.addZone(new Residential(3, 3));   // will not be added
        s.addZone(new Residential(100, 100));
        // Assert statement to check the size of the zone list
        Assert.assertEquals(s.getZones().size(), 1);

        // Create a new storage object and set pause to true
        Storage s2 = new Storage(playerName, cityName);
        s2.setPause(true);
        // Try to add a zone with pause enabled
        s2.addZone(new Residential(1, 1));
    }

    // Test case for removing a zone
    @Test
    public void testRemoveZone() {
        // Set player and city names
        String playerName = "Player", cityName = "City";
        // Create a storage object
        Storage s = new Storage(playerName, cityName);
        // Add a zone to the storage object
        s.addZone(new Industrial(1, 1));
        // Remove the zone
        s.removeZone(1, 1);
        // Assert statement to check the size of the zone list
        Assert.assertEquals(s.getZones().size(), 0);
    }

    // Test case for failing to remove a zone
    @Test
    public void testRemoveZoneFailure() {
        // Set player and city names
        String playerName = "Player", cityName = "City";
        // Create a storage object
        Storage s = new Storage(playerName, cityName);
        // Try to remove a zone that doesn't exist
        s.removeZone(1, 1);
        // Assert statement to check the size of the zone list
        Assert.assertEquals(s.getZones().size(), 0);
        
        // Create a new storage object and set pause to true
        Storage s2 = new Storage(playerName, cityName);
        s2.setPause(true);
        // Try to remove a zone with pause enabled
        s2.removeZone(0, 0);
    }

    // Test case for testing the load period functionality
    @Test
    public void testLoadPeriod() {
        // Set player and city names
        String playerName = "Player", cityName = "City";
        // Create a storage object
        Storage s = new Storage(playerName, cityName);

        // Add and remove buildings for testing
        for (int i = 0; i < 20; i++) {
            s.addBuilding(new Forest(i, i));
            s.removeBuilding(i, i);
        }

        // Tests must be implemented
    }

    // Test case for testing the increasePopulation functionality
    // @Test
    // public void testIncreasePopulation() {
    //     String playerName = "Player", cityName = "City";
    //     Storage s = new Storage(playerName, cityName);
    // }

    // Test case for testing the validateZones functionality
    @Test
    public void testValidateZones() {
        // Set player and city names
        String playerName = "Player", cityName = "City";
        // Create a storage object
        Storage s = new Storage(playerName, cityName);

        // Add a road building for testing
        s.addBuilding(new Road(1,1));

        // Add residential zones
        s.addZone(new Residential(0,0));
        s.addZone(new Residential(1,0));
        s.addZone(new Residential(3,3));

        // Perform zone validation
        s.validate(1, 1);
        s.validate(0, 0);
        s.validate(1, 0);
        
        s.validate(10, 10);

        // Get the zones for checking availability
        Zone z1 = s.getZones().get(s.getZoneIndex(0, 0));
        Zone z2 = s.getZones().get(s.getZoneIndex(1, 0));
        Zone z3 = s.getZones().get(s.getZoneIndex(3, 3));

        // Assert statements to check the availability of zones
        Assert.assertEquals(z1.getAvailable(),true);
        Assert.assertEquals(z2.getAvailable(),true);
        Assert.assertEquals(z3.getAvailable(),false);
    }
   
    // Test case for testing the validateBuildings functionality
    @Test
    public void testValidateBuildings() {
        // Set player and city names
        String playerName = "Player", cityName = "City";
        // Create a storage object
        Storage s = new Storage(playerName, cityName);

        // Add a road building for testing
        s.addBuilding(new Road(1,1));

        // Add forest buildings
        s.addBuilding(new Forest(4, 4));
        s.addBuilding(new Forest(1, 2));

        // Perform building validation
        s.validate(1, 1);

        // Get the buildings for checking availability
        Building b1 = s.getBuildings().get(s.getBuildingIndex(4, 4, 1));
        Building b2 = s.getBuildings().get(s.getBuildingIndex(1, 2, 1));

        // Assert statements to check the availability of buildings
        Assert.assertEquals(b1.getAvailable(),false);
        Assert.assertEquals(b2.getAvailable(),true);
    }

    // Test case for testing the validate functionality
    @Test
    public void testValidate() {
        // Set player and city names
        String playerName = "Player", cityName = "City";
        // Create a storage object
        Storage s = new Storage(playerName, cityName);
        // Perform validation
        s.validate(0, 0);
    }

    // Test case for testing the increasePopulation functionality
    @Test
    public void testIncreasePopulation() {
        // Set player and city names
        String playerName = "Player", cityName = "City";
        // Create a storage object
        Storage s = new Storage(playerName, cityName);
        // Add residential zones for testing
        s.addZone(new Residential(1, 0));
        s.addZone(new Residential(5, 5));
        // Add a road building
        s.addBuilding(new Road(1,1));
        // Perform validation
        s.validate(1, 1);
        // Perform population increase
        s.increasePopulation();
    }

    // Test case for testing the disaster functionality
    @Test
    public void testDisaster() {
        // Set player and city names
        String playerName = "Player", cityName = "City";
        // Create a storage object
        Storage s = new Storage(playerName, cityName);
        // Add a forest building for testing
        s.addBuilding(new Forest(0, 0));
        // Get the building index
        s.getBuildingIndex(0, 0, 1);
        // Simulate a disaster at (0, 0)
        s.disaster(0, 0);
        // Simulate a disaster at (1, 0)
        s.disaster(1, 0);
    }

    // Test case for testing the payMaintenanceFee functionality
    @Test
    public void testPayMaintenanceFee() {
        // Set player and city names
        String playerName = "Player", cityName = "City";
        // Create a storage object
        Storage s = new Storage(playerName, cityName);
        // Add a forest building for testing
        s.addBuilding(new Forest(0, 0));
        // Pay the maintenance fee
        s.payMaintenanceFee();

        // Create a new storage object
        Storage s2 = new Storage(playerName, cityName);
        // Pay the maintenance fee with no buildings
        s2.payMaintenanceFee();
    }
    
    // Test case for testing the isChanged functionality
    @Test
    public void testIsChanged() {
        // Set player and city names
        String playerName = "Player", cityName = "City";
        // Create a storage object
        Storage s = new Storage(playerName, cityName);
        // Create a zone object and check isChanged
        Zone z = new Residential(0, 0);
        s.isChanged();
        // Load an image for the zone and check isChanged
        z.loadImage("");
        s.addZone(z);
        s.isChanged();
        
    }
    
    // Test case for testing the updateSatisfaction functionality
    @Test
    public void testUpdateSatisfaction() {
        // Set player and city names
        String playerName = "Player", cityName = "City";
        // Create a storage object
        Storage s = new Storage(playerName, cityName);
        // Create a zone and building for testing
        Zone z = new Industrial(0, 0);
        //Building b = new Forest(0, 1);
        // Update the satisfaction level
        s.updateSatisfaction();
    }

    // Test case for testing the checkIndustrialEffect functionality
    @Test
    public void testCheckIndustrialEffect() {
        // Set player and city names
        String playerName = "Player", cityName = "City";
        // Create a storage object
        Storage s = new Storage(playerName, cityName);
        // Add a residential zone for testing
        Residential res = new Residential(0, 0);
        s.addZone(res);

        // Check if the industrial effect is applied to the residential zone
        Assert.assertEquals(s.checkIndustrialEffect(res) ,false);
        
        // Add an industrial zone adjacent to the residential zone
        Industrial ind = new Industrial(1, 0);
        s.addZone(ind);

        // Check if the industrial effect is applied to the residential zone
        Assert.assertEquals(s.checkIndustrialEffect(res) ,true);
        
    }
    
    // Test case for testing the addMoney functionality
    @Test
    public void testAddMoney() {
        // Set player and city names
        String playerName = "Player", cityName = "City";
        // Create a storage object
        Storage s = new Storage(playerName, cityName);
        // Set additional money
        int additionalMoney = 500;
        
        // Add money to the storage object
        s.addMoney(additionalMoney);
        
        // Check if the money is correctly updated
        int expectedMoney = 10000 + additionalMoney;
        int actualMoney = s.getMoney();
        
        Assert.assertEquals( 10000 + additionalMoney, actualMoney);
    }
    
    // Test case for testing the getWorkplace functionality
    @Test
    public void testGetWorkplace() {
        // Set player and city names
        String playerName = "Player", cityName = "City";
        // Create a storage object
        Storage s = new Storage(playerName, cityName);
        
        // Set the false value and check getWorkplace
        s.setFalse();
        int x1 = 5;
        int y1 = 5;
        Zone result1 = s.getWorkplace(x1, y1);
        Assert.assertNull(result1);
    }
    // Test case for testing the isGeneralZone functionality
    @Test
    public void testIsGeneralZone() {
        // Set player and city names
        String playerName = "Player", cityName = "City";
        // Create a storage object
        Storage s = new Storage(playerName, cityName);
        // Set the false value and check IsGeneralZone
        int x1 = 50;
        int y1 = 50;
        int x2 = 3;
        int y2 = 4;
        // Create a Zone objects
        Zone z1 = new Zone(x1, y1);
        Residential z2 = new Residential(x2, y2);
        
        s.addZone(z1);
        s.addZone(z2);
        Assert.assertTrue(s.isGeneralZone(x1, y1, 1));
        Assert.assertFalse(s.isGeneralZone(x2, y2, 2));
        
    }

    // Test case for testing the updateForestsAges functionality
    @Test
    public void testUpdateForestsAges() {
        // Set player and city names
        String playerName = "Player", cityName = "City";
        // Create a storage object
        Storage s = new Storage(playerName, cityName);
        
        // Create Forest object
        Forest f = new Forest(15, 15);
        s.addBuilding(f);
        // Create Police object
        Police p = new Police(10, 10);

        // Updating the age
        s.updateForestsAges();
    }

}
