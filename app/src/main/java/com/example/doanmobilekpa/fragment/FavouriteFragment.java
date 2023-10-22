package com.example.doanmobilekpa.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
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
import android.view.Window;
import android.view.WindowManager;

import com.example.doanmobilekpa.DetailActivity;
import com.example.doanmobilekpa.FavoriteAdapter;
import com.example.doanmobilekpa.Movie;
import com.example.doanmobilekpa.MovieAdapter;
import com.example.doanmobilekpa.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouriteFragment extends Fragment implements FavoriteAdapter.UserCallback {


    RecyclerView recyclerViews;
    ArrayList<Movie> movieArrayList;
    FavoriteAdapter favoriteAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavouriteFragment newInstance(String param1, String param2) {
        FavouriteFragment fragment = new FavouriteFragment();
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

        View v = inflater.inflate(R.layout.fragment_favourite, container, false);
        recyclerViews = v.findViewById(R.id.rvMovie);

        recyclerViews.setLayoutManager(new LinearLayoutManager(container.getContext()));

        movieArrayList = new ArrayList<Movie>();

        favoriteAdapter = new FavoriteAdapter(movieArrayList,  this);

        recyclerViews.setAdapter(favoriteAdapter);
        EventChangeListener();

        return v;
    }
    @Override
    public void onResume() {
        super.onResume();
        // Hide the ActionBar
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }
    private void EventChangeListener() {
        db.collection("Movie").whereEqualTo("Favourite",true)
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
                            favoriteAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
    @Override
    public void onItemClick(String Name, String Description, String Category, String ReleaseDate, String Time , String Image ,String Id) {
        Intent i= new Intent(getActivity(), DetailActivity.class);
        i.putExtra("Name",Name);
        i.putExtra("Description",Description);
        i.putExtra("Time",Time);
        i.putExtra("Category",Category);
        i.putExtra("ReleaseDate",ReleaseDate);
        i.putExtra("Image",Image);
        i.putExtra("Id",Id);
        startActivity(i);
    }

}