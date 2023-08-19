public class Collider {
    private int _x = -1;
    private int _y = -1;
    private int _width = 0;
    private int _height = 0;

    public Collider(int x, int y, int width, int height) {
        _x = x;
        _y = y;
        _width = width;
        _height = height;
    }

    public void update(int x, int y) {
        _x = x;
        _y = y;
    }

    public boolean collides(Collider c2) {
        int left1 = this._x;
        int left2 = c2._x;
        int right1 = this._x + this._width;
        int right2 = c2._x + c2._width;
        int top1 = this._y;
        int top2 = c2._y;
        int bottom1 = this._y + this._height;
        int bottom2 = c2._y + c2._height;

        // if either left or right edge of obj 1 is in between the left and right edges (x-values) of obj 2
        if((left1 > left2 && left1 < right2) || (right1 > left2 && right1 < right2)) {
            //if the top or bottom edge is in between the top and bottom edges (y-values) of obj 2
            if((top1 > top2 && top1 < bottom2) || (bottom1 > top2 && bottom1 < bottom2)) {
                return true;
            }
        }
        return false;
    }
}
