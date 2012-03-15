package com.example;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: ko3a4ok
 * Date: 3/15/12
 * Time: 1:33 PM
 */
class ClearGLSurfaceView extends GLSurfaceView {

    public ClearGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRenderer = new ChessboardRenderer(context);
        setRenderer(mRenderer);
    }

    public boolean onTouchEvent(final MotionEvent event) {
        queueEvent(new Runnable(){
            public void run() {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    mRenderer.setPosition((int) (event.getX() / getWidth() * 8), (int) (event.getY() / getHeight()*8));
            }});
        return true;
    }

    ChessboardRenderer mRenderer;
}