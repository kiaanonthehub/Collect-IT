package com.kiaan.collect_it.ui.progressbar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kiaan.collect_it.R;
import com.kiaan.collect_it.databinding.FragmentHomeBinding;
import com.kiaan.collect_it.ui.progresschart.ProgressChartData;

import java.util.ArrayList;
import java.util.List;

import Model.CURRENT_USER;
import Model.Category;
import Model.CategoryItem;
import Model.Item;

public class progressBarFragment extends Fragment {
    static ArrayList<Category> lstCategory = new ArrayList<>();
    static ArrayList<Item> lstItem = new ArrayList<>();
    static String selectedCat = null, displayPercentage = null;
    static int PercentageView = 0;
    Spinner spnViewCatProgress;
    Button btnViewCatProgress;
    ProgressBar progress;
    TextView tvProgress;
    TextView holder;
    TextView status;
    ImageView Badge;
    private FragmentHomeBinding binding;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        RotateAnimation anim = new RotateAnimation(0f, 350f, 15f, 15f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(700);


        View view = inflater.inflate(R.layout.fragment_progressbar, parent, false);

        spnViewCatProgress = view.findViewById(R.id.spinnerSelectProgress);
        btnViewCatProgress = view.findViewById(R.id.buttonViewCategoryProgress);
        progress = view.findViewById(R.id.progressBarCategory);
        tvProgress = view.findViewById(R.id.textViewCategoryPercent);
        Badge = view.findViewById(R.id.imageview50);
        holder = view.findViewById(R.id.textView7);
        status = view.findViewById(R.id.statusText);

        progress.setProgress(0, true);
        tvProgress.setText("% Complete");
        lstCategory.clear();
        lstItem.clear();

        // populate the spinner box
        // get database reference
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference categoryRef = rootRef.child("User").child(CURRENT_USER.displayName);

        // set listener to retrieve data from realtime database
        categoryRef.child("Category").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // declare temp arraylist
                final List<String> list = new ArrayList<>();

                // retrieve data from real time database
                for (DataSnapshot addressSnapshot : dataSnapshot.getChildren()) {
                    String value = addressSnapshot.getKey();
                    if (value != null) {
                        // add value to the list
                        list.add(value);
                    }
                }
                // declare and initialise Array Adapter to bind values to the spinner in real time
                ArrayAdapter<String> addressAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, list);
                addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnViewCatProgress.setAdapter(addressAdapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnViewCatProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Badge.setVisibility(View.VISIBLE);
                try {
                    PercentageView = 0;
                    selectedCat = spnViewCatProgress.getSelectedItem().toString();
                    progress.setProgress(0, true);
                    tvProgress.setText("% Complete");
                    GetChartData();
                } catch (java.lang.NullPointerException x) {
                    Toast.makeText(getActivity(), "Please select a Category from the dropdown", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    }

    private void GetChartData() {
        // get database reference
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference categoryRef = rootRef.child("User").child(CURRENT_USER.displayName);

        lstCategory.clear();
        lstItem.clear();


        // get category db data
        // set listener to retrieve data from realtime database
        categoryRef.child("Category").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // retrieve data from real time database
                for (DataSnapshot addressSnapshot : dataSnapshot.getChildren()) {
                    lstCategory.add(addressSnapshot.getValue(Category.class));
                }
                // Item
                DatabaseReference ItemRef = rootRef.child("Collections").child(CURRENT_USER.displayName);
                ItemRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot addressSnapshot : snapshot.getChildren()) {
                            lstItem.add(addressSnapshot.getValue(Item.class));
                        }
                        GetCategoryItemCount();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void GetCategoryItemCount() {
        // declare and initialise values
        int count = 0, goal;
        double percentage;
        goal = 0;
        percentage = 0;
        PercentageView = 0;

        // iterate through the lists - nested
        for (Category x : lstCategory) {
            for (Item y : lstItem) {
                if (x.getName().equals(y.getCategory())) {
                    if (selectedCat.equals(x.getName()) && selectedCat.equals(y.getCategory())) {
                        count++;
                        goal = Integer.parseInt(x.getGoal());
                    }
                }
            }

            // calculate the percentage of the progress bar
            percentage = (double) count / (double) goal;
            percentage = percentage * 100.0;
            PercentageView = (int) percentage;
            displayPercentage = percentage + "% Complete";
            if (PercentageView != 0) {
                progress.setProgress(PercentageView, true);
                tvProgress.setText(displayPercentage);
            }

            if (PercentageView <= 25) {
                Badge.setImageResource(0);

                RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setInterpolator(new LinearInterpolator());
                anim.setRepeatCount(Animation.INFINITE);
                anim.setDuration(700);
                Badge.startAnimation(anim);
                Badge.setImageResource(R.drawable.badge1);

                Badge.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Badge.setAnimation(null);
                    }
                }, 2500);
                status.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        status.setText("You are Level 4 Milestone");
                    }
                }, 3000);
            }


            if (PercentageView > 25 && PercentageView <= 50) {
                Badge.setImageResource(0);
                RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setInterpolator(new LinearInterpolator());
                anim.setRepeatCount(Animation.INFINITE);
                anim.setDuration(700);
                Badge.startAnimation(anim);
                Badge.setImageResource(R.drawable.badge2);

                Badge.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Badge.setAnimation(null);
                    }
                }, 2500);
                status.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        status.setText("You are Level 3 Milestone");
                    }
                }, 3000);
            }



            if (PercentageView > 50 && PercentageView <= 75) {
                Badge.setImageResource(0);
                RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setInterpolator(new LinearInterpolator());
                anim.setRepeatCount(Animation.INFINITE);
                anim.setDuration(700);
                Badge.startAnimation(anim);
                Badge.setImageResource(R.drawable.badge3);

                Badge.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Badge.setAnimation(null);
                    }
                }, 2500);
                status.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        status.setText("You are Level 2 Milestone");
                    }
                }, 3000);

            }



            if (PercentageView > 75 && PercentageView <= 100) {
                Badge.setImageResource(0);
                RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setInterpolator(new LinearInterpolator());
                anim.setRepeatCount(Animation.INFINITE);
                anim.setDuration(700);
                Badge.startAnimation(anim);
                Badge.setImageResource(R.drawable.badge4);

                Badge.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Badge.setAnimation(null);
                    }
                }, 3000);

                status.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        status.setText("You are Level 1 Milestone");
                    }
                }, 2000);
            }


        }
    }
}