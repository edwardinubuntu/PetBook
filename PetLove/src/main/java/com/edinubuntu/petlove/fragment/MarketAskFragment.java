package com.edinubuntu.petlove.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragment;
import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.edinubuntu.petlove.PetLove;
import com.edinubuntu.petlove.R;
import com.edinubuntu.petlove.model.AdaptPetsModel;
import com.edinubuntu.petlove.model.AsyncModel;
import com.edinubuntu.petlove.object.Event;
import com.edinubuntu.petlove.object.Record;
import com.edinubuntu.petlove.util.converter.RecordsJsonConverter;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by edward_chiang on 13/10/4.
 */
public class MarketAskFragment extends SherlockFragment {

    private Spinner choiceTypeSpinner;
    private Spinner choiceSexSpinner;
    private Spinner choiceAgeSpinner;
    private Spinner choiceBuildSpinner;

    private AdaptPetsModel adaptPetsModel;

    private ProgressDialog waitingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO move to manager
        adaptPetsModel = new AdaptPetsModel();
        java.util.List<Record> recordList = new Select().from(Record.class).execute();
        if (recordList.isEmpty()) {
            askToDownloadRecords();
        }

        waitingDialog = new ProgressDialog(getSherlockActivity());
        waitingDialog.setTitle(getString(R.string.action_load_https_records));
        waitingDialog.setMessage(getString(R.string.waiting));
        waitingDialog.setIndeterminate(false);
        waitingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        waitingDialog.setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_market_ask, container, false);

        choiceTypeSpinner = (Spinner)rootView.findViewById(R.id.question_type_spinner);
        ArrayAdapter<String> choiceTypeAdapter = new ArrayAdapter<String>(getSherlockActivity(),
                R.layout.spinner_item_market_choice, new String[] {
                getString(R.string.dropdown_selection_dog),
                getString(R.string.dropdown_selection_cat)});
        choiceTypeSpinner.setAdapter(choiceTypeAdapter);

        choiceSexSpinner = (Spinner)rootView.findViewById(R.id.question_sex_spinner);
        ArrayAdapter<String> choiceSexAdapter = new ArrayAdapter<String>(getSherlockActivity(),
                R.layout.spinner_item_market_choice, new String[] {
                getString(R.string.choice_ask_any),
                getString(R.string.choice_ask_sex_female),
                getString(R.string.choice_ask_sex_male)});
        choiceSexSpinner.setAdapter(choiceSexAdapter);

        choiceAgeSpinner = (Spinner)rootView.findViewById(R.id.question_age_spinner);
        ArrayAdapter<String> choiceAgeAdapter = new ArrayAdapter<String>(getSherlockActivity(),
                R.layout.spinner_item_market_choice, new String[] {
                getString(R.string.choice_ask_any),
                getString(R.string.choice_ask_age_young),
                getString(R.string.choice_ask_age_adult),
                getString(R.string.choice_ask_age_old) }
                );
        choiceAgeSpinner.setAdapter(choiceAgeAdapter);

        choiceBuildSpinner = (Spinner)rootView.findViewById(R.id.question_build_spinner);
        ArrayAdapter<String> choiceBuildAdapter = new ArrayAdapter<String>(getSherlockActivity(),
                R.layout.spinner_item_market_choice, new String[] {
                getString(R.string.choice_ask_any),
                getString(R.string.choice_ask_build_young_small),
                getString(R.string.choice_ask_build_small),
                getString(R.string.choice_ask_build_normal),
                getString(R.string.choice_ask_build_big)
                }
                );
        choiceBuildSpinner.setAdapter(choiceBuildAdapter);

        Button collectButton = (Button)rootView.findViewById(R.id.market_choice_submit_button);
        collectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Event(Event.Action.VISIT_MARKET_QUESTIONS_ANSWER).save();

                Map<String, String> queryHashMap = new HashMap<String, String>();
                switch (choiceTypeSpinner.getSelectedItemPosition()) {
                    case 0:
                        queryHashMap.put("Type", getString(R.string.record_type_dog));
                        break;
                    case 1:
                        queryHashMap.put("Type", getString(R.string.record_type_cat));
                        break;
                }
                switch (choiceSexSpinner.getSelectedItemPosition()) {
                    case 1:
                        queryHashMap.put("Sex", getString(R.string.record_sex_female));
                        break;
                    case 2:
                        queryHashMap.put("Sex", getString(R.string.record_sex_male));
                        break;
                }
                switch (choiceAgeSpinner.getSelectedItemPosition()) {
                    case 1:
                        queryHashMap.put("Age", getString(R.string.choice_ask_age_young));
                        break;
                    case 2:
                        queryHashMap.put("Age", getString(R.string.choice_ask_age_adult));
                        break;
                    case 3:
                        queryHashMap.put("Age", getString(R.string.choice_ask_age_old));
                        break;
                }
                switch (choiceBuildSpinner.getSelectedItemPosition()) {
                    case 1:
                        queryHashMap.put("Build", getString(R.string.choice_ask_build_young_small));
                        break;
                    case 2:
                        queryHashMap.put("Build", getString(R.string.choice_ask_build_small));
                        break;
                    case 3:
                        queryHashMap.put("Build", getString(R.string.choice_ask_build_normal));
                        break;
                    case 4:
                        queryHashMap.put("Build", getString(R.string.choice_ask_build_big));
                        break;
                }
                FragmentManager fragmentManager = getSherlockActivity().getSupportFragmentManager();
                MarketSuggestionFragment fragment = new MarketSuggestionFragment();
                fragment.setQueryMap(queryHashMap);
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            }
        });

        return rootView;
    }

    private void askToDownloadRecords() {

        new AlertDialog.Builder(getSherlockActivity())
                .setTitle(getResources().getString(R.string.records_download_from_https_title))
                .setMessage(getResources().getString(R.string.records_download_from_https_message))
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadObjects(adaptPetsModel, false);
                    }
                })
                .setNegativeButton(R.string.dialog_no, null)
                .show();
    }


    public void loadObjects(final AsyncModel asyncModel, final boolean more) {
        asyncModel.load(new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                asyncModel.setLoading(true);

                waitingDialog.show();
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                loadJsonResult(s, true);
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                Toast.makeText(getSherlockActivity(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                asyncModel.setLoading(false);
                waitingDialog.dismiss();
            }
        });
    }

    private void loadJsonResult(String s, boolean saveData) {
        RecordsJsonConverter recordsJsonConverter = new RecordsJsonConverter();

        ActiveAndroid.beginTransaction();
        try {
            recordsJsonConverter.convert(s);

            List<Record> recordList = recordsJsonConverter.getRecords();

            if (saveData) {
                int savedCount = 0;
                for (Record record : recordList) {
                    // Check before save
                    Record savedRecord = new Select().from(Record.class).where("RecordId = ?", record.getRecordId()).executeSingle();
                    if (savedRecord == null) {
                        record.save();
                        savedCount++;
                        Log.d(PetLove.TAG, "One record saved. Id: " + record.getRecordId());
                    }
                }
                ActiveAndroid.setTransactionSuccessful();

                Toast.makeText(getSherlockActivity(), getString(R.string.records_download_saved) + savedCount, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(PetLove.TAG, e.toString());
            Toast.makeText(getSherlockActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }
}
