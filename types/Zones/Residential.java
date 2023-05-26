package types.Zones;

import types.*;

/**

Residential zone class representing a residential zone in the city.
Inherits from the Zone class.
*/
public class Residential extends Zone {
    private int distToIndustrial = 0;
    private boolean industrialEffect = false;
    private boolean policeBonus = false;
    private boolean stadiumBonus = false;
    private int forestBonus = 0;
    /**
     * Constructs a new Residential zone object with the given coordinates.
     *
     * @param x The x-coordinate of the zone.
     * @param y The y-coordinate of the zone.
     */
    public Residential(int x, int y){
        super(x, y, 0);
    }

    /**
     * Increases the saturation level of the residential zone.
     *
     * @param inc The amount by which to increase the saturation level.
     */
    public void increaseSaturation(int inc){
        saturation += inc;
        if(saturation <= 50 && saturation > 0){
            image = super.loadImage("oneHouse.png");
        }
        else{
            image = super.loadImage("twoHouses.png");
        }
    }
/**
 * Returns the distance to the nearest industrial zone.
 *
 * @return The distance to the nearest industrial zone.
 */
    public int getDistance(){
        return distToIndustrial;
    }
/**
 * Sets the distance to the nearest industrial zone.
 *
 * @param dist The distance to set.
 */
    public void setDistance(int dist){

        this.distToIndustrial = (int) Math.floor(Math.sqrt(dist));
    }
/**
 * Sets the industrial effect status of the residential zone.
 *
 * @param b The industrial effect status to set.
 */
    public void setIndustrialEffect(boolean b){
        this.industrialEffect = b;
    }

/**
 * Sets the police bonus status of the residential zone.
 *
 * @param b The police bonus status to set.
 */
    public void setPoliceBonus(boolean b){
        this.policeBonus = b;
    }
/**
 * Returns the police bonus status of the residential zone.
 *
 * @return The police bonus status of the residential zone.
 */
    public boolean getPoliceBonus(){
        return policeBonus;
    }
/**
 * Sets the stadium bonus status of the residential zone.
 *
 * @param b The stadium bonus status to set.
 */
    public void setStadiumBonus(boolean b){
        this.stadiumBonus = b;
    }
    /**
 * Returns the stadium bonus status of the residential zone.
 *
 * @return The stadium bonus status of the residential zone.
 */
    public boolean getStadiumBonus(){
        return stadiumBonus;
    }
/**
 * Sets the forest bonus of the residential zone.
 *
 * @param bonus The forest bonus to set.
 */
    public void setForestBonus(int bonus){
        this.forestBonus = bonus;
    }
    /**
 * Returns the forest bonus of the residential zone.
 *
 * @return The forest bonus of the residential zone.
 */
    public int getForestBonus(){
        return this.forestBonus;
    }
/**
 * Updates the satisfaction level of the residential zone based on various factors.
 */
    public void updateSatisfaction(){
        int change = 100;
        if(policeBonus) change += (int)Math.floor(10*(1+this.saturation/this.capacity));
        if(stadiumBonus) change += 10;
        if(industrialEffect) change -= 10;
        if(distToIndustrial>0) change -= 10*(3-distToIndustrial);
        if(forestBonus > 0) change+=forestBonus;
        satisfaction = change;
        if(satisfaction > 100) satisfaction = 100;
        for(Citizen c: people){
            c.setSatisfaction(satisfaction);
        }
    }
/**
 * Checks if all residents in the residential zone have a workplace.
 *
 * @return True if all residents have a workplace, false otherwise.
 */
    public boolean workplaceExists(){
        for(Citizen c: people){
            if(c.getWorkplace() == null){
                return false;
            }
        }
        return true;
    }
/**
 * Sets the workplace for residents in the residential zone.
 *
 * @param workPlace The zone to set as the workplace.
 */
    public void setWorkplace(Zone workPlace){
        Industrial industry;
        Service serviceZone;
        if(workPlace instanceof Industrial){
            industry = (Industrial) workPlace;
            for(Citizen c: people){
                if(c.getWorkplace() == null){
                    if(industry.addWorker(c)){
                        c.setWorkplace(industry);
                    }
                }
            }
        }
        else{
            serviceZone = (Service) workPlace;
            for(Citizen c: people){
                if(c.getWorkplace() == null){
                    if(serviceZone.addWorker(c)){
                        c.setWorkplace(workPlace);
                    }
                }
            }
        }
    }
}
