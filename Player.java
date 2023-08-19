import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player extends Collider {
    private int _x = -1;
    private int _y = -1;
    private double _dx = 0;
    private double _dy = 0;
    private int _width;
    private int _height;
    private int _fuelAmount;
    private int _spawnX;
    private int _spawnY;
    private BufferedImage _leftTexture;
    private BufferedImage _rightTexture;
    private BufferedImage _forwardTexture;
    private BufferedImage _thrustTexture;
    private int _livesRemaining;
    private boolean _jetpackOn = false;

    private int GRAVITY_FORCE = 5;
    private int JETPACK_FORCE = -15;
    private int MAX_JETPACK_SPEED = -120;
    private int MAX_DOWN_SPEED = 200;
    private int START_FUEL = 512;
    private int START_LIVES = 5;
    private double EXTRA_LIFE_CUTOFF = 2;
    
    public Player(int spawnX, int spawnY, int width, int height) throws IOException {
        super(spawnX, spawnY, width, height);

        _spawnX = spawnX;
        _spawnY = spawnY;
        _width = width;
        _height = height;

        _leftTexture = ImageIO.read(new File("./assets/textures/ship-left.png"));
        _rightTexture = ImageIO.read(new File("./assets/textures/ship-right.png"));
        _forwardTexture = ImageIO.read(new File("./assets/textures/ship-forward.png"));
        _thrustTexture = ImageIO.read(new File("./assets/textures/thrust.png"));
    }
    
    public int getX() { return _x; }
    public int getY() { return _y; }
    public int getWidth() { return _width; }
    public int getHeight() { return _height; }
    public int getSpawnX() { return _spawnX; }
    public int getSpawnY() { return _spawnY; }
    public boolean getJetpackStatus() { return _jetpackOn; }
    public int getFuelAmount() { return _fuelAmount; }
    public int getLivesRemaining() { return _livesRemaining; }

    public BufferedImage getTexture() {
        if(_dx == 0) {
            return _forwardTexture;
        }
        return (_dx > 0) ? _rightTexture : _leftTexture;
    }

    public BufferedImage getThrustTexture() { return _thrustTexture; }

    public void setDX(double dx) { _dx = dx; }
    public void setDY(double dy) { _dy = dy; }
    public void setSpawnX(int x) { _spawnX = x; }
    public void setSpawnY(int y) { _spawnY = y; }
    public void setJetpackOn(boolean b) { _jetpackOn = b; }

    public void teleport(int x, int y) {
        _x = x;
        _y = y;
    }

    public void spawn() {
        _dx = 0;
        _dy = 0;
        _x = _spawnX;
        _y = _spawnY;
        _fuelAmount = START_FUEL;
        _livesRemaining = START_LIVES;
    }


    public void update(double dt) {
        int hypotheticalX = (int)((double)_x + _dx * dt);

        double hypotheticalDY = _dy;
        hypotheticalDY += 5; // GRAVITY

        if(_jetpackOn && hypotheticalDY > -60) {
            hypotheticalDY += JETPACK_FORCE;
        }

        if(hypotheticalDY < MAX_DOWN_SPEED) {
            hypotheticalDY += GRAVITY_FORCE;
        }

        int hypotheticalY = (int)((double)_y + hypotheticalDY * dt);

        super.update(hypotheticalX, hypotheticalY);

        boolean jetpackAllowed = true;

        for(Rock rock : GameAreaPanel.getRoom().getElementsAsArrayList()) {
            if(this.collides(rock)) {
                _livesRemaining--;
                teleport(GameAreaPanel.getRoom().getSpawnX(), GameAreaPanel.getRoom().getSpawnY());
                _dy = 0;
            }
        }

        if(GameAreaPanel.getRoom().hasFuelCan()) {
            if(this.collides(GameAreaPanel.getRoom().getFuelCan())) {
                _fuelAmount = START_FUEL;
                if(_livesRemaining <= EXTRA_LIFE_CUTOFF) {
                    _livesRemaining++;
                }
                GameAreaPanel.getRoom().removeFuelCan();
            }
        }

        if(_jetpackOn  && _dy > MAX_JETPACK_SPEED) {
            if(jetpackAllowed) {
                _dy += JETPACK_FORCE;
            }
            _fuelAmount -= 1;
        }

        _x = (int)((double)_x + _dx * dt);
        _y = (int)((double)_y + _dy * dt);

        if(_dy < MAX_DOWN_SPEED) {
            _dy += GRAVITY_FORCE;
        }

        super.update(_x, _y);
    }
}