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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link FileListActivity}
 * in two-pane mode (on tablets) or a {@link FileDetailActivity}
 * on handsets.
 */
public class FileDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
//    private FileItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FileDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments().containsKey(ARG_ITEM_ID)) {
//            // Load the dummy content specified by the fragment
//            // arguments. In a real-world scenario, use a Loader
//            // to load content from a content provider.
//            mItem = ListContent.getItemMap().get(getArguments().getString(ARG_ITEM_ID));
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

        // Show the dummy content as text in a TextView.
//        if (mItem != null) {
//            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.getFileName());
//        }

        return rootView;
    }
}
