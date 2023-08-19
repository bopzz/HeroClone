import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Rock extends Collider {
    private int _x;
    private int _y;
    private int _width;
    private int _height;

    public Rock(int x, int y, int width, int height) throws IOException {
        super(x, y, width, height);

        _x = x;
        _y = y;
        _width = width;
        _height = height;

    }

    public int getX() { return _x; }
    public int getY() { return _y; }
    public int getWidth() { return _width; }
    public int getHeight() { return _height; }

    public void print() {
        System.out.printf("Rock at position: %d, %d, scale: %dx%d\n", _x, _y, _width, _height);
    }
}
