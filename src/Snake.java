import java.util.LinkedList;

public class Snake {
    private LinkedList<Point> body = new LinkedList<>();
    private Direction direction = Direction.RIGHT;

    public Snake(int startX, int startY) {
        body.add(new Point(startX, startY));
    }

    public LinkedList<Point> getBody() {
        return body;
    }

    public void setDirection(Direction dir) {
        this.direction = dir;
    }

    public Direction getDirection() {
        return direction;
    }

    public Point getHead() {
        return body.getFirst();
    }

    public void move(boolean grow) {
        Point head = getHead();
        Point newHead = null;

        switch (direction) {
            case UP -> newHead = new Point(head.x, head.y - 1);
            case DOWN -> newHead = new Point(head.x, head.y + 1);
            case LEFT -> newHead = new Point(head.x - 1, head.y);
            case RIGHT -> newHead = new Point(head.x + 1, head.y);
        }
        body.addFirst(newHead);

        if(!grow) {
            body.removeLast();
        }
    }

    // Ham xu ly tu va cham
    public boolean isSelfCollide() {
        Point head = getHead();
        for(int i = 1; i < body.size(); i++) {
            if(head.equals(body.get(i))) {
                return true;
            }
        }
        return false;
    }
}
