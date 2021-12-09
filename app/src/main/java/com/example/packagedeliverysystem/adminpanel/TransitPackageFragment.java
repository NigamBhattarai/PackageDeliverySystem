package com.example.packagedeliverysystem.adminpanel;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class TransitPackageFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View rootView;
    Button updateTransitUpdateBtn;
    ImageButton updateTransitPackageBtn, updateTransitTransitBtn, updateTransitDeliveryStatusBtn;
    EditText updateTransitPackageName, updateTransitTransitName, updateTransitDeliveryStatusName;
    ArrayList<JSONObject> allPackages, allDeliveryStatuses, allTransits;
    Package sPackage;
    DeliveryStatus deliveryStatus;
    Transit transit;
    AlertDialog.Builder selectPackage, selectDeliveryStatus, selectTransit;
    Integer selectedPackageId, selectedDeliveryStatusId, selectedTransitId;

    private String mParam1;
    private String mParam2;

    public TransitPackageFragment() {
    }

    public static TransitPackageFragment newInstance(String param1, String param2) {
        TransitPackageFragment fragment = new TransitPackageFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_transit_package, container, false);

        updateTransitUpdateBtn = rootView.findViewById(R.id.updateTransitUpdateBtn);
        updateTransitPackageBtn = rootView.findViewById(R.id.updateTransitAddPackage);
        updateTransitTransitBtn = rootView.findViewById(R.id.updateTransitAddTransit);
        updateTransitDeliveryStatusBtn = rootView.findViewById(R.id.updateTransitAddDeliveryStatus);
        updateTransitPackageName = rootView.findViewById(R.id.updateTransitPackageName);
        updateTransitTransitName = rootView.findViewById(R.id.updateTransitTransitName);
        updateTransitDeliveryStatusName = rootView.findViewById(R.id.updateTransitDeliveryStatusName);

        sPackage = new Package(rootView.getContext());
        deliveryStatus = new DeliveryStatus(rootView.getContext());
        transit = new Transit(rootView.getContext());

        selectPackage = new AlertDialog.Builder(rootView.getContext());
        selectDeliveryStatus = new AlertDialog.Builder(rootView.getContext());
        selectTransit = new AlertDialog.Builder(rootView.getContext());

        selectedPackageId = -1;
        selectedTransitId = -1;
        selectedDeliveryStatusId = -1;

        initSelectPackage();
        initSelectDeliveryStatus();
        initSelectTransit();

        updateTransitClicked();

        selectPackageClicked();
        selectDeliveryStatusClicked();
        selectTransitClicked();

        return rootView;
    }

    private void updateTransitClicked() {
        updateTransitUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedPackageId != -1 && selectedDeliveryStatusId != -1 && selectedTransitId != -1) {
                    Package tempPackage = new Package(selectedPackageId, rootView.getContext());
                    tempPackage.update(selectedPackageId, tempPackage.getName(), tempPackage.getSeverity(), tempPackage.getWeight(), tempPackage.getExpected_delivery_days(), tempPackage.getSender_id(), tempPackage.getReceiver_id(), tempPackage.getDelivery_from_id(), tempPackage.getDelivery_to_id(), tempPackage.getCompany_id(), selectedDeliveryStatusId);
                    Timeline timeline = new Timeline(rootView.getContext());
                    timeline.insert(new Date(), selectedPackageId, selectedDeliveryStatusId, selectedTransitId);
                    Toast.makeText(rootView.getContext(), "Transit Updated Successfully.", Toast.LENGTH_LONG).show();
                    clearAllFields();
                }
                else {
                    Toast.makeText(rootView.getContext(), "Please don't leave any empty spaces.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void clearAllFields() {
        selectedPackageId = -1;
        selectedDeliveryStatusId = -1;
        selectedTransitId = -1;
        resetFragment();
    }

    private void resetFragment() {
        updateTransitPackageName.setText("");
        updateTransitDeliveryStatusName.setText("");
        updateTransitTransitName.setText("");
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
                try {
                    selectedPackageId = allPackages.get(i).getInt("id");
                    updateTransitPackageName.setText(new Package(selectedPackageId, rootView.getContext()).getName());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
    }

    private void initSelectDeliveryStatus() {
        selectDeliveryStatus.setTitle("Select a package:-");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.select_dialog_singlechoice);
        allDeliveryStatuses = deliveryStatus.getAllAsJSON();
        try {
            for (JSONObject jsonObject : allDeliveryStatuses) {
                arrayAdapter.add(jsonObject.getString("status"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        selectDeliveryStatus.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        selectDeliveryStatus.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                try {
                    selectedDeliveryStatusId = allDeliveryStatuses.get(i).getInt("id");
                    updateTransitDeliveryStatusName.setText(new DeliveryStatus(selectedDeliveryStatusId, rootView.getContext()).getStatus());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
    }

    private void initSelectTransit() {
        selectTransit.setTitle("Select a package:-");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.select_dialog_singlechoice);
        allTransits = transit.getAllAsJSON();
        try {
            for (JSONObject jsonObject : allTransits) {
                arrayAdapter.add(jsonObject.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        selectTransit.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        selectTransit.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                try {
                    selectedTransitId = allTransits.get(i).getInt("id");
                    updateTransitTransitName.setText(new Transit(selectedTransitId, rootView.getContext()).getName());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
    }

    private void selectPackageClicked() {
        updateTransitPackageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPackage.show();
            }
        });
    }

    private void selectDeliveryStatusClicked() {
        updateTransitDeliveryStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDeliveryStatus.show();
            }
        });
    }

    private void selectTransitClicked() {
        updateTransitTransitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTransit.show();
            }
        });
    }

}