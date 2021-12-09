package com.example.packagedeliverysystem.mainactivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.packagedeliverysystem.R;
import com.example.packagedeliverysystem.models.Timeline;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View rootView;
    Integer packageId;

    private String mParam1;
    private String mParam2;

    public TrackFragment() {
        // Required empty public constructor
    }

    public static TrackFragment newInstance(String param1, String param2) {
        TrackFragment fragment = new TrackFragment();
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
        rootView = inflater.inflate(R.layout.fragment_track, container, false);
        packageId = getArguments().getInt("packageId");

        initTrackPackage();

        return rootView;
    }

    private void initTrackPackage() {
        ArrayList<JSONObject> timelines = new Timeline(rootView.getContext()).getPackageTimelineAsJSON(packageId);
        for (JSONObject timeline : timelines) {
            try {
                System.out.println("ID: "+timeline.getInt("id"));
                System.out.println("Arrival Date: "+timeline.getString("arrival_date"));
                System.out.println("Delivery Status id: "+timeline.getInt("delivery_status_id"));
                System.out.println("Transit id: "+timeline.getInt("transit_id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}