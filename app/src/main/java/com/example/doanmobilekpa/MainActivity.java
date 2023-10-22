package com.example.doanmobilekpa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.doanmobilekpa.fragment.FavouriteFragment;
import com.example.doanmobilekpa.fragment.HomeFragment;
import com.example.doanmobilekpa.fragment.SettingFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.UserCallback {

    RecyclerView recyclerViews;
    ArrayList<Movie> movieArrayList;
    MovieAdapter movieAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference moviesRef = db.collection("Movie");
    public  BottomNavigationView bnv;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bnv = findViewById(R.id.bottom_menu);
        bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                display(item.getItemId());
                return true;
            }
        });
        display(R.id.mnuHome);
    }

    void display(int id) {
        Fragment fragment = null;
        Activity activity = null;
        switch (id) {
            case R.id.mnuHome:
                fragment = new HomeFragment();
                getSupportActionBar().setTitle("KPA MOVIE");
                break;
            case R.id.mnFavourite:
                fragment = new FavouriteFragment();
                getSupportActionBar().hide();
                break;
            case R.id.mnSetting:
                fragment = new SettingFragment();
                getSupportActionBar().hide();
                break;
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.containter, fragment);
        ft.commit();
    }

    @Override
    public void onItemClick(String Name, String Description, String Category, String ReleaseDate, String Time, String Image, String Id , String Video) {
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra("Name", Name);
        i.putExtra("Description", Description);
        i.putExtra("Time", Time);
        i.putExtra("Category", Category);
        i.putExtra("ReleaseDate", ReleaseDate);
        i.putExtra("Image", Image);
        i.putExtra("Id", Id);
        i.putExtra("Video",Video);
        startActivity(i);
    }
    //Tìm kiếm
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("Tìm kiếm theo tên phim");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String searchText = newText.trim();

                if (!searchText.isEmpty()) {
//                    Query query = moviesRef.whereEqualTo("Name", searchText);
                    Query query = moviesRef.whereGreaterThanOrEqualTo("Name", searchText)
                            .whereLessThan("Name", searchText + "\uf8ff");

                    query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot querySnapshot) {
                            recyclerViews = findViewById(R.id.rvMovie);
                            recyclerViews.setLayoutManager (new LinearLayoutManager(MainActivity.this));
                            movieArrayList = new ArrayList<Movie>();
                            movieAdapter = new MovieAdapter( movieArrayList,MainActivity.this);
                            recyclerViews.setAdapter(movieAdapter);
                            for (DocumentSnapshot document : querySnapshot) {
                                Movie movie = document.toObject(Movie.class);
                                movieArrayList.add(movie);
                            }
                            movieAdapter.notifyDataSetChanged();
                        }
                    });
                }
                return true;
            }
        });
        return true;
    }

}

