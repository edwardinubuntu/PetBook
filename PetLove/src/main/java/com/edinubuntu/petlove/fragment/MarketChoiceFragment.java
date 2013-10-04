package com.edinubuntu.petlove.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import com.actionbarsherlock.app.SherlockFragment;
import com.edinubuntu.petlove.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by edward_chiang on 13/10/4.
 */
public class MarketChoiceFragment extends SherlockFragment {

    private Spinner choiceTypeSpinner;
    private Spinner choiceSexSpinner;
    private Spinner choiceAgeSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragement_market_choice, container, false);

        choiceTypeSpinner = (Spinner)rootView.findViewById(R.id.question_type_spinner);
        ArrayAdapter<String> choiceTypeAdapter = new ArrayAdapter<String>(getSherlockActivity(),
                R.layout.spinner_item_market_choice, new String[] {
                getString(R.string.dropdown_selection_dog),
                getString(R.string.dropdown_selection_cat)});
        choiceTypeSpinner.setAdapter(choiceTypeAdapter);

        choiceSexSpinner = (Spinner)rootView.findViewById(R.id.question_sex_spinner);
        ArrayAdapter<String> choiceSexAdapter = new ArrayAdapter<String>(getSherlockActivity(),
                R.layout.spinner_item_market_choice, new String[] {
                getString(R.string.choice_ask_sex_female),
                getString(R.string.choice_ask_sex_male)});
        choiceSexSpinner.setAdapter(choiceSexAdapter);

        choiceAgeSpinner = (Spinner)rootView.findViewById(R.id.question_age_spinner);
        ArrayAdapter<String> choiceAgeAdapter = new ArrayAdapter<String>(getSherlockActivity(),
                R.layout.spinner_item_market_choice, new String[] {
                getString(R.string.choice_ask_age_young),
                getString(R.string.choice_ask_age_adult),
                getString(R.string.choice_ask_age_old) }
                );
        choiceAgeSpinner.setAdapter(choiceAgeAdapter);

        Button collectButton = (Button)rootView.findViewById(R.id.market_choice_submit_button);
        collectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    case 0:
                        queryHashMap.put("Sex", getString(R.string.record_sex_female));
                        break;
                    case 1:
                        queryHashMap.put("Sex", getString(R.string.record_sex_male));
                        break;
                }
                switch (choiceAgeSpinner.getSelectedItemPosition()) {
                    case 0:
                        queryHashMap.put("Age", getString(R.string.choice_ask_age_young));
                        break;
                    case 1:
                        queryHashMap.put("Age", getString(R.string.choice_ask_age_adult));
                        break;
                    case 2:
                        queryHashMap.put("Age", getString(R.string.choice_ask_age_old));
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
}
