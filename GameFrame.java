import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GameFrame extends JFrame {
    private int _gameState = 0;
    private GameAreaPanel _gameAreaPanel;
    private GameInfoPanel _gameInfoPanel;
    protected Timer _timer;
    private int _frameInterval = 16;
    private TitleCardsHandler _titleCardsHandler;

    public GameFrame(String windowTitle, int w, int h) {
        super(windowTitle);

        this.setSize(w, h);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        _gameAreaPanel = new GameAreaPanel(w, h * 3 / 4);
        _gameInfoPanel = new GameInfoPanel(w, h * 1 / 4);

        try {
            _titleCardsHandler = new TitleCardsHandler();
        } catch(Exception e) {
            System.out.println("error loading title card images with error:");
            System.out.println(e);
        }

        this.add(_titleCardsHandler);
        this.add(_gameAreaPanel);
        this.add(_gameInfoPanel);

        _timer = new Timer(_frameInterval, new TimerCallback()); // 100 ms = 0.1 sec
		_timer.start();
    }
	
	protected class TimerCallback implements ActionListener {
		public void actionPerformed(ActionEvent e) {
            _gameAreaPanel.updateGame();
            _gameInfoPanel.updateInfo();

            if(_gameState == 1 || _gameState == 2) {
                _gameState = _gameAreaPanel.getGameState();
                _titleCardsHandler.setGameState(_gameState);
            } else {
                _gameState = _titleCardsHandler.getGameState();
                _gameAreaPanel.setGameState(_gameState);
            }

            _gameInfoPanel.updateInfoPackage(new int[]{_gameState, _gameAreaPanel.getLivesRemaining(), _gameAreaPanel.getFuelAmount()});

            _titleCardsHandler.updateGameState(_gameState);

            if(_gameState != 1 && _gameState != 2) { // If not playing or paused
                _gameAreaPanel.setFocusable(false);
                _gameAreaPanel.removeKeyListener(_gameAreaPanel);
                _gameAreaPanel.setVisible(false);
                _gameInfoPanel.setVisible(false);
                _titleCardsHandler.addKeyListener(_titleCardsHandler);
                _titleCardsHandler.requestFocus();
                _titleCardsHandler.setFocusable(true);
                _titleCardsHandler.setVisible(true);
                _gameAreaPanel.resetGame();
            } else {
                _gameAreaPanel.setFocusable(true);
                _titleCardsHandler.removeKeyListener(_titleCardsHandler);
                _gameAreaPanel.setVisible(true);
                _gameInfoPanel.setVisible(true);
                _gameAreaPanel.addKeyListener(_gameAreaPanel);
                _gameAreaPanel.requestFocus();
                _titleCardsHandler.setFocusable(false);
                _titleCardsHandler.setVisible(false);
            }

            _gameAreaPanel.repaint();
            _gameInfoPanel.repaint();
            _titleCardsHandler.repaint();
            repaint();
		}
	}
}
