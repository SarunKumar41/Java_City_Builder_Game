package types;
import java.io.Serializable;

import types.Zones.*;

/**

Citizen class representing a citizen in the city.
Implements the Serializable interface.
*/
public class Citizen implements Serializable {
    
    private Residential residence;
    private int satisfaction;
    private Zone workplace = null;

    /**
 * Constructs a new Citizen object with the given residence.
 *
 * @param aResidence The residential zone where the citizen resides.
 */
    public Citizen(Residential aResidence){
        this.residence = aResidence;
    }
/**
 * Returns the residence of the citizen.
 *
 * @return The residential zone where the citizen resides.
 */
    public Residential getResidence(){
        return residence;
    }
/**
 * Returns the satisfaction level of the citizen.
 *
 * @return The satisfaction level of the citizen.
 */
    public int getSatisfaction(){
        return satisfaction;
    }
/**
 * Sets the satisfaction level of the citizen.
 * If the satisfaction level drops below 10, the citizen leaves their residence and workplace.
 *
 * @param newSatisfaction The new satisfaction level to set.
 */
    public void setSatisfaction(int newSatisfaction){
        this.satisfaction = newSatisfaction;
        if(satisfaction <= 10){
            residence.leave(this);
            workplace.leave(this);
        }
    }
/**
 * Returns the workplace of the citizen.
 *
 * @return The zone where the citizen works.
 */
    public Zone getWorkplace(){
        return workplace;
    }
/**
 * Sets the workplace of the citizen.
 *
 * @param newWorkplace The zone where the citizen works.
 */
    public void setWorkplace(Zone newWorkplace){
        this.workplace = newWorkplace;
    }
}
