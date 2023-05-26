package types.Zones;

import types.Citizen;
import types.Zone;
/**

Service zone class representing a service zone in the city.
Inherits from the Zone class.
*/

public class Service extends Zone {
    /**
    * Constructs a new Service zone object with the given coordinates.
    *
    * @param x The x-coordinate of the zone.
    * @param y The y-coordinate of the zone.
    */
    public Service(int x, int y){
        super(x, y, 1);
    }
    /**
     * Adds a worker to the service zone.
     *
     * @param c The citizen to add as a worker.
     * @return True if the worker was successfully added, false otherwise.
     */
    public boolean addWorker(Citizen c){
        if(super.getAvailableSize() == 0){
            return false;
        }
        saturation++;
        if(saturation <= 50 && saturation > 0){
            image = super.loadImage("smallService.png");
        }
        else{
            image = super.loadImage("largeService.png");
        }
        people.add(c);
        this.updateSatisfaction();
        return true;
    }
    /**
     * Updates the satisfaction level of the service zone based on the average satisfaction of its workers.
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
