
/**

The Game class represents a Java Swing application for a city-building game.
It extends the Window class and provides a graphical user interface for the game.
*/

package gui.elements;

import settings.*;
import gui.Window;
import types.Building;
import types.Buildings.*;
import types.Zone;
import types.Zones.*;


import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**

The Game class represents a Java Swing application for a city-building game.

It extends the Window class and provides a graphical user interface for the game.
*/

public class Game extends Window {

    private JPanel panel;
    private Canvas canvas;

    private JPanel leftPanel, rightPanel, topPanel;
    private JLabel date, money, satisfaction;

    Color[] zoneColors = {Color.GREEN, Color.BLUE, Color.YELLOW};
    String[] zonesNames = {"residential", "service", "industrial"};
    private JButton[] zoneButtons;

    String[] buildingNames = {"fire", "police", "stadium", "forest", "road"};
    private JButton[] buildingButtons;
    private JButton demolishButton, disasterButton;

    private String selected;
    private Storage storage;

    private Timer timer;
    //boolean isPaused = false;
    int pausedSpeed = 1;

    /**
    Constructs a Game object with the specified player name and city name.
    @param playerName the name of the player
    @param cityName the name of the city
    */
    public Game(String playerName, String cityName) {
        super();
        this.storage = new Storage(playerName, cityName);
        this.startGame();
    }

    /**

    Constructs a Game object with the specified file name.
    @param filename the name of the file
    */
    public Game(String filename) {
        super();
        this.storage = new Storage("","");
        this.storage.load(filename);
        this.startGame();
    }

    private void startGame() {
        this.assignMenuBar();
        this.setup();
        this.timer = new Timer(1000, e -> {
            this.updateTopPanel();
            if(storage.getSatisfaction() <= 10){
                JOptionPane.showMessageDialog(null, "Residents are dissatisfied. You are fired!", "Game End", JOptionPane.INFORMATION_MESSAGE );
                timer.stop();
                this.dispose();
                new Menu().setVisible(true);
            }
            if(storage.isChanged()) canvas.repaint();
        });
        this.timer.start();
    }

