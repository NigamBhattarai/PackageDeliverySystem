package com.example.packagedeliverysystem.adminpanel;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.packagedeliverysystem.R;
import com.example.packagedeliverysystem.models.Package;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewPackagesFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View rootView;
    ListView packageListView;
    EditText searchQuery;
    ArrayList<String> allPackages;
    ArrayAdapter<String> itemsAdapter;

    private String mParam1;
    private String mParam2;

    public ViewPackagesFragment() {
    }

    public static ViewPackagesFragment newInstance(String param1, String param2) {
        ViewPackagesFragment fragment = new ViewPackagesFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_view_packages, container, false);
        initListView();
        initSearchPackage();
        return rootView;
    }

    private void initSearchPackage() {
        searchQuery = rootView.findViewById(R.id.packageSearchQuery);
        searchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                itemsAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void initListView() {
        Package aPackage = new Package(rootView.getContext());
        allPackages = aPackage.getAll();
        itemsAdapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_list_item_1, allPackages);
        packageListView = rootView.findViewById(R.id.packageListView);
        packageListView.setAdapter(itemsAdapter);
    }


}