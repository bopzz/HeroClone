import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TitleCardsHandler extends JComponent implements KeyListener {
    private int _gameState = 0;
    private BufferedImage _menu;
    private BufferedImage _gameOver;

    public TitleCardsHandler() throws IOException {
        _menu = ImageIO.read(new File("./assets/textures/menu.png"));
        _gameOver = ImageIO.read(new File("./assets/textures/game-over.png"));

        addKeyListener(this);
        setFocusable(true);
    }

    public int getGameState() { return _gameState; }
    public void setGameState(int gameState) { _gameState = gameState; }

    public void updateGameState(int gameState) {
        _gameState = gameState;
        // System.out.println("title card handler thinks state: " + _gameState);
    }

    @Override
    public void paintComponent(Graphics g) {
        // g.setColor(Color.red);
        // g.fillRect(100, 100, 200, 200);
        switch(_gameState) {
            case 0:
                g.drawImage(_menu, 0, 0, null);
                break;
            case 4:
                g.drawImage(_gameOver, 0, 0, null);
                break;

        }
        if(_gameState == 0) {
            // System.out.println("SHOWING MENU CARD");
            // g.setColor(Color.red);
            // g.fillRect(100, 100, 200, 200);
        }
    }

    @Override
	public void keyPressed(KeyEvent e) {
		// System.out.printf("pressed:  %c (%d)\n", e.getKeyChar(), e.getKeyCode());
        switch(e.getKeyCode()) {

            case 10: // enter key - play (when in menu), return to main menu
                if(_gameState == 3 || _gameState == 4) { _gameState = 0; }
                break;

            case 32: // space key - jetpack

                if(_gameState == 0) {
                    System.out.println("starting");
                    _gameState = 1;
                }
                break;

            case 27: // escape key - pause, return to main menu, or exit
                if(_gameState == 0) { System.exit(0); }
                else if(_gameState == 1) { _gameState = 2; }
                else if(_gameState == 2 || _gameState == 3 || _gameState == 4) { _gameState = 0; }
                break;
        }
	}

	@Override
	public void keyReleased(KeyEvent e) { }
	
	@Override
	public void keyTyped(KeyEvent e) { }
}
