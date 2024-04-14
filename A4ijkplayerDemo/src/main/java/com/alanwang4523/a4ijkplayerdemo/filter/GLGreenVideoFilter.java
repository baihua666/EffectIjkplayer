package com.alanwang4523.a4ijkplayerdemo.filter;

import android.content.Context;
import android.graphics.Rect;
import android.opengl.GLES20;

import com.alanwang4523.a4ijkplayerdemo.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import tv.danmaku.ijk.media.player.filter.IjkFilter;
import tv.danmaku.ijk.media.player.filter.ShaderUtils;

public class GLGreenVideoFilter implements IjkFilter {
    private Context context;

    private FloatBuffer vertexBuffer;
    private FloatBuffer textureVertexBuffer;

    private int programId = 0;

    private int aPositionHandle;
    private int uTextureSamplerHandle;
    private int aTextureCoordHandle;

    //初始化必需要为false,因为打开需要额外的设置
    private boolean enabled = false;

    public GLGreenVideoFilter(Context context){
        this.context = context;
        final float[] vertexData = {
                -1.0f, -1.0f,
                1.0f, -1.0f,
                -1.0f, 1.0f,
                1.0f, 1.0f
        };

        final float[] textureVertexData = {
                0.0f, 1.0f,
                1.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f
        };

        vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
        vertexBuffer.position(0);

        textureVertexBuffer = ByteBuffer.allocateDirect(textureVertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(textureVertexData);
        textureVertexBuffer.position(0);
    }

    @Override
    public boolean enable() {
        return enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void onCreated() {
        String vertexShader = ShaderUtils.readRawTextFile(context, R.raw.test_vertext_shader);
        String fragmentShader = ShaderUtils.readRawTextFile(context, R.raw.test_fragment_sharder);

        programId = ShaderUtils.createProgram(vertexShader, fragmentShader);

        aPositionHandle = GLES20.glGetAttribLocation(programId, "aPosition");

        uTextureSamplerHandle = GLES20.glGetUniformLocation(programId, "tex");
        aTextureCoordHandle = GLES20.glGetAttribLocation(programId, "aTexCoord");


        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    }

    private Rect rect = new Rect();
    @Override
    public void onSizeChanged(int width, int height) {
        rect.left = 0;
        rect.top = 0;
        rect.right = width;
        rect.bottom = height;
    }

    boolean createSurface = true;

    @Override
    public int onDrawFrame(int textureId) {
        if(!enabled){
            return textureId;
        }
        if(createSurface){
//            显示textureId的surface
            createSurface = false;
        }

        GLES20.glUseProgram(programId);
        GLES20.glViewport(0,0,rect.right,rect.bottom);
        GLES20.glClearColor(0.f, 0, 0f, 0);

        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glEnableVertexAttribArray(aPositionHandle);
        GLES20.glVertexAttribPointer(aPositionHandle, 2, GLES20.GL_FLOAT, false,
                8, vertexBuffer);

        GLES20.glEnableVertexAttribArray(aTextureCoordHandle);
        GLES20.glVertexAttribPointer(aTextureCoordHandle, 2, GLES20.GL_FLOAT, false, 8, textureVertexBuffer);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(uTextureSamplerHandle, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glUseProgram(GLES20.GL_NONE);
        return textureId;
    }

    public void onTexcoords(float[] texcoords){
        textureVertexBuffer.clear();
        textureVertexBuffer.put(texcoords);
        textureVertexBuffer.position(0);
    }
    public void onVertices(float[] vertices){
        vertexBuffer.clear();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
    }
    public void onRelease(){
        if(programId != 0){
            GLES20.glDeleteProgram(programId);
        }
    }

}
