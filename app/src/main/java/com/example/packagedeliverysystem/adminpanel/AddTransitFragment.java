package com.example.packagedeliverysystem.adminpanel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.example.packagedeliverysystem.models.Transit;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddTransitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTransitFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    SharedPreferences pref;

    Button SaveTransitButton;
    View rootView;
    ImageButton EditTransitBtn;
    EditText AddTransitName, AddTransitAddress, AddTransitPhone;
    TextView AddTransitTitle;
    AlertDialog.Builder builderSingle;
    Transit transit;
    ArrayList<JSONObject> allTransits;
    Boolean isEdit;
    Integer selectedTransitId;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddTransitFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddTransitFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddTransitFragment newInstance(String param1, String param2) {
        AddTransitFragment fragment = new AddTransitFragment();
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
        rootView = inflater.inflate(R.layout.fragment_add_transit, container, false);

        SaveTransitButton = rootView.findViewById(R.id.saveTransitBtn);
        EditTransitBtn = rootView.findViewById(R.id.editTransitBtn);
        AddTransitTitle = rootView.findViewById(R.id.addTransitTitle);
        AddTransitName = rootView.findViewById(R.id.addTransitName);
        AddTransitAddress = rootView.findViewById(R.id.addTransitAddress);
        AddTransitPhone = rootView.findViewById(R.id.addTransitPhone);
        builderSingle = new AlertDialog.Builder(rootView.getContext());
        transit = new Transit(rootView.getContext());
        isEdit = false;
        selectedTransitId = -1;

        initSelectTransit();
        checkIsUpdate();
        saveTransitClicked();
        editTransitClicked();
        return rootView;
    }


    private void saveTransitClicked() {
        SaveTransitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, address, phone;
                name = AddTransitName.getText().toString().trim();
                address = AddTransitAddress.getText().toString().trim();
                phone = AddTransitPhone.getText().toString().trim();
                if(name.length() > 0 && address.length() > 0 && phone.length() > 0) {
                    if(isEdit) {
                        //For Editing the selected customer
                        System.out.println("HEREEE");
                        Transit transit = new Transit(selectedTransitId, rootView.getContext());
                        transit.update(selectedTransitId, name, address, phone, "", pref.getInt("userid", -1));
                        Toast.makeText(rootView.getContext(), "Transit Updated Successfully.", Toast.LENGTH_LONG).show();
                        clearAllFields();
                    }
                    else {
                        //For Adding a new customer
                        Transit transit = new Transit(getActivity().getApplicationContext());
                        transit.insert(name, address, phone, "", pref.getInt("userid", -1));
                        Toast.makeText(rootView.getContext(), "Transit Added Successfully.", Toast.LENGTH_LONG).show();
                        clearAllFields();
                    }
                    initSelectTransit();
                }
                else {
                    Toast.makeText(rootView.getContext(), "Please don't leave any empty spaces.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void checkIsUpdate() {
        if(isEdit) {
            Transit selectedTransit = new Transit(selectedTransitId, rootView.getContext());
            AddTransitTitle.setText("Update a transit");
            AddTransitName.setText(selectedTransit.getName());
            AddTransitAddress.setText(selectedTransit.getFull_address());
            AddTransitPhone.setText(selectedTransit.getPhone());
            EditTransitBtn.setBackgroundResource(R.drawable.ic_add_foreground);
        }
    }

    private void clearAllFields() {
        isEdit = false;
        selectedTransitId = -1;
        resetFragment();
    }

    private void resetFragment() {
        EditTransitBtn.setBackgroundResource(R.drawable.ic_edit_foreground);
        AddTransitTitle.setText("Add a Transit");
        AddTransitName.setText("");
        AddTransitAddress.setText("");
        AddTransitPhone.setText("");
    }

    private void editTransitClicked() {
        EditTransitBtn.setOnClickListener(new View.OnClickListener() {
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

    private void initSelectTransit() {
        builderSingle.setTitle("Select a transit:-");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.select_dialog_singlechoice);
        allTransits = transit.getAllAsJSON();
        try {
            for (JSONObject jsonObject : allTransits) {
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
                    selectedTransitId = allTransits.get(i).getInt("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                checkIsUpdate();
            }
        });
    }
}