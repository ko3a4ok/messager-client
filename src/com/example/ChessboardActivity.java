package com.example;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

/**
 * Created by IntelliJ IDEA.
 * User: ko3a4ok
 * Date: 3/15/12
 * Time: 1:17 PM
 */
public class ChessboardActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chess_layout);
//        mGLView = new ClearGLSurfaceView(this);
//        setContentView(mGLView);
        mGLView = (GLSurfaceView) findViewById(R.id.surface_view_id);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGLView != null)    mGLView.onResume();
    }

    private GLSurfaceView mGLView;
}