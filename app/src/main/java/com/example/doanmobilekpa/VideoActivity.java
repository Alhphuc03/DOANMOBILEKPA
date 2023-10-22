package com.example.doanmobilekpa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;

public class VideoActivity extends AppCompatActivity  {
    private VideoView videoView;
    Movie movie;
    private ProgressDialog progressDialog;

    private boolean isFullscreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_video);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Intent intent = getIntent();
        String Id = intent.getStringExtra("Id");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Movie").document(Id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        // Lấy đường dẫn của video trong kho lưu trữ của Firebase Storage
                        String videoPath = documentSnapshot.getString("Video");

                        // Hiển thị video trong VideoView
                        videoView = (VideoView) findViewById(R.id.fullScreen);
                        videoView.setVideoURI(Uri.parse(videoPath));

                        // Hiển thị dialog tiến trình khi đang tải xuống video
                        progressDialog = new ProgressDialog(VideoActivity.this);
                        progressDialog.setMessage("Đang tải video...");
                        progressDialog.show();
                        // Sự kiện nhấn nút play video
                        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                // Ẩn dialog tiến trình khi tải xuống thành công
                                progressDialog.dismiss();
                                // Bắt đầu phát video\
                                MediaController mediaController = new MediaController(VideoActivity.this);
                                mediaController.setAnchorView(videoView);

                                // Thiết lập MediaController cho VideoView
                                videoView.setMediaController(mediaController);
                                videoView.start();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Hiển thị thông báo lỗi khi không tìm thấy tài liệu video
                        Toast.makeText(VideoActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

