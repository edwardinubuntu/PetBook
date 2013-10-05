package com.edinubuntu.petlove.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.edinubuntu.petlove.PetLove;
import com.edinubuntu.petlove.R;

/**
 * Created by edward_chiang on 13/10/5.
 */
public class SettingsActivity extends SherlockPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Version
        Preference versionPreference = findPreference("setting_version_key");
        assert versionPreference != null;
        try {
            versionPreference.setSummary(
                    this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName
            );
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e(PetLove.TAG, e.toString());
        }
    }
}
