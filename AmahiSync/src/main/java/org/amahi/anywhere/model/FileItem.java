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

import com.google.gson.annotations.SerializedName;

/**
 * Created by setdosa on 8/27/13.
 */
public class FileItem {
    String FileName;

    String IsDirectory = "";

    public static long idCnt = 1;


    public long id;

    public FileItem() {
        id = idCnt++;
    }

    public String getId() {
        return id + "";
    }

    public boolean getIsDirectory() {
        //return IsDirectory.equalsIgnoreCase("true") ? true : false;
        if (mimi_type == "text/directory")
            return true;
        else
            return false;
    }

//    public void setIsDirectory(String isDirectory) {
//        IsDirectory = isDirectory;
//    }

//    public String getFileName() {
//
//        return FileName;
//    }

//    public void setFileName(String fileName) {
//        FileName = fileName;
//    }


    @Override
    public String toString() {
        return FileName;
    }

    @SerializedName("mime_type")
    public String mimi_type;

    @SerializedName("name")
    public String name;

    @SerializedName("size")
    public String size;


}
