package com.example.doanmobilekpa;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {
    ArrayList<Movie> movieArrayList;
    UserCallback userCallback;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MovieAdapter(ArrayList<Movie> movieArrayList, UserCallback userCallback) {
        this.movieArrayList = movieArrayList;
        this.userCallback = userCallback;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_cell,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MyViewHolder holder, int position) {
        Movie movie = movieArrayList.get(position);
        Picasso.get()
                .load(movie.getImage())
                .fit()
                .into(holder.imgThumb);
        holder.txName.setText(movie.getName());
        NumberFormat numberFormatter = NumberFormat.getInstance(Locale.US);
        numberFormatter.setMinimumFractionDigits(2);
        if (movie.isFavourite()==false) {
            holder.btnFavourite.setVisibility(View.GONE);
        } else {
            holder.btnFavourite.setVisibility(View.VISIBLE);
        }
        if(movie.isFavourite()==true){
            holder.btnFavourite.setColorFilter(Color.RED);
        }
        else if (movie.isFavourite()==false) {
            holder.btnFavourite.setColorFilter(Color.GRAY);
        }
        holder.itemView.setOnClickListener(view -> userCallback.onItemClick(
                movie.getName(),
                movie.getDescription(),
                movie.getCategory(),
                movie.getReleaseDate(),
                movie.getTime(),
                movie.getImage(),
                movie.getId(),
                movie.getVideo()
        ));
        holder.btnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map=new HashMap<>();
                map.put("Favourite",true);
                map.put("Image",movie.getImage());
                map.put("Name",movie.getName());
                map.put("Description",movie.getDescription());
                map.put("Time",movie.getTime());
                map.put("Id",movie.getId());
                db.collection("Movie").document(movie.getId())
                        .set(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView  txName , txCategory , txTime ,txReleaseDate,txDescription;
        ImageView imgThumb;

        ImageButton btnFavourite;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txName = itemView.findViewById(R.id.txName);
            txCategory = itemView.findViewById(R.id.txCategory);
            txTime = itemView.findViewById(R.id.txTime);
            imgThumb = itemView.findViewById(R.id.imgThumb);
            txReleaseDate = itemView.findViewById(R.id.txReleaseDate);
            btnFavourite = itemView.findViewById(R.id.btnFavourite);
        }
    }
    public interface UserCallback{
        void onItemClick(String name, String description, String category, String releaseDate, String time, String Image,String Id, String Video);
    }
}