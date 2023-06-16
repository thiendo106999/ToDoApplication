package com.example.todoapplication.data.remote;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.example.todoapplication.data.models.Task;
import com.example.todoapplication.utils.APIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AppApi {
    private final String url = "http://192.168.17.154:8080/api/task";
    private static AppApi mInstance;

    public static AppApi getInstance() {
        if (mInstance == null) {
            mInstance = new AppApi();
        }
        return mInstance;
    }

    public void getTaskList(Context context, ApiResponseListener listener) {
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            Type taskListType = new TypeToken<ArrayList<Task>>() {
            }.getType();
            Gson gson = new Gson();
            ArrayList<Task> tasks = gson.fromJson(response, taskListType);
            listener.onResponse(tasks);
        }, error -> {
            listener.onErrorResponse(error.getMessage());
        });
        RequestQueue requestQueue = APIUtils.getInstance(context).getRequestQueue();
        requestQueue.add(request);
    }

    public void createTask(Context context, ApiResponseListener listener, Task task) {
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            listener.onResponse(response);
        }, error -> {
            Log.e("TAG", "createTask: " + error.toString() );
        }){
            @Override
            public byte[] getBody() throws AuthFailureError {
                String taskJson = new Gson().toJson(task);
                return taskJson.getBytes();
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");

                return headers;
            }

        };
        RequestQueue requestQueue = APIUtils.getInstance(context).getRequestQueue();
        requestQueue.add(request);
    }

    public void deleteTaskById(Long id, Context context, ApiResponseListener listener) {
        String uri = url + "/" + id;
        StringRequest request = new StringRequest(Request.Method.DELETE, uri, response -> {
            listener.onResponse(response);
        }, error -> {
            listener.onErrorResponse(error.getMessage());
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id.toString());
                return params;
            }
        };
        RequestQueue requestQueue = APIUtils.getInstance(context).getRequestQueue();
        requestQueue.add(request);
    }

    public interface ApiResponseListener<T> {
        void onResponse(T t);

        void onErrorResponse(String errorMessage);
    }
}
