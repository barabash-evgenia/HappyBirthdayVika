import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends JFrame {
    private static final int ROWS = 5;
    private static final int COLS = 5;
    private static final int TOTAL_GIFTS = 11;
    private static final int TOTAL_BOMBS = 14;
    private static final int TOTAL_LIVES = 8;

    private JButton[][] buttons;
    private List<Point> bombPositions;
    private List<Point> giftPositions;
    private int remainingGifts;
    private int remainingLives;

    public Game() {
        initializeGame();
        initializeUI();
    }

    private void initializeGame() {
        bombPositions = generateRandomPositions(TOTAL_BOMBS);
        giftPositions = generateGiftsPositions();
        remainingGifts = TOTAL_GIFTS;
        remainingLives = TOTAL_LIVES;
    }

    private void initializeUI() {
        setTitle("Happy Birthday");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(ROWS, COLS));

        buttons = new JButton[ROWS][COLS];

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                add(buttons[i][j]);
            }
        }

        setSize(400, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private ArrayList<Point> generateRandomPositions(int count) {
        ArrayList<Point> positions = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int row = (int) (Math.random() * ROWS);
            int col = (int) (Math.random() * COLS);
            Point position = new Point(row, col);
            if (!positions.contains(position)) {
                positions.add(position);
            } else {
                i--;
            }
        }
        return positions;
    }

    private List<Point> generateGiftsPositions() {
        List<Point> positions = new ArrayList<>();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                Point position = new Point(i, j);
                if (!bombPositions.contains(position)) {
                    positions.add(position);
                }
            }
        }
        return positions;
    }

    private class ButtonClickListener implements ActionListener {
        private int row;
        private int col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (bombPositions.contains(new Point(row, col))) {
                handleBombClick();
            } else if (giftPositions.contains(new Point(row, col))) {
                handleGiftClick();
            }
            buttons[row][col].setEnabled(false);
            checkGameStatus();
        }

        private void handleBombClick() {
            remainingLives--;
            JOptionPane.showMessageDialog(null, "<html><b><font color='#990000'>" + "БУМ! Ты попала на бомбу! <br><br>Осталось жизней: " + remainingLives + "</b></font></html>");
            if (remainingLives == 0) {
                endGame("<html><b><font color='#990000'>" + "Конец игры! Закончились жизни." + "</b></font></html>");
            }
        }

        private void handleGiftClick() {
            remainingGifts--;
            Random random = new Random();
            int index = random.nextInt(DoNotOpen.secret.size());
            JOptionPane.showMessageDialog(null, "<html><b><font color='" + DoNotOpen.textColor.get(index) + "'>" + DoNotOpen.secret.get(index) + "<br><br>Осталось подарков: " + remainingGifts + "</b></font></html>");
            DoNotOpen.secret.remove(index);
            DoNotOpen.textColor.remove(index);
            if (remainingGifts == 0) {
                endGame("<html><b><font color='#003300'>" + "Поздравляем! Ты нашла все подарки." + "</b></font></html>");
            }
        }

        private void checkGameStatus() {
            if (remainingLives == 0) {
                endGame("<html><b><font color='#990000'>" + "Конец игры! Закончились жизни." + "</b></font></html>");
            } else if (remainingGifts == 0) {
                endGame("<html><b><font color='#003300'>" + "Поздравляем! Ты нашла все подарки." + "</b></font></html>");
            }
        }

        private void endGame(String message) {
            JOptionPane.showMessageDialog(null, message);
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Game::new);
    }
}
