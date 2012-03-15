package com.example;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ko3a4ok
 * Date: 3/15/12
 * Time: 6:51 PM
 */
public class Game {
    List<Figure> figures = new ArrayList<Figure>();
    public boolean allowWhite;
    public void setFigures(List<Figure> figures) {
        this.figures = figures;
    }
    
    public boolean allowSelect(int x, int y) {
        Figure f = getFigure(x, y);
        return f != null && (f.getColor() == Figure.Color.BLACK ^ allowWhite);
    }
    
    public void step(Point p0, Point p1) {
        allowWhite = !allowWhite;
        Figure f = getFigure(p1.x, p1.y);
        if (f != null) f.die();
    }

    public boolean allowStep(Point previosStep, Point currentStep) {
        Figure f0 = getFigure(previosStep.x, previosStep.y);
        Figure f1 = getFigure(currentStep.x, currentStep.y);
        return f1 == null || f0.getColor() != f1.getColor();
    }

    private Figure getFigure(int x, int y) {
        for (Figure f: figures)
            if (f.contain(x, y))
                return f;
        return null;
    }
}
