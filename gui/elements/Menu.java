package gui.elements;

import gui.Window;

import java.awt.*;
import javax.swing.*;

/**
 * The Menu class represents a Java Swing application for the game menu.
 * It extends the Window class and provides a graphical user interface for the menu.
 */

public class Menu extends Window {

    /**
     * Constructs a Menu object.
     * Sets up the menu interface by initializing panels, labels, text fields, and buttons.
     */
    public Menu() {
        super();
        this.setLayout(new GridBagLayout());
        int rows = 4, cols = 1, hgap = 0, vgap = 10;

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(rows, cols, hgap, vgap));

            JLabel title = new JLabel("Game menu", SwingConstants.CENTER);
            title.setFont(new Font("Serif", Font.PLAIN, 32));
            panel.add(title);

            JPanel input = new JPanel();
            input.setLayout(new GridLayout(2, 2, 0, 0));

                JLabel name_label = new JLabel("Player name: ");
                input.add(name_label);
                JTextField name = new JTextField();
                input.add(name);

                JLabel city_label = new JLabel("City name: ");
                input.add(city_label);
                JTextField city = new JTextField();
                input.add(city);

            panel.add(input);

            JPanel buttons = new JPanel();
            buttons.setLayout(new GridLayout(1, 4, hgap, vgap));

                JButton start = new JButton("Start game");
                buttons.add(start);

                JButton load = new JButton("Load game");
                buttons.add(load);

                JButton settings = new JButton("Settings");
                buttons.add(settings);

                JButton exit = new JButton("Exit");
                buttons.add(exit);
        
            panel.add(buttons);

        this.add(panel);
        this.pack();

        // ActionListener for the "Start game" button

        start.addActionListener(e -> {
            String player_name = name.getText();
            String city_name = city.getText();
            if (player_name.length() > 0 && city_name.length() > 0) {
                this.dispose();
                new Game(player_name, city_name).setVisible(true);
            }
        });

        // ActionListener for the "Load game" button

        load.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new java.io.File("./saves"));
            fileChooser.setDialogTitle("Choose the save file");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                String filepath = fileChooser.getSelectedFile().getAbsolutePath();
                new Game(filepath).setVisible(true);
                this.dispose();
            }
        });
        
        // ActionListener for the "Exit" button

        exit.addActionListener(e -> {
            this.dispose();
            System.exit(0);
        });
    }
}
