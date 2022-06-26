package com.kiaan.collect_it.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.kiaan.collect_it.R;
import com.kiaan.collect_it.databinding.FragmentHomeBinding;
import com.kiaan.collect_it.ui.NavigationActivity;
import com.kiaan.collect_it.ui.about_us.AboutUsFragment;
import com.kiaan.collect_it.ui.contact_us.ContactUsFragment;
import com.kiaan.collect_it.ui.create_collection.CreateCollectionFragment;
import com.kiaan.collect_it.ui.create_item.CreateItemFragment;
import com.kiaan.collect_it.ui.faq.FAQFragment;
import com.kiaan.collect_it.ui.item.ItemFragment;
import com.kiaan.collect_it.ui.progressbar.progressBarFragment;
import com.kiaan.collect_it.ui.progresschart.progressChartFragment;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private CardView filter, graph, bar, cCollection, cItem, about, contact, faq;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, parent, false);

        // initialise java variables
        filter = view.findViewById(R.id.filter_btn);
        graph = view.findViewById(R.id.graph_btn);
        bar = view.findViewById(R.id.bar_btn);
        cCollection = view.findViewById(R.id.createCollection_btn);
        cItem = view.findViewById(R.id.createItem_Btn);
        about = view.findViewById(R.id.About_Btn);
        contact = view.findViewById(R.id.Contact_Btn);
        faq = view.findViewById(R.id.faq_Btn);

        filter.setOnClickListener(view1 -> {
            // Create new fragment and transaction
            Fragment newFragment = new ItemFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack if needed
            transaction.replace(R.id.nav_host_fragment_content_navigation, newFragment);
            transaction.addToBackStack(null);

           // Commit the transaction
            transaction.commit();
        });

        graph.setOnClickListener(view1 -> {
            // Create new fragment and transaction
            Fragment newFragment = new progressChartFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack if needed
            transaction.replace(R.id.nav_host_fragment_content_navigation, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        });

        bar.setOnClickListener(view1 -> {
            // Create new fragment and transaction
            Fragment newFragment = new progressBarFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack if needed
            transaction.replace(R.id.nav_host_fragment_content_navigation, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        });

        cCollection.setOnClickListener(view1 -> {
            // Create new fragment and transaction
            Fragment newFragment = new CreateCollectionFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack if needed
            transaction.replace(R.id.nav_host_fragment_content_navigation, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        });

        cItem.setOnClickListener(view1 -> {
            // Create new fragment and transaction
            Fragment newFragment = new CreateItemFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack if needed
            transaction.replace(R.id.nav_host_fragment_content_navigation, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        });

        about.setOnClickListener(view1 -> {
            // Create new fragment and transaction
            Fragment newFragment = new AboutUsFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack if needed
            transaction.replace(R.id.nav_host_fragment_content_navigation, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        });

        contact.setOnClickListener(view1 -> {
            // Create new fragment and transaction
            Fragment newFragment = new ContactUsFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack if needed
            transaction.replace(R.id.nav_host_fragment_content_navigation, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        });

        faq.setOnClickListener(view1 -> {
            // Create new fragment and transaction
            Fragment newFragment = new FAQFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack if needed
            transaction.replace(R.id.nav_host_fragment_content_navigation, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        });


        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    }


}