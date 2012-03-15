package com.example;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by IntelliJ IDEA.
 * User: ko3a4ok
 * Date: 3/15/12
 * Time: 3:35 PM
 */
public class Figure extends Entity{

    public static enum Color {
        BLACK,
        WHITE
    };
    
    public static enum Type{
        PAWN(R.drawable.pawn),
        KNIGHT(R.drawable.hourse),
        BISHOP(R.drawable.bishop),
        ROOK(R.drawable.rook),
        QUEEN(R.drawable.queen),
        KING(R.drawable.king);
        
        

        private int type;
        Type(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }
    private Color color;
    private Type type;
    private FloatBuffer textureBuffer;  // buffer holding the texture coordinates
    private float texture[] = {
            // Mapping coordinates for the vertices
            0.0f, 1.0f,     // top left     (V2)
            0.0f, 0.0f,     // bottom left  (V1)
            1.0f, 1.0f,     // top right    (V4)
            1.0f, 0.0f      // bottom right (V3)
    };
    private FloatBuffer vertexBuffer;

    private float vertices[] = {
            -0.0f, -0.0f, 0.0f,        // V1 - bottom left
            -0.0f, 1.0f, 0.0f,        // V2 - top left
            1.0f, -0.0f, 0.0f,        // V3 - bottom right
            1.0f, 1.0f, 0.0f         // V4 - top right
    };

    public Figure(Type type, Color color, int x, int y) {
        this.type = type;
        this.color = color;
        setPosition(x, y);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        byteBuffer = ByteBuffer.allocateDirect(texture.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuffer.asFloatBuffer();
        textureBuffer.put(texture);
        textureBuffer.position(0);
    }

    /** The texture pointer */
    private int[] textures = new int[1];

    public void loadGLTexture(GL10 gl, Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), type.getType());
        gl.glGenTextures(1, textures, 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
    }

    public void draw(GL10 gl) {
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        if (color == Color.WHITE) gl.glColor4f(1f, 1f, 1f, 1f);
        else gl.glColor4f(0, 0, 0, 1f);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        gl.glFrontFace(GL10.GL_CW);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
        gl.glTranslatef(this.x - 4, this.y - 3, 0);
        gl.glScalef(1f, -1f, 1f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);
        gl.glScalef(1f, -1f, 1f);
        gl.glTranslatef(-this.x + 4, -this.y +3, 0);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(GL10.GL_TEXTURE_2D);
        gl.glDisable(GL10.GL_BLEND);
    }

    
}
