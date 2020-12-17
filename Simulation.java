
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

class Simulation extends JFrame implements ActionListener, MouseListener, MouseMotionListener, ChangeListener
{
    /*
    This class is main driver of the game. It contains all the buttons and 'on-off' features.
     */
    static Timer t;
    static int width = 1000, height = 800;
    static Map colony= new Map(width, height);
    static JSlider speedSldr, humanBirth, zombieBirth;
    static JComboBox disasters, godControls, music;
    static JButton simulateBtn = new JButton ("Simulate");
    static JButton helpBtn = new JButton("Help");
    static JButton resetBtn = new JButton("Demo Run");
    static JButton clearBtn = new JButton("Clear");
    static JButton applyBtn = new JButton("Apply");
    static JButton playBtn = new JButton("Play");
    static JLabel title = new JLabel("HUMANS VS ZOMBIES", JLabel.CENTER);
    static JLabel humanBirthRate = new JLabel("Human Birth Rate", JLabel.CENTER);
    static JLabel zombieBirthRate = new JLabel("Zombie Birth Rate", JLabel.CENTER);
    static JLabel speed_rate = new JLabel("Simulation Speed", JLabel.CENTER);
    static JLabel nd = new JLabel("Natural Disasters: ", JLabel.CENTER);
    static JLabel god = new JLabel("God Controls: ", JLabel.CENTER);
    static JLabel allowNat = new JLabel("Natural Events: ", JLabel.CENTER);
    static JLabel zLabel = new JLabel("Zombies: ", JLabel.CENTER);
    static JLabel pop = new JLabel("Population: ", JLabel.CENTER);
    static JLabel zombieCount = new JLabel("", JLabel.CENTER);
    static JLabel hLabel = new JLabel("Humans: ", JLabel.CENTER);
    static JLabel humanCount = new JLabel("", JLabel.CENTER);
    static JLabel winLabel = new JLabel("Winning: ", JLabel.CENTER);
    static JLabel win = new JLabel("", JLabel.CENTER);
    static JLabel mc = new JLabel("Music: ", JLabel.CENTER);
    static JCheckBox refreshZombie = new JCheckBox("Revive Zombies");
    static JCheckBox refreshHuman = new JCheckBox("Revive Humans");
    static JCheckBox refreshResource = new JCheckBox("Revive Resources");
    static JCheckBox populationCap = new JCheckBox("Population Limit");
    AudioInputStream audio;
    Clip clip;
    // Constructor
    public Simulation ()
    {
        //MOUSE LISTENERS-----------------------------------------------------------------------------------------------
        addMouseListener(this);
        addMouseMotionListener((MouseMotionListener) this);

        // BUTTONS AND SLIDERS------------------------------------------------------------------------------------------
        simulateBtn.addActionListener (this);
        helpBtn.addActionListener(this);
        resetBtn.addActionListener(this);
        clearBtn.addActionListener(this);
        applyBtn.addActionListener(this);
        playBtn.addActionListener(this);

        speedSldr = new JSlider();
        humanBirth = new JSlider();
        zombieBirth = new JSlider();
        speedSldr.addChangeListener (this);
        humanBirth.addChangeListener (this);
        zombieBirth.addChangeListener (this);

        // JCOMBO BOXES-------------------------------------------------------------------------------------------------

        String[] naturalDis = {"Virus", "Flood", "Supply Drop", "Cure", "Earthquake", "Famine", "Drought"};
        disasters = new JComboBox(naturalDis);
        disasters.addActionListener(this);

        String[] gControls = {"Add Humans", "Add Zombies", "Flood", "Add Supply Drops", "Remove Humans", "Remove Zombies", "Remove Water", "Remove Supply Drops", "Lightning", "Mini Earthquake", "Insert Cure", "Insert Virus", "Add Buildings"};
        godControls = new JComboBox(gControls);
        godControls.addActionListener(this);

        String[] Tracks = {"Twist and Shout", "Hey Jude", "Blinding Lights", "Depressing Music", "Punjabi Music", "More Punjabi Music","Battle"};
        music = new JComboBox(Tracks);
        music.addActionListener(this);

        // PANELS AND LAYOUT ------------------------------------------------------------------------------------------
        // Will Contain Everything
        JPanel content = new JPanel ();
        content.setLayout (new FlowLayout ());

        // Will Store User Stuff to the left and Game to the right
        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());

        // Left Panel for User Input
        JPanel left = new JPanel ();
        left.setLayout (new BorderLayout ());

        // Will Store Title
        JPanel leftTop = new JPanel ();
        leftTop.setLayout (new FlowLayout ());

