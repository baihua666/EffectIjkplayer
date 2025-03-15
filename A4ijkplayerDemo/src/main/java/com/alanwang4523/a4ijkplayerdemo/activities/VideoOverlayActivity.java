/*
 * Copyright (C) 2015 Bilibili
 * Copyright (C) 2015 Zhang Rui <bbcallen@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alanwang4523.a4ijkplayerdemo.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;

import androidx.appcompat.app.AppCompatActivity;

import com.alanwang4523.a4ijkplayerdemo.FileUtil;
import com.alanwang4523.a4ijkplayerdemo.R;
import com.alanwang4523.a4ijkplayerdemo.gles.Drawable2d;
import com.alanwang4523.a4ijkplayerdemo.gles.GlUtil;
import com.alanwang4523.a4ijkplayerdemo.gles.Sprite2d;
import com.alanwang4523.a4ijkplayerdemo.gles.Texture2dProgram;
import com.alanwang4523.a4ijkplayerdemo.widget.IjkVideoContainer;
import com.alanwang4523.a4ijkplayerdemo.widget.IjkVideoView;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import tv.danmaku.ijk.media.player.filter.IjkFilter;

public class VideoOverlayActivity extends AppCompatActivity implements GLSurfaceView.Renderer {
    private static final String TAG = "VideoOverlayActivity";

    GLSurfaceView mGLSurfaceView;
    private IjkVideoContainer mVideoView;


    private Sprite2d mTargetImage;
    private Sprite2d mTargetVideo0;
    private Sprite2d mTargetVideo1;


    private Texture2dProgram mTexProgram;
    private float[] mDisplayProjectionMatrix = new float[16];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_overlay);

        try {
            testSurfaceView();
//            testTwoView();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoView != null) {
            mVideoView.release(true);
        }
    }

    //直接用两个视图叠加
    void testTwoView() throws IOException {
        String filePath = FileUtil.copyAssetFileToCache(this, "test1.mp4");
        IjkVideoView mVideoView = (IjkVideoView) findViewById(R.id.video_view);
        mVideoView.setVideoPath(filePath);
        mVideoView.start();

        String filePath1 = FileUtil.copyAssetFileToCache(this, "green_video.mp4");
        IjkVideoView mVideoView1 = (IjkVideoView) findViewById(R.id.video_view_1);
        mVideoView1.setVideoPath(filePath1);
        mVideoView1.setMattingGreenEnabled(true);
        mVideoView1.start();
    }

    void testSurfaceView() throws IOException {
        mGLSurfaceView = (GLSurfaceView) findViewById(R.id.glSurfaceView);
        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setRenderer(this);
        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        mGLSurfaceView.requestRender();

        mGLSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
        mGLSurfaceView.setZOrderOnTop(true);
        mGLSurfaceView.setBackgroundColor(Color.TRANSPARENT);
    }

    void initPlayer0() throws IOException {
        String filePath = FileUtil.copyAssetFileToCache(this, "test1.mp4");

        mVideoView = new IjkVideoContainer(this);
        mVideoView.setVideoPath(filePath);
        mVideoView.setMattingGreenEnabled(true);
        mVideoView.setFilter(new IjkFilter() {
            @Override
            public void onCreated() {

            }

            @Override
            public void onSizeChanged(int width, int height) {
                mTargetVideo0.setScale(width, height);

            }

            @Override
            public int onDrawFrame(int textureId) {
                mTargetVideo0.setTexture(textureId);
                return 0;
            }

            @Override
            public void onTexcoords(float[] texcoords) {

            }

            @Override
            public void onVertices(float[] vertices) {

            }

            @Override
            public void onRelease() {

            }

            @Override
            public boolean enable() {
                return true;
            }
        });
        mVideoView.start();
    }

    void initPlayer1() throws IOException {
        String filePath = FileUtil.copyAssetFileToCache(this, "green_video.mp4");

        IjkVideoContainer videoView = new IjkVideoContainer(this);
        videoView.setVideoPath(filePath);
        videoView.setMattingGreenEnabled(true);

        videoView.setFilter(new IjkFilter() {
            @Override
            public void onCreated() {

            }

            @Override
            public void onSizeChanged(int width, int height) {
                mTargetVideo1.setScale(width, height);
            }

            @Override
            public int onDrawFrame(int textureId) {
                mTargetVideo1.setTexture(textureId);
                return 0;
            }

            @Override
            public void onTexcoords(float[] texcoords) {

            }

            @Override
            public void onVertices(float[] vertices) {

            }

            @Override
            public void onRelease() {

            }

            @Override
            public boolean enable() {
                return true;
            }
        });
        videoView.start();
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        try {
            prepareGl(mGLSurfaceView.getHolder().getSurface());

            initPlayer0();
            initPlayer1();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        updateGL(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        draw();
    }

    private void prepareGl(Surface surface) {
        Log.d(TAG, "prepareGl");

        mTexProgram = new Texture2dProgram(Texture2dProgram.ProgramType.TEXTURE_2D);

        Drawable2d drawable2d = new Drawable2d(Drawable2d.Prefab.RECTANGLE);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_theme_play_arrow);

        int mImageTexture = GlUtil.createImageTexture(bitmap);

        mTargetImage = new Sprite2d(drawable2d);
        mTargetImage.setColor(0.9f, 0.1f, 0.1f);
        mTargetImage.setTexture(mImageTexture);
        mTargetImage.setScale(bitmap.getWidth(), bitmap.getHeight());

        mTargetVideo0 = new Sprite2d(drawable2d);
        mTargetVideo0.setMirrorY(true);

        mTargetVideo1 = new Sprite2d(drawable2d);
        mTargetVideo1.setMirrorY(true);

        // Set the background color.
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // Disable depth testing -- we're 2D only.
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);

        // Don't need backface culling.  (If you're feeling pedantic, you can turn it on to
        // make sure we're defining our shapes correctly.)
        GLES20.glDisable(GLES20.GL_CULL_FACE);
    }

    private void updateGL(int width, int height) {

        // Use full window.
        GLES20.glViewport(0, 0, width, height);

        // Simple orthographic projection, with (0,0) in lower-left corner.
        Matrix.orthoM(mDisplayProjectionMatrix, 0, 0, width, 0, height, -1, 1);

//        int smallDim = Math.min(width, height);
        mTargetImage.setPosition(width / 2.0f - 100, height / 2.0f + 100);
        mTargetVideo0.setPosition(width / 2.0f, height / 2.0f);
        mTargetVideo1.setPosition(width / 2.0f, height / 2.0f - 200);
    }

    private void draw() {
        GlUtil.checkGlError("draw start");

        // Clear to a non-black color to make the content easily differentiable from
        // the pillar-/letter-boxing.
        GLES20.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Textures may include alpha, so turn blending on.
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        if (mTargetVideo0.getTextureId() > 0) {
            mTargetVideo0.draw(mTexProgram, mDisplayProjectionMatrix);
        }
        if (mTargetVideo1.getTextureId() > 0) {
            mTargetVideo1.draw(mTexProgram, mDisplayProjectionMatrix);
        }
        mTargetImage.draw(mTexProgram, mDisplayProjectionMatrix);

        GLES20.glDisable(GLES20.GL_BLEND);

        GlUtil.checkGlError("draw done");
    }
}
