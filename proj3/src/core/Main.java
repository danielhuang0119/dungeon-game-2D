package core;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import tileengine.TETile;
import tileengine.TERenderer;

import java.awt.*;

// https://docs.oracle.com/javase/8/docs/api/java/io/FileWriter.html READ READ READ
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Main {


    public static String inputHistory = "";
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;
    public static boolean toggleLOS = false;
    private static int timeLeft;

    public static void main(String[] args) {

        Audio.playLongSound(Audio.mainMenuMusic);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.enableDoubleBuffering();
        mainMenu();
        StdDraw.enableDoubleBuffering();
        StdDraw.enableDoubleBuffering();
        StdDraw.enableDoubleBuffering();
        StdDraw.enableDoubleBuffering();
        StdDraw.enableDoubleBuffering();





        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = Character.toLowerCase(StdDraw.nextKeyTyped());

                if (key == 'n') {
                    inputHistory += 'n';
                    long seed = getSeed();
                    World newWorld = new World(seed, WIDTH, HEIGHT);

//                    Audio.stopMusic();
                    runGameLoop(newWorld);
                }
                if (key == 'l') {
//                    Audio.stopMusic();
                    String saveFile = loadSaved();
                    runSavedWorld(saveFile);
                }
                if (key == 'q') {
                    System.exit(0);
                }
            }

            // checking for main menu mouse clicks. this is my secondary ambition feature
            if (StdDraw.isMousePressed()) {
                int mouseX = (int) StdDraw.mouseX();
                int mouseY = (int) StdDraw.mouseY();

                /*
                the checking mouse conditions is hardcoded but if we need to we
                can change it later to be an actual method
                 */
                if (mouseX >= 16 && mouseX <= 34 && mouseY >= 28 && mouseY <= 32) {
                    inputHistory += 'n';
                    long seed = getSeed();
                    World newWorld = new World(seed, WIDTH, HEIGHT);

//                    Audio.stopMusic();
                    runGameLoop(newWorld);
                }
                if (mouseX >= 16 && mouseX <= 34 && mouseY >= 24 && mouseY <= 28) {
//                    Audio.stopMusic();
                    String saveFile = loadSaved();
                    runSavedWorld(saveFile);
                }
                if (mouseX >= 16 && mouseX <= 34 && mouseY >= 20 && mouseY <= 24) {
                    System.exit(0);
                }
            }


        }

    }


    public static void displayHUD(int width, int height, World world,  boolean insaneMode) {
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();
        if (x >= 0 && x < width && y >= 0 && y < height) {
            if (!insaneMode) {
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
                StdDraw.textRight(width - 1, height - 1, "Coins collected: " + world.getCoinsCollected() + "/" + world.getTotalCoins());
                StdDraw.text(width / 2, height - 1, "You are at (" + world.avatarX() + ", " + world.avatarY() + ")");

                StdDraw.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
                StdDraw.textLeft(1, height - 1, "[L] Toggle Line of Sight");
                StdDraw.textLeft(1, height - 2.5, "[T] Toggle Teleporters");
                StdDraw.textLeft(1, height - 4, "[I] Toggle INSANE MODE");
                StdDraw.textLeft(1, height - 5.5, "[N] Toggle NIGHTMARE MODE");
                StdDraw.textLeft(1, height - 6.7, "    (strobing effect)");


                StdDraw.setPenColor(Color.ORANGE);
                StdDraw.text(x, y - 1.5, world.getWorld()[x][y].description().toUpperCase() +
                        " : (" + x + ", " + y + ")");
            } else if (timeLeft >= 0 && insaneMode) {
                StdDraw.setPenColor(Color.RED);
                StdDraw.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
                StdDraw.text(WIDTH / 2, 3, "TIME LEFT: " + timeLeft + "s");
                StdDraw.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
                StdDraw.text(WIDTH / 2, 5.5, "COINS COLLECTED: " + world.getCoinsCollected() + "/" + world.getTotalCoins());

            }
            StdDraw.show();
        }

    }


    //  simply displays the main menu. Maybe refactor if we need more displaying??
    public static void mainMenu() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);

        StdDraw.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
        StdDraw.text(25, 40, "2D Dungeon Game");

        StdDraw.setFont(new Font("Comic Sans MS", Font.BOLD, 25));
        StdDraw.text(25, 30, "(N) New Game");
        StdDraw.text(25, 26, "(L) Load Game");
        StdDraw.text(25, 22, "(Q) Quit Game");



        StdDraw.show();
    }

    public static long getSeed() {
        String seedInput = "";

        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);

        StdDraw.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        StdDraw.text(25, 30, "Enter seed followed by S.");

        StdDraw.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
        StdDraw.text(25, 40, "CS61B: BYOW");

        StdDraw.show();

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (key == 's') {
                    if (!seedInput.isEmpty()) {
                        inputHistory += seedInput + "s";
                        try {
                            return Long.parseLong(seedInput);
                        } catch (NumberFormatException e) {
                            long randomSeeed = (int) (Math.random() * 999999);
                            inputHistory = "n" + randomSeeed + "s";
                            return randomSeeed;
                        }
                    }
                } else if (Character.isDigit(key)) {
                    seedInput += key;
                    StdDraw.clear(Color.BLACK);
                    StdDraw.setPenColor(Color.WHITE);

                    StdDraw.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
                    StdDraw.text(25, 40, "CS61B: BYOW");

                    StdDraw.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
                    StdDraw.text(25, 30, "Enter seed followed by S.");

                    StdDraw.setPenColor(Color.YELLOW);
                    StdDraw.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
                    StdDraw.text(25, 27, seedInput);

                    StdDraw.show();
                }
                if (key == 'q') {
                    System.exit(0);
                }
            }
        }


    }

    // maybe we should  create another class for the saving and loading functionality
    public static void checkForSave() {
        if (inputHistory.length() >= 2) {
            if (inputHistory.charAt(inputHistory.length() - 1 ) == 'q' && inputHistory.charAt(inputHistory.length() - 2) == ':') {
                saveGame();
                System.exit(0);
            }
        }
    }

    public static void saveGame() {
        /*
        I used ChatGPT here to figure out how to create and write into a text file.
        The prompt I used was "how do i create a text file into my project
        directory and input a string."
         */
        try {
            FileWriter fileWriter = new FileWriter("save.txt");
            fileWriter.write(inputHistory);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }
    }

    public static String loadSaved() {
        // if we run into issues maybe we can use the Java scanner but im using In for now since we are already famiilar with
        In reader = new In("save.txt");
        return reader.readAll().toLowerCase();
    }

    public static void runSavedWorld(String inputHistory) {
        long seed = Long.parseLong(inputHistory.substring(1, inputHistory.indexOf('s')));
        int userInputStartIndex = inputHistory.indexOf('s') + 1;
        World savedWorld = new World(seed, WIDTH, HEIGHT);
        Audio.muted = true;

        // copy all the moves from the save file
        for (int i = userInputStartIndex; i < inputHistory.length(); i++) {
            char direction = inputHistory.charAt(i);
            savedWorld.moveDude(direction);
        }
        Audio.muted = false;
        Main.inputHistory = inputHistory;
        runGameLoop(savedWorld);

    }

    public static void runGameLoop(World world) {
        Audio.stopMusic();
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        Audio.playLongSound(Audio.normalMusice);

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char direction = Character.toLowerCase(StdDraw.nextKeyTyped());
                inputHistory += direction;
                checkForSave();
//                System.out.println(inputHistory);
                world.moveDude(direction);

                if (direction == 'l') {
                    Audio.playSound(Audio.toggleSound);
                    toggleLOS = !toggleLOS;
                }
                if (direction == 't') {
                    Audio.playSound(Audio.toggleSound);
                    world.toggleTeleporters();
                }

                if (direction == 'i' && world.getCoinsCollected() < world.getTotalCoins()) {
                    Audio.stopMusic();
                    Audio.playLongSound(Audio.challengeModeMusic);
                    enableInsaneMode(world);
                }

                if (direction == 'n' && world.getCoinsCollected() < world.getTotalCoins()) {
                    Audio.stopMusic();
                    Audio.playLongSound(Audio.nightmareModeMusic);
                    enableNightmareMode(world);
                }


            }
            TETile[][] losWorld = LineOfSight.apply(world.getWorld(), world.avatarX(), world.avatarY(), toggleLOS, 4);

            /*
            Got help at office hours, and she told me to take the body code from renderFrame(TETile[][] world) from
            TERenderer.java but exclude the call to StdDraw.show().
            This fixed flickering issue! Yay!
             */
            StdDraw.clear(new Color(0, 0, 0));
            ter.drawTiles(losWorld);
            displayHUD(WIDTH, HEIGHT, world, false);
        }
    }

    public static void enableInsaneMode(World world) {

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // used this video to guide me through how to set up a countdown: https://www.youtube.com/watch?v=IwC5T5gtFI4
        int totalCoins = world.getTotalCoins();
        world.setEnableTeleporters();

        timeLeft = 30;
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                timeLeft --;
                if (timeLeft < 0) {
                    timer.cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);

        while (timeLeft >= 0 && world.getCoinsCollected() < totalCoins) {
            // copy pasted from runGameLoop()
            if (StdDraw.hasNextKeyTyped()) {
                char direction = Character.toLowerCase(StdDraw.nextKeyTyped());
                inputHistory += direction;
//                checkForSave();
                world.moveDude(direction);

//                if (direction == 'l') {toggleLOS = !toggleLOS;}


            }
            TETile[][] losWorld = LineOfSight.apply(world.getWorld(), world.avatarX(), world.avatarY(), true, 5);
            StdDraw.clear(new Color(0, 0, 0));
            ter.drawTiles(losWorld);

            displayHUD(WIDTH, HEIGHT, world,  true);


        }
        endScreen(world);
    }

    // exactly the same as Insane Mode but with intentionally added flickering
    public static void enableNightmareMode(World world) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // used this video to guide me through how to set up a countdown: https://www.youtube.com/watch?v=IwC5T5gtFI4
        int totalCoins = world.getTotalCoins();
        world.setEnableTeleporters();

        timeLeft = 30;
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                timeLeft --;
                if (timeLeft < 0) {
                    timer.cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);

        while (timeLeft >= 0 && world.getCoinsCollected() < totalCoins) {
            // copy pasted from runGameLoop()
            if (StdDraw.hasNextKeyTyped()) {
                char direction = Character.toLowerCase(StdDraw.nextKeyTyped());
                inputHistory += direction;
//                checkForSave();
                world.moveDude(direction);

//                if (direction == 'l') {toggleLOS = !toggleLOS;}


            }
            TETile[][] losWorld = LineOfSight.apply(world.getWorld(), world.avatarX(), world.avatarY(), true, 4);
            StdDraw.clear(new Color(0, 0, 0));
            ter.drawTiles(losWorld);


            if (Math.random() < 0.10) {
                StdDraw.filledRectangle(WIDTH / 2.0, HEIGHT / 2.0, WIDTH, HEIGHT);
                StdDraw.show();
                StdDraw.pause(125);
            }
            displayHUD(WIDTH, HEIGHT, world,  true);


        }
        endScreen(world);
    }

    public static void endScreen(World world) {
        Audio.stopMusic();
        StdDraw.clear(Color.BLACK);

        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Comic Sans MS", Font.BOLD, 30));

        if (world.getCoinsCollected() >= world.getTotalCoins()) {

            Timer timer = new Timer();
            // FIVE BIG BOOMS!!!
            for (int i = 0; i < 5; i++) {
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        Audio.playSound(Audio.winSound);
                    }
                };
                timer.schedule(task, i * 650);
            }

            StdDraw.picture(WIDTH/2, HEIGHT/2 - 7 , "images/win.png");
            StdDraw.text(WIDTH/2, HEIGHT/2 + 20, "YOU WIN. FIVE BIG BOOMS!");
            StdDraw.text(WIDTH/2, HEIGHT/2 + 15, "BOOM! BOOM! BOOM! BOOM! BOOM!");
            StdDraw.setFont(new Font("Comic Sans MS", Font.BOLD, 17));
            StdDraw.text(WIDTH/2, HEIGHT/2 + 10,  "(" + world.getCoinsCollected() + "/" + world.getTotalCoins() + " coins collected)");
        } else {
            Audio.playSound(Audio.loseSound1);
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Audio.playSound(Audio.loseSound2);
                }
            };
            timer.schedule(task, 2000);

            StdDraw.picture(WIDTH/2, HEIGHT/2 - 8 , "images/lose.png");
            StdDraw.text(WIDTH/2, HEIGHT/2 + 7, "YOU LOSE!");
            StdDraw.setFont(new Font("Comic Sans MS", Font.BOLD, 17));

            StdDraw.text(WIDTH/2, HEIGHT/2 + 3,  "(" + world.getCoinsCollected() + "/" + world.getTotalCoins() + " coins collected)");

        }
        StdDraw.show();
        StdDraw.pause(5000);
        Audio.playLongSound(Audio.normalMusice);

    }

}
