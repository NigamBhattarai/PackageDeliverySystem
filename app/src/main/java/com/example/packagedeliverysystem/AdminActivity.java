package com.example.packagedeliverysystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.packagedeliverysystem.adminpanel.AddCustomerFragment;
import com.example.packagedeliverysystem.adminpanel.AddPackageFragment;
import com.example.packagedeliverysystem.adminpanel.AddTransitFragment;
import com.example.packagedeliverysystem.adminpanel.TransitPackageFragment;
import com.example.packagedeliverysystem.adminpanel.ViewPackagesFragment;
import com.example.packagedeliverysystem.mainactivity.HomeFragment;
import com.example.packagedeliverysystem.models.Company;
import com.example.packagedeliverysystem.models.DeliveryStatus;

public class AdminActivity extends AppCompatActivity {

    SharedPreferences pref;
    Intent mainIntent;
    Company company;
    TextView companyName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        mainIntent = new Intent(AdminActivity.this, MainActivity.class);
        TransitionInflater inflater = TransitionInflater.from(getApplicationContext());
        getWindow().setEnterTransition(inflater.inflateTransition(R.transition.slide_left));
        getWindow().setExitTransition(inflater.inflateTransition(R.transition.slide_right));
        if(!(pref.contains("username") && pref.contains("password"))){
            logoutInside();
        }
        company = new Company(pref.getInt("userid",-1), getApplicationContext());
        initElements();
        fillGaps();
        createDeliveryStatusesIfNotExists();
        getSupportActionBar().hide();
    }

    public void logout(View view) {
        logoutInside();
    }

    public void gotoHome(View view) {
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
    }

    public void openAddPackage(View view) {
        AddPackageFragment addPackageFragment = new AddPackageFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.adminFragmentContainer, addPackageFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void openViewPackages(View view) {
        ViewPackagesFragment viewPackagesFragment = new ViewPackagesFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.adminFragmentContainer, viewPackagesFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void openTransitPackage(View view) {
        TransitPackageFragment transitPackageFragment = new TransitPackageFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.adminFragmentContainer, transitPackageFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void openAddCustomer(View view) {
        AddCustomerFragment addCustomerFragment = new AddCustomerFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.adminFragmentContainer, addCustomerFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void openAddTransit(View view) {
        AddTransitFragment addTransitFragment = new AddTransitFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.adminFragmentContainer, addTransitFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    private void initElements() {
        companyName = findViewById(R.id.companyName);
    }

    private void  fillGaps() {
        companyName.setText(company.getName());
    }


    private void logoutInside() {
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("username");
        editor.remove("password");
        editor.commit();
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
    }

    private void reloadActivity() {
        finish();
        startActivity(getIntent());
    }
    private void createDeliveryStatusesIfNotExists() {
        DeliveryStatus deliveryStatus = new DeliveryStatus(getApplicationContext());
        if(deliveryStatus.getAll().size()==0) {
//            Delivery Status severities descriptions:
//            1 : Normal
//            2 : Good
//            3 : Done
//            4 : Bad

            deliveryStatus.insert("Confirmed", 1);
            deliveryStatus.insert("Collected", 1);
            deliveryStatus.insert("Packed", 1);
            deliveryStatus.insert("Dispatched", 2);
            deliveryStatus.insert("Out For Delivery", 2);
            deliveryStatus.insert("Arrived", 2);
            deliveryStatus.insert("Delivered", 3);
            deliveryStatus.insert("Delivery Unsuccessful", 4);
            deliveryStatus.insert("Package Refused", 4);
            deliveryStatus.insert("Requested for return", 3);
            deliveryStatus.insert("Return declined", 4);
            deliveryStatus.insert("Return Accepted", 2);
            deliveryStatus.insert("Cancelled", 4);
            deliveryStatus.insert("Unknown", 4);
        }
    }
}