    /**
     * Assigns the city menu to the game.
     * 
     * @return the JMenu object representing the city menu
     */
    private void assignMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(assignCityMenu());
        JMenu menu = new JMenu("Speed");
            menu.add(assignSpeedItem("Normal", 1));
            menu.add(assignSpeedItem("Faster", 2));
            menu.add(assignSpeedItem("Extra Fast", 5));
            menu.add(assignPlayPauseItem());
        menuBar.add(menu);
        menuBar.add(assignDisasterMenu());
        this.setJMenuBar(menuBar);  
    }

    /**

    Assigns the city menu to the game.
    @return the JMenu object representing the city menu
    */
    public JMenu assignCityMenu(){
        JMenu menu = new JMenu("City");
        JMenuItem menuItem = new JMenuItem("Save");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                storage.save();
            }
        });
        menu.add(menuItem);
        return menu;
    }

    /**
    Assigns a speed item to the game.
    @param mode the speed mode
    @param speed the speed value
    @return the JMenuItem object representing the speed item
    */

    public JMenuItem assignSpeedItem(String mode, int aSpeed){
        JMenuItem speed1 = new JMenuItem(mode);
        speed1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                storage.setSpeed(aSpeed);
            }
        });
        return speed1;
    }

    public JMenu assignDisasterMenu(){
        JMenu dMenu = new JMenu("Disasters");
            JMenuItem explosion = new JMenuItem("Explosion (3x3)");
            explosion.addActionListener((e) -> { disasterHelper(0); });
        dMenu.add(explosion);

        JMenuItem earthquake = new JMenuItem("Earthquake (1x1)");
            earthquake.addActionListener((e) -> { disasterHelper(1); });
        dMenu.add(earthquake);
        return dMenu;
    }

    public JMenuItem assignPlayPauseItem(){
        JMenuItem playPause = new JMenuItem("Play/Pause");
        playPause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.print(storage.isPaused());
                if(!storage.isPaused())
                {
                    pausedSpeed = storage.getSpeed();
                    System.out.print(pausedSpeed);
                    storage.setSpeed(0);
                    storage.setPause(true);
                    ImageIcon takeBreak = new ImageIcon("./images/TakeBreak.jpeg");
                    // ImageIcon imageIcon = new ImageIcon("./img/imageName.png"); // load the image to a imageIcon
                    Image image = takeBreak.getImage(); // transform it 
                    Image newimg = image.getScaledInstance(200, 200,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
                    takeBreak = new ImageIcon(newimg); 
                    JOptionPane.showMessageDialog(null, "Game Paused", "Game Status", JOptionPane.INFORMATION_MESSAGE, takeBreak);
                }
                else
                {
                    System.out.print(pausedSpeed);
                    storage.setSpeed(pausedSpeed);
                    storage.setPause(false);
                    ImageIcon welcomeBack = new ImageIcon("./images/HappyTo.jpeg");
                    Image image = welcomeBack.getImage(); // transform it 
                    Image newimg = image.getScaledInstance(200, 200,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
                    welcomeBack = new ImageIcon(newimg);
                    JOptionPane.showMessageDialog(null, "Game Conitnued", "Game Status", JOptionPane.INFORMATION_MESSAGE, welcomeBack);
                }
                        
            }
        });
        return playPause;
    }

    /**
    * Sets up the game interface by initializing panels, buttons, and canvas.
    */
    private void setup() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);

            topPanel = new JPanel();
                topPanel.setLayout(new BorderLayout());
                topPanel.setBackground(Color.YELLOW);
                topPanel.setPreferredSize(new Dimension(General.width, 50));
                topPanel.setLayout(new GridLayout(1, 3, General.cellSize, 0));
                this.drawTopPanel();
            panel.add(topPanel, BorderLayout.NORTH);

            leftPanel = new JPanel();
                leftPanel.setLayout(new BorderLayout());
                leftPanel.setBackground(Color.BLUE);
                leftPanel.setPreferredSize(new Dimension(200, General.height));
                leftPanel.setLayout(new GridLayout(4, 1, 0, General.cellSize));
                this.addZoneButtons();
                this.addBuildingButtons();
                this.addDemolitionButton();
                this.addDisasterButton();
                this.addForest();
            this.panel.add(leftPanel, BorderLayout.WEST);

            rightPanel = new JPanel();
                rightPanel.setLayout(new BorderLayout());
                rightPanel.setBackground(Color.RED);
                this.setupCanvas();
            this.panel.add(rightPanel, BorderLayout.CENTER);

        this.add(panel);
        this.pack();
    }
    /**
     * Loads an image with the specified name from the "images" directory.
     * 
     * @param name the name of the image file
     * @return the loaded `ImageIcon` object
     */
    private ImageIcon loadImage(String name) {
        Image img = new ImageIcon("./images/" + name).getImage();
        Image newimg = img.getScaledInstance(
            60, 60, java.awt.Image.SCALE_SMOOTH
        );
        return new ImageIcon(newimg);
    }
    /**
     * Draws the top panel of the game interface, displaying the date, money, and satisfaction labels.
     */
    private void drawTopPanel() {
        // create the labels
        date = new JLabel("Date: " + this.storage.getStringDate());
        money = new JLabel("Money: " + this.storage.getMoney());
        satisfaction = new JLabel("Satisfaction: " + this.storage.getSatisfaction());
        // add the labels
        topPanel.add(date);
        topPanel.add(money);
        topPanel.add(satisfaction);
        // center the text
        date.setHorizontalAlignment(JLabel.CENTER);
        money.setHorizontalAlignment(JLabel.CENTER);
        satisfaction.setHorizontalAlignment(JLabel.CENTER);
    }
