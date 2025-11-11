package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.*;




public class World {
    private final int width;
    private final int height;
    private final TETile[][] world;
    private final ArrayList<Room> rooms;
    private final Random random;
    private final Set<List<Integer>> positionSet;
    private int avatarX;
    private int avatarY;
    private TETile aang; //create an Avatar class if we are gonna do more than just move it
    private int coinsCollected;
    private int totalCoins;
    private ArrayList<List<Integer>> teleporterPositions;
    private boolean enableTeleporters;

    public World (long seed, int width, int height) {
        this.width = width;
        this.height = height;
        this.world = new TETile[width][height];
        this.rooms = new ArrayList<>();
        this.random = new Random(seed);
        this.positionSet = new HashSet<>();
        this.aang = Tileset.AVATAR;
        this.coinsCollected = 0;
        this.teleporterPositions = new ArrayList<>();
        this.enableTeleporters = true;

        makeEmptyWorld();
        makeRooms();
        makeHallways();
        makeWalls();
        makeDude();
        makeTeleporters();
        toggleTeleporters();
        scatterCoins();
    }

    public void makeDude() {
        int randomRoomNum = this.random.nextInt(0, rooms.size());
        this.avatarX = rooms.get(randomRoomNum).centerX;
        this.avatarY = rooms.get(randomRoomNum).centerY;
        world[avatarX][avatarY] = aang;
    }

    public void moveDude(char key) {
        key = Character.toLowerCase(key);
        int newX = avatarX;
        int newY = avatarY;
        TETile currAvatar = aang;

        if (key == 'w') {
            newY++;
            currAvatar = Tileset.AVATAR_UP;
        }
        if (key == 'a') {
            newX--;
            currAvatar = Tileset.AVATAR_LEFT;
        }
        if (key == 's') {
            newY--;
            currAvatar = Tileset.AVATAR_DOWN;
        }
        if (key == 'd') {
            newX++;
            currAvatar = Tileset.AVATAR;
        }

        if (world[newX][newY] != Tileset.WALL) {
            if (world[newX][newY] == Tileset.COIN) {
                coinsCollected++;
                Audio.playSound(Audio.coinSound);
            }

            world[avatarX][avatarY] = Tileset.FLOOR;

            avatarX = newX;
            avatarY = newY;

            if (world[avatarX][avatarY] == Tileset.TELEPORTER) {

                // https://www.youtube.com/watch?v=IwC5T5gtFI4
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        teleportNextRoom();
                    }
                };
                timer.schedule(task, 750);
            }

