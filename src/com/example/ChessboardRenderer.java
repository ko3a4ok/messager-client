package com.example;

import android.content.Context;
import android.graphics.Point;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.util.ArrayList;
import java.util.List;

import static android.opengl.GLU.gluLookAt;

/**
 * Created by IntelliJ IDEA.
 * User: ko3a4ok
 * Date: 3/15/12
 * Time: 1:28 PM
 */
public class ChessboardRenderer implements GLSurfaceView.Renderer {
    private List<Figure> figures = new ArrayList<Figure>() ;
    private Board board;

    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
        for (Figure figure : figures)
            figure.loadGLTexture(gl, this.context);

        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
        gl.glDisable(GL10.GL_DITHER);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
        gl.glClearColor(.5f, .5f, .5f, 1);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);

        gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
        gl.glClearColor(0.3f, 0.0f, 0.0f, 0.5f); 	//Black Background
        gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
        gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
        gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
    }

    public void onSurfaceChanged(GL10 gl, int w, int h) {
        gl.glViewport(0, 0, w, h);
        float ratio = (float) w / h;
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glFrustumf(-ratio, ratio, -1, 1, 3, 13);
    }

    public void onDrawFrame(GL10 gl) {
        gl.glDisable(GL10.GL_DITHER);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gluLookAt(gl, 0, 0, -12, 0f, 0f, 0f, 0f, -1.0f, 0.0f);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        draw(gl);
    }


    private Context context;

    public ChessboardRenderer(Context context) {
        this.context = context;
        board = new Board();

        figures.add(new Figure(Figure.Type.ROOK, Figure.Color.WHITE, 0, 0));
        figures.add(new Figure(Figure.Type.KNIGHT, Figure.Color.WHITE, 1, 0));
        figures.add(new Figure(Figure.Type.BISHOP, Figure.Color.WHITE, 2, 0));
        figures.add(new Figure(Figure.Type.QUEEN, Figure.Color.WHITE, 3, 0));
        figures.add(new Figure(Figure.Type.KING, Figure.Color.WHITE, 4, 0));
        figures.add(new Figure(Figure.Type.BISHOP, Figure.Color.WHITE, 5, 0));
        figures.add(new Figure(Figure.Type.KNIGHT, Figure.Color.WHITE, 6, 0));
        figures.add(new Figure(Figure.Type.ROOK, Figure.Color.WHITE, 7, 0));
        for (int i = 0; i < 8; i++)
            figures.add(new Figure(Figure.Type.PAWN, Figure.Color.WHITE, i, 1));

        figures.add(new Figure(Figure.Type.ROOK, Figure.Color.BLACK, 0, 7));
        figures.add(new Figure(Figure.Type.KNIGHT, Figure.Color.BLACK, 1, 7));
        figures.add(new Figure(Figure.Type.BISHOP, Figure.Color.BLACK, 2, 7));
        figures.add(new Figure(Figure.Type.QUEEN, Figure.Color.BLACK, 3, 7));
        figures.add(new Figure(Figure.Type.KING, Figure.Color.BLACK, 4, 7));
        figures.add(new Figure(Figure.Type.BISHOP, Figure.Color.BLACK, 5, 7));
        figures.add(new Figure(Figure.Type.KNIGHT, Figure.Color.BLACK, 6, 7));
        figures.add(new Figure(Figure.Type.ROOK, Figure.Color.BLACK, 7, 7));
        for (int i = 0; i < 8; i++)
            figures.add(new Figure(Figure.Type.PAWN, Figure.Color.BLACK, i, 6));

    }

    //overriden method
    protected void draw(GL10 gl) {
        board.draw(gl);
        for (Figure figure : figures)
            figure.draw(gl);

    }


    private Figure selectedFigure;
    public void setPosition(int x, int y) {
        boolean clickOnFreeSpace = true;
        for (Figure figure : figures) {
            if (figure.contain(x,y)) {
                clickOnFreeSpace = false;
                selectedFigure = figure;
                figure.setSelected(true);
                break;
            }
        }
        if (selectedFigure != null && clickOnFreeSpace)
            step(x, y);
        if (clickOnFreeSpace) selectedFigure = null;
        board.setSelected(!clickOnFreeSpace);
        board.setPosition(x,y);
    }
    
    private Point previosStep;
    private Point currentStep;
    private void step(int x, int y) {
        previosStep = new Point(selectedFigure.x, selectedFigure.y);
        currentStep = new Point(x,y);
        selectedFigure.setPosition(x, y);
        board.setStep(previosStep, currentStep);
    }

}