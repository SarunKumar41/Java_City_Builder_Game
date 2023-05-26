package types.Buildings;
import types.Building;

/**

Fire class representing a fire building in the city.
Extends the Building class.
*/
public class Fire extends Building {
    /**
 * Constructs a new Fire object with the given coordinates.
 *
 * @param x The x-coordinate of the fire building.
 * @param y The y-coordinate of the fire building.
 */
    public Fire(int x, int y) {
        super(x,y,1,100,"fire.png");
    }
}
