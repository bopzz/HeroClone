import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.*;

public class GameInfoPanel extends JPanel {
    private JLabel _stateDisplay;
    private BufferedImage _playerTexture;
    private BufferedImage _backgroundTexture;
    private int[] _infoPackage = new int[3];
    private int _livesRemaining;
    private int _fuelRemaining;
    private int _initialFuelAmount = -1;

    public GameInfoPanel(int w, int h) {
        this.setPreferredSize(new Dimension(w, h));
        this.setBackground(Color.blue);

        _stateDisplay = new JLabel();
        _stateDisplay.setForeground(Color.green);
        _stateDisplay.setFont(new Font("monospaced", Font.ITALIC, 48));
        _stateDisplay.setSize(new Dimension(200, 50));

        try {
            _backgroundTexture = ImageIO.read(new File("./assets/textures/infobar.png"));
            _playerTexture = ImageIO.read(new File("./assets/textures/ship-forward.png"));
        } catch(Exception ex) {
            System.out.println("error loading player image (gameInfoPanel)");
        }
        
        this.add(_stateDisplay);
    }

    public void updateInfo() {
        if(_initialFuelAmount <= 0) {
            _initialFuelAmount = _infoPackage[2];
        }

        _stateDisplay.setText((_infoPackage[0] == 1) ? "SCORE: " + GameAreaPanel.getScore() : "PAUSED");

        _livesRemaining = _infoPackage[1];
        _fuelRemaining = _infoPackage[2];
    }

    public void updateInfoPackage(int[] p) { _infoPackage = p; }

    @Override
    public void paintComponent(Graphics g) {
        if(_infoPackage[0] == 1 || _infoPackage[0] == 2) {
            g.drawImage(_backgroundTexture, 0, 0, getWidth(), getHeight(), null);

            for(int i = 0; i < _livesRemaining; i++) {
                g.drawImage(_playerTexture, getWidth() / 3 + (i * 40), getHeight() / 2, null);
            }

            g.setColor(Color.red);
            g.fillRect(getWidth() / 3, getHeight() / 2 + 40, (int)(((double)_fuelRemaining / (double)_initialFuelAmount) * getWidth() / 3), 20);
        }
    }
}
