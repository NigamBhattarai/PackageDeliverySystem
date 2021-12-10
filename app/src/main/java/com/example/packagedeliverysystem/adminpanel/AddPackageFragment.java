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
import com.example.packagedeliverysystem.models.DeliveryStatus;
import com.example.packagedeliverysystem.models.Package;
import com.example.packagedeliverysystem.models.Timeline;
import com.example.packagedeliverysystem.models.Transit;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPackageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPackageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    SharedPreferences pref;


    Button SavePackageBtn;
    View rootView;
    ImageButton EditPackageBtn, selectSenderBtn, selectReceiverBtn, selectStartTransitBtn, selectDeliveryTransitBtn;
    EditText AddPackageName, AddPackageSender, AddPackageReceiver, AddPackageStartTransit, AddPackageDeliverTo, AddPackageWeight, AddPackageNumberOfDays;
    RadioButton AddPackageSevereYes, AddPackageSevereNo;
    TextView AddPackageTitle;
    AlertDialog.Builder selectPackage, selectSender, selectReceiver, selectStartTransit, selectDeliveryTransit;
    Package sPackage;
    Customer customer;
    Transit transit;
    ArrayList<JSONObject> allPackages, allCustomers, allTransits;
    Boolean isEdit;
    Integer selectedPackageId, selectedSenderId, selectedReceiverId, selectedStartId, selectedDeliveryId;



    private String mParam1;
    private String mParam2;

    public AddPackageFragment() {
        // Required empty public constructor
    }

    public static AddPackageFragment newInstance(String param1, String param2) {
        AddPackageFragment fragment = new AddPackageFragment();
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
        rootView = inflater.inflate(R.layout.fragment_add_package, container, false);
        SavePackageBtn = rootView.findViewById(R.id.savePackageBtn);
        EditPackageBtn = rootView.findViewById(R.id.editPackageBtn);
        AddPackageTitle = rootView.findViewById(R.id.addPackageTitle);
        AddPackageName = rootView.findViewById(R.id.addPackageName);
        AddPackageSender = rootView.findViewById(R.id.addPackageSender);
        selectSenderBtn = rootView.findViewById(R.id.selectSenderBtn);
        AddPackageReceiver = rootView.findViewById(R.id.addPackageReceiver);
        selectReceiverBtn = rootView.findViewById(R.id.selectReceiverBtn);
        AddPackageStartTransit = rootView.findViewById(R.id.addPackageDeliveryFrom);
        selectStartTransitBtn = rootView.findViewById(R.id.selectStartTransitBtn);
        AddPackageDeliverTo = rootView.findViewById(R.id.addPackageDeliveryTo);
        selectDeliveryTransitBtn = rootView.findViewById(R.id.selectDeliveryTransitBtn);
        AddPackageSevereYes = rootView.findViewById(R.id.addPackageSevereYes);
        AddPackageSevereNo = rootView.findViewById(R.id.addPackageSevereNo);
        AddPackageWeight = rootView.findViewById(R.id.addPackageWeight);
        AddPackageNumberOfDays = rootView.findViewById(R.id.addPackageDeliveryDays);

        selectPackage = new AlertDialog.Builder(rootView.getContext());
        selectSender = new AlertDialog.Builder(rootView.getContext());
        selectReceiver = new AlertDialog.Builder(rootView.getContext());
        selectStartTransit = new AlertDialog.Builder(rootView.getContext());
        selectDeliveryTransit = new AlertDialog.Builder(rootView.getContext());

        sPackage = new Package(rootView.getContext());
        customer = new Customer(rootView.getContext());
        transit = new Transit(rootView.getContext());

        isEdit = false;
        selectedPackageId = -1;
        selectedSenderId = -1;
        selectedReceiverId = -1;
        selectedStartId = -1;
        selectedDeliveryId = -1;

        initSelectPackage();
        initSelectSender();
        initSelectReceiver();
        initSelectStartTransit();
        initSelectDeliveryTransit();

        checkIsUpdate();

        savePackageClicked();
        editPackageClicked();
        selectSenderClicked();
        selectReceiverClicked();
        selectStartTransitClicked();
        selectDeliveryTransitClicked();

        return rootView;
    }

    private void checkIsUpdate() {
        if(isEdit) {
            Package selectedPackage = new Package(selectedPackageId, rootView.getContext());

            AddPackageTitle.setText("Update a package");
            AddPackageName.setText(selectedPackage.getName());
            AddPackageSender.setText(new Customer(selectedSenderId, rootView.getContext()).getName());
            AddPackageReceiver.setText(new Customer(selectedReceiverId, rootView.getContext()).getName());
            AddPackageStartTransit.setText(new Transit(selectedStartId, rootView.getContext()).getName());
            AddPackageDeliverTo.setText(new Transit(selectedDeliveryId, rootView.getContext()).getName());
            AddPackageSevereYes.setChecked(selectedPackage.getSeverity().equals("true"));
            AddPackageSevereNo.setChecked(selectedPackage.getSeverity().equals("false"));
            AddPackageWeight.setText(selectedPackage.getWeight());
            AddPackageNumberOfDays.setText(selectedPackage.getExpected_delivery_days());
            EditPackageBtn.setBackgroundResource(R.drawable.ic_add_foreground);
        }
    }

    private void savePackageClicked() {
        SavePackageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, weight, numberofdays;
                Boolean severe;
                name = AddPackageName.getText().toString().trim();
                weight = AddPackageWeight.getText().toString().trim();
                numberofdays = AddPackageNumberOfDays.getText().toString().trim();
                severe = AddPackageSevereYes.isChecked();

                if(name.length() > 0 && weight.length() > 0 && numberofdays.length() > 0 && selectedSenderId != -1 && selectedReceiverId != -1 && selectedStartId != -1 && selectedDeliveryId != -1 && (AddPackageSevereYes.isChecked() || AddPackageSevereNo.isChecked())) {
                    if(isEdit) {
                        //For Editing the selected customer
                        Package aPackage = new Package(selectedPackageId, rootView.getContext());
                        aPackage.update(selectedPackageId, name, severe.toString(), weight, numberofdays, selectedSenderId, selectedReceiverId, selectedStartId, selectedDeliveryId, pref.getInt("userid", -1), aPackage.getDelivery_status_id());
                        Toast.makeText(rootView.getContext(), "Package Updated Successfully.", Toast.LENGTH_LONG).show();
                        clearAllFields();
                    }
                    else {
                        //For Adding a new customer
                        Package aPackage = new Package(rootView.getContext());
                        Long packageId = aPackage.insert(name, severe.toString(), weight, numberofdays, selectedSenderId, selectedReceiverId, selectedStartId, selectedDeliveryId, pref.getInt("userid", -1), 1);
                        Timeline timeline = new Timeline(rootView.getContext());
                        timeline.insert(new Date(), Integer.parseInt(packageId.toString()), new DeliveryStatus(rootView.getContext()).getIdByName("Confirmed"), selectedStartId);
                        Toast.makeText(rootView.getContext(), "Package Added Successfully.", Toast.LENGTH_LONG).show();
                        clearAllFields();
                    }
                    initSelectPackage();
                }
                else {
                    Toast.makeText(rootView.getContext(), "Please don't leave any empty spaces.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void clearAllFields() {
        isEdit = false;
        selectedPackageId = -1;
        selectedSenderId = -1;
        selectedReceiverId = -1;
        selectedStartId = -1;
        selectedDeliveryId = -1;
        resetFragment();
    }

    private void resetFragment() {
        EditPackageBtn.setBackgroundResource(R.drawable.ic_edit_foreground);
        AddPackageTitle.setText("Add a Package");
        AddPackageName.setText("");
        AddPackageSender.setText("");
        AddPackageReceiver.setText("");
        AddPackageStartTransit.setText("");
        AddPackageDeliverTo.setText("");
        AddPackageWeight.setText("");
        AddPackageNumberOfDays.setText("");
        AddPackageSevereYes.setChecked(false);
        AddPackageSevereNo.setChecked(false);
    }

    private void editPackageClicked() {
        EditPackageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEdit) {
                    clearAllFields();
                }
                else {
                    selectPackage.show();
                }
            }
        });
    }

    private void selectSenderClicked() {
        selectSenderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectSender.show();
            }
        });
    }

    private void selectReceiverClicked() {
        selectReceiverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectReceiver.show();
            }
        });
    }

    private void selectStartTransitClicked() {
        selectStartTransitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectStartTransit.show();
            }
        });
    }

    private void selectDeliveryTransitClicked() {
        selectDeliveryTransitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDeliveryTransit.show();
            }
        });
    }


    private void initSelectPackage() {
        selectPackage.setTitle("Select a package:-");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.select_dialog_singlechoice);
        allPackages = sPackage.getAllAsJSON();
        try {
            for (JSONObject jsonObject : allPackages) {
                arrayAdapter.add(jsonObject.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        selectPackage.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        selectPackage.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                isEdit = true;
                try {
                    selectedPackageId = allPackages.get(i).getInt("id");
                    Package selectedPackage = new Package(selectedPackageId, rootView.getContext());
                    selectedSenderId = selectedPackage.getSender_id();
                    selectedReceiverId = selectedPackage.getReceiver_id();
                    selectedStartId = selectedPackage.getDelivery_from_id();
                    selectedDeliveryId = selectedPackage.getDelivery_to_id();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                checkIsUpdate();
            }
        });
    }

    private void initSelectSender() {
        selectSender.setTitle("Select a sender:-");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.select_dialog_singlechoice);
        allCustomers = customer.getAllAsJSON();
        try {
            for (JSONObject jsonObject : allCustomers) {
                arrayAdapter.add(jsonObject.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        selectSender.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        selectSender.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                try {
                    selectedSenderId = allCustomers.get(i).getInt("id");
                    AddPackageSender.setText(new Customer(selectedSenderId, rootView.getContext()).getName());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                checkIsUpdate();
            }
        });
    }

    private void initSelectReceiver() {
        selectReceiver.setTitle("Select a receiver:-");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.select_dialog_singlechoice);
        allCustomers = customer.getAllAsJSON();
        System.out.println("Receivers: ");
        try {
            for (JSONObject jsonObject : allCustomers) {
                arrayAdapter.add(jsonObject.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        selectReceiver.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        selectReceiver.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                try {
                    selectedReceiverId = allCustomers.get(i).getInt("id");
                    AddPackageReceiver.setText(new Customer(selectedReceiverId, rootView.getContext()).getName());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                checkIsUpdate();
            }
        });
    }

    private void initSelectStartTransit() {
        selectStartTransit.setTitle("Select a package:-");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.select_dialog_singlechoice);
        allTransits = transit.getAllAsJSON();
        try {
            for (JSONObject jsonObject : allTransits) {
                arrayAdapter.add(jsonObject.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        selectStartTransit.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        selectStartTransit.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                try {
                    selectedStartId = allTransits.get(i).getInt("id");
                    AddPackageStartTransit.setText(new Transit(selectedStartId, rootView.getContext()).getName());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                checkIsUpdate();
            }
        });
    }

    private void initSelectDeliveryTransit() {
        selectDeliveryTransit.setTitle("Select a package:-");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.select_dialog_singlechoice);
        allTransits = transit.getAllAsJSON();
        try {
            for (JSONObject jsonObject : allTransits) {
                arrayAdapter.add(jsonObject.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        selectDeliveryTransit.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        selectDeliveryTransit.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                try {
                    selectedDeliveryId = allTransits.get(i).getInt("id");
                    AddPackageDeliverTo.setText(new Transit(selectedDeliveryId, rootView.getContext()).getName());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                checkIsUpdate();
            }
        });
    }
}