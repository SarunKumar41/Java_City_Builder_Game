package types.Zones;

import types.Citizen;
import types.Zone;

/**
Industrial zone class representing an industrial zone in the city.
Inherits from the Zone class.
*/
public class Industrial extends Zone {

    private int radius = 3;

/**
 * Constructs a new Industrial zone object with the given coordinates.
 *
 * @param x The x-coordinate of the zone.
 * @param y The y-coordinate of the zone.
 */
public Industrial(int x, int y){
    super(x, y, 2);
}

/**
 * Returns the radius of the industrial zone.
 *
 * @return The radius of the industrial zone.
 */
public int getRadius(){
    return radius;
}

/**
 * Adds a worker to the industrial zone.
 *
 * @param c The citizen to be added as a worker.
 * @return True if the worker was successfully added, false otherwise.
 */
public boolean addWorker(Citizen c){
    if(super.getAvailableSize() == 0){
        return false;
    }
    saturation++;
    if(saturation <= 50 && saturation > 0){
        image = super.loadImage("smallIndustrial.png");
    }
    else{
        image = super.loadImage("largeIndustrial.png");
    }
    people.add(c);
    this.updateSatisfaction();
    return true;
}

/**
 * Updates the satisfaction level of the industrial zone based on the satisfaction levels of the workers.
 */
public void updateSatisfaction(){
    int avg = 0;
    for(Citizen c: people){
        avg+=c.getSatisfaction();
    }
    if(people.size() != 0) {
        avg = avg/people.size();
        satisfaction = avg;
    }
}

}