            teleporterVisiblity();
            aang = currAvatar;
            world[avatarX][avatarY] = aang;
        }
    }

    public void makeEmptyWorld() {
        for (TETile[] ar : world) {
            Arrays.fill(ar, Tileset.NOTHING);
        }
    }

    public void makeRooms() {
        for (int i = 0; i < 80 + random.nextInt(80); i++) {
            generateRoom();
        }
    }

    public void generateRoom() {
        int minRoomSize = average(width, height) / 10;
        int maxRoomSize = average(width, height) / 4;

        int roomWidth = minRoomSize + random.nextInt(maxRoomSize - minRoomSize + 1);
        int roomHeight = minRoomSize + random.nextInt(maxRoomSize - minRoomSize + 1);

        int startX = 1 + random.nextInt(width - roomWidth - 1);
        int startY = 1 + random.nextInt(height - roomHeight - 1);


        /*
        im checcking very tile that is one tile larger in every direction aroudn the room direction and if
        any of those tiles already exist in positionMAP then its another room so we just return and
        make another room.
         */
        for (int x = startX - 1; x < startX + roomWidth + 2; x++) {
            for (int y = startY - 1; y < startY + roomHeight + 2; y++) {
                ArrayList<Integer> coordinate = new ArrayList<>(List.of(x, y));
                if (positionSet.contains(coordinate)) {
                    return; //quit out of generating this room so it doesn't overlap another room
                }
            }
        }


        //This double for loop actually builds the room and adds the coordinates into position map
        for (int x = startX; x < startX + roomWidth; x++) {
            for (int y = startY; y < startY + roomHeight; y++) {
                ArrayList<Integer> coordinate = new ArrayList<>(List.of(x, y));
                world[x][y] = Tileset.FLOOR;
                positionSet.add(coordinate);
            }
        }

        /*
         my idea here is that once the rooms are created, we go thru
        the peremeter of the room and replace the floors with walls.
         */
        for (int x = startX; x < startX + roomWidth; x++) {
            for (int y = startY; y < startY + roomHeight; y++) {
                if (isBorder(x, y, startX, startY, roomWidth, roomHeight)) {
                    world[x][y] = Tileset.WALL;
                }
            }
        }

        rooms.add(new Room(startX, startY, roomWidth, roomHeight));

    }

    public void scatterCoins() {
        for (Room room : rooms) {
            for (int i = 0; i < random.nextInt(4); i++) {
                int x = random.nextInt(room.x + 1, room.x + room.width - 1);
                int y = random.nextInt(room.y + 1, room.y + room.height - 1);
                if (world[x][y] == Tileset.FLOOR && !teleporterPositions.contains(List.of(x, y)) && world[x][y] != Tileset.TELEPORTER) {
                    world[x][y] = Tileset.COIN;
                    totalCoins++;
                }
            }
        }
    }

    private void makeHallways() {
        for (int i = 0; i < rooms.size() - 1; i++) {
            Room a = rooms.get(i);
            Room b = rooms.get(i + 1);

//            System.out.println("Connected Room " + i + " (" + a.centerX + ", " + a.centerY + ") to Room " + (i+1) + " (" + b.centerX + ", " + b.centerY + ")");
            Hallway.connectRooms(a, b, this.getWorld(), this.random);
        }
    }

    public TETile[][] getWorld() {return world;}

    public ArrayList<Room> getRoom() {return rooms;}

    public int avatarX() {return avatarX;}

    public int avatarY() {return avatarY;}

    public int getCoinsCollected() {return coinsCollected;}

    public  int getTotalCoins() {return totalCoins ;}

    private int average(int a, int b) {return (a + b) / 2;}

    public void makeTeleporters() {
        for (Room room : rooms) {
            ArrayList<List<Integer>> outerBorderCoordinates = new ArrayList<>();
            for (int x = room.x + 1; x < room.x + room.width - 1; x++) {
                for (int y = room.y + 1; y < room.y + room.height - 1; y++) {
                    if (world[x][y] == Tileset.FLOOR && isOutermostFloor(x, y, room.x, room.y, room.width, room.height)) {
                        outerBorderCoordinates.add(List.of(x, y));
                    }
                }
            }
            List<Integer> teleporterSpot = outerBorderCoordinates.get(random.nextInt(outerBorderCoordinates.size()));
            world[teleporterSpot.get(0)][teleporterSpot.get(1)] = Tileset.TELEPORTER;
            teleporterPositions.add(teleporterSpot);
        }
    }

    public void teleportNextRoom() {
        for (int i = 0; i < teleporterPositions.size(); i++) {
            List<Integer> teleportPosition = teleporterPositions.get(i);
            if (avatarX == teleportPosition.get(0) && avatarY == teleportPosition.get(1)) {
                int nextIndex;

                if (i + 1 < teleporterPositions.size()) {
                    nextIndex = i + 1;
                } else {
                    nextIndex = 0;
                }

                List<Integer> nextTeleportPosition = teleporterPositions.get(nextIndex);
                avatarX = nextTeleportPosition.get(0);
                avatarY = nextTeleportPosition.get(1);
                world[avatarX][avatarY] = aang;
                world[teleportPosition.get(0)][teleportPosition.get(1)] = Tileset.TELEPORTER;
                Audio.playSound(Audio.teleportSound);
                break;
            }
        }
    }

    public void teleporterVisiblity() {
        for (List<Integer> teleporter : teleporterPositions) {
            int x = teleporter.get(0);
            int y = teleporter.get(1);
            if (enableTeleporters) {
                world[x][y] = Tileset.TELEPORTER;
            } else {
                world[x][y] = Tileset.FLOOR;
            }
        }
    }

    public void toggleTeleporters() {
        enableTeleporters = !enableTeleporters;
        teleporterVisiblity();
    }

    public void setEnableTeleporters() {enableTeleporters = true;} //this is sole for the purpose of insane mode

    public void makeWalls() {
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                if (world[x][y] == Tileset.NOTHING && isAdjacentToFloor(x, y)) {
                        world[x][y] = Tileset.WALL;
                }
            }
        }
    }

    private boolean isAdjacentToFloor(int x, int y) {
        return world[x + 1][y] == Tileset.FLOOR ||
                world[x - 1][y] == Tileset.FLOOR ||
                world[x][y + 1] == Tileset.FLOOR ||
                world[x][y - 1] == Tileset.FLOOR;
    }

    private boolean isBorder(int x, int y, int startX, int startY, int roomWidth, int roomHeight) {
        return x == startX || x == startX + roomWidth - 1 || y == startY || y == startY + roomHeight - 1;
    }

    private boolean isOutermostFloor(int x, int y, int startX, int startY, int roomWidth, int roomHeight) {
        return x == startX + 1|| x == startX + roomWidth - 2 || y == startY + 1 || y == startY + roomHeight - 2;
    }


}
