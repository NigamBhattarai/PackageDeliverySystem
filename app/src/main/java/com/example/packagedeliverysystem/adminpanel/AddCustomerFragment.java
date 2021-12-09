package com.example.packagedeliverysystem.adminpanel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.packagedeliverysystem.R;
import com.example.packagedeliverysystem.models.Customer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddCustomerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCustomerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    SharedPreferences pref;

    Button SaveCustomerBtn;
    View rootView;
    ImageButton EditCustomerBtn;
    EditText AddCustomerName, AddCustomerAddress, AddCustomerPhone;
    RadioButton AddCustomerTrustYes, AddCustomerTrustNo;
    TextView AddCustomerTitle;
    AlertDialog.Builder builderSingle;
    Customer customer;
    ArrayList<JSONObject> allCustomers;
    Boolean isEdit;
    Integer selectedCustomerId;


    private String mParam1;
    private String mParam2;

    public AddCustomerFragment() {
        // Required empty public constructor
    }

    public static AddCustomerFragment newInstance(String param1, String param2) {
        AddCustomerFragment fragment = new AddCustomerFragment();
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
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.slide_left));
        setExitTransition(inflater.inflateTransition(R.transition.slide_right));
        pref = getActivity().getSharedPreferences("user_details",getActivity().MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_customer, container, false);

        SaveCustomerBtn = rootView.findViewById(R.id.saveCustomerBtn);
        EditCustomerBtn = rootView.findViewById(R.id.editCustomerBtn);
        AddCustomerTitle = rootView.findViewById(R.id.addCustomerTitle);
        AddCustomerName = rootView.findViewById(R.id.addCustomerName);
        AddCustomerAddress = rootView.findViewById(R.id.addCustomerAddress);
        AddCustomerPhone = rootView.findViewById(R.id.addCustomerPhone);
        AddCustomerTrustYes = rootView.findViewById(R.id.addCustomerTrustYes);
        AddCustomerTrustNo = rootView.findViewById(R.id.addCustomerTrustNo);
        builderSingle = new AlertDialog.Builder(rootView.getContext());
        customer = new Customer(rootView.getContext());
        isEdit = false;
        selectedCustomerId = -1;

        initSelectCustomer();
        checkIsUpdate();
        saveCustomerClicked();
        editCustomerClicked();
        return rootView;
    }
    private void checkIsUpdate() {
        if(isEdit) {
            Customer selectedCustomer = new Customer(selectedCustomerId, rootView.getContext());
            AddCustomerTitle.setText("Update a customer");
            AddCustomerName.setText(selectedCustomer.getName());
            AddCustomerAddress.setText(selectedCustomer.getAddress());
            AddCustomerPhone.setText(selectedCustomer.getPhone());
            AddCustomerTrustYes.setChecked((selectedCustomer.getVerified().equals("1")));
            AddCustomerTrustNo.setChecked((selectedCustomer.getVerified().equals("0")));
            EditCustomerBtn.setBackgroundResource(R.drawable.ic_add_foreground);
        }
    }
    private void saveCustomerClicked() {
        SaveCustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, address, phone;
                Boolean trust;
                name = AddCustomerName.getText().toString().trim();
                address = AddCustomerAddress.getText().toString().trim();
                phone = AddCustomerPhone.getText().toString().trim();
                trust = AddCustomerTrustYes.isChecked();
                if(name.length() > 0 && address.length() > 0 && phone.length() > 0 && (AddCustomerTrustYes.isChecked() || AddCustomerTrustNo.isChecked())) {
                    if(isEdit) {
                        //For Editing the selected customer
                        System.out.println("HEREEE");
                        Customer customer = new Customer(selectedCustomerId, rootView.getContext());
                        customer.update(selectedCustomerId, name, address, phone, trust, pref.getInt("userid", -1));
                        Toast.makeText(rootView.getContext(), "Customer Updated Successfully.", Toast.LENGTH_LONG).show();
                        clearAllFields();
                    }
                    else {
                        //For Adding a new customer
                        Customer customer = new Customer(getActivity().getApplicationContext());
                        customer.insert(name, address, phone, trust, pref.getInt("userid", -1));
                        Toast.makeText(rootView.getContext(), "Customer Added Successfully.", Toast.LENGTH_LONG).show();
                        clearAllFields();
                    }
                    initSelectCustomer();
                }
                    else {
                    Toast.makeText(rootView.getContext(), "Please don't leave any empty spaces.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void clearAllFields() {
        isEdit = false;
        selectedCustomerId = -1;
        resetFragment();
    }
    private void resetFragment() {
        EditCustomerBtn.setBackgroundResource(R.drawable.ic_edit_foreground);
        AddCustomerTitle.setText("Add a Customer");
        AddCustomerName.setText("");
        AddCustomerAddress.setText("");
        AddCustomerPhone.setText("");
        AddCustomerTrustYes.setChecked(false);
        AddCustomerTrustNo.setChecked(false);
    }
    private void editCustomerClicked() {
        EditCustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEdit) {
                    clearAllFields();
                }
                else {
                    builderSingle.show();
                }
            }
        });
    }


    private void initSelectCustomer() {
        builderSingle.setTitle("Select a customer:-");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.select_dialog_singlechoice);
        allCustomers = customer.getAllAsJSON();
        try {
            for (JSONObject jsonObject : allCustomers) {
                    arrayAdapter.add(jsonObject.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                isEdit = true;
                try {
                    selectedCustomerId = allCustomers.get(i).getInt("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                checkIsUpdate();
            }
        });
    }
}