package edu.northeastern.lifeassistant;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import util.ActivityAdapter;
import util.ActivityAdapterItem;

public class ActivityFragment extends Fragment {

    ListView listView;

    ArrayList<ActivityAdapterItem> activities = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_activity_screen, null);

        listView = view.findViewById(R.id.activityListView);

        populateList();

        ActivityAdapter adapter = new ActivityAdapter(getActivity(), activities);

        listView.setAdapter(adapter);

        return view;
    }

    private void populateList() {
        activities.add(new ActivityAdapterItem("Running", Color.rgb(100,240, 100)));
        activities.add(new ActivityAdapterItem("Class", Color.rgb(240,100, 100)));
        activities.add(new ActivityAdapterItem("Studying", Color.rgb(100,100, 240)));
    }
}
