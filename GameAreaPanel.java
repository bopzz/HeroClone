import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;
import java.util.HashMap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class GameAreaPanel extends JPanel implements KeyListener {
    private Player _player;
    private ArrayList<Integer> _directionalKeysPressed = new ArrayList<Integer>();
    private boolean _spaceKeyPressed = false;
    private Map<Integer, Boolean> _generalKeysPressed = new HashMap<Integer, Boolean>();
    private static ArrayList<Rock> _rockArray = new ArrayList<Rock>(); 

    private static ArrayList<Room> _rooms = new ArrayList<Room>();
    private static int _currentRoomIndex = 0;
    
    private BufferedImage _backgroundTexture;
    private static BufferedImage _rockTexture;
    private BufferedImage _fuelCanTexture;

    
    private int _gameState;

    private int[] INITIAL_SPAWN = new int[]{480, 300};

    public GameAreaPanel(int w, int h) {
        this.setPreferredSize(new Dimension(w, h));
        this.setBackground(Color.gray);

        try {
            _player = new Player(INITIAL_SPAWN[0], INITIAL_SPAWN[1], 32, 32);
        } catch (Exception ex)  {
            System.out.println("error: missing image files for ships");
            System.exit(1);
        }

        try {
            _backgroundTexture = ImageIO.read(new File("./assets/textures/background.png"));
        } catch(Exception ex) {
            System.out.println("error loading background texture: " + ex);
        }

        try {
            _rockTexture = ImageIO.read(new File("./assets/textures/rock.png"));
            _fuelCanTexture = ImageIO.read(new File("./assets/textures/fuel.png"));
        } catch(Exception ex) {
            System.out.println("error loading background texture: " + ex);
        }
    }

    public int getGameState() { return _gameState; }
    public void setGameState(int gameState) { _gameState = gameState; }

    public static int getScore() { return _rooms.size() - 1; }

    public static Room getRoom() { return _rooms.get(_currentRoomIndex); }

    public int getFuelAmount() { return _player.getFuelAmount(); }
    public int getLivesRemaining() { return _player.getLivesRemaining(); }

    public void resetGame() {
        if(_rooms.size() != 1) {
            _rooms.clear();
            _currentRoomIndex = 0;
            try {
                _rooms.add(new Room());
                _rockArray = _rooms.get(_currentRoomIndex).getElementsAsArrayList();
            } catch(Exception ex) {
                System.out.println("error loading rooms: " + ex);
            }
        }
        _directionalKeysPressed.clear();
        _spaceKeyPressed = false;
        _generalKeysPressed.clear();
        _player.setSpawnX(INITIAL_SPAWN[0]);
        _player.setSpawnY(INITIAL_SPAWN[1]);
        _player.spawn();
    }

    public String getDownKeysAsString() {
        String f = "";
        for(int key : _directionalKeysPressed) {
            f += (char)key;
        }
        return f;
    }

    public void printArrayList(ArrayList<Integer> a) {
        for(int b : a) {
            System.out.print(b);
        }
        System.out.println();
    }

    public void updateGame() {
        if(_player.getY() > 525 || _player.getY() < -12) {
            if(_player.getY() > 525) { // If player goes out the bottom of the window
                _currentRoomIndex++;
                if(_currentRoomIndex == _rooms.size()) {
                    try {
                        _rooms.add(new Room(_rooms.get(_currentRoomIndex - 1), _player.getX(), 0));
                    } catch(Exception ex) {
                        System.out.println("error generating new room: " + ex);
                    }
                }
                _player.teleport(_player.getX(), 0);
            } else if(_player.getY() < -12) { // If player goes out the top of the window
                _currentRoomIndex--;
                _player.teleport(_player.getX(), 524);
            }
            _rockArray = _rooms.get(_currentRoomIndex).getElementsAsArrayList();
            _player.setSpawnX(_rooms.get(_currentRoomIndex).getSpawnX());
            _player.setSpawnY(_rooms.get(_currentRoomIndex).getSpawnY());
        }

        if(_player.getFuelAmount() <= 0 || _player.getLivesRemaining() == -1) { _gameState = 4; }
        
        if(_gameState != 1) return;

        int movementKey = 0;
        if(_directionalKeysPressed.size() >= 1) {
            movementKey = _directionalKeysPressed.get(_directionalKeysPressed.size() - 1);
        }

        if(movementKey == 68) {
            _player.setDX(40);
        } else if(movementKey == 65) {
            _player.setDX(-40);
        } else {
            _player.setDX(0);
        }

        _player.setJetpackOn(_spaceKeyPressed);

        _player.update(0.05);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // System.out.println(_player.getX() + ", " + _player.getY());

        g.drawImage(_backgroundTexture, 0, 0, null);

        g.drawImage(_player.getTexture(), _player.getX(), _player.getY(), _player.getWidth(), _player.getHeight(), null);
        if(_player.getJetpackStatus()) {
            g.drawImage(_player.getThrustTexture(), _player.getX(), _player.getY() + _player.getHeight(), _player.getWidth(), _player.getHeight(), null);
        }

        for(Rock rock : _rockArray) {
            g.drawImage(_rockTexture, rock.getX(), rock.getY(), rock.getWidth(), rock.getHeight(), null);
        }

        if(_rooms.get(_currentRoomIndex).hasFuelCan()) {
            FuelCan fc = _rooms.get(_currentRoomIndex).getFuelCan();
            g.drawImage(_fuelCanTexture, fc.getX(), fc.getY(), fc.getWidth(), fc.getHeight(), null);
        }
        
    }

    @Override
	public void keyPressed(KeyEvent e) {
		// System.out.printf("pressed:  %c (%d)\n", e.getKeyChar(), e.getKeyCode());
        if(!_generalKeysPressed.containsKey(e.getKeyCode())) {
            if(e.getKeyCode() != 68 && e.getKeyCode() != 65) {
                _generalKeysPressed.put(e.getKeyCode(), true);
            }
        } else { return; }

        switch(e.getKeyCode()) {
            case 68: // d key - move right
            case 65: // a key - move left
                if(_gameState != 1) break;
                if(_directionalKeysPressed.size() == 0) {
                    _directionalKeysPressed.add(e.getKeyCode());
                    break;
                }

                if(_directionalKeysPressed.size() == 1) {
                    if(!_directionalKeysPressed.contains(e.getKeyCode())) {
                        _directionalKeysPressed.add(e.getKeyCode());
                    }
                }

                if(_directionalKeysPressed.size() == 2) {
                    if(e.getKeyCode() == _directionalKeysPressed.get(0)) {
                        _directionalKeysPressed.remove(0);
                        _directionalKeysPressed.add(e.getKeyCode());
                    }
                }
                break;

            // case 10: // enter key
            //     if(_gameState == 3 || _gameState == 4) { _gameState = 0; }
            //     break;

            case 32: // space key - jetpack, play, return to main menu after win
                if(_gameState == 1) { _spaceKeyPressed = true; }
                else if(_gameState == 0 || _gameState == 2) { _gameState = 1; }
                break;

            case 27: // escape key - pause, return to main menu, or exit
                if(_gameState == 1) { _gameState = 2; }
                else if(_gameState == 2) {
                    _gameState = 0;
                    // _player.spawn();
                }
                break;

            case 72: // h key - debug print
                System.out.println("keys down: " + getDownKeysAsString());
                System.out.println("current state: " + _gameState);
                break;
        }
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// System.out.printf("released: %c (%d)\n", e.getKeyChar(), e.getKeyCode());

        if(_directionalKeysPressed.contains(e.getKeyCode())) {
            _directionalKeysPressed.remove(_directionalKeysPressed.indexOf(e.getKeyCode()));
        }

        if(e.getKeyCode() == 68 || e.getKeyCode() == 65) { return; } //TODO: needed?

        _generalKeysPressed.remove(e.getKeyCode());

        if(e.getKeyCode() == 32) { _spaceKeyPressed = false; }
	}
	
	@Override
	public void keyTyped(KeyEvent e) { }
}
