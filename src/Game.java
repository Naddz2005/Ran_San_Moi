import java.io.IOException;
import java.util.Scanner;

public class Game {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

//        InputHandler input = new InputHandler();

        while (true) {
            System.out.println("\n===== SNAKE GAME =====");
            System.out.println("1. Start Game");
            System.out.println("2. Exit");
            System.out.print("Choose: ");
            int choice = sc.nextInt();

            if (choice == 2) {
                System.out.println("Bye!");
                sc.close();
                return;
            }
            GameBoard gb = new GameBoard(40, 20);

            System.out.println("W/A/S/D to move | P = pause | R = resume");

            boolean running = true;
            boolean paused = false;

            while (running) {

                if (!paused) {
                    gb.draw();
                    running = gb.update();
                }

                long start = System.currentTimeMillis();

                while (System.currentTimeMillis() - start < 500) {
                    if (System.in.available() > 0) {
                        char c = (char) System.in.read();

                        switch (Character.toLowerCase(c)) {
                            case 'w' -> gb.getSnake().setDirection(Direction.UP);
                            case 's' -> gb.getSnake().setDirection(Direction.DOWN);
                            case 'a' -> gb.getSnake().setDirection(Direction.LEFT);
                            case 'd' -> gb.getSnake().setDirection(Direction.RIGHT);
                            case 'p' -> paused = true;
                            case 'r' -> paused = false;
                        }
                    }
                }

                if (!running) {
                    System.out.println("\nGAME OVER!");
                    gb.saveHighestScore();
                    break;
                }

                if (paused) {
                    System.out.println("== PAUSED ==");
                    Thread.sleep(300);
                }
            }
        }
    }
}
