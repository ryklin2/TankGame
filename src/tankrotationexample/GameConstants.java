package tankrotationexample;

public final class GameConstants {
    private GameConstants() {}

    // Standard screen dimensions
    public static final int GAME_SCREEN_WIDTH = 1280; // Width for regular gameplay
    public static final int GAME_SCREEN_HEIGHT = 960; // Height for regular gameplay
    public static final int START_MENU_SCREEN_WIDTH = 500;
    public static final int START_MENU_SCREEN_HEIGHT = 550;
    public static final int END_MENU_SCREEN_WIDTH = 500;
    public static final int END_MENU_SCREEN_HEIGHT = 500;

    public static final int LARGE_MAP_WIDTH = 2000;
    public static final int LARGE_MAP_HEIGHT = 2000;
    public static final int SCREEN_WIDTH = 1000;
    public static final int SCREEN_HEIGHT = 600;


    // Split-screen dimensions
    public static final int SPLIT_SCREEN_MAP_WIDTH = 2000; // Width for split-screen map
    public static final int SPLIT_SCREEN_MAP_HEIGHT = 2000; // Height for split-screen map
    public static final int SPLIT_SCREEN_VIEW_WIDTH = GAME_SCREEN_WIDTH / 2; // Split-screen width per view
    public static final int SPLIT_SCREEN_VIEW_HEIGHT = GAME_SCREEN_HEIGHT; // Split-screen height per view

    // Resource paths
    public static final String TANK1_IMAGE_PATH = "tank1.png";
    public static final String TANK2_IMAGE_PATH = "tank2.png";
    public static final String MENU_BACKGROUND = "Title.bmp";
    public static final String GAME_BACKGROUND = "Background.bmp";
    public static final String WALL1_IMAGE_PATH = "wall1.png";
    public static final String WALL2_IMAGE_PATH = "wall2.png";

    // Animation constants
    public static final int BULLET_HIT_FRAME_COUNT = 6;
    public static final int EXPLOSION_LARGE_FRAME_COUNT = 6;
    public static final String BULLET_HIT_ANIMATION_PATH = "animations/bullethit/bullethit_";
    public static final String EXPLOSION_ANIMATION_PATH = "animations/explosion_lg/explosion_lg_";

    // Sound paths
    public static final String GAME_MUSIC = "Music.mp3";
    public static final String EXPLOSION_LARGE_SOUND = "Explosion_large.wav";
    public static final String EXPLOSION_SMALL_SOUND = "Explosion_small.wav";

    // Power-up paths
    public static final String SHIELD1_POWERUP = "Shield1.gif";
    public static final String SHIELD2_POWERUP = "Shield2.gif";
    public static final String WEAPON_POWERUP = "Weapon.gif";
}
