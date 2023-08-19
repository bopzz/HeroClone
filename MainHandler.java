public class MainHandler {
    public static void main(String[] args) {
        try {
            GameFrame frame = new GameFrame("Hero Clone", 960, 730);
            frame.setVisible(true);
        } catch (Exception ex)  {
            System.out.println("error: missing image file for titlecard");
            System.exit(1);
        }
    }
}