package tankrotationexample;

import tankrotationexample.SoundPlayer;
import tankrotationexample.game.GameWorld;
import tankrotationexample.menus.EndGamePanel;
import tankrotationexample.menus.StartMenuPanel;
import tankrotationexample.game.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class Launcher {

    private JPanel mainPanel;
    private GameWorld gamePanel;
    private final JFrame jf;
    private CardLayout cl;
    private static Launcher instance;
    private EndGamePanel endGamePanel;

    // Track current state
    private GameState currentState;

    public Launcher() {
        this.jf = new JFrame();
        this.jf.setTitle("Tank Wars Game");
        this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SoundPlayer.playMenuMusic();
    }

    public static Launcher getInstance() {
        if (instance == null) {
            instance = new Launcher();
        }
        return instance;
    }

    public void setEndGamePanel(EndGamePanel panel) {
        this.endGamePanel = panel;
    }

    public EndGamePanel getEndGamePanel() {
        return this.endGamePanel;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void setFrame(GameState state) {
        this.currentState = state; // Track the current state
        this.jf.setVisible(false);
        switch (state) {
            case START -> {
                this.jf.setSize(GameConstants.START_MENU_SCREEN_WIDTH, GameConstants.START_MENU_SCREEN_HEIGHT);
                this.cl.show(mainPanel, GameState.START.name().toLowerCase());
            }
            case GAME -> {
                this.jf.setSize(GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
                this.gamePanel.InitializeGame();
                new Thread(this.gamePanel).start();
                this.cl.show(mainPanel, GameState.GAME.name().toLowerCase());
            }
            case SPLITSCREEN -> {
                this.jf.setSize(GameConstants.SPLIT_SCREEN_VIEW_WIDTH * 2, GameConstants.SPLIT_SCREEN_VIEW_HEIGHT);
                this.gamePanel.InitializeSplitScreenGame(); // Make sure this is called
                new Thread(this.gamePanel).start();
                this.cl.show(mainPanel, GameState.SPLITSCREEN.name().toLowerCase());
            }
            case END -> {
                this.jf.setSize(GameConstants.END_MENU_SCREEN_WIDTH, GameConstants.END_MENU_SCREEN_HEIGHT);
                this.cl.show(mainPanel, GameState.END.name().toLowerCase());
            }
        }
        this.jf.setVisible(true);
    }

    private void initUIComponents() {
        this.mainPanel = new JPanel();
        JPanel startPanel = new StartMenuPanel(this);
        this.gamePanel = new GameWorld(this); // Default game panel for regular levels
        this.gamePanel.InitializeGame();

        // Create a larger GameWorld for split-screen mode
        GameWorld splitScreenGamePanel = new GameWorld(this);
        splitScreenGamePanel.InitializeSplitScreenGame(); // Add new map initialization for split-screen mode

        // Create end game panel
        this.endGamePanel = new EndGamePanel(this);

        // Add panels to mainPanel
        this.cl = new CardLayout();
        this.mainPanel.setLayout(cl);
        this.mainPanel.add(startPanel, GameState.START.name().toLowerCase());
        this.mainPanel.add(gamePanel, GameState.GAME.name().toLowerCase());
        this.mainPanel.add(splitScreenGamePanel, GameState.SPLITSCREEN.name().toLowerCase()); // Add split-screen panel
        this.mainPanel.add(endGamePanel, GameState.END.name().toLowerCase());

        this.jf.add(mainPanel);
        this.jf.setResizable(false);
        this.setFrame(GameState.START); // Set initial state to START
    }

    public JFrame getJf() {
        return jf;
    }

    public void closeGame() {
        this.jf.dispatchEvent(new WindowEvent(this.jf, WindowEvent.WINDOW_CLOSING));
    }


    public static void main(String[] args) {
        Launcher launcher = Launcher.getInstance();
        launcher.initUIComponents();
    }
}
