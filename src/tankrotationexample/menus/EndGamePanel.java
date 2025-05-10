package tankrotationexample.menus;

import tankrotationexample.Launcher;
import tankrotationexample.game.GameState;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class EndGamePanel extends JPanel {

    private BufferedImage menuBackground;
    private final Launcher lf;
    private static String winner = "Tank 1"; // Default value
    private JLabel winnerLabel;

    public EndGamePanel(Launcher lf) {
        this.lf = lf;
        try {
            menuBackground = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("title.png")));
        } catch (IOException e) {
            System.out.println("Error loading title image");
            e.printStackTrace();
        }
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.BLACK);

        JLabel titleLabel = new JLabel("Game Over!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        winnerLabel = new JLabel(winner + " Wins!");
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        winnerLabel.setForeground(Color.WHITE);
        winnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton restartButton = new JButton("Restart Game");
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartButton.addActionListener((e) -> {
            this.lf.setFrame(GameState.START); // Go back to level selection
        });

        JButton exitButton = new JButton("Exit Game");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener((e) -> this.lf.closeGame());

        this.add(Box.createRigidArea(new Dimension(0, 50)));
        this.add(titleLabel);
        this.add(Box.createRigidArea(new Dimension(0, 30)));
        this.add(winnerLabel);
        this.add(Box.createRigidArea(new Dimension(0, 50)));
        this.add(restartButton);
        this.add(Box.createRigidArea(new Dimension(0, 30)));
        this.add(exitButton);
    }

    public static void setWinner(String winner) {
        EndGamePanel.winner = winner;
    }

    public void updateWinnerDisplay() {
        winnerLabel.setText(winner + " Wins!");
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(menuBackground, 0, 0, null);
    }
}
