package com.example;

/**
 * Created by IntelliJ IDEA.
 * User: ko3a4ok
 * Date: 3/15/12
 * Time: 4:21 PM
 */
public class Entity {
    private boolean selected;
    protected int x = -1;
    protected int y;
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean contain(int x, int y) {
        return this.x == x && this.y == y;
    }


    public void setSelected(boolean s) {
        selected = s;
    }

    public boolean isSelected() {
        return selected;
    }
}