/**
 * Updates the top panel of the game interface with the latest date, money, and satisfaction values.
 */
    private void updateTopPanel() {
        date.setText("Date: " + this.storage.getStringDate());
        money.setText("Money: " + this.storage.getMoney());
        satisfaction.setText("Satisfaction: " + this.storage.getSatisfaction());
    }
/**
 * Adds zone buttons to the left panel of the game interface.
 */
    private void addZoneButtons() {
        JPanel zones = new JPanel();
        zones.setLayout(new GridLayout(zonesNames.length, 1, 0, 0));
        zoneButtons = new JButton[zonesNames.length];

        for (int i = 0; i < zonesNames.length; i++) {
            final int index = i;
            final String zone = zonesNames[i];
            zoneButtons[i] = new JButton(this.loadImage("zones/" + zone + ".png"));
            zoneButtons[i].setPreferredSize(new Dimension(General.cellSize*2, General.cellSize));
            zoneButtons[i].setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            zoneButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectOne(zoneButtons, zone, index);
                }
            });
            zones.add(zoneButtons[i]);
        }

        leftPanel.add(zones, BorderLayout.NORTH);
    }
/**
 * Adds building buttons to the left panel of the game interface.
 */
    private void addBuildingButtons() {
        JPanel buildings = new JPanel();
        buildings.setLayout(new GridLayout((buildingNames.length/2)+1, 2, 0, 0));
        buildingButtons = new JButton[buildingNames.length];

        for (int i = 0; i < buildingNames.length; i++) {
            final int index = i;
            final String building = buildingNames[i];
            buildingButtons[i] = new JButton(this.loadImage("buildings/" + building + ".png"));
            buildingButtons[i].setPreferredSize(new Dimension(General.cellSize, General.cellSize));
            buildingButtons[i].setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            buildingButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectOne(buildingButtons, building, index);
                }
            });
            buildings.add(buildingButtons[i]);
        }

        leftPanel.add(buildings, BorderLayout.NORTH);
    }
    /**
     * Adds a demolish button to the left panel of the game interface.
     */
    private void addDemolitionButton() {
        JPanel demolish = new JPanel();
        demolish.setLayout(new GridLayout(1, 1, 0, 0));
            demolishButton = new JButton(this.loadImage("demolish.png"));
                demolishButton.setPreferredSize(new Dimension(General.cellSize*2, General.cellSize));
                demolishButton.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
                demolishButton.setBackground(Color.WHITE);
                demolishButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        deselect();
                        if (selected == "demolish") {
                            selected = null;
                            demolishButton.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
                        } else {
                            selected = "demolish";
                            demolishButton.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
                        }
                    }
                });
            demolish.add(demolishButton);
        leftPanel.add(demolish, BorderLayout.NORTH);
    }

    public void disasterHelper(int dType) {
        int x = (int)(Math.random() * General.width) / General.cellSize;
        int y = (int)(Math.random() * General.height) / General.cellSize;
        storage.disaster(x,y,dType);
        drawDisaster(x,y,dType);
    }

    public void randomDisasterHelper() {
        int x = (int)(Math.random() * General.width) / General.cellSize;
        int y = (int)(Math.random() * General.height) / General.cellSize;
        int dType = storage.disaster(x,y);
        drawDisaster(x,y,dType);
    }

    private void addDisasterButton() {
        JPanel disaster = new JPanel();
        disaster.setLayout(new GridLayout(1, 1, 0, 0));
            disasterButton = new JButton("Disaster");
                disasterButton.setPreferredSize(new Dimension(General.cellSize*2, General.cellSize));
                disasterButton.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
                disasterButton.setBackground(Color.WHITE);  
                disasterButton.addActionListener((e) -> { randomDisasterHelper(); });
            disaster.add(disasterButton);
        leftPanel.add(disaster, BorderLayout.NORTH);
    }
    

    public void addForest(){
        int x = (int)(Math.random() * General.width) / General.cellSize;
        int y = (int)(Math.random() * General.height) / General.cellSize;
        Forest newForest = new Forest(x, y);
        boolean status = storage.addBuilding(newForest);
        if(status) storage.addMoney(newForest.getCost());
    }

    private void drawDisaster(int x, int y, int dType) {
        Graphics g = canvas.getGraphics();

        int width = 0;
        if (dType == 0) width = 1;

        final int dim = width;
        new Thread(() -> {
            if (dType == 0) {
                Image img = Toolkit.getDefaultToolkit().getImage("./images/explosion.gif");
                for (int i = 0; i < 10; i++) {
                    g.drawImage(img, (x-dim)*General.cellSize, (y-dim)*General.cellSize, General.cellSize*3, General.cellSize*3, null);
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {}
                }
            }
            g.setColor(Color.BLACK);
            g.fillRect((x-dim)*General.cellSize, (y-dim)*General.cellSize, General.cellSize*((dim*2)+1), General.cellSize*((dim*2)+1));
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
            canvas.repaint();
        }).start();
    }
