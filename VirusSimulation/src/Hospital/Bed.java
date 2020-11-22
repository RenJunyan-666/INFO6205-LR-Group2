package Hospital;

import Util.Point;

public class Bed extends Point {
    /*the condition of empty beds in the hospital*/

    public Bed(int x, int y) {
        super(x, y);
    }

    private boolean isEmpty = true;

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }
}
