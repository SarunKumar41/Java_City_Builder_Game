package types;
import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import settings.General;

/**
Zone class representing a zone in the city.
Implements the Serializable interface.
*/
public class Zone implements Serializable {
    private int size = 1;
    private int x,y,type;
    private int cost = 50;
    protected ImageIcon image = null;
    private boolean available = false; 
    protected int capacity = 100;
    protected int saturation = 0;
    private boolean changed = false;
    protected int satisfaction = 100;
    protected ArrayList<Citizen> people = new ArrayList<Citizen>();

    
/**
 * Constructs a new Zone object with the given coordinates and type.
 *
 * @param x The x-coordinate of the zone.
 * @param y The y-coordinate of the zone.
 * @param type The type of the zone.
 */
    public Zone(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }
    /**
     * Constructs a new Zone object with the given coordinates.
     *
     * @param x The x-coordinate of the zone.
     * @param y The y-coordinate of the zone.
     */
    public Zone(int x, int y){
        this.x = x;
        this.y = y;
    }
/**
 * Returns the available size in the zone.
 *
 * @return The available size in the zone.
 */
public int getAvailableSize(){
    return capacity - saturation;
}

/**
 * Returns the size of the zone.
 *
 * @return The size of the zone.
 */
public int getSize(){
    return size;
}

/**
 * Loads and returns the image of the zone with the given name.
 * Scales the image to match the cell size specified in the General settings.
 *
 * @param name The name of the image file.
 * @return The ImageIcon representing the zone image.
 */
public ImageIcon loadImage(String name) {
    Image img = new ImageIcon("./images/buildings/" + name).getImage();
    Image newimg = img.getScaledInstance(
        General.cellSize * size, General.cellSize * size, java.awt.Image.SCALE_SMOOTH
    );
    changed = true;
    return new ImageIcon(newimg);
}

/**
 * Returns the x-coordinate of the zone.
 *
 * @return The x-coordinate of the zone.
 */
public int getX(){
    return x;
}

/**
 * Returns the y-coordinate of the zone.
 *
 * @return The y-coordinate of the zone.
 */
public int getY(){
    return y;
}

/**
 * Returns the type of the zone.
 *
 * @return The type of the zone.
 */
public int getType(){
    return type;
}

/**
 * Returns the image of the zone.
 *
 * @return The image of the zone as an Image object.
 */
public Image getImage() {
    if(image == null) return null;
    else return image.getImage();
}

/**
 * Returns the cost of the zone.
 *
 * @return The cost of the zone.
 */
public int getCost(){
    return cost;
}

/**
 * Returns whether the zone is available or not.
 *
 * @return True if the zone is available, false otherwise.
 */
public boolean getAvailable(){
    return available;
}

/**
 * Sets the availability of the zone.
 *
 * @param b The availability status to set.
 */
public void setAvailable(boolean b){
    available = b;
}

/**
 * Returns the changed status of the zone.
 *
 * @return True if the zone has changed, false otherwise.
 */
public boolean getChanged(){
    return changed;
}

/**
 * Sets the changed status of the zone.
 *
 * @param b The changed status to set.
 */
public void setChanged(boolean b){
    this.changed = b;
}

/**
 * Returns the saturation level of the zone.
 *
 * @return The saturation level of the zone.
 */
public int getSaturation()
{
    return saturation;
}

/**
 * Returns the satisfaction level of the zone.
 *
 * @return The satisfaction level of the zone.
 */
public int getSatisfaction(){
    return satisfaction;
}

/**
 * Returns the list of citizens in the zone.
 *
 * @return The ArrayList of Citizen objects in the zone.
 */
public ArrayList<Citizen> getPeople(){
    return people;
}

/**
 * Adds a citizen to the zone.
 *
 * @param c The Citizen object to add.
 */
public void addCitizen(Citizen c){
    people.add(c);
}

/**
 * Removes a citizen from the zone.
 *
 * @param c The Citizen object to remove.
 */
public void leave(Citizen c){
    people.remove(c);
}
}