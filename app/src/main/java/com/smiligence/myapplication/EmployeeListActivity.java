package com.smiligence.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EmployeeListActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener{

    RecyclerView employeelistrecyclerview;
    EmployeelistAdapter employeelistAdapter;
    ApiService apiService;
    Retrofit retrofit;

    private NetworkStateReceiver networkStateReceiver;
    AlertDialog network_alertDialog;
    ViewGroup viewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("Employee List");

        viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.internet_dialog, viewGroup, false);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setView(dialogView);
        network_alertDialog = builder1.create();
        startNetworkBroadcastReceiver(this);

        employeelistrecyclerview = findViewById(R.id.all_emplistrecyclerview);





        employeelistrecyclerview.setHasFixedSize(true);
        employeelistrecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        employeelistrecyclerview.setNestedScrollingEnabled(false);

        getEmployeelist();




    }

    public void getEmployeelist(){

        retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/") // Replace with the base URL of your API
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService =retrofit.create(ApiService.class);

        Call<List<User>> call = apiService.getemployeeList();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()){

                    List<User> userList = response.body();
                    employeelistAdapter = new EmployeelistAdapter(EmployeeListActivity.this,userList);
                    employeelistrecyclerview.setAdapter(employeelistAdapter);
                    employeelistAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });



    }
    @Override
    public void networkAvailable() {
        network_alertDialog.dismiss();
    }

    @Override
    public void networkUnavailable() {
        if (!((Activity) EmployeeListActivity.this).isFinishing()) {
            showCustomDialog();
        }
    }

    public void startNetworkBroadcastReceiver(Context currentContext) {
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener((NetworkStateReceiver.NetworkStateReceiverListener) currentContext);
        registerNetworkBroadcastReceiver(currentContext);
    }


    public void registerNetworkBroadcastReceiver(Context currentContext) {
        currentContext.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public void unregisterNetworkBroadcastReceiver(Context currentContext) {
        currentContext.unregisterReceiver(networkStateReceiver);
    }

    private void showCustomDialog() {

        network_alertDialog.setCancelable(false);
        network_alertDialog.show();

    }
}