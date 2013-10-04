package com.edinubuntu.petlove.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;
import com.activeandroid.query.Select;
import com.edinubuntu.petlove.PetLove;
import com.edinubuntu.petlove.R;
import com.edinubuntu.petlove.adapter.EventsAdapter;
import com.edinubuntu.petlove.object.Event;

/**
 * Created by edward_chiang on 13/10/4.
 */
public class EventsFragment extends SherlockFragment {

    private EventsAdapter eventAdapter;

    private ListView eventListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_events, container, false);

        eventListView = (ListView)rootView.findViewById(R.id.fragment_events_list_view);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        java.util.List<Event> eventList = new Select().from(Event.class).orderBy("CreatedDate DESC").execute();
        eventAdapter = new EventsAdapter(getActivity(), android.R.layout.simple_list_item_1, eventList);

        eventListView.setAdapter(eventAdapter);

        Log.d(PetLove.TAG, "View Created");
    }
}
