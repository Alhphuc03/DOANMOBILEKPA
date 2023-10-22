package com.example.doanmobilekpa.fragment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.doanmobilekpa.DetailActivity;
import com.example.doanmobilekpa.Movie;
import com.example.doanmobilekpa.MovieAdapter;
import com.example.doanmobilekpa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements MovieAdapter.UserCallback{

    RecyclerView recyclerViews;
    ArrayList<Movie> movieArrayList;
    MovieAdapter movieAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public TabLayout tabLayout;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerViews = v.findViewById(R.id.rvMovie);
        recyclerViews.setHasFixedSize(true);
        recyclerViews.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        movieArrayList = new ArrayList<Movie>();
        movieAdapter = new MovieAdapter(movieArrayList,this);

        recyclerViews.setAdapter(movieAdapter);
        db.collection("Movie")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {

                            Log.e( "Firestore error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                movieArrayList.add(dc.getDocument().toObject (Movie.class));
                            }
                            movieAdapter.notifyDataSetChanged();
                        }
                    }
                });
        tabLayout = v.findViewById(R.id.tabCategory);
//        tabLayout.setTabTextColors(defaultColor, Color.RED);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                tabLayout.setSelectedTabIndicatorColor(Color.RED);
                // Lấy vị trí của tab được chọn
                int position = tab.getPosition();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference collectionRef = db.collection("Movie");
                // Xử lý sự kiện tương ứng với tab được chọn
                switch (position) {
                    case 0:
                        db.collection("Movie")
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                        if (error != null) {

                                            Log.e( "Firestore error", error.getMessage());
                                            return;
                                        }
                                        for (DocumentChange dc : value.getDocumentChanges()) {
                                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                                movieArrayList.add(dc.getDocument().toObject (Movie.class));
                                            }
                                            movieAdapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                        break;
                    case 1:
                        Query query = collectionRef.whereEqualTo("Category", "Hành Động");
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    movieArrayList.clear();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        // Chuyển đổi tài liệu thành đối tượng Book
                                        Movie movies = document.toObject(Movie.class);
                                        movieArrayList.add(movies);
                                    }
                                    // Cập nhật danh sách sách vào adapter
                                    movieAdapter.notifyDataSetChanged();
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
                        break;
                    case 2:
                        Query query1 = collectionRef.whereEqualTo("Category", "Kinh Dị");
                        query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    movieArrayList.clear();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        // Chuyển đổi tài liệu thành đối tượng Book
                                        Movie movies = document.toObject(Movie.class);
                                        movieArrayList.add(movies);
                                    }
                                    // Cập nhật danh sách sách vào adapter
                                    movieAdapter.notifyDataSetChanged();
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
                        break;
                    case 3:
                        Query query2 = collectionRef.whereEqualTo("Category", "Anime");
                        query2.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    movieArrayList.clear();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        // Chuyển đổi tài liệu thành đối tượng Book
                                        Movie movies = document.toObject(Movie.class);
                                        movieArrayList.add(movies);
                                    }
                                    // Cập nhật danh sách sách vào adapter
                                    movieAdapter.notifyDataSetChanged();
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });;

                    default:
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Không làm gì
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Không làm gì
            }
        });

        return v;

    }
    @Override
    public void onResume() {
        super.onResume();
        // Hide the ActionBar
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
    @Override
    public void onItemClick(String Name, String Description, String Category, String ReleaseDate, String Time , String Image,String Id, String Video ) {
        Intent i= new Intent(getActivity(),DetailActivity.class);
        i.putExtra("Name",Name);
        i.putExtra("Description",Description);
        i.putExtra("Time",Time);
        i.putExtra("Category",Category);
        i.putExtra("ReleaseDate",ReleaseDate);
        i.putExtra("Image",Image);
        i.putExtra("Id",Id);
        i.putExtra("Video",Video);
        startActivity(i);
    }
}