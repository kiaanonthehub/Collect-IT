package com.kiaan.collect_it.ui.progresschart;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.kiaan.collect_it.R;
import com.kiaan.collect_it.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class progressChartFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chart, parent, false);

        return view;

    }

    BarChart barchart;
    ArrayList<BarEntry> barEntriesArrayList;
    ArrayList<String > lableName;
    ArrayList<ProgressChartData> ProgressChartDataArrayList = new ArrayList<>();



    //ArrayList barArrayList;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);



        barchart = view.findViewById(R.id.barchart);
        barEntriesArrayList = new ArrayList<>();
        lableName  = new ArrayList<>();
       // ArrayList<ProgressChartData> ProgressChartDataArrayList = new ArrayList<>();


        fillProgressChartArrayList();

        for (int i =0; i < ProgressChartDataArrayList.size();i++){
            String categories = ProgressChartDataArrayList.get(i).getCategories();
            int number = ProgressChartDataArrayList.get(i).getNumber();
            barEntriesArrayList.add(new BarEntry(i,number));
            lableName.add(categories);

        }

        BarDataSet barDataSet = new BarDataSet(barEntriesArrayList,"categories");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);



      Description description = new Description();
       description.setText("categories");
        barchart.setDescription(description);

        BarData barData = new BarData(barDataSet);
        barchart.setData(barData);





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


}
