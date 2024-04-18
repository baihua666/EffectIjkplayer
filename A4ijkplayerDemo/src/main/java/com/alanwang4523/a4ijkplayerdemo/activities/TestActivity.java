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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alanwang4523.a4ijkplayerdemo.FileUtil;
import com.alanwang4523.a4ijkplayerdemo.R;
import com.alanwang4523.a4ijkplayerdemo.widget.AndroidMediaController;
import com.alanwang4523.a4ijkplayerdemo.widget.IjkVideoView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);

        findViewById(R.id.btn_test_ijk_player).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTestIJKPlayer();
            }
        });

        findViewById(R.id.btn_test_green_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startTestGreenVideo();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        findViewById(R.id.btn_test_multi_green_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startMultiTest();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        findViewById(R.id.btn_test_video_overlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTestVideoOverlay();
            }
        });
    }

    void startTestIJKPlayer() {
        Intent intent = new Intent(this, FileExplorerActivity.class);
        startActivity(intent);
    }

    void startTestGreenVideo() throws IOException {
        String filePath = FileUtil.copyAssetFileToCache(this, "green_video.mp4");
        GreenVideoActivity.intentTo(this, filePath, "test");
    }

    void startMultiTest() throws IOException {
        Intent intent = new Intent(this, MultiVideoActivity.class);
        startActivity(intent);
    }

    //    void startTestVideoContainer() throws IOException {
//        String filePath = FileUtil.copyAssetFileToCache(this, "test1.mp4");
//        VideoContainerActivity.intentTo(this, filePath, "test");
//    }

    void startTestVideoOverlay() {
        Intent intent = new Intent(this, VideoOverlayActivity.class);
        startActivity(intent);
    }
}
