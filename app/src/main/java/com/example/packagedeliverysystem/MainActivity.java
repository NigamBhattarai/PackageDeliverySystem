package com.example.packagedeliverysystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.example.packagedeliverysystem.mainactivity.LoginFragment;
import com.example.packagedeliverysystem.mainactivity.DetailsFragment;
import com.example.packagedeliverysystem.mainactivity.HomeFragment;
import com.example.packagedeliverysystem.mainactivity.TrackFragment;
import com.example.packagedeliverysystem.models.Company;
import com.example.packagedeliverysystem.models.Customer;
import com.example.packagedeliverysystem.models.DeliveryStatus;
import com.example.packagedeliverysystem.models.Package;
import com.example.packagedeliverysystem.models.Timeline;
import com.example.packagedeliverysystem.models.Transit;

public class MainActivity extends AppCompatActivity {

    SharedPreferences pref;
    EditText username, password;
    Intent adminIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        adminIntent = new Intent(MainActivity.this, AdminActivity.class);
        TransitionInflater inflater = TransitionInflater.from(getApplicationContext());
        getWindow().setEnterTransition(inflater.inflateTransition(R.transition.slide_left));
        getWindow().setExitTransition(inflater.inflateTransition(R.transition.slide_right));
        createCompanyIfNotExists();
        initAllTables();
        getSupportActionBar().hide();
    }
    public void backToHome(View view) {
        HomeFragment homeFragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFragmentContainer, homeFragment);
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
        fragmentTransaction.commit();
    }
    public void openLogin(View view) {
        if(pref.contains("username") && pref.contains("password")){
            startActivity(adminIntent);
        }
        else {
            LoginFragment loginFragment = new LoginFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.mainFragmentContainer, loginFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
    public void login(View view) {
        SharedPreferences.Editor editor = pref.edit();
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        String user = username.getText().toString();
        String pwd = password.getText().toString();
        Integer userId = validateUser(user, pwd);
        if(userId!=-1) {
            editor.putInt("userid", userId);
            editor.putString("username", user);
            editor.putString("password", pwd);
            editor.commit();
            Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_LONG).show();
            openHomeFragment();
            startActivity(adminIntent);
        }
        else {
            ShapeDrawable shape = new ShapeDrawable(new RectShape());
            shape.getPaint().setColor(Color.RED);
            shape.getPaint().setStyle(Paint.Style.STROKE);
            shape.getPaint().setStrokeWidth(3);
            username.setBackground(shape);
            password.setBackground(shape);
            Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_LONG).show();
        }
    }
    private void initAllTables() {
        Company company = new Company(getApplicationContext());
        Customer customer = new Customer(getApplicationContext());
        DeliveryStatus deliveryStatus = new DeliveryStatus(getApplicationContext());
        Transit transit = new Transit(getApplicationContext());
        Package package1 = new Package(getApplicationContext());
        Timeline timeline = new Timeline(getApplicationContext());
        company.onCreate(company.getWritableDatabase());
        customer.onCreate(customer.getWritableDatabase());
        deliveryStatus.onCreate(deliveryStatus.getWritableDatabase());
        transit.onCreate(transit.getWritableDatabase());
        package1.onCreate(package1.getWritableDatabase());
        timeline.onCreate(timeline.getWritableDatabase());
    }
    private void openHomeFragment() {
        HomeFragment homeFragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFragmentContainer, homeFragment);
        fragmentManager.popBackStack();
        fragmentTransaction.commit();
    }
    private Integer validateUser(String username, String password) {
        Company company = new Company(getApplicationContext());
        return company.validateUser(username, password);
    }
    private void createCompanyIfNotExists() {
        Company company = new Company(getApplicationContext());
        if(company.getAll().size()==0) {
            company.insert("Delivery Man", "#43a4fc", "", "9:00", "17:00", true, "inigambhattarai@gmail.com", "+977 9846065409", "delivery", "12345");
        }
        System.out.println(company.getAll());
    }
}