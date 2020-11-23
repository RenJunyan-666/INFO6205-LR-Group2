package City;

import Util.Point;

public class Block {
    private int blockX;
    private int blockY;

    public Block(int blockX, int blockY) {
        this.blockX = blockX;
        this.blockY = blockY;
    }

    public int getBlockX() {
        return blockX;
    }

    public void setBlockX(int blockX) {
        this.blockX = blockX;
    }

    public int getBlockY() {
        return blockY;
    }

    public void setBlockY(int blockY) {
        this.blockY = blockY;
    }
}