        // Will Store Controls and etc
        JPanel leftBottom = new JPanel ();
        leftBottom.setLayout (new FlowLayout ());

        // Set Dimensions
        left.setSize(new Dimension(300, height));
        leftBottom.setPreferredSize(new Dimension(300, height-50));
        leftTop.setPreferredSize(new Dimension(300, 50));

        // EDITING AND ADDING COMPONENTS--------------------------------------------------------------------------------
        //EX. Font Size, Color, etc

        // LEFT TOP
        // Title
        Font f = new Font ("Nunito",Font.BOLD,25);
        title.setFont(f);
        title.setForeground(Color.red);
        title.setPreferredSize(new Dimension(300, 50));
        leftTop.add(title);


        // LEFT BOTTOM
        f = new Font ("Nunito",Font.BOLD,15);
        simulateBtn.setPreferredSize(new Dimension(117, 25));
        simulateBtn.setFont(f);
        leftBottom.add (simulateBtn);

        helpBtn.setFont(f);
        helpBtn.setPreferredSize(new Dimension(117, 25));
        leftBottom.add(helpBtn);

        resetBtn.setFont(f);
        resetBtn.setPreferredSize(new Dimension(117, 25));
        leftBottom.add(resetBtn);

        clearBtn.setFont(f);
        clearBtn.setPreferredSize(new Dimension(117, 25));
        leftBottom.add(clearBtn);

        f = new Font ("Nunito",Font.PLAIN,20);
        nd.setFont(f);
        nd.setPreferredSize(new Dimension(233, 25));
        leftBottom.add(nd);

        disasters.setPreferredSize(new Dimension(217, 25));
        disasters.setFont(f);
        leftBottom.add (disasters);

        f = new Font ("Nunito",Font.BOLD,15);
        applyBtn.setFont(f);
        applyBtn.setPreferredSize(new Dimension(117, 25));
        leftBottom.add(applyBtn);

        f = new Font ("Nunito",Font.PLAIN,20);
        god.setFont(f);
        god.setPreferredSize(new Dimension(233, 25));
        leftBottom.add(god);

        godControls.setPreferredSize(new Dimension(250, 25));
        godControls.setFont(f);
        leftBottom.add (godControls);


        humanBirthRate.setPreferredSize(new Dimension(233, 27));
        humanBirthRate.setFont(f);
        leftBottom.add (humanBirthRate);
        leftBottom.add(humanBirth);

        zombieBirthRate.setFont(f);
        leftBottom.add (zombieBirthRate);
        leftBottom.add(zombieBirth);

        speed_rate.setPreferredSize(new Dimension(233, 27));
        speed_rate.setFont(f);
        leftBottom.add (speed_rate);
        leftBottom.add (speedSldr);

        // Check Boxes
        allowNat.setFont(f);
        allowNat.setPreferredSize(new Dimension(200, 25));
        leftBottom.add(allowNat);
        f = new Font ("Nunito",Font.PLAIN,15);
        refreshZombie.setFont(f);
        refreshHuman.setFont(f);
        refreshResource.setFont(f);
        populationCap.setFont(f);
        refreshHuman.setSelected(true);
        refreshZombie.setSelected(true);
        refreshResource.setSelected(true);
        populationCap.setSelected(true);
        leftBottom.add(refreshHuman);
        leftBottom.add(refreshZombie);
        leftBottom.add(refreshResource);
        leftBottom.add(populationCap);

        // Population Counters and Labels
        f = new Font ("Nunito",Font.PLAIN,20);
        pop.setFont(f);
        pop.setPreferredSize(new Dimension(200, 25));
        leftBottom.add(pop);
        f = new Font ("Nunito",Font.PLAIN,19);
        hLabel.setFont(f);
        humanCount.setFont(f);
        zLabel.setFont(f);
        zombieCount.setFont(f);
        hLabel.setPreferredSize(new Dimension(117, 16));
        humanCount.setPreferredSize(new Dimension(117, 16));
        leftBottom.add(hLabel);
        leftBottom.add(humanCount);
        zLabel.setPreferredSize(new Dimension(117, 16));
        zombieCount.setPreferredSize(new Dimension(117, 16));
        leftBottom.add(zLabel);
        leftBottom.add(zombieCount);
        win.setFont(f);
        winLabel.setFont(f);
        winLabel.setPreferredSize(new Dimension(117, 23));
        win.setPreferredSize(new Dimension(117, 23));
        leftBottom.add(winLabel);
        leftBottom.add(win);

        f = new Font ("Nunito",Font.PLAIN,20);
        mc.setFont(f);
        mc.setPreferredSize(new Dimension(233, 25));
        leftBottom.add(mc);

