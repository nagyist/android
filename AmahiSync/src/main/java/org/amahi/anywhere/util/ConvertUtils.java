/*
 * This source module contains confidential and proprietary
 * information of Amahi Inc. It is not to be disclosed or used
 * except in accordance with applicable agreements. This
 * copyright notice does not evidence any actual or intended
 * publication of such source code.
 *
 * Copyright (c) 2014 Amahi. All rights reserved.
 */

package org.amahi.anywhere.util;

//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.*;


import android.util.Log;

import com.google.gson.Gson;

import org.amahi.anywhere.model.FileItem;

/**
 * Created by setdosa on 8/23/13.
 */
public final class ConvertUtils {

    private static final String TAG = "ConvertUtils";

//    public static void main(String[] args) {
    //String json = "{\"name\":\"mkyong\", \"age\":\"29\"}";
//        String json = "{"
//                + "  \"query\": \"Pizza\", "
//                + "  \"locations\": [ 94043, 90210 ] "
//                + "}";

//        String json = "[{\"FileName\":\"%2F00-Special+Cases\",\"IsDirectory\":true},{\"FileName\":\"%2FMusic\",\"IsDirectory\":true},{\"FileName\":\"%2F05-Other\",\"IsDirectory\":true},{\"FileName\":\"%2F02-Images\",\"IsDirectory\":true},{\"FileName\":\"%2F04-Extensions\",\"IsDirectory\":true},{\"FileName\":\"%2F03-Directories\",\"IsDirectory\":true},{\"FileName\":\"%2FMedia\",\"IsDirectory\":true}]";
    //[{\"FileName\":\"%2FUltravox+-+Dancing+With+Tears+In+My+Eyes.mp3\",\"IsDirectory\":false},{\"FileName\":\"%2FKesha+Feat.+3OH%213+-+Blah+Blah+Blah.mp3\",\"IsDirectory\":false},{\"FileName\":\"%2FColdplay+-+Yellow.mp3\",\"IsDirectory\":false},{\"FileName\":\"%2FBlackeyed+Peas+-+01+-+Let%27s+Get+It+Started.mp3\",\"IsDirectory\":false},{\"FileName\":\"%2FCarry+Out+%28feat+Justin+Timberlake%29.mp3\",\"IsDirectory\":false},{\"FileName\":\"%2FTaio+Cruz+-+Break+Your+Heart.mp3\",\"IsDirectory\":false},{\"FileName\":\"%2FB.o.B.+Feat.+Bruno+Mars+-+Nothing+On+You.mp3\",\"IsDirectory\":false},{\"FileName\":\"%2FRihanna+-+Rude+Boy.mp3\",\"IsDirectory\":false},{\"FileName\":\"%2FIn+My+Place+-+Coldplay.mp3\",\"IsDirectory\":false},{\"FileName\":\"%2FUsher-OMG+%28feat.+Will.I.Am%29.mp3\",\"IsDirectory\":false},{\"FileName\":\"%2FLady+Gaga+Ft.+Beyonce+-+Telephone.mp3\",\"IsDirectory\":false}]
//        convert2Java(json);
//    }

    public static FileItem[] convert2Java(String jsonString) {

        Log.d(TAG, "JSON String is: " + jsonString);
        FileItem[] fileItem = null;

        try {
            Gson gson = new Gson();
            fileItem = gson.fromJson(jsonString, FileItem[].class);
            Log.d(TAG, "Number of files in JSON: " + (fileItem == null ? "null" : fileItem.length));
        } catch (Exception e) {
            Log.e(TAG, "JSON conversion", e);

        }
        return fileItem;
    }
}
