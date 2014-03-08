/*
 * This source module contains confidential and proprietary
 * information of Amahi Inc. It is not to be disclosed or used
 * except in accordance with applicable agreements. This
 * copyright notice does not evidence any actual or intended
 * publication of such source code.
 *
 * Copyright (c) 2014 Amahi. All rights reserved.
 */

package org.amahi.anywhere;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link FileDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link FileListFragment} and the item details
 * (if present) is a {@link FileDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link FileListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class FileListActivity extends FragmentActivity
        implements FileListFragment.Callbacks {
    //BGM this is the preference file
    public static final String PREFS_NAME = "SavedCredentials";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences(this.PREFS_NAME, Context.MODE_PRIVATE);
        //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
        boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);

        if(hasLoggedIn)
        {
            //Go directly to main activity
            setContentView(R.layout.activity_item_list);
            if (findViewById(R.id.item_detail_container) != null) {
                // The detail container view will be present only in the
                // large-screen layouts (res/values-large and
                // res/values-sw600dp). If this view is present, then the
                // activity should be in two-pane mode.
                mTwoPane = true;

                // In two-pane mode, list items should be given the
                // 'activated' state when touched.
                ((FileListFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.item_list))
                        .setActivateOnItemClick(true);
            }
            // TODO: If exposing deep links into your app, handle intents here.
        } else{
            Intent LoginIntent = new Intent(this, LoginActivity.class);
            startActivity(LoginIntent);
            //finish();
        }
    }

    /**
     * Callback method from {@link FileListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(FileDetailFragment.ARG_ITEM_ID, id);
            FileDetailFragment fragment = new FileDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, FileDetailActivity.class);
            detailIntent.putExtra(FileDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        ((FileListFragment) getSupportFragmentManager()
  //              .findFragmentById(R.id.item_list)).backButtonWasPressed();
    }
}
