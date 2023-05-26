package settings;

import types.*;
import types.Buildings.*;
import types.Zones.Industrial;
import types.Zones.Residential;
import types.Zones.Service;

import java.io.*;
import javax.swing.Timer;
import java.util.ArrayList;

/**
 * The Storage class represents the storage of game data.
 * It contains information about zones, buildings, citizens, game settings, and various game-related operations.
 * It also provides methods to save and load game data.
 */
public class Storage implements Serializable {
    private ArrayList<Zone> zones;
    private Zone[][] zoneMatrix = new Zone[26][23];
    private Building[][] buildingMatrix = new Building[26][23];
    private ArrayList<Building> buildings;
    private ArrayList<Citizen> citizens;

    private final int tax = 1;
    private int time = 0;
    private int speed = 1;
    private int money = 10000;
    private int satisfaction = 100;
    private int loanPeriod = 0;
    private int population = 0;
    private boolean paused = false;
    private int countDays = 0;
    private Timer timer;

    private String playerName;
    private String cityName;

    private int lastAnnualFee = 0;

    private boolean checked[][] = new boolean[26][23];
    private boolean recursionFlag = true;

    /**
     * Constructs a Storage object with the given player name and city name.
     * Initializes the necessary lists and sets up the game timer.
     * Starts the game timer to update game data periodically.
     *
     * @param playerName the name of the player
     * @param cityName   the name of the city
     */
    public Storage(String playerName, String cityName) {
        this.zones = new ArrayList<Zone>();
        this.buildings = new ArrayList<Building>();
        this.citizens = new ArrayList<Citizen>();
        this.playerName = playerName;
        this.cityName = cityName;
        this.timer = new Timer(1000, e -> {
            time += speed;
            countDays+=speed;
            if(money <= 0){
                loanPeriod++;
            }
            else{
                loanPeriod = 0;
            }
            if(countDays >= 30){
                this.increasePopulation();
                countDays = 0;
            }
            if((time/30) % 12 == 11){
                int year = 1850 + (time / (12 * 30));
                if(lastAnnualFee != year){
                    this.payMaintenanceFee();
                    this.updateForestsAges();
                    this.money += this.population * this.tax;
                    lastAnnualFee = year;
                }
            }
            this.updateSatisfaction();
            this.updateWorkplaces();
        });
        this.timer.start();
    }

    /**
     * Returns the name of the player.
     *
     * @return the player name
     */
    public String getPlayerName() {
        return playerName;
    }
    /**
     * Returns the name of the city.
     *
     * @return the city name
     */
    public String getCityName() {
        return cityName;
    }
    /**
     * Sets the game time to the given value.
     *
     * @param t the game time to set
     */
    public void setDate(int t) {
        time = t;
    }
     /**
     * Returns the current game time.
     *
     * @return the current game time
     */
    public int getDate() {
        return time;
    }
    /**
     * Checks if the game is currently paused.
     *
     * @return true if the game is paused, false otherwise
     */
    public boolean isPaused()
    {
        return paused;
    }

