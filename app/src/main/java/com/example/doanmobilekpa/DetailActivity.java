package com.example.doanmobilekpa;

import static android.app.PendingIntent.getActivity;
import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    TextView txDescriptionDetail, txNameDetail, txCategoryDetail, txTimeDetail, txReleaseDateDetail;
    ImageView imgImage;
    ImageButton imgBack;
    Button btnAdd, btnPlay;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private YouTubePlayerView youTubePlayerView;
    private String videoId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        txNameDetail = findViewById(R.id.txNameDetail);
        txTimeDetail = findViewById(R.id.txTimeDetail);
        txCategoryDetail = findViewById(R.id.txCategoryDetail);
        txReleaseDateDetail = findViewById(R.id.txReleaseDateDetail);
        txDescriptionDetail = findViewById(R.id.txDescriptionDetail);
        imgImage = findViewById(R.id.imgDetail);
        btnAdd = findViewById(R.id.btnAddFilm);
        btnPlay = findViewById(R.id.btnPlay);
        String Name = getIntent().getStringExtra("Name");
        String Time = getIntent().getStringExtra("Time");
        String Category = getIntent().getStringExtra("Category");
        String ReleaseDate = getIntent().getStringExtra("ReleaseDate");
        String Description = getIntent().getStringExtra("Description");
        String Image = getIntent().getStringExtra("Image");
        String Id = getIntent().getStringExtra("Id");
        String Video = getIntent().getStringExtra("Video");
        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        txNameDetail.setText(Name);
        txDescriptionDetail.setText(Description);
        txCategoryDetail.setText(Category);
        txTimeDetail.setText(Time);
        txReleaseDateDetail.setText(ReleaseDate);
        Picasso.get()
                .load(Image)
                .fit()
                .into(imgImage);

        // Thêm vào danh sách yêu thích
        btnAdd = findViewById(R.id.btnAddFilm);
        db = FirebaseFirestore.getInstance();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cập nhật trạng thái yêu thích của tài liệu
                DocumentReference docRef = db.collection("Movie").document(Id);
                docRef.update("Favourite", true)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(DetailActivity.this, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DetailActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        btnPlay = findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, VideoActivity.class);
                intent.setData(Uri.parse("videoPath"));
                intent.putExtra("Id", Id);
                startActivity(intent);
            }
        });
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Movie")
                .document(Id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    videoId = documentSnapshot.getString("Trailer");
                    if (videoId != null) {
                        getLifecycle().addObserver(youTubePlayerView);
                        youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> {
                            youTubePlayer.cueVideo(videoId, 0f);
                        });
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Get video ID failed.", e));
    }

}
