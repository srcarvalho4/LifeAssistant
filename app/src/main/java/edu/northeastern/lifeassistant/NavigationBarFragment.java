package edu.northeastern.lifeassistant;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import utils.SetAlarmManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationBarFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {


    public NavigationBarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation_bar, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        BottomNavigationView navView = getView().findViewById(R.id.nav_view);
        String selected = getActivity().getIntent().getStringExtra("location");
        if (selected != null ) {
            if (selected.equals("Activity")) {
                navView.setSelectedItemId(R.id.navigation_activity);
            } else if (selected.equals("Schedule")) {
                navView.setSelectedItemId(R.id.navigation_scheduler);
            } else if (selected.equals("Spontaneous")) {
                navView.setSelectedItemId(R.id.navigation_spontaneous);
            } else if (selected.equals("History")) {
                navView.setSelectedItemId(R.id.navigation_history);
            }
        }
        navView.setOnNavigationItemSelectedListener(this);
    }



        @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent = null;
        switch (menuItem.getItemId()) {
            case R.id.navigation_activity:
                intent = new Intent(getContext(), ActivityScreen.class);
                intent.putExtra("location", "Activity");
                break;
            case R.id.navigation_scheduler:
                intent = new Intent(getContext(), ScheduleScreen.class);
                intent.putExtra("location", "Schedule");
                break;
            case R.id.navigation_spontaneous:
                String activityId = SetAlarmManager.getActiveScheduleEvent(getActivity().getApplicationContext());
                if (activityId == null) {
                    intent = new Intent(getContext(), SpontaneousScreen.class);
                }
                else {
                    intent = new Intent(getContext(), SpontaneousActive.class);
                    intent.putExtra("name", activityId);
                    intent.putExtra("location", "Spontaneous");
                }
                break;
            case R.id.navigation_history:
                intent = new Intent(getContext(), HistoryActivity.class);
                intent.putExtra("location", "History");
                break;
        }
        startActivity(intent);

        return true;
    }

}
