package com.example.packagedeliverysystem.mainactivity;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.packagedeliverysystem.R;
import com.example.packagedeliverysystem.models.Company;
import com.example.packagedeliverysystem.models.Customer;
import com.example.packagedeliverysystem.models.DeliveryStatus;
import com.example.packagedeliverysystem.models.Package;
import com.example.packagedeliverysystem.models.Timeline;
import com.example.packagedeliverysystem.models.Transit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View rootView;
    Integer packageId;
    TextView detailsSentBy, detailsSentTo, detailsPackageName, detailsCurrentStatus, detailsCurrentTransit, detailsCompanyName;

    private String mParam1;
    private String mParam2;

    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance(String param1, String param2) {
        DetailsFragment fragment = new DetailsFragment();
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
        rootView = inflater.inflate(R.layout.fragment_details, container, false);
        packageId = getArguments().getInt("packageId");

        detailsSentBy = rootView.findViewById(R.id.detailsSentBy);
        detailsSentTo = rootView.findViewById(R.id.detailsSentTo);
        detailsPackageName = rootView.findViewById(R.id.detailsPackageName);
        detailsCurrentStatus = rootView.findViewById(R.id.detailsCurrentStatus);
        detailsCurrentTransit = rootView.findViewById(R.id.detailsCurrentTransit);
        detailsCompanyName = rootView.findViewById(R.id.detailsCompanyName);

        initPackageDetails();

        return rootView;
    }

    private void initPackageDetails() {
        Package aPackage = new Package(packageId, rootView.getContext());
        Customer sentBy = new Customer(aPackage.getSender_id(), rootView.getContext());
        Customer sentTo = new Customer(aPackage.getReceiver_id(), rootView.getContext());
        DeliveryStatus currentStatus = new DeliveryStatus(aPackage.getDelivery_status_id(), rootView.getContext());
        Transit transit = new Transit(new Timeline(rootView.getContext()).getTransitIdByPackage(packageId), rootView.getContext());
        Company company = new Company(aPackage.getCompany_id(), rootView.getContext());

        detailsSentBy.setText(sentBy.getName());
        detailsSentTo.setText(sentTo.getName());
        detailsPackageName.setText(aPackage.getName());
        detailsCurrentStatus.setText(currentStatus.getStatus());
        detailsCurrentTransit.setText(transit.getName());
        detailsCompanyName.setText(company.getName());

        switch (currentStatus.getSeverity()) {
            case 1:
                detailsCurrentStatus.setTextColor(Color.parseColor("#008000"));
                break;
            case 2:
                detailsCurrentStatus.setTextColor(Color.parseColor("#008000"));
                break;
            case 3:
                detailsCurrentStatus.setTextColor(Color.parseColor("#00DD00"));
                break;
            default:
                detailsCurrentStatus.setTextColor(Color.parseColor("#CC0000"));
                break;
        }
    }

}