package com.example.doanmobilekpa.fragment;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.doanmobilekpa.MainActivity;
import com.example.doanmobilekpa.Movie;
import com.example.doanmobilekpa.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {
    Switch aSwitch , fontSizeButton;

    private Button btnIncreaseFont;
    private Spinner spinnerFontSize;
    BottomNavigationView bnv;

    private float fontSize;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
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
//        return inflater.inflate(R.layout.fragment_setting, container, false);
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        Button btnFontSize = view.findViewById(R.id.changeFontSize);
        btnFontSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFontSizeDialog();
            }
        });

        return view;
    }

    private void showFontSizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Chọn kích thước font chữ");

        String[] fontSizes = {"Nhỏ", "Bình Thường", "Lớn"};
        int checkedItem = 1;

        builder.setSingleChoiceItems(fontSizes, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        setFontSize(0.6f);
                        break;
                    case 1:
                        setFontSize(1.0f);
                        break;
                    case 2:
                        setFontSize(1.4f);
                        break;
                }
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setFontSize(float size) {
        fontSize = size;
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.fontScale = size;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        getActivity().recreate();
    }
    @Override
    public void onResume() {
        super.onResume();
        // Hide the ActionBar
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bnv = getActivity().findViewById(R.id.bottom_menu); // find BottomNavigationView in MainActivity
        aSwitch = view.findViewById(R.id.switch1);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int mode = isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
                AppCompatDelegate.setDefaultNightMode(mode);
                bnv.setItemTextColor(ResourcesCompat.getColorStateList(getResources(),
                        isChecked ? R.color.bottom_nav_text_color_dark : R.color.bottom_nav_text_color, null));
                bnv.setItemIconTintList(ResourcesCompat.getColorStateList(getResources(),
                        isChecked ? R.color.selector_bottom_nav_icon_color_dark : R.color.selector_bottom_nav_icon_color, null));
            }
        });
    }
}