package com.lky.whome.network;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lky.whome.R;
import com.lky.whome.dialog.ProgressDialog;
import com.lky.whome.toast.CustomToast;

import java.util.HashMap;
import java.util.Map;

public class VolleyNetwork {

    public interface VolleyCallback{
        void callback(String response);
    }

    private final String TAG = VolleyNetwork.class.getSimpleName();
    private static RequestQueue mRequestQueue;
    private Context mContext;

    public VolleyNetwork(Context context){
        if(mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
        mContext = context;
    }

    public void requestGet(String url, ContentValues params, VolleyCallback callback){
        try {
            ProgressDialog.showProgressDialog(mContext);
            String setParams = getParamURL(params);
            String completeUrl = url + setParams;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, completeUrl,
                    response -> {
                        // Display the first 500 characters of the response string.
                        Log.d(TAG, "onResponse: " + response);

                        callback.callback(response);
                        ProgressDialog.dismissProgressDialog();

                    }, error -> {
                Log.d(TAG, "onErrorResponse: " + error);
                ProgressDialog.dismissProgressDialog();
                Toast.makeText(mContext, mContext.getString(R.string.NO_CONNECTED_SERVER), Toast.LENGTH_SHORT).show();
            });
            mRequestQueue.add(stringRequest);
        }catch(Exception e){
            e.printStackTrace();
        }finally {

        }
    }

    public void requestPost(String url, ContentValues params, VolleyCallback callback){
        try {
            ProgressDialog.showProgressDialog(mContext);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        // Display the first 500 characters of the response string.
                        Log.d(TAG, "onResponse: " + response);

                        callback.callback(response);
                        ProgressDialog.dismissProgressDialog();

                    },
                    error -> {
                        Log.d(TAG, "onErrorResponse: " + error);
                        ProgressDialog.dismissProgressDialog();
                        Toast.makeText(mContext, mContext.getString(R.string.NO_CONNECTED_SERVER), Toast.LENGTH_SHORT).show();
            })
            {
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> bodyParams = getParamBody(params);
                    return bodyParams;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    final HashMap<String, String> headers = new HashMap<>();
                    return headers;
                }
            };
            mRequestQueue.add(stringRequest);
        }catch(Exception e){
            e.printStackTrace();
        }finally {

        }
    }

    public String getParamURL(ContentValues contentValues){

        StringBuilder parameter = new StringBuilder();

        if(contentValues == null){
            parameter.append("");
        }else{
            boolean hasNext = false;
            for(Map.Entry<String, Object> param : contentValues.valueSet()){
                String key = param.getKey();
                String value = param.getValue().toString();

                if(hasNext){
                    parameter.append("&");
                }else{
                    parameter.append("?");
                }
                    parameter.append(key)
                             .append("=")
                             .append(value);
                if(parameter.length() > 1){
                    hasNext = true;
                }
            }
        }
        return parameter.toString();
    }

    public static Map<String,String> getParamBody(ContentValues contentValues){

        Map<String,String> resultMap = new HashMap<>();

        for(Map.Entry<String, Object> param : contentValues.valueSet()){
            String key = param.getKey();
            String value = param.getValue().toString();
            resultMap.put(key,value);
        }
        return resultMap;
    }
}


