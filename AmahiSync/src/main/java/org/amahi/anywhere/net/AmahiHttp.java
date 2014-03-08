/*
 * This source module contains confidential and proprietary
 * information of Amahi Inc. It is not to be disclosed or used
 * except in accordance with applicable agreements. This
 * copyright notice does not evidence any actual or intended
 * publication of such source code.
 *
 * Copyright (c) 2014 Amahi. All rights reserved.
 */

package org.amahi.anywhere.net;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by setdosa on 9/4/13.
 */
public class AmahiHttp {

    private static String TAG = "AmahiHTTP";
    private String mPath;
    private Activity mActivity;
    private final String URL = "https://pfe.amahi.org/";

    //to accept self signed certificate

    /**
     * Trust every server - don't check for any certificate
     */
    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //--

    public AmahiHttp(String path, Activity activity) {
        mPath = path;
        mActivity = activity;
    }



    public String getData() throws IOException {
        ConnectivityManager connMgr = (ConnectivityManager)
                mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
//            new DownloadTask().execute(mPath);
            return downloadUrl();
        } else {
            //TODO raise toast
            return null;
        }
    }

    private String downloadUrl() throws java.io.IOException {
        InputStream is = null;

        try {
            Log.d(TAG, "Path is: " + URL + mPath);
            URL url = new URL(URL + mPath);
//            //needed to ignore self signed certificate
//            trustAllHosts();
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

//            //needed to ignore self signed certificate
//            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//                    return true;
//                }
//            });


            //---

            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
//            conn.setRequestProperty("API-Key", "58159b212b3748a3b360a3f5ac99dc16535202c6");
            conn.setRequestProperty("API-Key", "34f9cdf4dc9174d0e1d8a14ef9c3c0b4c8ad7477");
            conn.setRequestProperty("User-Agent", "Amahi Anywhere (1.0) Android");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            return readIt(is, conn.getContentLength());


            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } catch (Exception e) {
            Log.e(TAG, "Http Error", e);
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return null;
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException {
        Reader reader;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

//    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... urls) {
//
//            // params comes from the execute() call: params[0] is the url.
//            try {
//                return downloadUrl(urls[0]);
//            } catch (IOException e) {
//                return "Unable to retrieve web page. URL may be invalid.";
//            }
//        }
//        // onPostExecute displays the results of the AsyncTask.
//        @Override
//        protected void onPostExecute(String result) {
//
//        }
//    }

}

