package City;

import Util.Graph;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class City {
    private int centerX;
    private int centerY;
    private List<Block> blockList = new ArrayList<Block>();

    public City(int centerX, int centerY) {
        this.centerX = centerX;
        this.centerY = centerY;

        blockList.add(new Block(centerX+50,centerY+50));
        blockList.add(new Block(centerX-60,centerY-40));
        blockList.add(new Block(centerX-60,centerY+80));
        blockList.add(new Block(centerX+40,centerY-50));
        blockList.add(new Block(centerX,centerY-100));
    }

    public int getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public List<Block> getBlockList() {
        return blockList;
    }
}
