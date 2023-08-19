import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FuelCan extends Collider {
    private int _x;
    private int _y;
    private int _width;
    private int _height;

    public FuelCan(int x, int y, int width, int height) throws IOException {
        super(x, y, width, height);

        _x = x;
        _y = y;
        _width = width;
        _height = height;
    }

    public int getX() { return _x; }
    public int getY() { return _y; }
    public void setX(int x) { _x = x; }
    public void setY(int y) { _y = y; }
    public int getWidth() { return _width; }
    public int getHeight() { return _height; }

    public void print() {
        System.out.printf("FuelCan at position: %d, %d, scale: %dx%d\n", _x, _y, _width, _height);
    }

    public void update() {
        super.update(_x, _y);
    }

    public boolean collidesAny(ArrayList<Rock> rocks) {
        for(Rock rock : rocks) {
            if(this.collides(rock)) {
                return true;
            }
        }
        return false;
    }
}
