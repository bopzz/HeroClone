import java.io.IOException;
import java.time.chrono.IsoChronology;
import java.util.ArrayList;
import java.util.Random;

public class Room {

    private ArrayList<Rock> _top = new ArrayList<Rock>();
    private ArrayList<Rock> _mid = new ArrayList<Rock>();
    private ArrayList<Rock> _bot = new ArrayList<Rock>();

    private double HAS_FUEL_CHANCE = 1;
    private boolean _hasFuelCan = false;
    private FuelCan _fuelCan;

    private int _spawnX;
    private int _spawnY;

    public Room() throws IOException {
        _spawnX = 480;
        _spawnY = 300;

        // Start Room
        _top.add(new Rock(0, 0, 960, 175));
        _bot.add(new Rock(0, 350, 320, 175));
        _bot.add(new Rock(640, 350, 320, 175));
        _mid.add(new Rock(0, 175, 75, 175));
        _mid.add(new Rock(885, 175, 75, 175));
    }
    
    public Room(Room lastRoom, int spawnX, int spawnY) throws IOException {
        this();
        
        _spawnX = spawnX;
        _spawnY = spawnY;
        
        _top.clear();
        _mid.clear();
        _bot.clear();

        
        Random r = new Random();

        _hasFuelCan = r.nextDouble() <= HAS_FUEL_CHANCE;
        
        generateRoom(lastRoom._bot, r);

        if(_hasFuelCan) {
            _fuelCan = new FuelCan(r.nextInt(960), 175 + r.nextInt(145), 32, 32);
            while(_fuelCan.collidesAny(_mid)) {
                _fuelCan.setX(r.nextInt(960));
                _fuelCan.update();
            }
        }
    }

    public int getSpawnX() { return _spawnX; }
    public int getSpawnY() { return _spawnY; }
    public FuelCan getFuelCan() { return _fuelCan; }
    public boolean hasFuelCan() { return _hasFuelCan; }

    public void removeFuelCan() {
        _hasFuelCan = false;
        _fuelCan = null;
    }
    
    private void generateRoom(ArrayList<Rock> lastBot, Random r) throws IOException {
        for(Rock c : lastBot) {
            _top.add(new Rock(c.getX(), 0, c.getWidth(), 175));
        }

        int leftBlock = r.nextInt(4)+1;
        int rightBlock = r.nextInt(4)+1;
        _mid.add(new Rock(0,175, 48*leftBlock, 175));
        _mid.add(new Rock(960 - 48*rightBlock,175, 48*rightBlock, 175));
        // do {
        int midBlockWidth = r.nextInt(4);
        // } while ();
        int midBlock = r.nextInt(20 - leftBlock - 3 - rightBlock -3 )+leftBlock+3;
        _mid.add(new Rock(midBlock*48,175,midBlockWidth*48 , 175));
    

        int holeWidth = r.nextInt(5)+5;
        int botLeftBlock = r.nextInt(10 - holeWidth)+4;
        int botRightBlock = 20 - botLeftBlock - holeWidth;
        _bot.add(new Rock(0,350,botLeftBlock*48, 350));
        _bot.add(new Rock(960 - botRightBlock*48,350, botRightBlock*48, 350));
        if (holeWidth >= 9){
        int botObstacleWidth = r.nextInt(4);
        int botObstacle = r.nextInt(botLeftBlock + holeWidth - 4) + botLeftBlock + 2;
        _bot.add(new Rock(botObstacle*48,350,botObstacleWidth*48, 350)); 
        }

    }

    public ArrayList<Rock> getElementsAsArrayList() {
        ArrayList<Rock> output = new ArrayList<Rock>();

        for(Rock c : _top) {
            output.add(c);
        }
        for(Rock c : _mid) {
            output.add(c);
        }
        for(Rock c : _bot) {
            output.add(c);
        }
        return output;
    }
}
