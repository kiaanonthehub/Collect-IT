package com.kiaan.collect_it.ui.progresschart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kiaan.collect_it.R;
import com.kiaan.collect_it.databinding.FragmentHomeBinding;

import java.util.ArrayList;

import Model.CURRENT_USER;
import Model.Category;
import Model.CategoryItem;
import Model.Item;

public class progressChartFragment extends Fragment {

    static ArrayList<Category> lstCategory = new ArrayList<>();
    static ArrayList<String> lstKeyCategory = new ArrayList<>();
    static ArrayList<Item> lstItem = new ArrayList<>();
    static ArrayList<String> lstItemCount = new ArrayList<>();
    static ArrayList<Integer> lstData = new ArrayList<>();
    static ArrayList<CategoryItem> lstCatItem = new ArrayList<>();

    BarChart barchart;
    ArrayList<BarEntry> barEntriesArrayList;
    ArrayList<String> lableName;
    ArrayList<ProgressChartData> ProgressChartDataArrayList = new ArrayList<>();
    private FragmentHomeBinding binding;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chart, parent, false);


        return view;

    }


    //ArrayList barArrayList;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        barchart = view.findViewById(R.id.barchart);
        barEntriesArrayList = new ArrayList<>();
        lableName = new ArrayList<>();
        // ArrayList<ProgressChartData> ProgressChartDataArrayList = new ArrayList<>();


        fillProgressChartArrayList();

        for (int i = 0; i < ProgressChartDataArrayList.size(); i++) {
            String categories = ProgressChartDataArrayList.get(i).getCategories();
            int number = ProgressChartDataArrayList.get(i).getNumber();
            barEntriesArrayList.add(new BarEntry(i, number));
            lableName.add(categories);

        }

        BarDataSet barDataSet = new BarDataSet(barEntriesArrayList, "categories");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);


        Description description = new Description();
        description.setText("categories");
        barchart.setDescription(description);

        BarData barData = new BarData(barDataSet);
        barchart.setData(barData);

        GetChartData();

    }

    private void fillProgressChartArrayList() {
        ProgressChartDataArrayList.clear();
        ProgressChartDataArrayList.add(new ProgressChartData("cars", 30));
        ProgressChartDataArrayList.add(new ProgressChartData("games", 60));
        ProgressChartDataArrayList.add(new ProgressChartData("books", 40));
        ProgressChartDataArrayList.add(new ProgressChartData("recipes", 90));
        ProgressChartDataArrayList.add(new ProgressChartData("movies", 20));
        ProgressChartDataArrayList.add(new ProgressChartData("finance", 60));
    }

    private void GetChartData() {
        // get database reference
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference categoryRef = rootRef.child("User").child(CURRENT_USER.displayName);

        // get category db data
        // set listener to retrieve data from realtime database
        categoryRef.child("Category").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // retrieve data from real time database
                for (DataSnapshot addressSnapshot : dataSnapshot.getChildren()) {
                    lstCategory.add(addressSnapshot.getValue(Category.class));
                    lstKeyCategory.add(addressSnapshot.getKey());

                }
                GetCategoryItemCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // Item
        DatabaseReference ItemRef = rootRef.child("Collections").child(CURRENT_USER.displayName);
        ItemRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot addressSnapshot : snapshot.getChildren()) {
                    lstItem.add(addressSnapshot.getValue(Item.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void GetCategoryItemCount() {
        CategoryItem categoryItem ;

        int count = 0;
        for (String x : lstKeyCategory) {
            for (Item y : lstItem) {
                if (y.getCategory().equals(x)) {
                    count ++;
                }
            }

            categoryItem =  new CategoryItem(x,count);
            lstCatItem.add(categoryItem);
            count = 0;
        }
    }
}