        music.setPreferredSize(new Dimension(217, 27));
        music.setFont(f);
        leftBottom.add (music);

        f = new Font ("Nunito",Font.BOLD,15);
        playBtn.setFont(f);
        playBtn.setPreferredSize(new Dimension(117, 25));
        leftBottom.add(playBtn);


        // Game Area
        DrawArea simArea = new DrawArea(width, height);

        // Adding to Panels
        left.add(leftTop, "North");
        left.add(leftBottom, "South");
        main.add(left, "West");
        main.add(simArea, "East");
        content.add(main);

        //Setting Window Attributes
        setContentPane (content);
        //pack();
        setTitle ("Humans Vs Zombies");
        setSize (width + 305, height + 73);
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo (null);
        //setResizable (false);
    } // Simulation Constructor

    // Method called when an action is performed (to do a task)
    public void actionPerformed (ActionEvent e)
    {
        // If the simulate button was pressed
        if (e.getActionCommand().equals("Simulate")) {

            Movement moveColony = new Movement(colony); // ActionListener
            simulateBtn.setText("Stop"); // Change the button to say stop
            t = new Timer(200, moveColony); // Set the Timer
            t.start(); // Start Simulation
        }
        // If the stop button was pressed
        else if (e.getActionCommand().equals("Stop")) {
            simulateBtn.setText("Simulate"); // Change the button to say simulate
            t.stop(); // Stop the Simulation
        }
        // help Button was pressed
        else if (e.getActionCommand ().equals ("Help"))
        {
            // Display message
            JOptionPane.showMessageDialog(null, "Welcome! \nPlease press SIMULATE in order to begin the " +
                    "simulation. The objective\n of this game is to have the game run as long as possible. You,\n the user, " +
                    "can influence the game by applying natural disasters which\n will kill humans or zombies. FLOOD has a 50% " +
                    "chance of killing each\n zombie and DISEASE has a 10 - 40% chance to kill humans. ");
        }
        // If reset button was pressed
        else if (e.getActionCommand ().equals ("Demo Run"))
        {
            colony = new Map(width, height);
        }
        // If clear button was pressed
        else if (e.getActionCommand ().equals ("Clear"))
        {
            colony = new Map(width, height, ' ');
        }
        // If apply button was pressed
        else if (e.getActionCommand().equals("Apply"))
        {
            //Check which natural disaster was selected and call respective method
            if (disasters.getSelectedItem().equals("Virus"))
                colony.virus();
            else if (disasters.getSelectedItem().equals("Flood"))
                colony.flood();
            else if (disasters.getSelectedItem().equals("Cure"))
                colony.cure();
            else if (disasters.getSelectedItem().equals("Earthquake"))
                colony.earthquake();
            else if (disasters.getSelectedItem().equals("Supply Drop"))
                colony.supplyDrop();
            else if (disasters.getSelectedItem().equals("Famine"))
                colony.famine();
            else if (disasters.getSelectedItem().equals("Drought"))
                colony.drought();
        }
        // If play was pressed
        else if (e.getActionCommand().equals("Play"))
        {

            playBtn.setText("Stop "); // Change the button to say stop

            //Check which track was selected and call respective method
            if (music.getSelectedItem().equals("Twist and Shout"))
                playSound("TwistAndShout");
            else if (music.getSelectedItem().equals("Hey Jude"))
                playSound("HeyJude");
            else if (music.getSelectedItem().equals("Blinding Lights"))
                playSound("BlindingLights");
            else if (music.getSelectedItem().equals("Depressing Music"))
                playSound("MarvinsRoom");
            else if (music.getSelectedItem().equals("Punjabi Music"))
                playSound("PunjabiMusic2");
            else if (music.getSelectedItem().equals("More Punjabi Music"))
                playSound("PunjabiMusic");
            else if (music.getSelectedItem().equals("Battle"))
                playSound("Battle");
        }
        // If stop was pressed
        else if (e.getActionCommand().equals("Stop "))
        {
            playBtn.setText("Play"); // Change the button to say Play
            playSound("Stop"); //Calls the playSound method to stop
        }

        repaint (); //Refresh display of colony
    }

    //MOUSE-------------------------------------------------------------------------------------------------------------

    // Method called to respond to the mouse being dragged
    public void mouseDragged(MouseEvent e) {

        // Find coords of where it was dragged
        int x = Math.round( ( (e.getX() - 300) )/ 25);
        int y = Math.round( ( (e.getY()) - 50)/ 25);
        // Get the grid
        Entity [][] grid = colony.getGrid();

        // If the Add Humans option was selected
        if (godControls.getSelectedItem().toString().equals("Add Humans")) {

                grid[(y)][(x)] = new Human('R'); // Add Humans to clicked spot

        }
        // If Add Zombies was selected
        else if (godControls.getSelectedItem().toString().equals("Add Zombies")) {

            grid[(y)][(x)] = new Zombie(); // Add zombies to clicked spot
        }
        // Flood was selected
        else if (godControls.getSelectedItem().toString().equals("Flood")) {

                grid[(y)][(x)] = new Water(); // Add water to clicked spot
        }
        // Supply Drops was selected
        else if (godControls.getSelectedItem().toString().equals("Add Supply Drops")) {

                grid[(y)][(x)] = new SupplyDrop(); // Add supply drop to clicked spot
        }
        // Remove Humans was selected
        else if (godControls.getSelectedItem().toString().equals("Remove Humans")) {

            // Check if clicked spot is human
            if (grid[(y)][(x)] instanceof Human)
            grid[(y)][(x)] = new Entity(); // Remove Human from clicked spot
        }
        // Remove Zombies was selected
        else if (godControls.getSelectedItem().toString().equals("Remove Zombies")) {

            // If there is a zombie at the clicked spot
            if (grid[(y)][(x)] instanceof Zombie)
                grid[(y)][(x)] = new Entity();  // Remove Zombie from clicked spot

        }
        // Remove water was selected
        else if (godControls.getSelectedItem().toString().equals("Remove Water")) {

            // Check if there is water at the clicked spot
            if (grid[(y)][(x)] instanceof Water)
                grid[(y)][(x)] = new Entity(); // Remove water
        }
        // Remove Supply Drops was selected
        else if (godControls.getSelectedItem().toString().equals("Remove Supply Drops")) {

            // IF there is a supply drop at the clicked spot
            if (grid[(y)][(x)] instanceof SupplyDrop)
                grid[(y)][(x)] = new Entity(); // Remove supply drop
        }
        else if (godControls.getSelectedItem().toString().equals("Lightning")) {

            // If there is a person at the clicked spot
            if (grid[(y)][(x)] instanceof Person)
                grid[(y)][(x)] = new Entity(); // Remove the person
        }
        else if (godControls.getSelectedItem().toString().equals("Mini Earthquake")) {

            // Clear anything at the clicked spot
                grid[(y)][(x)] = new Entity();

        }
        else if (godControls.getSelectedItem().toString().equals("Insert Cure")) {

            // If there is a zombie at the clicked spot
            if (grid[(y)][(x)] instanceof Zombie)
            grid[(y)][(x)] = new Human(); // Convert to human
        }
        else if (godControls.getSelectedItem().toString().equals("Insert Virus")) {

            // If there is a human at the clicked spot
            if (grid[(y)][(x)] instanceof Human)
                grid[(y)][(x)] = new Zombie(); // Convert to zombie
        }
        else if (godControls.getSelectedItem().toString().equals("Add Buildings")) {

            // If there is a human at the clicked spot
                grid[(y)][(x)] = new Barrier(); // Convert to zombie
        }


        colony.setGrid(grid); // Set the new grid
        repaint(); // repaint

    } //Mouse Dragged

    // Need to implement because of interface
    public void mouseMoved(MouseEvent e) {

    }

    // Responds to the mouse being clicked
    public void mouseClicked(MouseEvent e)
    {
        // Find coords of where it was dragged
        int x = Math.round( ( (e.getX() - 300) )/ 25);
        int y = Math.round( ( (e.getY()) -50 )/ 25);
        // Get the grid
        Entity [][] grid = colony.getGrid();

        // If the Add Humans option was selected
        if (godControls.getSelectedItem().toString().equals("Add Humans")) {

            grid[(y)][(x)] = new Human('R'); // Add Humans to clicked spot

        }
        // If Add Zombies was selected
        else if (godControls.getSelectedItem().toString().equals("Add Zombies")) {

            grid[(y)][(x)] = new Zombie(); // Add zombies to clicked spot
        }
        // Flood was selected
        else if (godControls.getSelectedItem().toString().equals("Flood")) {

            grid[(y)][(x)] = new Water(); // Add water to clicked spot
        }
        // Supply Drops was selected
        else if (godControls.getSelectedItem().toString().equals("Add Supply Drops")) {

            grid[(y)][(x)] = new SupplyDrop(); // Add supply drop to clicked spot
        }
        // Remove Humans was selected
        else if (godControls.getSelectedItem().toString().equals("Remove Humans")) {

            // Check if clicked spot is human
            if (grid[(y)][(x)] instanceof Human)
                grid[(y)][(x)] = new Entity(); // Remove Human from clicked spot
        }
        // Remove Zombies was selected
        else if (godControls.getSelectedItem().toString().equals("Remove Zombies")) {

            // If there is a zombie at the clicked spot
            if (grid[(y)][(x)] instanceof Zombie)
                grid[(y)][(x)] = new Entity();  // Remove Zombie from clicked spot

        }
        // Remove water was selected
        else if (godControls.getSelectedItem().toString().equals("Remove Water")) {

            // Check if there is water at the clicked spot
            if (grid[(y)][(x)] instanceof Water)
                grid[(y)][(x)] = new Entity(); // Remove water
        }
        // Remove Supply Drops was selected
        else if (godControls.getSelectedItem().toString().equals("Remove Supply Drops")) {

            // IF there is a supply drop at the clicked spot
            if (grid[(y)][(x)] instanceof SupplyDrop)
                grid[(y)][(x)] = new Entity(); // Remove supply drop
        }
        else if (godControls.getSelectedItem().toString().equals("Lightning")) {

            // If there is a person at the clicked spot
            if (grid[(y)][(x)] instanceof Person)
                grid[(y)][(x)] = new Entity(); // Remove the person
        }
        else if (godControls.getSelectedItem().toString().equals("Mini Earthquake")) {

            // Clear anything at the clicked spot
            grid[(y)][(x)] = new Entity();

        }
        else if (godControls.getSelectedItem().toString().equals("Insert Cure")) {

            // If there is a zombie at the clicked spot
            if (grid[(y)][(x)] instanceof Zombie)
                grid[(y)][(x)] = new Human(); // Convert to human
        }
        else if (godControls.getSelectedItem().toString().equals("Insert Virus")) {

            // If there is a human at the clicked spot
            if (grid[(y)][(x)] instanceof Human)
                grid[(y)][(x)] = new Zombie(); // Convert to zombie
        }


        colony.setGrid(grid); // Set the new grid
        repaint(); // repaint
    }

    // Need to implement these methods because of an interface

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    //------------------------------------------------------------------------------------------------------------------

    public void stateChanged (ChangeEvent e)
    {
        if (t != null)
            t.setDelay (400 - 2 * speedSldr.getValue ()); // 0 to 400 ms
    }

    // Plays Music
    public void playSound(String song) {

        if(song.equals("Stop")) //If stop is passed through to the playSound method
            clip.stop(); //Stop the clip

        else {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(colony.source + song + ".wav").getAbsoluteFile());
                clip = AudioSystem.getClip(); //Obtains the clip
                clip.open(audioInputStream); //Opens the clip
                clip.start(); //Start playing the clip
                clip.loop(Clip.LOOP_CONTINUOUSLY); //Play the clip infinitely

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Drawing Board
    class DrawArea extends JPanel
    {
        private int w, h;

        // Constructor to create Drawing area
        public DrawArea (int w, int h)
        {
            this.w = w;
            this.h = h;

            this.setPreferredSize (new Dimension (w, h)); // set size
        }

        // Display Grid
        public void paintComponent (Graphics g)
        {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, w, h);

            try {
                colony.show(g);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Movement implements ActionListener
    {
        private Map colony;

        public Movement (Map col)
        {
            colony = col;
        }

        // Responds to an action being performed
        public void actionPerformed (ActionEvent e)
        {
            boolean revH = false;
            boolean revZ = false;
            boolean revR = false;
            boolean popCap = false;

            // Check the checkboxes, add see what is selected or not
            if (refreshHuman.isSelected())
                revH = true;
            if (refreshZombie.isSelected())
                revZ = true;
            if (refreshResource.isSelected())
                revR = true;
            if (populationCap.isSelected())
                popCap = true;

            // Send in values, and call advance method to advance the game
            colony.advance(humanBirth.getValue(), zombieBirth.getValue(), revH, revZ, revR, popCap);

            // Check and Set who is winning
            zombieCount.setText(String.valueOf(colony.totalZombies));
            humanCount.setText(String.valueOf(colony.totalHumans));
            String winning = "";

            if (colony.totalZombies < colony.totalHumans)
                winning = "Humans";
            else if (colony.totalZombies > colony.totalHumans)
                winning = "Zombies";
            else
                winning = "";

            win.setText(winning);
            repaint (); // repaint
        }
    }

    public static void main (String[] args)
    {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter source: (/home/prodigy/Personal Projects/Simulation-Game/) :");
        colony.source = sc.nextLine();

        Simulation window = new Simulation ();
        window.setVisible (true);
    }
}
