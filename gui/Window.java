package gui;
import java.awt.*;
import javax.swing.*;
import settings.General;

/**
 * The Window class represents a Java Swing window.
 * It extends the JFrame class and provides a basic window setup.
 */
public class Window extends JFrame {

    /**
     * Constructs a Window object.
     * Sets up the window by setting the title, size, dimensions, close operation, and resizable properties.
     */
    public Window() {
        super();
        setTitle(General.title);
        setSize(General.width, General.height);
        setPreferredSize(new Dimension(General.width, General.height));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }
}