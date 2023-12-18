package com.smiligence.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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

public class EmployeeDetailActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {

    RecyclerView recyclerView;
    int id;
    ApiService apiService;
    Retrofit retrofit;

    EmployeeDetailAdapter employeeDetailAdapter;

    private NetworkStateReceiver networkStateReceiver;
    AlertDialog network_alertDialog;
    ViewGroup viewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_detail);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("Employee Detail");


        viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.internet_dialog, viewGroup, false);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setView(dialogView);
        network_alertDialog = builder1.create();
        startNetworkBroadcastReceiver(this);


        Intent intent = getIntent();
         id = intent.getIntExtra("employeeid",0);



         recyclerView = findViewById(R.id.all_empdetailrecyclerview);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setNestedScrollingEnabled(false);

        getEmployeeDetailList();





    }

    public void getEmployeeDetailList(){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/") // Replace with the base URL of your API
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService =retrofit.create(ApiService.class);

        Call<List<Usetdetails>> call = apiService.getuserDetailList(id);


        call.enqueue(new Callback<List<Usetdetails>>() {
            @Override
            public void onResponse(Call<List<Usetdetails>> call, Response<List<Usetdetails>> response) {
                if (response.isSuccessful()){

                    List<Usetdetails> userList = response.body();
                    employeeDetailAdapter = new EmployeeDetailAdapter(EmployeeDetailActivity.this,userList);
                    recyclerView.setAdapter(employeeDetailAdapter);
                    employeeDetailAdapter.notifyDataSetChanged();

                }else {
                    Toast.makeText(EmployeeDetailActivity.this, "Failure", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<List<Usetdetails>> call, Throwable t) {
                Toast.makeText(EmployeeDetailActivity.this, "Failure", Toast.LENGTH_SHORT).show();

            }
        });




    }

    @Override
    public void networkAvailable() {
        network_alertDialog.dismiss();
    }

    @Override
    public void networkUnavailable() {
        if (!((Activity) EmployeeDetailActivity.this).isFinishing()) {
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