/**
 * Sets up the canvas for drawing the game elements.
 */
    private void setupCanvas() {
        // get graphics of right panel
        canvas = new Canvas() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                drawGrid(g);
                for (Building building : storage.getBuildings()) {
                    drawBuilding(building, g);
                }
                for (Zone zone: storage.getZones()){
                    drawZone(zone, g);
                }
            }
            /**
             * Draws the grid lines on the canvas.
             *
             * @param g the Graphics object for drawing
             */
            private void drawGrid(Graphics g) {
                g.setColor(Color.BLACK);
                for (int i = 0; i < General.width; i += General.cellSize) {
                    g.drawLine(i, 0, i, General.height);
                }
                for (int i = 0; i < General.height; i += General.cellSize) {
                    g.drawLine(0, i, General.width, i);
                }
            }
        };


        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX()/General.cellSize;
                int y = e.getY()/General.cellSize;
                System.out.println("Clicked: " + x + ", " + y);

                int b_ind = storage.getZoneIndex(x, y);
                System.out.println("Checking b_ind" + b_ind);
                if (b_ind != -1 && selected == null ){
                    Zone zone = storage.getZones().get(b_ind);
                        //System.out.println("Maybe Not working");
                        int residentCount =  zone.getSaturation();
                        int residentSatisfaction = zone.getSatisfaction();
                        String stats = "Number of people : " + residentCount + "\n Satisfaction Level : " + residentSatisfaction ; 
                        JOptionPane.showMessageDialog(null, stats, "Statistics", JOptionPane.INFORMATION_MESSAGE );
                }

                if (selected != null) {
                    Graphics g = canvas.getGraphics();
                    
                    if (isBuilding(selected)) {
                        Building b = getBuildingClass(selected, x, y);
                        boolean added = storage.addBuilding(b);
                        if (added) {
                            drawBuilding(b, g);
                            updateTopPanel();
                        }
                    }
                    else if (isZone(selected)) {
                        Zone zone = getZoneClass(selected, x, y);
                        boolean added = storage.addZone(zone);
                        if(added){
                            g.setColor(zoneColors[getZoneIndex(selected)]);
                            g.fillRect(x*General.cellSize, y*General.cellSize, General.cellSize, General.cellSize);
                            updateTopPanel();
                        }

                    }
                    else if (selected == "demolish") {
                        boolean removed = storage.removeBuilding(x, y);
                        if (removed) {
                            canvas.repaint();
                            updateTopPanel();
                        }
                        else{
                            removed = storage.removeZone(x, y);
                            if (removed) {
                                canvas.repaint();
                                updateTopPanel();
                            }
                        }
                    }
                }

                
            }
        }); 

        canvas.setBackground(General.backgroundColor);
        canvas.setPreferredSize(new Dimension(500, 500));
        rightPanel.add(canvas, BorderLayout.CENTER);
    }

        /**
     * Draws a building on the canvas.
     * 
     * @param b the building to be drawn
     * @param g the Graphics object for drawing
     */
    private void drawBuilding(Building b, Graphics g) {
        int x = b.getX() * General.cellSize;
        int y = b.getY() * General.cellSize;
        int side = b.getSize() * General.cellSize;
        g.setColor(General.backgroundColor);
        g.fillRect(x,y,side,side);
        g.drawImage(b.getImage(), x, y, side, side, null);
    }

     /**
     * Draws a zone on the canvas.
     * 
     * @param zone the zone to be drawn
     * @param g    the Graphics object for drawing
     */
    private void drawZone(Zone zone, Graphics g){
        int x = zone.getX() * General.cellSize;
        int y = zone.getY() * General.cellSize;
        if(zone.getImage() == null){
            g.setColor(zoneColors[zone.getType()]);
            g.fillRect(x, y, General.cellSize, General.cellSize);
        }
        else{
            int side = General.cellSize;
            g.setColor(General.backgroundColor);
            g.fillRect(x,y,side,side);
            g.drawImage(zone.getImage(), x, y, side, side, null);
        }
    }
    /**
     * Deselects all the buttons in the interface.
     */

    private void deselect() {
        for (int i = 0; i < zonesNames.length; i++) {
            zoneButtons[i].setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        }
        for (int i = 0; i < buildingNames.length; i++) {
            buildingButtons[i].setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        }
    }
