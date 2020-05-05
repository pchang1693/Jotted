package com.example.portiac.jotted.controller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.portiac.jotted.R;
import com.example.portiac.jotted.model.Note;
import com.example.portiac.jotted.persistence.JSONSerializer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_calendar:
                    selectedFragment = new CalendarFragment();
                    break;
                case R.id.navigation_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.navigation_journal:
                    selectedFragment = new JournalFragment();
                    break;
            }

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.cross_fade_in, R.anim.fade_out);
            ft.replace(R.id.mainFragmentHolder, selectedFragment, "curr").commit();
            return true;
        }
    };

    private List<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);

        JSONSerializer serializer = new JSONSerializer("Notes.json", getApplicationContext());
        try {
            notes = serializer.loadNotes();
        } catch (Exception e) {
            notes = new ArrayList<Note>();
            Log.e("error", "Error loading notes: ", e);
        }
    }

    public void switchToJournalFragment() {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_journal);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFragmentHolder, new JournalFragment(), "curr").commit();
    }

    public List<Note> getNotes() {
        return notes;
    }

}
