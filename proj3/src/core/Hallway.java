package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.*;

public class Hallway {

    public static void connectRooms(Room a, Room b, TETile[][] world, Random random) {
        int aCenterX = a.centerX;
        int aCenterY = a.centerY;
        int bCenterX = b.centerX;
        int bCenterY = b.centerY;
        /*
        made a randomizer to basically randomize whether we would start drawing horizontally or veritcally
         */
        int choice = random.nextInt(2);

        if (choice == 0) {
            drawHorizontalHallway(world, aCenterX, bCenterX, aCenterY);
            drawVerticalHallway(world, bCenterX, aCenterY, bCenterY);
        } else {
            drawVerticalHallway(world, aCenterX, aCenterY, bCenterY);
            drawHorizontalHallway(world, aCenterX, bCenterX, bCenterY);
        }
    }

    /*
    There's gonna be a lot of overlapping where we loop through tiles that are already FLOORS.
    Change this if its gonna cause a problem.
     */

    private static void drawHorizontalHallway(TETile[][] world, int x1, int x2, int y) {
        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
            if (world[x][y] == Tileset.NOTHING || world[x][y] == Tileset.WALL) {
                world[x][y] = Tileset.FLOOR;
            }
        }
    }

    private static void drawVerticalHallway(TETile[][] world, int x, int y1, int y2) {
        for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
            if (world[x][y] == Tileset.NOTHING || world[x][y] == Tileset.WALL) {
                world[x][y] = Tileset.FLOOR;
            }
        }
    }
}





//  i will keep this hear just in case my  idea doesnt work
//    public void generateHallways() {
//        List<Room> rooms = new ArrayList<>();
//        rooms = this.world.getRoom();
//
//        // loop thru the peremeter of each "room"
//        for (Room r : rooms) {
//            for (int x = r.x; x < r.x + r.width; x++) {
//                for (int y = r.y; y < r.y + r.height; y++) {
//                    if (x == r.x + 1 || x == r.x + r.width - 1 || y == r.y + 1 || y == r.y + r.height - 1) {
//                        if (worldGrid[r.x][r.y + 1] == Tileset.FLOOR) {
//                            drawLine("down", r.x, r.y);
//                        }
//                        if (worldGrid[r.x][r.y - 1] == Tileset.FLOOR) {
//                            drawLine("up", r.x, r.y);
//                        }
//                        if (worldGrid[r.x + 1][r.y] == Tileset.FLOOR) {
//                            drawLine("left", r.x, r.y);
//                        }
//                        if (worldGrid[r.x - 1][r.y] == Tileset.FLOOR) {
//                            drawLine("right", r.x, r.y);
//                        }
//                    }
//                }
//            }
//        }
//
//
//    }
//    public void drawLine(String direction, int x, int y) {
//        if (direction.equals("down")) {
//            for (int i = y; i > 0; i--) {
//                if (worldGrid[x][i] == Tileset.WALL || i == 1) {
//                    break;
//                }
//                worldGrid[x][i] = Tileset.FLOOR;
//            }
//        }
////    }
//
//    public void drawTestLine() {
//        List<Room> room = world.getRoom();
//        System.out.println(room.size());
//        Room r = room.get(0);
//        for (int i = r.y; i > 2; i--) {
//            worldGrid[r.x + r.width][i] = Tileset.FLOOR;
//        }
//    }



