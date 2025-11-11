package core;

public class Room {
    public int x;
    public int y;
    public int width;
    public int height;
    public int centerX;
    public int centerY;

    public Room (int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.centerX = x + (width / 2);
        this.centerY = y + (height / 2);
    }
}
