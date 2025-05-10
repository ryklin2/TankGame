package tankrotationexample;

import javax.sound.sampled.*;
import javax.sound.midi.*;
import java.io.IOException;
import java.net.URL;

public class SoundPlayer {
    private static Clip gameMusic;
    private static Sequencer menuMusic;

    // For one-shot sound effects
    public static void playSound(String soundFileName) {
        try {
            URL soundFile = SoundPlayer.class.getClassLoader().getResource(soundFileName);
            if (soundFile == null) {
                System.out.println("Sound file not found: " + soundFileName);
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error playing sound: " + e.getMessage());
        }
    }

    // For menu music (MIDI)
    public static void playMenuMusic() {
        try {
            if (menuMusic != null && menuMusic.isRunning()) {
                menuMusic.stop();
                menuMusic.close();
            }

            URL midiFile = SoundPlayer.class.getClassLoader().getResource("Music.mid");
            if (midiFile == null) {
                System.out.println("MIDI file not found");
                return;
            }

            menuMusic = MidiSystem.getSequencer();
            menuMusic.open();
            Sequence sequence = MidiSystem.getSequence(midiFile);
            menuMusic.setSequence(sequence);
            menuMusic.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
            menuMusic.start();
        } catch (Exception e) {
            System.out.println("Error playing menu music: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // For game music (WAV)
    public static void playGameMusic() {
        try {
            if (gameMusic != null && gameMusic.isActive()) {
                gameMusic.stop();
                gameMusic.close();
            }

            URL musicFile = SoundPlayer.class.getClassLoader().getResource("Music.wav");
            if (musicFile == null) {
                System.out.println("Game music file not found");
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(musicFile);
            gameMusic = AudioSystem.getClip();
            gameMusic.open(audioIn);
            gameMusic.loop(Clip.LOOP_CONTINUOUSLY);
            gameMusic.start();
        } catch (Exception e) {
            System.out.println("Error playing game music: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void stopMenuMusic() {
        if (menuMusic != null && menuMusic.isRunning()) {
            menuMusic.stop();
            menuMusic.close();
        }
    }

    public static void stopGameMusic() {
        if (gameMusic != null && gameMusic.isActive()) {
            gameMusic.stop();
            gameMusic.close();
        }
    }

    public static void stopAllMusic() {
        stopMenuMusic();
        stopGameMusic();
    }
}