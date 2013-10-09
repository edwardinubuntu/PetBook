package com.edinubuntu.petlove.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.edinubuntu.petlove.R;
import com.edinubuntu.petlove.adapter.KnowContentAdapter;
import com.edinubuntu.petlove.object.Event;
import com.edinubuntu.petlove.object.KnowContent;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward_chiang on 13/10/9.
 */
public class KnowContentFragment extends SherlockFragment {

    private List<KnowContent> knowContentList;

    private ListView knowContentListView;

    private KnowContentAdapter knowContentAdapter;

    private ProgressDialog waitingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        knowContentList = new ArrayList<KnowContent>();

        List<KnowContent> knowContents = new Select().from(KnowContent.class).execute();

        waitingDialog = new ProgressDialog(getSherlockActivity());
        waitingDialog.setTitle(getString(R.string.know_contents_check_waiting_title));
        waitingDialog.setMessage(getString(R.string.waiting));
        waitingDialog.setIndeterminate(false);
        waitingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        waitingDialog.setCancelable(false);

        if (knowContents == null || knowContents.isEmpty()) {

            updateKnowContents();
        } else {
            knowContentList.clear();
            knowContentList.addAll(knowContents);
        }

        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_know_content, container, false);

        knowContentListView = (ListView)rootView.findViewById(R.id.fragment_know_content_list_view);

        knowContentAdapter = new KnowContentAdapter(getSherlockActivity(), android.R.layout.simple_list_item_1, knowContentList);

        knowContentListView.setAdapter(knowContentAdapter);

        knowContentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                KnowContent knowContent = knowContentList.get(position);

                Event readEvent = new Event(Event.Action.READ_KNOW_CONTENT);
                readEvent.setMessage(knowContent.getTitle());
                readEvent.save();

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(knowContent.getUrl()));
                startActivity(browserIntent);
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_know_contents, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                updateKnowContents();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateKnowContents() {

        waitingDialog.show();

        // Remove all
        knowContentList.clear();
        new Delete().from(KnowContent.class).execute();

        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("KnowWebObject");
        parseQuery.orderByDescending("createAt");
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                for (ParseObject parseObject : parseObjects) {

                    KnowContent knowContent = new KnowContent();
                    knowContent.setTitle(parseObject.getString("title"));
                    knowContent.setUrl(parseObject.getString("url"));

                    KnowContent savedRecord = new Select().from(KnowContent.class).where("Title = ?", knowContent.getTitle()).executeSingle();
                    if (savedRecord == null) {
                        knowContent.save();
                    }

                    knowContentList.add(knowContent);
                }
                knowContentAdapter.notifyDataSetChanged();

                waitingDialog.dismiss();
            }
        });
    }
}
