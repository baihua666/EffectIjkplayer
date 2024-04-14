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
import android.os.Bundle;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.alanwang4523.a4ijkplayerdemo.FileUtil;
import com.alanwang4523.a4ijkplayerdemo.R;
import com.alanwang4523.a4ijkplayerdemo.widget.AndroidMediaController;
import com.alanwang4523.a4ijkplayerdemo.widget.IjkVideoView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MultiVideoActivity extends AppCompatActivity {
    private static final String TAG = "MultiVideoActivity";

    private List<IjkVideoView> mVideoViewList = new ArrayList<>();

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
//        String filePath = FileUtil.copyAssetFileToCache(this, "test1.mp4");


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
            mVideoView.setMattingGreenEnabled(true);

            mVideoView.setLayoutParams(params);
            layout.addView(mVideoView);
            mVideoViewList.add(mVideoView);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        for (IjkVideoView videoView : mVideoViewList) {
            videoView.release(true);
        }
        mVideoViewList.clear();
    }
}
