package com.kiaan.collect_it.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.kiaan.collect_it.R;
import com.kiaan.collect_it.databinding.ActivityNavigationBinding;
import com.kiaan.collect_it.ui.create_item.CreateItemFragment;

import Model.CURRENT_USER;

public class NavigationActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private TextView tvDisplayName, tvDisplayEmail;
    public static final int CAMERA_PERM_CODE = 101;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.kiaan.collect_it.databinding.ActivityNavigationBinding binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarNavigation.toolbar);


        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_item,
                R.id.nav_logout, R.id.nav_create_collection,
                R.id.nav_create_item, R.id.nav_about_us,
                R.id.nav_contact_us, R.id.nav_faq)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        ActivityCompat.requestPermissions(NavigationActivity.this, new String[] {Manifest.permission.CAMERA}, 101);
        //ActivityCompat.requestPermissions(NavigationActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 102);
        // update the header view
        updateHeaderView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void updateHeaderView()
    {
        NavigationView mNavigationView = findViewById(R.id.nav_view);
        View headerView = mNavigationView.getHeaderView(0);
        // get user name and email textViews
        TextView userName = headerView.findViewById(R.id.textViewDisplayName);
        TextView userEmail = headerView.findViewById(R.id.textViewDisplayEmail);
        // set user name and email
        //userName.setText(CURRENT_USER.displayName);
        //userEmail.setText(CURRENT_USER.email);
        //Google User
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestId().requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount != null){
            userName.setText(signInAccount.getDisplayName());
            userEmail.setText(signInAccount.getEmail());
        }
        else {
            userName.setText(CURRENT_USER.displayName);
            userEmail.setText(CURRENT_USER.email);
        }
    }
}
/*
Code Attribution
Author : Ebi Igweze
Subject : Changed name and email on navigation drawer display profil like playstore [duplicate]
Code available : https://stackoverflow.com/questions/51686113/changed-name-and-email-on-navigation-drawer-display-profil-like-playstore [answered Aug 4, 2018 at 13:58]
Date accessed : [28/05/2022]
 */
