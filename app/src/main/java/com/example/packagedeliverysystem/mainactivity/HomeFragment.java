package com.example.packagedeliverysystem.mainactivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.packagedeliverysystem.R;
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
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View rootView;
    TextView packageNameHome;
    AlertDialog.Builder selectPackage, cancelPackage, returnPackage;
    ArrayList<JSONObject> allPackages;
    LinearLayout packageNameLinearLayout, selectPackageBtn, cancelPackageBtn, returnPackageBtn, viewDetailsBtn, trackPackageBtn;
    Package sPackage;
    Integer selectedPackageId;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        sPackage = new Package(rootView.getContext());
        packageNameHome = rootView.findViewById(R.id.packageNameHome);
        packageNameLinearLayout = rootView.findViewById(R.id.packageNameLinearLayout);
        selectPackageBtn = rootView.findViewById(R.id.selectPackageBtn);
        cancelPackageBtn = rootView.findViewById(R.id.cancelPackageBtn);
        returnPackageBtn = rootView.findViewById(R.id.returnPackageBtn);
        viewDetailsBtn = rootView.findViewById(R.id.viewDetailsBtn);
        trackPackageBtn = rootView.findViewById(R.id.trackPackageBtn);

        selectPackage = new AlertDialog.Builder(rootView.getContext());
        cancelPackage = new AlertDialog.Builder(rootView.getContext());
        returnPackage = new AlertDialog.Builder(rootView.getContext());

        selectedPackageId = -1;

        initSelectPackage();

        selectPackageBtnOnClick();
        cancelPackageClicked();
        returnPackageClicked();
        viewDetailsClicked();
        trackPackageClicked();

        return rootView;
    }

    private void selectPackageBtnOnClick() {
        selectPackageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPackage.show();
            }
        });
    }

    private void initSelectPackage() {
        if(sPackage.numberOfRows()>0) {
            selectPackage.setTitle("Select a package:-");
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.select_dialog_singlechoice);
            allPackages = sPackage.getAllActiveAsJSON();
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

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    checkIsSelected();
                    dialog.dismiss();
                }
            });
        }
    }

    private void checkIsSelected() {
        if(selectedPackageId!=-1) {
            packageNameLinearLayout.setVisibility(View.VISIBLE);
            packageNameHome.setText(new Package(selectedPackageId, rootView.getContext()).getName());
        }
    }

    private void clearPackage() {
        selectedPackageId = -1;
        packageNameLinearLayout.setVisibility(View.GONE);
        packageNameHome.setText("");
        initSelectPackage();
    }

    private AlertDialog.Builder initCancelPackage() {
        cancelPackage.setTitle("Are you sure you want to cancel delivery of " + new Package(selectedPackageId, rootView.getContext()).getName() + "?");
        cancelPackage.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        cancelPackage.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Package tempPackage = new Package(selectedPackageId, rootView.getContext());
                tempPackage.update(selectedPackageId, tempPackage.getName(), tempPackage.getSeverity(), tempPackage.getWeight(), tempPackage.getExpected_delivery_days(), tempPackage.getSender_id(), tempPackage.getReceiver_id(), tempPackage.getDelivery_from_id(), tempPackage.getDelivery_to_id(), tempPackage.getCompany_id(), new DeliveryStatus(rootView.getContext()).getIdByName("Cancelled"));
                Timeline timeline = new Timeline(rootView.getContext());
                timeline.insert(new Date(), selectedPackageId, new DeliveryStatus(rootView.getContext()).getIdByName("Cancelled"), new Timeline(rootView.getContext()).getTransitIdByPackage(selectedPackageId));
                Toast.makeText(rootView.getContext(), "Package Cancelled Successfully!", Toast.LENGTH_LONG).show();
                clearPackage();
                dialogInterface.dismiss();
            }
        });
        return cancelPackage;
    }


    private AlertDialog.Builder initReturnPackage() {
        cancelPackage.setTitle("Are you sure you want to return " + new Package(selectedPackageId, rootView.getContext()).getName() + "?");
        cancelPackage.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        cancelPackage.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Package tempPackage = new Package(selectedPackageId, rootView.getContext());
                tempPackage.update(selectedPackageId, tempPackage.getName(), tempPackage.getSeverity(), tempPackage.getWeight(), tempPackage.getExpected_delivery_days(), tempPackage.getSender_id(), tempPackage.getReceiver_id(), tempPackage.getDelivery_from_id(), tempPackage.getDelivery_to_id(), tempPackage.getCompany_id(), new DeliveryStatus(rootView.getContext()).getIdByName("Requested for return"));
                Timeline timeline = new Timeline(rootView.getContext());
                timeline.insert(new Date(), selectedPackageId, new DeliveryStatus(rootView.getContext()).getIdByName("Requested for return"), new Timeline(rootView.getContext()).getTransitIdByPackage(selectedPackageId));
                Toast.makeText(rootView.getContext(), "Package Applied For Return Successfully!", Toast.LENGTH_LONG).show();
                dialogInterface.dismiss();
            }
        });
        return cancelPackage;
    }


    private void cancelPackageClicked() {
        cancelPackageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedPackageId!=-1) {
                    initCancelPackage().show();
                }
                else {
                    Toast.makeText(rootView.getContext(), "Please select a package first.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void returnPackageClicked() {
        returnPackageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedPackageId!=-1) {
                    initReturnPackage().show();
                }
                else {
                    Toast.makeText(rootView.getContext(), "Please select a package first.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void viewDetailsClicked() {
        viewDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedPackageId !=-1 ) {
                    DetailsFragment detailsFragment = new DetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("packageId", selectedPackageId);
                    detailsFragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.mainFragmentContainer, detailsFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else {
                    Toast.makeText(rootView.getContext(), "Please select a package first.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void trackPackageClicked() {
        trackPackageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedPackageId !=-1 ) {
                    TrackFragment trackFragment = new TrackFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("packageId", selectedPackageId);
                    trackFragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.mainFragmentContainer, trackFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else {
                    Toast.makeText(rootView.getContext(), "Please select a package first.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}