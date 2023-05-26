package types.Buildings;
import types.Building;
/**
Forest class representing a forest building in the city.
Extends the Building class.
*/
public class Forest extends Building {
    private int age = 0;
    private int bonus = 1;

    /**
     * Constructs a new Forest object with the given coordinates.
     *
     * @param x The x-coordinate of the forest building.
     * @param y The y-coordinate of the forest building.
     */
    public Forest(int x, int y) {
        super(x,y,1,100,"forest.png");
        super.setRadius(3);
    }
    /**
 * Gets the age of the forest.
 *
 * @return The age of the forest.
 */
    public int getAge(){
        return age;
    }
/**
 * Increments the age of the forest and updates the bonus value.
 */
    public void incrementAge(){
        age++;
        bonus = age;
        if(age > 10){
            super.setFee(0);
            bonus = 11;
        }
    }
/**
 * Gets the bonus value of the forest.
 *
 * @return The bonus value of the forest.
 */
    public int getBonus(){
        return bonus;
    }
}
