package tankrotationexample.game;

public enum GameState {
    START("Start Menu", 500, 550),
    GAME("Gameplay", 1280, 960),
    END("End Menu", 500, 500),
    SPLITSCREEN("Split-Screen Gameplay", 2000, 1000);

    private final String description;
    private final int width;
    private final int height;

    // Constructor
    GameState(String description, int width, int height) {
        this.description = description;
        this.width = width;
        this.height = height;
    }

    // Getters
    public String getDescription() {
        return description;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
