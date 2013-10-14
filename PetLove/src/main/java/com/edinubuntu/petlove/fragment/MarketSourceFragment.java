package com.edinubuntu.petlove.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.actionbarsherlock.app.SherlockFragment;
import com.edinubuntu.petlove.R;
import com.edinubuntu.petlove.adapter.MarketSourceAdapter;
import com.edinubuntu.petlove.object.MarketSource;

/**
 * Created by edward_chiang on 13/10/9.
 */
public class MarketSourceFragment extends SherlockFragment {

    private GridView sourceGridView;

    private static int GRID_VIEW_TWO_COLUMN_PER_PAGE_WIDTH;

    private MarketSource[] marketSources;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        GRID_VIEW_TWO_COLUMN_PER_PAGE_WIDTH = metrics.widthPixels / 2;

        marketSources = new MarketSource[]{
                new MarketSource(MarketSource.TYPE_SELF, getSherlockActivity()),
                new MarketSource(MarketSource.TYPE_TAIPEI_OPEN_DATA, getSherlockActivity())};
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_market_source, container, false);

        sourceGridView = (GridView)rootView.findViewById(R.id.market_source_grid_view);
        sourceGridView.setColumnWidth(GRID_VIEW_TWO_COLUMN_PER_PAGE_WIDTH);

        MarketSourceAdapter marketSourceAdapter = new MarketSourceAdapter(getSherlockActivity(), android.R.layout.simple_list_item_1, marketSources);
        sourceGridView.setAdapter(marketSourceAdapter);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sourceGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < marketSources.length) {
                    MarketSource marketSource = marketSources[position];
                    switch (marketSource.getType()) {
                        case MarketSource.TYPE_SELF:
                            break;
                        case MarketSource.TYPE_TAIPEI_OPEN_DATA:

                            getSherlockActivity().getSupportActionBar().setTitle(marketSource.getTitle());

                            FragmentManager fragmentManager = getSherlockActivity().getSupportFragmentManager();
                            MarketAskFragment fragment = new MarketAskFragment();
                            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                            break;
                    }
                }
            }
        });
    }
}
