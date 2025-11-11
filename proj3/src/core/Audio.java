package core;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Audio {
    public static final File teleportSound = new File("audio/teleport.wav");
    public static final File coinSound = new File("audio/coin.wav");
    public static final File winSound = new File("audio/win.wav");
    public static final File loseSound1 = new File ("audio/lose1.wav");
    public static final File loseSound2 = new File("audio/lose2.wav");
    public static final File challengeModeMusic = new File("audio/challengeMode.wav");
    public static final File normalMusice = new File("audio/normalMode.wav");
    public static final File toggleSound = new File("audio/toggle.wav");
    public static final File mainMenuMusic = new File("audio/mainMenu.wav");
    public static final File nightmareModeMusic = new File("audio/nightmareMode.wav");

    public static boolean muted = false;

    public static Clip currentMusic = null;



    /*
    main menu music: https://www.youtube.com/watch?v=xY8U9byTrzI
    challenge mode music: https://www.youtube.com/watch?v=LfC8Jcyv7I0
    normal mode music: https://www.youtube.com/watch?v=-oCpgNjYeVM
    toggle teleporters / LOS sound: https://www.youtube.com/watch?v=h8y0JMVwdmM
    teleport sound: https://www.youtube.com/watch?v=_dOnc_jJMMU
    coin sound: https://www.youtube.com/watch?v=mQSmVZU5EL4
    win/lose sound: https://soundbuttonsworld.com/
    nightmare mode: https://www.youtube.com/watch?v=zroFzv7sFis

     */
    // followed this guide to play music: https://www.youtube.com/watch?v=wJO_cq5XeSA

    public static void playSound(File file) {
        if (muted) {
            return;
        }

        try {
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();
        } catch (Exception e) {}
    }

    public static void playLongSound(File file) {
        if (muted) {
            return;
        }

        try {
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(file);
            currentMusic = AudioSystem.getClip();
            currentMusic.open(audioInput);
            currentMusic.start();
            currentMusic.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (Exception e) {}
    }
    public static void stopMusic() {
        try {
            if (currentMusic != null && currentMusic.isRunning()) {
                currentMusic.stop();
                currentMusic.close();
                currentMusic = null;
            }
        } catch (Exception e) {}
    }

}
