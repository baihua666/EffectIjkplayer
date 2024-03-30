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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alanwang4523.a4ijkplayerdemo.FileUtil;
import com.alanwang4523.a4ijkplayerdemo.R;
import com.alanwang4523.a4ijkplayerdemo.application.Settings;
import com.alanwang4523.a4ijkplayerdemo.widget.AndroidMediaController;
import com.alanwang4523.a4ijkplayerdemo.widget.IjkVideoView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MultiVideoActivity extends AppCompatActivity {
    private static final String TAG = "VideoActivity";

//    private String mVideoPath;
    private Uri    mVideoUri;

//    private AndroidMediaController mMediaController;
    private List<IjkVideoView> mVideoViewList = new ArrayList<>();
    private TextView mToastTextView;
    private TableLayout mHudView;
    private ViewGroup mRightDrawer;

    private Settings mSettings;
    private boolean mBackPressed;

    public static Intent newIntent(Context context, String videoPath, String videoTitle) {
        Intent intent = new Intent(context, MultiVideoActivity.class);
        intent.putExtra("videoPath", videoPath);
        intent.putExtra("videoTitle", videoTitle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player);

        mSettings = new Settings(this);

        // init player
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

        try {
            test();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void test() throws IOException {
        GridLayout layout = findViewById(R.id.gl_video);
        layout.setColumnCount(3);
        layout.setRowCount(3);

        String filePath = FileUtil.copyAssetFileToCache(this, "green_video.mp4");

        for (int i = 0; i < 9; i++) {
            GridLayout.Spec rowSpec = GridLayout.spec(i / 3, 1f);
            GridLayout.Spec columnSpec = GridLayout.spec(i % 3, 1f);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
            params.width = 0;
            params.height = 0;
            params.leftMargin = 2;
            params.rightMargin = 2;

            AndroidMediaController mMediaController = new AndroidMediaController(this, false);

            IjkVideoView mVideoView = new IjkVideoView(this);
            mVideoView.setMediaController(mMediaController);
            mVideoView.setVideoPath(filePath);
            mVideoView.start();
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                mVideoView.setBackgroundColor(getColor(R.color.ijk_color_blue_50));
//            }

//            Button btn = new Button(this);
            mVideoView.setLayoutParams(params);
            layout.addView(mVideoView);
        }
    }

    @Override
    public void onBackPressed() {
        mBackPressed = true;

        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();

        for(IjkVideoView mVideoView : mVideoViewList) {
            if (mBackPressed || !mVideoView.isBackgroundPlayEnabled()) {
                mVideoView.stopPlayback();
                mVideoView.release(true);
                mVideoView.stopBackgroundPlay();
            } else {
                mVideoView.enterBackground();
            }
        }

        IjkMediaPlayer.native_profileEnd();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.action_toggle_ratio) {
//            int aspectRatio = mVideoView.toggleAspectRatio();
//            String aspectRatioText = MeasureHelper.getAspectRatioText(this, aspectRatio);
//            mToastTextView.setText(aspectRatioText);
//            mMediaController.showOnce(mToastTextView);
//            return true;
//        } else if (id == R.id.action_toggle_player) {
//            int player = mVideoView.togglePlayer();
//            String playerText = IjkVideoView.getPlayerText(this, player);
//            mToastTextView.setText(playerText);
//            mMediaController.showOnce(mToastTextView);
//            return true;
//        } else if (id == R.id.action_toggle_render) {
//            int render = mVideoView.toggleRender();
//            String renderText = IjkVideoView.getRenderText(this, render);
//            mToastTextView.setText(renderText);
//            mMediaController.showOnce(mToastTextView);
//            return true;
//        } else if (id == R.id.action_show_info) {
//            mVideoView.showMediaInfo();
//        } else if (id == R.id.action_show_tracks) {
//            if (mDrawerLayout.isDrawerOpen(mRightDrawer)) {
//                Fragment f = getSupportFragmentManager().findFragmentById(R.id.right_drawer);
//                if (f != null) {
//                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                    transaction.remove(f);
//                    transaction.commit();
//                }
//                mDrawerLayout.closeDrawer(mRightDrawer);
//            } else {
//                Fragment f = TracksFragment.newInstance();
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.right_drawer, f);
//                transaction.commit();
//                mDrawerLayout.openDrawer(mRightDrawer);
//            }
//        }

        return super.onOptionsItemSelected(item);
    }

}
