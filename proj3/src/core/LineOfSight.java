package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.Arrays;

public class LineOfSight {

    public static TETile[][] apply(TETile[][] world, int avatarX, int avatarY, boolean los, int radius) {
        if (!los) {
            return world;
        }
        int width = world.length;
        int height = world[0].length;

        TETile[][] fogOfWar = new TETile[width][height];
        for (TETile[] ar : fogOfWar) { Arrays.fill(ar, Tileset.NOTHING); }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (inBounds(x, y, avatarX, avatarY, radius)) {
                    fogOfWar[x][y] = world[x][y];
                }
            }
        }
        return fogOfWar;
    }

    // I used Google to learn the Manhattan distance formula because specs suggests a diamond shaped line of sight:
    // credit: https://chris3606.github.io/GoRogue/articles/grid_components/measuring-distance.html paragraph 3
    public static boolean inBounds(int x, int y, int avatarX, int avatarY, int radius) {
        return Math.abs(x - avatarX) + Math.abs(y - avatarY) <= radius;
    }

}
