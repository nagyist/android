/*
 * This source module contains confidential and proprietary
 * information of Amahi Inc. It is not to be disclosed or used
 * except in accordance with applicable agreements. This
 * copyright notice does not evidence any actual or intended
 * publication of such source code.
 *
 * Copyright (c) 2014 Amahi. All rights reserved.
 */

package org.amahi.anywhere.model;

import android.app.Activity;
import android.util.Log;

import org.amahi.anywhere.net.AmahiHttp;
import org.amahi.anywhere.util.ConvertUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by setdosa on 8/27/13.
 */
public class ListContent {
    private static final String TAG = "ListContent";
    //TODO: get the file content from Server


    private String mPath;

    public static String sharePath = "shares";

    private Date mTimeStamp;

    private List<FileItem> mItems;

    private Map<String, FileItem> mItemMap = new HashMap<String, FileItem>();

    private Activity mActivity;

    public List<FileItem> getItems() {
        return mItems;
    }

//    public Map<String, FileItem> getItemMap() {
//        return mItemMap;
//    }

    public ListContent(String path, Activity activity) {
        mTimeStamp = new Date();
        mActivity = activity;
        mPath = path;

        //mPath = "files?s=Media"; //
//        if (!mPath.equalsIgnoreCase("%2f")) {
//            mPath = "%2f" + mPath;
//        }

//        DBAdapter dbAdapter = new DBAdapter(activity);

        //Check to see if already downloaded
        //TODO: add check for timestamp and do update if already exists
//        String json = dbAdapter.getFolderList(mPath);
        String json = null;
        if (json == null) {
            Log.d(TAG, "Not in database, getting from Amahi");
            //        String json = "[{\"FileName\":\"/00-Special+Cases\",\"IsDirectory\":true},{\"FileName\":\"/Music\",\"IsDirectory\":true},{\"FileName\":\"/05-Other\",\"IsDirectory\":true},{\"FileName\":\"/02-Images\",\"IsDirectory\":true},{\"FileName\":\"/04-Extensions\",\"IsDirectory\":true},{\"FileName\":\"/03-Directories\",\"IsDirectory\":true},{\"FileName\":\"/Media\",\"IsDirectory\":true}]";

            AmahiHttp amahiHttp = new AmahiHttp(mPath, mActivity);
            //        AmahiHttp amahiHttp = new AmahiHttp("http://google.com", mActivity);
            try {
                json = amahiHttp.getData();
            } catch (IOException io) {
                Log.e(TAG, "json get error", io);
            }
            //[{\"FileName\":\"/Ultravox+-+Dancing+With+Tears+In+My+Eyes.mp3\",\"IsDirectory\":false},{\"FileName\":\"/Kesha+Feat.+3OH%213+-+Blah+Blah+Blah.mp3\",\"IsDirectory\":false},{\"FileName\":\"/Coldplay+-+Yellow.mp3\",\"IsDirectory\":false},{\"FileName\":\"/Blackeyed+Peas+-+01+-+Let%27s+Get+It+Started.mp3\",\"IsDirectory\":false},{\"FileName\":\"/Carry+Out+%28feat+Justin+Timberlake%29.mp3\",\"IsDirectory\":false},{\"FileName\":\"/Taio+Cruz+-+Break+Your+Heart.mp3\",\"IsDirectory\":false},{\"FileName\":\"/B.o.B.+Feat.+Bruno+Mars+-+Nothing+On+You.mp3\",\"IsDirectory\":false},{\"FileName\":\"/Rihanna+-+Rude+Boy.mp3\",\"IsDirectory\":false},{\"FileName\":\"/In+My+Place+-+Coldplay.mp3\",\"IsDirectory\":false},{\"FileName\":\"/Usher-OMG+%28feat.+Will.I.Am%29.mp3\",\"IsDirectory\":false},{\"FileName\":\"/Lady+Gaga+Ft.+Beyonce+-+Telephone.mp3\",\"IsDirectory\":false}]
            if (json != null) {
                try {
                    json = URLDecoder.decode(json, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "Decoding error", e);
                }
//                dbAdapter.addFolder(mPath, json, mTimeStamp);
            }
        }


        //TODO: make the database an application var
//        dbAdapter.closeDatabase();
        //only add if there is data
        if (json != null) {
            mItems = new ArrayList<FileItem>(Arrays.asList(ConvertUtils.convert2Java(json)));

//        for (FileItem fileItem : mItems) {
//            mItemMap.put(fileItem.getId(), fileItem);
//        }
        }
    }

//    public String getRootDirectory () {
//        return sharePath;
//    }
}
