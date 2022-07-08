package com.kakaologin_sample;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class Requester {
    private String HOST;
    private String PORT;
    private String URL;
    private JSONObject jsonObject;

    public Requester(String host, String port){
        this.HOST = host;
        this.PORT = port;
        this.URL = "http://" + host + ":" + port + "/";
    }

    public void post(String dir, JSONObject parameters, Context context){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, this.URL, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());

                    String resultId = jsonObject.getString("approve_id");
                    String resultPassword = jsonObject.getString("approve_pw");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    public void get(String dir, JSONObject parameters, Context context){
        RequestQueue requestQueue = Volley.newRequestQueue(context);

    }

}

//    //JSON형식으로 데이터 통신을 진행합니다!
//    JSONObject testjson = new JSONObject();
//    //입력해둔 edittext의 id와 pw값을 받아와 put해줍니다 : 데이터를 json형식으로 바꿔 넣어주었습니다.
//        testjson.put("id", id.getText().toString());
//        testjson.put("password", pw.getText().toString());
//    String jsonString = testjson.toString(); //완성된 json 포맷


//
//Requester requester = new Requester();
//req = requester.request('Washteria')
//req.get("location")
//
//
//DB : WashteriaDB
//TABLE : Washteria
//Values : location