    /**
     * Sets the pause status of the game.
     *
     * @param b true to pause the game, false to resume
     */
    public void setPause(boolean b)
    {
        paused = b;
    }
    /**
     * Returns the current game time as a formatted string.
     *
     * @return the formatted game time string
     */
    public String getStringDate() {
        int year = 1850 + (time / (12 * 30));
        int month = 1 + ((time / 30) % 12);
        int day = 1 + (time % 30);

        return (day < 10 ? "0" : "") + day + "/" + (month < 10 ? "0" : "") + month + "/" + year;
    }
    /**
     * Returns the game timer.
     *
     * @return the game timer
     */
    public Timer getTimer() {
        return timer;
    }
    /**
     * Returns the current amount of money.
     *
     * @return the current amount of money
     */
    public int getMoney() {
        return money;
    }
     /**
     * Returns the current game speed.
     *
     * @return the current game speed
     */
    public int getSpeed() {
        return speed;
    }
    /**
     * Sets the game speed to the given value.
     *
     * @param speed the game speed to set
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }
     /**
     * Returns the current satisfaction level.
     *
     * @return the current satisfaction level
     */
    public int getSatisfaction() {
        return satisfaction;
    }
    /**
     * Returns the list of zones in the city.
     *
     * @return the list of zones
     */
    public ArrayList<Zone> getZones() {
        return zones;
    }
    /**
     * Returns the list of buildings in the city.
     *
     * @return the list of buildings
     */
    public ArrayList<Building> getBuildings() {
        return buildings;
    }
    /**
     * Removes a building at the specified coordinates from the city.
     *
     * @param x the x-coordinate of the building
     * @param y the y-coordinate of the building
     * @return true if the building was successfully removed, false otherwise
     */
    public boolean removeBuilding(int x, int y) {
        if(!paused)
        {
            int index = getBuildingIndex(x, y, 1);
            if (index == -1) {
                return false;
            }

            Building building = buildings.get(index);
            if(building instanceof Road){
                Road road = (Road) building;
                if(!road.getDestructible()) return false;
            }

            money += (int)Math.floor(building.getCost()/2);
            buildings.remove(index);
            buildingMatrix[x][y] = null;

            return true;
        }
        return false; 
        
    }
    /**
     * Removes a zone at the specified coordinates from the city.
     *
     * @param x the x-coordinate of the zone
     * @param y the y-coordinate of the zone
     * @return true if the zone was successfully removed, false otherwise
     */
    public boolean removeZone(int x, int y){
        if(!paused)
        {
            int index = getZoneIndex(x, y);
            if(index != -1){
                Zone zone = zones.get(index);
                if(zone.getSaturation() != 0) return false;
                money += (int)Math.floor(zone.getCost()/2);
                zones.remove(index);
                zoneMatrix[x][y] = null;
                return true;
            }
            else{
                return false;
            }
        }
        return false;
    }

   
    /**
     * Adds a building to the city.
     *
     * @param building the building to add
     * @return true if the building was successfully added, false otherwise
     */
    public boolean addBuilding(Building building) {
        if(!paused)
        {
            if(!validCoordinates(building.getX(), building.getY(), building.getSize())){
                return false;
            }
            if (collide(building.getX(), building.getY(), building.getSize())) {
                return false;
            }
            if (!isGeneralZone(building.getX(), building.getY(), building.getSize())){
                return false;
            }
            buildings.add(building);
            buildingMatrix[building.getX()][building.getY()] = building;
            money -= building.getCost();

            int x = building.getX();
            int y = building.getY();
            if(building instanceof Road){
                this.validate(x,y);
            }
            else if(roadExists(x, y)){
                building.setAvailable(true);
            }
            return true;
        }
        return false;
        
    }
    /**
     * Checks if the given coordinates are valid for placing a building.
     *
     * @param x    the x-coordinate
     * @param y    the y-coordinate
     * @param size the size of the building
     * @return true if the coordinates are valid, false otherwise
     */
    public boolean validCoordinates(int x, int y, int size){
        int increment = size - 1;
        if(x-increment < 0 || x+increment > 25 || y-increment < 0 || y+increment > 22) return false;
        return true;
    }
    /**
     * Validates the neighboring cells of the specified coordinates for building placement.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public void validate(int x, int y){
        validateHelper(x, y, -1, 0);
        validateHelper(x, y, 1, 0);
        validateHelper(x, y, 0, -1);
        validateHelper(x, y, 0, 1);        
    }
     /**
     * Helper method to validate a specific neighboring cell for building placement.
     *
     * @param x          the x-coordinate of the original cell
     * @param y          the y-coordinate of the original cell
     * @param incrementX the x-coordinate increment to the neighboring cell
     * @param incrementY the y-coordinate increment to the neighboring cell
     */
    public void validateHelper(int x, int y, int incrementX, int incrementY){
        if(validCoordinates(x+incrementX, y+incrementY, 1)){
            int ind = getBuildingIndex(x+incrementX, y+incrementY, 1);
            if(ind != -1){
                Building building = buildings.get(ind);
                building.setAvailable(true);
            }
            ind = getZoneIndex(x+incrementX, y+incrementY);
            if(ind != -1){
                Zone zone = zones.get(ind);
                zone.setAvailable(true);
            }
        }
    }
    /**
     * Adds a zone to the city.
     *
     * @param zone the zone to add
     * @return true if the zone was successfully added, false otherwise
     */
    public boolean addZone(Zone zone){
        if(!paused)
        {
            if(!validCoordinates(zone.getX(), zone.getY(), 1)){
                return false;
            }
            if(collide(zone.getX(), zone.getY(), zone.getSize())){
                return false;
            }
    
            // check if place has a zone in it
            if(getZoneIndex(zone.getX(), zone.getY()) != -1) return false;
    
            money -= zone.getCost();
            zones.add(zone);
            zoneMatrix[zone.getX()][zone.getY()] = zone;
            if(roadExists(zone.getX(), zone.getY())){
                zone.setAvailable(true);
            }
            return true;
        }
        return false;
        // check if place has a building in it
        
    }
    /**
     * Checks if there is a road neighboring the specified coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if a road exists, false otherwise
     */
    public boolean roadExists(int x, int y){
        if(validCoordinates(x-1, y, 1) && buildingMatrix[x-1][y] instanceof Road){
            return true;
        }
        if(validCoordinates(x, y-1, 1) && buildingMatrix[x][y-1] instanceof Road){
            return true;
        }
        if(validCoordinates(x+1, y, 1) && buildingMatrix[x+1][y] instanceof Road){
            return true;
        }
        if(validCoordinates(x, y+1, 1) && buildingMatrix[x][y+1] instanceof Road){
            return true;
        }
        return false;
    }
    /**
     * Checks if there is a building at the specified coordinates with the given size.
     *
     * @param x    the x-coordinate
     * @param y    the y-coordinate
     * @param size the size of the building
     * @return true if there is a building, false otherwise
     */
    public boolean collide(int x, int y, int size) {
        return getBuildingIndex(x, y, size) != -1;
    }
    /**
     * Returns the index of the building at the specified coordinates with the given size.
     *
     * @param x    the x-coordinate
     * @param y    the y-coordinate
     * @param size the size of the building
     * @return the index of the building, or -1 if not found
     */
    public int getBuildingIndex(int x, int y, int size) {
        for (int i = 0; i < buildings.size(); i++) {
            Building building = buildings.get(i);
            int buildingX = building.getX();
            int buildingY = building.getY();
            int buildingSize = building.getSize();
            if (buildingX <= x + (size-1) && x <= buildingX + (buildingSize - 1) && buildingY <= y + (size-1) && y <= buildingY + (buildingSize-1)){
                return i;
            }
        }
        return -1;
    }
    /**
     * Returns the index of the zone at the specified coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the index of the zone, or -1 if not found
     */
    public int getZoneIndex(int x, int y) {
        for (int i = 0; i < zones.size(); i++) {
            Zone zone = zones.get(i);
            if (zone.getX() == x && zone.getY() == y) {
                return i;
            }
        }
        return -1;
    }
    /**
     * Saves the current game data to a file.
     */
    public void save() {
        String filename = "saves/" + playerName + "_" + cityName + ".sav";
        try {
            // create if not exists
            File file = new File(filename);
            file.getParentFile().mkdirs();
            file.createNewFile();

            // then save
            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Loads game data from a file and updates the current game state.
     *
     * @param filename the name of the file to load from
     */
    public void load(String filename) {
        // String filename = "saves/" + playerName + "_" + cityName + ".sav";
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Storage storage = (Storage) in.readObject();
            in.close();
            fileIn.close();
            this.zones = storage.zones;
            this.zoneMatrix = storage.zoneMatrix;
            this.buildingMatrix = storage.buildingMatrix;
            this.buildings = storage.buildings;
            this.citizens = storage.citizens;
            this.time = storage.time;
            this.speed = storage.speed;
            this.money = storage.money;
            this.satisfaction = storage.satisfaction;
            this.loanPeriod = storage.loanPeriod;
            this.population = storage.population;
            this.countDays = storage.countDays;
            this.playerName = storage.playerName;
            this.cityName = storage.cityName;
            this.lastAnnualFee = storage.lastAnnualFee;
            this.checked = storage.checked;
        } catch (Exception e) {
        }
    }

    public void disasterAction(int x, int y, int dim) {
        for (int i = x-dim; i <= x+dim; i++) {
            for (int j = y-dim; j <= y+dim; j++) {
                int index = getBuildingIndex(i, j, 1);
                if (index != -1 && !(buildings.get(index) instanceof Road)) {
                    buildings.remove(index);
                    buildingMatrix[i][j] = null;
                }
            }
        }
    }

    // public void explosion(int x, int y) {
    //     int dim = 1;
    //     for (int i = x-dim; i <= x+dim; i++) {
    //         for (int j = y-1; j <= y+1; j++) {
    //             int index = getBuildingIndex(i, j, 1);
    //             if (index != -1 && !(buildings.get(index) instanceof Road)) {
    //                 buildings.remove(index);
    //                 buildingMatrix[i][j] = null;
    //             }
    //         }
    //     }
    // }

    // public void earthquare(int x, int y) {
    //     int dim = 2;
    //     for (int i = x-dim; i <= x+dim; i++) {
    //         for (int j = y-dim; j <= y+dim; j++) {
    //             int index = getBuildingIndex(i, j, 1);
    //             if (index != -1 && !(buildings.get(index) instanceof Road)) {
    //                 buildings.remove(index);
    //                 buildingMatrix[i][j] = null;
    //             }
    //         }
    //     }
    // }

    public int disaster(int x, int y, int dType) {
        if (dType == 0) {
            // explosion: 1+x+1 (3x3)
            disasterAction(x, y, 1);
        } else if (dType == 1) {
            // earthquake: 0+x+0 (1x1)
            disasterAction(x, y, 0);
        }
        return dType;
    }

    public int disaster(int x, int y) {
        int dType = (int) Math.floor(Math.random() * 2);
        return disaster(x, y, dType);
    }

    public boolean isGeneralZone(int x, int y, int size){
        if(getZoneIndex(x, y) != -1) return false;
        if(size > 1){
            if(getZoneIndex(x+1, y) != -1) return false;
            if(getZoneIndex(x+1, y+1) != -1) return false;
            if(getZoneIndex(x, y+1) != -1) return false;
        }
        return true;
    }
    /**
     * Increases the population in the city based on the current satisfaction level.
     */
    public void increasePopulation(){
        int inc = (int) Math.floor(0.5*satisfaction);
        Residential resZone;
        for(int i = 0; i < zones.size(); i++){
            Zone zone = zones.get(i);
            if(zone instanceof Residential){
                resZone = (Residential) zone;
                int availableSize = resZone.getAvailableSize();
                if(availableSize>0 && resZone.getAvailable()){
                    if(availableSize >= inc){
                        resZone.increaseSaturation(inc);
                        this.population += inc;
                        for(int j = 0; j < inc; j++){
                            Citizen newCitizen = new Citizen(resZone);
                            this.citizens.add(newCitizen);
                            resZone.addCitizen(newCitizen);
                        }
                        return;
                    }
                    else{
                        inc -= availableSize;
                        resZone.increaseSaturation(availableSize);
                        for(int j = 0; j < availableSize; j++){
                            Citizen newCitizen = new Citizen(resZone);
                            this.citizens.add(newCitizen);
                            resZone.addCitizen(newCitizen);
                        }
                        this.population += availableSize;
                    }
                }
            }
        }
    }
    /**
     * Pays the maintenance fee for all buildings in the city.
     */
    public void payMaintenanceFee(){
        for(Building building: buildings){
            money-=building.getFee();
        }
    }
    /**
     * Checks if any zones in the city have been changed and updates the population accordingly.
     *
     * @return true if a zone has been changed, false otherwise
     */
    public boolean isChanged(){
        for(Zone zone: zones){
            if(zone.getChanged()){
                zone.setChanged(false);
                this.updatePopulation();
                return true;
            }
        }
        return false;
    }
    /**
     * Updates the total population in the city based on the current zone saturations.
     */
    public void updatePopulation(){
        int popCount = 0;
        for(Zone zone: zones){
            if(zone instanceof Residential){
                popCount += zone.getSaturation();
            }
        }
        this.population = popCount;
    }
    /**
     * Updates the ages of the forest buildings in the city.
     */
    public void updateForestsAges(){
        for(Building building: buildings){
            if(building instanceof Forest){
                Forest f = (Forest) building;
                f.incrementAge();
            }
        }
    }
    /**
     * Updates the satisfaction level of the zones in the city based on various factors.
     */
    public void updateSatisfaction(){
        for(Zone zone: zones){
            if(zone instanceof Residential){
                Residential resZone = (Residential) zone;
                boolean policeExists = this.checkPolice(resZone);
                boolean stadiumExists = this.checkStadium(resZone);
                Forest forest = this.checkForest(resZone);
                resZone.setPoliceBonus(policeExists);
                resZone.setStadiumBonus(stadiumExists);
                if(forest != null) resZone.setForestBonus(forest.getBonus());
                else resZone.setForestBonus(0);

                Industrial industry = this.checkIndustrial(resZone);
                if(industry!=null){
                    int distance = (int) (Math.pow(resZone.getX()-industry.getX(), 2) + Math.pow(resZone.getY()-industry.getY(), 2));
                    resZone.setDistance(distance);
                }
                else{
                    resZone.setDistance(0);
                }
                boolean industrialEffect = checkIndustrialEffect(resZone);
                resZone.setIndustrialEffect(industrialEffect);
                resZone.updateSatisfaction();
            }
        }
        for(Zone zone: zones){
            if(zone instanceof Service){
                Service serviceZone = (Service) zone;
                serviceZone.updateSatisfaction();
            }
            else if(zone instanceof Industrial){
                Industrial industrialZone = (Industrial) zone;
                industrialZone.updateSatisfaction();
            }
        }
        int sum = 0;
        for(Citizen citizen: citizens){
            sum += citizen.getSatisfaction();
        }
        if(citizens.size() != 0){
            int avg = sum / citizens.size();
            this.satisfaction = avg;
        }

        if(money < 0){
            int change = money / 10000;
            satisfaction -= satisfaction*change;
            satisfaction -= satisfaction*(loanPeriod/3000);
        }
        
    }
    /**
     * Checks if there is a police building within a certain radius of the residential zone.
     *
     * @param resZone the residential zone
     * @return true if a police building exists nearby, false otherwise
     */
    public boolean checkPolice(Residential resZone){
        for(int i = resZone.getX() - 3; i <= resZone.getX()+3; i++){
            for(int j = resZone.getY() - 3; j<=resZone.getY()+3;j++){
                if(validCoordinates(i, j, 1)){
                    if(buildingMatrix[i][j] instanceof Police){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * Checks if there is a stadium building within a certain radius of the residential zone.
     *
     * @param resZone the residential zone
     * @return true if a stadium building exists nearby, false otherwise
     */
    public boolean checkStadium(Residential resZone){
        for(int i = resZone.getX() - 5; i <= resZone.getX()+5; i++){
            for(int j = resZone.getY() - 5; j<=resZone.getY()+5;j++){
                if(validCoordinates(i, j, 1)){
                    if(buildingMatrix[i][j] instanceof Stadium){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * Checks if there is a forest building within a certain radius of the residential zone and returns the one with the highest bonus.
     *
     * @param resZone the residential zone
     * @return the forest building with the highest bonus, or null if none exist
     */
    public Forest checkForest(Residential resZone){
        int maxBonus = 0;
        Forest resultForest = null;
        for(int i = resZone.getX() - 3; i <= resZone.getX()+3; i++){
            for(int j = resZone.getY() - 3; j<=resZone.getY()+3;j++){
                if(validCoordinates(i, j, 1)){
                    if(buildingMatrix[i][j] instanceof Forest){
                        Forest forest = (Forest) buildingMatrix[i][j];
                        if(forest.getBonus() > maxBonus){
                            resultForest = (Forest) buildingMatrix[i][j];
                            maxBonus = resultForest.getBonus();
                        }
                    }
                }
            }
        }
        return resultForest;
    }
    /**
     * Checks if there is an industrial zone within a certain radius of the residential zone and returns the one with the shortest distance.
     *
     * @param resZone the residential zone
     * @return the industrial zone with the shortest distance, or null if none exist
     */
    public Industrial checkIndustrial(Residential resZone){
        int minDistance = 10;
        int distance;
        Industrial industry = null;
        int x = resZone.getX();
        int y = resZone.getY();
        for(int i = resZone.getX() - 3; i <= resZone.getX()+3; i++){
            for(int j = resZone.getY() - 3; j<=resZone.getY()+3;j++){
                if(validCoordinates(i, j, 1)){
                    if(zoneMatrix[i][j] instanceof Industrial){
                        distance = (int) (Math.pow(x-i, 2) + Math.pow(y-j, 2));
                        if(distance < minDistance){
                            industry = (Industrial) zoneMatrix[i][j];
                        }
                    }
                }
            }
        }
        return industry;
    }
    /**
     * Checks if the industrial zone has an effect on the residential zone based on its proximity and the presence of forests.
     *
     * @param res the residential zone
     * @return true if the industrial zone has an effect, false otherwise
     */
    public boolean checkIndustrialEffect(Residential res){
        int x = res.getX();
        int y = res.getY();
        for(int i = x; i <= x+3 && i<=25; i++){
            if(buildingMatrix[i][y] instanceof Forest) return false;
            if(zoneMatrix[i][y] instanceof Industrial) return true;
        }
        for(int i = x; i >= x-3 && i>=0; i--){
            if(buildingMatrix[i][y] instanceof Forest) return false;
            if(zoneMatrix[i][y] instanceof Industrial) return true;
        }
        for(int i = y; i <= y+3 && i <= 22; i++){
            if(buildingMatrix[x][i] instanceof Forest) return false;
            if(zoneMatrix[x][i] instanceof Industrial) return true;
        }
        for(int i = y; i <= y-3 && i >= 0; i--){
            if(buildingMatrix[x][i] instanceof Forest) return false;
            if(zoneMatrix[x][i] instanceof Industrial) return true;
        }

        return false;
    }
    /**
     * Updates the availability of workplace for each residential zone in the city.
     */
    public void updateWorkplaces(){
        for(Zone zone: zones){
            if(zone instanceof Residential){
                Residential resZone = (Residential) zone;
                if(!resZone.workplaceExists()){
                    this.setFalse();
                    Zone workPlace = getWorkplace(resZone.getX(), resZone.getY());
                    if(workPlace!=null) resZone.setWorkplace(workPlace);
                }
            }
        }
    }
    /**
     * Recursive function to find a suitable workplace for a residential zone.
     *
     * @param x the x-coordinate of the residential zone
     * @param y the y-coordinate of the residential zone
     * @return the workplace zone, or null if none is found
     */
    public Zone getWorkplace(int x, int y){
        if(!recursionFlag) return null;

        checked[x][y] = true;
        Zone workPlace = null;
        int index;
        ArrayList<Integer> zoneIndexes = new ArrayList<>();
        ArrayList<Integer> buildingIndexes = new ArrayList<>();
        ArrayList<Building> pathToWork = new ArrayList<>();

        checkWorkplace(x, y, zoneIndexes, buildingIndexes, -1, 0);
        checkWorkplace(x, y, zoneIndexes, buildingIndexes, 1, 0);
        checkWorkplace(x, y, zoneIndexes, buildingIndexes, 0, -1);
        checkWorkplace(x, y, zoneIndexes, buildingIndexes, 0, 1);

        int cnt = 0;
        while(workPlace == null && cnt <= 3){
            index = zoneIndexes.get(cnt);
            int incx = 0, incy = 0;
            switch(cnt){
                case 0: incx = -1; incy = 0; break;
                case 1: incx = 1; incy = 0; break;
                case 2: incx = 0; incy = -1; break;
                case 3: incx = 0; incy = 1; break;
                default: break;
            }
            if(index != -1){
                if(!(zones.get(index) instanceof Residential)){
                    workPlace = zones.get(index);
                    if(workPlace.getAvailableSize() == 0) workPlace = null;
                }
            }
            else{
                index = buildingIndexes.get(cnt);
                if(index != -1 && buildings.get(index) instanceof Road){
                    Building building = buildings.get(index);
                    pathToWork.add(building);
                    workPlace = getWorkplace(x+incx, y+incy);
                    if(workPlace == null) pathToWork.remove(building);
                }
            }
            cnt++;
        }

        if(workPlace != null){
            for(Building building: pathToWork){
                Road road = (Road) building;
                road.setDestructible(false);
            }
            recursionFlag = false;
        } 
        return workPlace;
    }
    /**
     * Helper function to check if a neighboring coordinate is a valid workplace option.
     *
     * @param x               the x-coordinate of the residential zone
     * @param y               the y-coordinate of the residential zone
     * @param zoneIndexes     the list to store the zone indexes
     * @param buildingIndexes the list to store the building indexes
     * @param incX            the increment in the x-coordinate
     * @param incY            the increment in the y-coordinate
     */
    public void checkWorkplace(int x, int y, ArrayList<Integer> zoneIndexes, ArrayList<Integer> buildingIndexes, int incX, int incY){
        if(validCoordinates(x+incX, y+incY, 1) && !checked[x+incX][y+incY]) {
            zoneIndexes.add(getZoneIndex(x+incX, y+incY));
            buildingIndexes.add(getBuildingIndex(x+incX, y+incY, 1));
        }
        else{
            zoneIndexes.add(-1);
            buildingIndexes.add(-1);
        }
    }
    /**
     * Resets the checked array and recursion flag to their initial values.
     */
    public void setFalse(){
        checked = new boolean[26][23];
        recursionFlag = true;
    }
    /**
     * Adds additional money to the city's budget.
     *
     * @param additionalMoney the amount of money to add
     */
    public void addMoney(int additionalMoney){
        this.money+=additionalMoney;
    }
}
