package types.Buildings;
import types.Building;

/**

Police class representing a police building in the city.
Extends the Building class.
*/

public class Police extends Building {
    
    /**
 * Constructs a new Police object with the given coordinates.
 *
 * @param x The x-coordinate of the police building.
 * @param y The y-coordinate of the police building.
 */
    public Police(int x, int y) {
        super(x,y,1,100,"police.png");
        super.setRadius(3);
    }
}
