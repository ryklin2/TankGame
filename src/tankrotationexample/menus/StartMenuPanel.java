package tankrotationexample.menus;

import tankrotationexample.Launcher;
import tankrotationexample.game.GameWorld;
import tankrotationexample.game.GameState;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class StartMenuPanel extends JPanel {

    private BufferedImage menuBackground;
    private final Launcher lf;

    public StartMenuPanel(Launcher lf) {
        this.lf = lf;
        try {
            menuBackground = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("title.png")));
        } catch (IOException e) {
            System.out.println("Error loading title image");
            e.printStackTrace();
        }
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.BLACK);

        // Create title
        JLabel title = new JLabel("Tank Wars");
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create level buttons panel
        JPanel levelPanel = new JPanel();
        levelPanel.setLayout(new BoxLayout(levelPanel, BoxLayout.Y_AXIS));
        levelPanel.setBackground(Color.BLACK);

        // Level 1 button
        JButton level1Button = new JButton("Level 1: Open Arena");
        level1Button.setAlignmentX(Component.CENTER_ALIGNMENT);
        level1Button.addActionListener((e) -> {
            GameWorld.setLevel(1);
            this.lf.setFrame(GameState.GAME);
        });

        // Level 2 button
        JButton level2Button = new JButton("Level 2: Maze");
        level2Button.setAlignmentX(Component.CENTER_ALIGNMENT);
        level2Button.addActionListener((e) -> {
            GameWorld.setLevel(2);
            this.lf.setFrame(GameState.GAME);
        });

        // Level 3 button
        JButton level3Button = new JButton("Level 3: Split Screen");
        level3Button.setAlignmentX(Component.CENTER_ALIGNMENT);
        level3Button.addActionListener((e) -> {
            GameWorld.setLevel(3);
            this.lf.setFrame(GameState.GAME);
        });

        // Exit button
        JButton exitButton = new JButton("Exit Game");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener((e) -> this.lf.closeGame());

        // Add components with spacing
        this.add(Box.createRigidArea(new Dimension(0, 50)));
        this.add(title);
        this.add(Box.createRigidArea(new Dimension(0, 50)));
        this.add(level1Button);
        this.add(Box.createRigidArea(new Dimension(0, 15)));
        this.add(level2Button);
        this.add(Box.createRigidArea(new Dimension(0, 15)));
        this.add(level3Button);
        this.add(Box.createRigidArea(new Dimension(0, 30)));
        this.add(exitButton);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(menuBackground, 0, 0, null);
    }
}