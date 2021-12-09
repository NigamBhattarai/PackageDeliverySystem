package com.example.packagedeliverysystem.mainactivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.packagedeliverysystem.CustomAdapter;
import com.example.packagedeliverysystem.R;
import com.example.packagedeliverysystem.models.DeliveryStatus;
import com.example.packagedeliverysystem.models.Package;
import com.example.packagedeliverysystem.models.Timeline;
import com.example.packagedeliverysystem.models.Transit;
import com.example.packagedeliverysystem.utils.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

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
    RecyclerView recyclerView;
    TextView trackPackageName;

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
        recyclerView = rootView.findViewById(R.id.trackRecyclerView);
        trackPackageName = rootView.findViewById(R.id.trackPackageName);

        initTrackPackage();

        return rootView;
    }

    private void initTrackPackage() {
        trackPackageName.setText(new Package(packageId, rootView.getContext()).getName());
        ArrayList<JSONObject> timelines = new Timeline(rootView.getContext()).getPackageTimelineAsJSON(packageId);
        ArrayList<JSONObject> jsonArrayToSend = new ArrayList<>();
        for (JSONObject timeline : timelines) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("status", new DeliveryStatus(timeline.getInt("delivery_status_id"), rootView.getContext()).getStatus());
                jsonObject.put("severity", new DeliveryStatus(timeline.getInt("delivery_status_id"), rootView.getContext()).getSeverity());
                jsonObject.put("transit", new Transit(timeline.getInt("transit_id"), rootView.getContext()).getName());
                jsonObject.put("arrival_date", TimeUtils.getRelativeTime(Date.parse(timeline.getString("arrival_date"))));
                jsonArrayToSend.add(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        CustomAdapter myAdapter = new CustomAdapter(rootView.getContext(), jsonArrayToSend);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.setAdapter(myAdapter);
    }
}