/**
 * Selects the specified button and sets it as the currently selected object.
 *
 * @param buttons the array of buttons
 * @param obj     the name of the selected object
 * @param index   the index of the selected object in the array
 */
    private void selectOne(JButton[] buttons, String obj, int index) {
        deselect();

        if (this.selected == obj) {
            this.selected = null;
            return; // deselect zone
        }
        this.selected = obj;
        buttons[index].setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
    }
/**
 * Returns the corresponding building class for the given building name and coordinates.
 *
 * @param name the name of the building
 * @param x    the x-coordinate of the building
 * @param y    the y-coordinate of the building
 * @return the Building object representing the specified building
 */
    private Building getBuildingClass(String name, int x, int y) {
        switch (name) {
            case "road": return new Road(x, y);
            case "fire": return new Fire(x, y);
            case "police": return new Police(x, y);
            case "forest": return new Forest(x, y);
            case "stadium": return new Stadium(x, y);
            default: return null;
        }
    }
    /**
     * Returns the corresponding zone class for the given zone name and coordinates.
     * 
     * @param name the name of the zone
     * @param x    the x-coordinate of the zone
     * @param y    the y-coordinate of the zone
     * @return the Zone object representing the specified zone
     */
    private Zone getZoneClass(String name, int x, int y){
        switch(name) {
            case "residential": return new Residential(x, y);
            case "industrial": return new Industrial(x, y);
            case "service": return new Service(x, y);
            default: return null;
        }
    }
/**
 * Returns the index of the specified building in the buildingNames array.
 *
 * @param target the name of the building
 * @return the index of the building in the buildingNames array, or -1 if not found
 */
    private int getBuildingIndex(String target) {
        for (int i = 0; i < buildingNames.length; i++) {
            if (target == buildingNames[i]) {
                return i;
            }
        }
        return -1;
    } 
    /**
 * Returns the index of the specified zone in the zonesNames array.
 *
 * @param target the name of the zone
 * @return the index of the zone in the zonesNames array, or -1 if not found
 */
    private int getZoneIndex(String target) {
        for (int i = 0; i < zonesNames.length; i++) {
            if (target == zonesNames[i]) {
                return i;
            }
        }
        return -1;
    }
/**
 * Checks if the specified target is a valid building name.
 *
 * @param target the name to check
 * @return true if the target is a valid building name, false otherwise
 */
    private boolean isBuilding(String target) {
        return getBuildingIndex(target) != -1;
    }

/**
 * Checks if the specified target is a valid zone name.
 *
 * @param target the name to check
 * @return true if the target is a valid zone name, false otherwise
 */
    private boolean isZone(String target) {
        return getZoneIndex(target) != -1;
    }   
}
