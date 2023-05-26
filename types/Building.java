package types;

import settings.General;

import java.awt.Image;
import java.io.Serializable;
import javax.swing.ImageIcon;

/**

Building class representing a building in the city.
Implements the Serializable interface.
*/

public class Building implements Serializable {
    private int size = 1;
    private int x,y,cost;
    private ImageIcon image;
    private int fee = 20;
    private boolean available = false;
    private int radius = 0;

    /**
 * Constructs a new Building object with the given coordinates, size, cost, and image filename.
 *
 * @param x        The x-coordinate of the building.
 * @param y        The y-coordinate of the building.
 * @param size     The size of the building.
 * @param cost     The cost of the building.
 * @param filename The filename of the building's image.
 */
public Building(int x, int y, int size, int cost, String filename) {
    this.x = x;
    this.y = y;
    this.size = size;
    this.cost = cost;
    this.image = loadImage(filename);
}

/**
 * Loads an image with the given filename and scales it to match the cell size.
 *
 * @param name The filename of the image to load.
 * @return The ImageIcon representing the loaded image.
 */
private ImageIcon loadImage(String name) {
    Image img = new ImageIcon("./images/buildings/" + name).getImage();
    Image newimg = img.getScaledInstance(
            General.cellSize * size, General.cellSize * size, java.awt.Image.SCALE_SMOOTH
    );
    return new ImageIcon(newimg);
}

/**
 * Returns the size of the building.
 *
 * @return The size of the building.
 */
public int getSize() {
    return size;
}

/**
 * Returns the x-coordinate of the building.
 *
 * @return The x-coordinate of the building.
 */
public int getX() {
    return x;
}

/**
 * Returns the y-coordinate of the building.
 *
 * @return The y-coordinate of the building.
 */
public int getY() {
    return y;
}

/**
 * Returns the image of the building.
 *
 * @return The image of the building.
 */
public Image getImage() {
    return image.getImage();
}

/**
 * Returns the maintenance fee of the building.
 *
 * @return The maintenance fee of the building.
 */
public int getFee() {
    return fee;
}

/**
 * Returns the cost of the building.
 *
 * @return The cost of the building.
 */
public int getCost() {
    return cost;
}

/**
 * Returns the availability status of the building.
 *
 * @return True if the building is available, false otherwise.
 */
public boolean getAvailable() {
    return available;
}

/**
 * Sets the availability status of the building.
 *
 * @param b The availability status to set.
 */
public void setAvailable(boolean b) {
    available = b;
}

/**
 * Returns the radius of the building.
 *
 * @return The radius of the building.
 */
public int getRadius() {
    return radius;
}

 public void setFee(int newFee){
        fee = newFee;
    }


/**
 * Sets the radius of the building.
 *
 * @param rad The radius to set.
 */
public void setRadius(int rad) {
    this.radius = rad;
}

}
