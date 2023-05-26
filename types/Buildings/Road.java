package types.Buildings;
import types.Building;

/**

Road class representing a road in the city.
Extends the Building class.
*/
public class Road extends Building {
    private boolean destructible = true;
    /**
     * Constructs a new Road object with the given coordinates.
     *
     * @param x The x-coordinate of the road.
     * @param y The y-coordinate of the road.
     */
    public Road(int x, int y) {
        super(x,y,1,100,"road.png");
    }
    /**
     * Checks if the road is destructible.
     *
     * @return true if the road is destructible, false otherwise.
     */
    public boolean getDestructible(){
        return destructible;
    }
    /**
 * Sets the destructible property of the road.
 *
 * @param b The value to set for destructible property.
 */
    public void setDestructible(boolean b){
        destructible = b;
    }
}
