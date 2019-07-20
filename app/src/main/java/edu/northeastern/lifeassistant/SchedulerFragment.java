package edu.northeastern.lifeassistant;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import utils.EventAdapter;
import utils.ScheduleEvent;


public class SchedulerFragment extends Fragment {

    ListView listView;
    Button button1;
    ArrayList<ScheduleEvent> events = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_schedule_screen, null);

        listView = view.findViewById(R.id.scheduleListView);
        button1 = view.findViewById(R.id.scheduleActivityButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateEventActivity.class);
                startActivity(intent);
            }
        });

        populateList();

        EventAdapter adapter = new EventAdapter(getActivity(), events);

        listView.setAdapter(adapter);
        return view;

    }

    private void populateList() {
        //TODO
    }
}
