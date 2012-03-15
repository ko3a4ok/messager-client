package com.example;

import android.graphics.Point;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by IntelliJ IDEA.
 * User: ko3a4ok
 * Date: 3/15/12
 * Time: 3:55 PM
 */
public class Board extends Entity{
    private final static int VERTS = 81;

    private FloatBuffer mFVertexBuffer;

    private ShortBuffer[] mIndexBuffer = new ShortBuffer[64];

    public Board() {
        ByteBuffer vbb = ByteBuffer.allocateDirect(VERTS * 3 * 4);
        vbb.order(ByteOrder.nativeOrder());
        mFVertexBuffer = vbb.asFloatBuffer();


        float[] coords = {
                -0.5f, -0.5f, 0, // (x1,y1,z1)
                0.5f, -0.5f, 0,
                0.0f,  0.5f, 0
        };
        for (int i = 0; i < VERTS; i++) {
            float x = (i/9-4f)/1f;
            float y = (i%9-4f)/1f;
            mFVertexBuffer.put(x);
            mFVertexBuffer.put(y);
            mFVertexBuffer.put(0);
        }
        mFVertexBuffer.position(0);
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                int idx = i*8+j;
                ByteBuffer ibb = ByteBuffer.allocateDirect(4 * 2);
                ibb.order(ByteOrder.nativeOrder());
                mIndexBuffer[idx] = ibb.asShortBuffer();
                mIndexBuffer[idx].put((short) (i*9+j));
                mIndexBuffer[idx].put((short) (i*9+j+1));
                mIndexBuffer[idx].put((short) (i*9+j+10));
                mIndexBuffer[idx].put((short) (i*9+j+9));
                mIndexBuffer[idx].position(0);
            }

    }

    public void draw(GL10 gl) {
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mFVertexBuffer);
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                if (i%2==0 ^ j%2==0)    gl.glColor4f(.7f, .3f, 0, 1f);
                else                    gl.glColor4f(.94f, .8f, .5f, 1f);
                gl.glDrawElements(GL10.GL_TRIANGLE_FAN, 4, GL10.GL_UNSIGNED_SHORT, mIndexBuffer[i*8+j]);
                if (isSelected() && i == this.x && j == this.y) {
                    gl.glEnable(GL10.GL_BLEND);
                    gl.glColor4f(1f, 0, 0, 1f);
                    gl.glLineWidth(1.2f);
                    gl.glDrawElements(GL10.GL_LINE_LOOP, 4, GL10.GL_UNSIGNED_SHORT, mIndexBuffer[i*8+j]);
                    gl.glDisable(GL10.GL_BLEND);
                }
                if (previosStep != null && (previosStep.equals(i, j) || currentStep.equals(i, j))) {
                    System.err.println("FUCK!!");
                    gl.glEnable(GL10.GL_BLEND);
                    gl.glColor4f(1f, 0, 0, .5f);
                    gl.glDrawElements(GL10.GL_TRIANGLE_FAN, 4, GL10.GL_UNSIGNED_SHORT, mIndexBuffer[i*8+j]);
                    gl.glDisable(GL10.GL_BLEND);
                }
            }
    }

    private Point previosStep;
    private Point currentStep;
    public void setStep(Point p0, Point p1) {
        previosStep = p0;
        currentStep = p1;
    }
}
