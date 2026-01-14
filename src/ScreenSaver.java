import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ScreenSaver extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ScreenSaver saver = new ScreenSaver();
            saver.setVisible(true);
        });
    }

    public ScreenSaver() {
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        ScreenPanel panel = new ScreenPanel();
        setContentPane(panel);

        // Выход по ESC или клику
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    // Панель, на которой всё рисуется
    static class ScreenPanel extends JPanel {

        private static final String[] CHARS = {"#", "@", "$", "*", "+", "%"};
        private static final int MIN_ITEMS = 15;
        private static final int MAX_ITEMS = 30;
        private static final Random random = new Random();

        private final List<Symbol> symbols = new ArrayList<>();

        public ScreenPanel() {
            setBackground(Color.BLACK);
            setDoubleBuffered(true); // стандарт для Swing [web:21][web:23]

            // Таймер для пополнения символов
            new Timer(200, e -> maintainSymbolCount()).start();

            // Таймер для анимации (60 FPS)
            new Timer(16, e -> {
                updateSymbols();
                repaint();
            }).start();
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            return new Dimension(screen.width, screen.height);
        }

        private void maintainSymbolCount() {
            long now = System.currentTimeMillis();
            long alive = symbols.stream().filter(s -> !s.isDead(now)).count();
            if (alive < MIN_ITEMS) {
                createSymbol();
            } else if (alive < MAX_ITEMS && random.nextDouble() > 0.6) {
                createSymbol();
            }
        }

        private void createSymbol() {
            int w = getWidth();
            int h = getHeight();
            if (w <= 0 || h <= 0) {
                return; // окно ещё не инициализировано
            }
            symbols.add(new Symbol(w, h));
        }

        private void updateSymbols() {
            long now = System.currentTimeMillis();
            Iterator<Symbol> it = symbols.iterator();
            while (it.hasNext()) {
                Symbol s = it.next();
                if (s.isDead(now)) {
                    it.remove();
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // заливает фон цветом панели [web:25]
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            long now = System.currentTimeMillis();
            for (Symbol s : symbols) {
                s.paint(g2, now);
            }

            g2.dispose();
        }

        // Один символ
        static class Symbol {
            private final String text;
            private final int x;
            private final int y;
            private final int fontSize;
            private final long birthTime;
            private final long lifeDuration; // мс
            private static final Random random = new Random();

            Symbol(int width, int height) {
                text = CHARS[random.nextInt(CHARS.length)];
                fontSize = 20 + random.nextInt(41); // 20–60

                int margin = fontSize * 2;
                x = margin + random.nextInt(Math.max(1, width - margin * 2));
                y = margin + random.nextInt(Math.max(1, height - margin * 2));

                lifeDuration = 5000 + random.nextInt(3001); // 5–8 секунд
                birthTime = System.currentTimeMillis();
            }

            boolean isDead(long now) {
                return now - birthTime > lifeDuration;
            }

            void paint(Graphics2D g2, long now) {
                long age = now - birthTime;
                if (age < 0 || age > lifeDuration) {
                    return;
                }
                float progress = (float) age / (float) lifeDuration;

                float alpha;
                if (progress < 0.2f) {           // 0–20%: появление
                    alpha = progress / 0.2f;
                } else if (progress > 0.8f) {    // 80–100%: исчезновение
                    alpha = 1f - (progress - 0.8f) / 0.2f;
                } else {                         // середина жизни
                    alpha = 1f;
                }

                alpha = Math.max(0f, Math.min(1f, alpha));

                Composite old = g2.getComposite();

                // Лёгкое свечение
                if (alpha > 0.3f) {
                    g2.setComposite(AlphaComposite.getInstance(
                            AlphaComposite.SRC_OVER,
                            alpha * 0.5f));
                    g2.setColor(new Color(0, 0, 255));
                    g2.setFont(new Font("Courier New", Font.BOLD, fontSize));
                    g2.drawString(text, x + 2, y + 2);
                }

                // Основной символ
                g2.setComposite(AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER,
                        alpha));
                g2.setColor(new Color(0, 0, 255));
                g2.setFont(new Font("Courier New", Font.BOLD, fontSize));
                g2.drawString(text, x, y);

                g2.setComposite(old);
            }
        }
    }
}


