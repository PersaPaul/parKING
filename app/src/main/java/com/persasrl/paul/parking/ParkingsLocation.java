package com.persasrl.paul.parking;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class ParkingsLocation extends StringRequest {
    private static final String PARK_REQUEST_URL = "http://parking.netau.net/getParkings.php";
    private Map<String, String> params;

    public ParkingsLocation(int park_id, Response.Listener<String> listener) {
        super(Method.POST, PARK_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("park_id", park_id + "");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
