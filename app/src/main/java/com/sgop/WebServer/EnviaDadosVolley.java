package com.sgop.WebServer;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.Map;

/**
 * Created by anderson on 11/10/15.
 */
public class EnviaDadosVolley extends Request<String> {

    private Map<String, String> mParams;

    public EnviaDadosVolley(int method, String url, String parmas, Response.ErrorListener listener) {
        super(method, url, listener);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        return null;
    }

    @Override
    protected void deliverResponse(String response) {

    }
}
