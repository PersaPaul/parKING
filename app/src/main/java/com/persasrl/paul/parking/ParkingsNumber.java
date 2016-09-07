package com.persasrl.paul.parking;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ParkingsNumber extends StringRequest {
    private static final String PARK_REQUEST_URL = "http://parking.netau.net/getParkings.php";
    private Map<String, String> params;

    public ParkingsNumber(Response.Listener<String> listener) {
        super(Method.POST, PARK_REQUEST_URL, listener, null);
        params = new HashMap<>();
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}