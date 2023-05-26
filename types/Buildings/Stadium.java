package types.Buildings;
import types.Building;

/**

Stadium class representing a stadium in the city.
Extends the Building class.
*/
public class Stadium extends Building {
    /**
 * Constructs a new Stadium object with the given coordinates.
 *
 * @param x The x-coordinate of the stadium.
 * @param y The y-coordinate of the stadium.
 */
    public Stadium(int x, int y) {
        super(x,y,2,100,"stadium.png");
        super.setRadius(5);
    }
}
