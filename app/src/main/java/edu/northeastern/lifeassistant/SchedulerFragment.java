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

import util.EventAdapter;
import util.EventAdapterItem;


public class SchedulerFragment extends Fragment {

    ListView listView;
    ArrayList<EventAdapterItem> events = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_schedule_screen, null);

        listView = view.findViewById(R.id.scheduleListView);

        populateList();

        EventAdapter adapter = new EventAdapter(getActivity(), events);

        listView.setAdapter(adapter);
        return view;

    }

    private void populateList() {
        events.add(new EventAdapterItem("5 Mile Jog", "8:00am", "9:00am", Color.rgb(100, 240, 100), 0b01000001));
        events.add(new EventAdapterItem("Mobile App Development", "11:40am", "1:20pm", Color.rgb(240, 100, 100), 0b00011110));
        events.add(new EventAdapterItem("Study Session", "6:30pm", "9:00pm", Color.rgb(100, 100, 240), 0b01111111));
        events.add(new EventAdapterItem("Group Meeting", "3:00pm", "5:00pm", Color.rgb(100, 100, 240), 0b00001010));
    }
}
