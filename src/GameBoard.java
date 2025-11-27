import java.io.*;
import java.util.*;

public class GameBoard {
    private final int width;
    private final int height;
    private Snake snake;
    private List<Food> foodList = new ArrayList<>();
    private List<Obstacle> obstacles = new ArrayList<>();
    private int score = 0;
    private int highestScore = 0;
    private final String SAVE_FILE = "highest_score.txt";

    public GameBoard(int width, int height) {
        this.width = width;
        this.height = height;
        loadHighestScore();

        snake = new Snake(width / 2, height / 2);

        // multiple foods
        for (int i = 0; i < 3; i++)
            spawnFood();

        // obstacles
        for (int i = 0; i < 5; i++)
            spawnObstacle();
    }

    public void loadHighestScore() {
        try {
            File f = new File(SAVE_FILE);
            if (!f.exists()) return;

            BufferedReader br = new BufferedReader(new FileReader(f));
            highestScore = Integer.parseInt(br.readLine());
        } catch (Exception ignored) {}
    }

    public void saveHighestScore() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(SAVE_FILE))) {
            pw.println(highestScore);
        } catch (Exception ignored) {}
    }

    private void spawnFood() {
        Random r = new Random();
        while (true) {
            int x = r.nextInt(width - 2) + 1;
            int y = r.nextInt(height - 2) + 1;

            Point p = new Point(x, y);

            if (!isOccupied(p)) {
                foodList.add(new Food(x, y));
                break;
            }
        }
    }

    private void spawnObstacle() {
        Random r = new Random();
        while (true) {
            Point p = new Point(r.nextInt(width - 2) + 1, r.nextInt(height - 2) + 1);

            if (!isOccupied(p)) {
                obstacles.add(new Obstacle(p.x, p.y));
                break;
            }
        }
    }

    private boolean isOccupied(Point p) {
        for (Point s : snake.getBody())
            if (s.equals(p)) return true;
        for (Food f : foodList)
            if (f.equals(p)) return true;
        for (Obstacle o : obstacles)
            if (o.equals(p)) return true;
        return false;
    }

    public boolean update() {
        snake.move(false);

        Point head = snake.getHead();

        if (head.x <= 0 || head.x >= width - 1 || head.y <= 0 || head.y >= height - 1)
            return false;

        if (snake.isSelfCollide()) return false;

        for (Obstacle o : obstacles)
            if (head.equals(o)) return false;

        Iterator<Food> it = foodList.iterator();
        while (it.hasNext()) {
            Food f = it.next();
            if (head.equals(f)) {
                score++;
                it.remove();
                spawnFood();
                snake.move(true);
            }
        }

        if (score > highestScore) highestScore = score;

        return true;
    }

    private void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
            }
        } catch (Exception e) {
            System.out.println("\n".repeat(50)); // fallback
        }
    }

    public void draw() {
        clearScreen();  // xoá toàn bộ màn hình trước khi in map

        System.out.println("Score: " + score + " | Highest: " + highestScore);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Point p = new Point(x, y);

                if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
                    System.out.print("#");  // border
                    continue;
                }

                boolean printed = false;

                // snake
                for (int i = 0; i < snake.getBody().size(); i++) {
                    if (snake.getBody().get(i).equals(p)) {
                        System.out.print(i == 0 ? "O" : "o");
                        printed = true;
                        break;
                    }
                }

                if (printed) continue;

                // food
                for (Food f : foodList) {
                    if (f.equals(p)) {
                        System.out.print("*");
                        printed = true;
                        break;
                    }
                }
                if (printed) continue;

                // obstacle
                for (Obstacle o : obstacles) {
                    if (o.equals(p)) {
                        System.out.print("X");
                        printed = true;
                        break;
                    }
                }
                if (printed) continue;

                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public Snake getSnake() {
        return snake;
    }
}
