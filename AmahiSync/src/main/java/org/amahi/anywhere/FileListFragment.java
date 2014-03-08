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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.amahi.anywhere.model.FileItem;
import org.amahi.anywhere.model.ListContent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import java.util.List;
import java.util.Stack;

/**
 * A list fragment representing a list of Items. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link FileDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class FileListFragment extends ListFragment {

    public class FileItemAdapter extends ArrayAdapter<FileItem> {
        Context context;
        int layoutResourceId;
        List<FileItem> data;

        public FileItemAdapter(Context context, int layoutResourceId, List<FileItem> data) {
            super(context, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.context = context;
            this.data = data;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//            return super.getView(position, convertView, parent);
            View row = convertView;
            FileItemHolder holder;

            if (row == null) {
                row = ((Activity) context).getLayoutInflater().inflate(layoutResourceId, parent, false);

                holder = new FileItemHolder();
                holder.imgIcon = (ImageView) row.findViewById(R.id.imgIcon);
                holder.txtTitle = (TextView) row.findViewById(R.id.txtTitle);

                row.setTag(holder);
            } else {
                holder = (FileItemHolder) row.getTag();
            }

            FileItem fileItem = data.get(position);
            if (fileItem != null) {
                holder.txtTitle.setText(fileItem.name);
                if (fileItem.getIsDirectory()) {
                    holder.imgIcon.setImageResource(R.drawable.folder);
                } else {
                    holder.imgIcon.setImageResource(R.drawable.file);
                }
            }

            return row;
        }

        class FileItemHolder {
            ImageView imgIcon;
            TextView txtTitle;
        }
    }

    private FileItemAdapter mFileItemAdapter;

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
//    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(String id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {

        @Override
        public void onItemSelected(String id) {

        }
    };

    private Stack<String> folderNameStack = new Stack<String>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FileListFragment() {
    }
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setListAdapter(new ArrayAdapter<FileItem>(
//                getActivity(),
//                android.R.layout.simple_list_item_activated_1,
//                android.R.id.text1,
//                (new ListContent("", getActivity())).getItems()));
        mFileItemAdapter = new FileItemAdapter(getActivity(), R.layout.listview_item_row, new ArrayList<FileItem>());
        setListAdapter(mFileItemAdapter);
        new PopulateListTask().execute("shares");
        JsonObject json = new JsonObject();
        //json.addProperty("API-key", "34f9cdf4dc9174d0e1d8a14ef9c3c0b4c8ad7477");
        //json.addProperty("User-Agent", "Amahi Anywhere (1.0) Android");
        Ion.with(getActivity())
                .load("PUT","https://pfe.amahi.org/client")
                .setHeader("API-key", "34f9cdf4dc9174d0e1d8a14ef9c3c0b4c8ad7477")
                .userAgent("Amahi Anywhere (1.0) Android")
                .setTimeout(10000)
                //.setStringBody("shares")
                //.setBodyParameter("API-Key", "88b25057afb13482b4c5d69f8279ea11a9a0bd4c")
                .asJsonObject()
                //.asString()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        //reader.read(buffer);
                        //return new String(buffer);
                        //String stringResult = getStringFromInputStream(result);
                        if (e == null) {
                            Log.d("BGM", "Result: " + result.toString());
                        } else {
                            Log.d("BGM", "Exception: " + e.toString());
                        }
                    }
                });
    }

    public void backButtonWasPressed() {
        //when back is pressed you pop and then peek to get the parent folder
//        folderNameStack.pop();
//        String fileName = folderNameStack.peek();
        new PopulateListTask().execute("shares");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

//        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
//        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        //mCallbacks.onItemSelected(((FileItem)getListAdapter().getItem(position)).getId());

        FileItem item = ((FileItem) getListAdapter().getItem(position));
        new PopulateListTask().execute("files?s=" + item.name);


//        String fileName = ((FileItem) getListAdapter().getItem(position)).getFileName();
//        if (((FileItem) getListAdapter().getItem(position)).getIsDirectory()) {
//            folderNameStack.push(fileName);
//            new PopulateListTask().execute(new String[]{fileName});
//        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    private class PopulateListTask extends AsyncTask<String, Void, List<FileItem>> {
    //private class PopulateListTask extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Fetching File List...");
            this.dialog.show();

        }

        @Override
        protected List<FileItem> doInBackground(String... urls) {
            return (new ListContent(urls[0], getActivity())).getItems();
            //String json = null;
            //return new ArrayList<FileItem>(Arrays.asList(ConvertUtils.convert2Java(json)));
        }
/*        @Override
            protected String doInBackground(String...urls){
            AmahiContent amahiContent = new AmahiContent(getActivity());
            return amahiContent.PfeConnect(getActivity());
        }
*/
/*
        @Override
        protected void onPostExecute(List<FileItem> fileItems) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            mFileItemAdapter.clear();
            if (fileItems != null) {
                mFileItemAdapter.addAll(fileItems);
                ((FileItemAdapter) getListAdapter()).notifyDataSetChanged();
            }

            //setListAdapter(new ArrayAdapter<Offer>(FileListFragment.this, android.R.layout.simple_list_item_1, offerList));
        }*/
    }
}
