package com.smiligence.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EmployeelistAdapter extends RecyclerView.Adapter<EmployeelistAdapter.EmployeeListViewHolder> {

    public Context mcontext;
    public List<User> muserList;



    public EmployeelistAdapter(Context context,List<User> userList){
        this.mcontext = context;
        this.muserList =userList;

    }

    @NonNull
    @Override
    public EmployeelistAdapter.EmployeeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mcontext).inflate(R.layout.employeelist_cardview,parent,false);
        EmployeeListViewHolder viewHolder = new EmployeeListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeelistAdapter.EmployeeListViewHolder holder, int position) {
      String camelname =  convertToCamelCase(muserList.get(position).getName());

        holder.user_name.setText(camelname);
        holder.user_emailid.setText(muserList.get(position).getEmail().toLowerCase());


        holder.user_emailid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" +muserList.get(position).getEmail()));
                mcontext.startActivity(emailIntent);
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext,EmployeeDetailActivity.class);
                intent.putExtra("employeeid",position+1);
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return muserList.size();
    }

    public class EmployeeListViewHolder extends RecyclerView.ViewHolder{

        TextView user_name,user_emailid;
        CardView cardView;

        public EmployeeListViewHolder(@NonNull View itemView) {
            super(itemView);
            user_name = itemView.findViewById(R.id.user_name);
            user_emailid = itemView.findViewById(R.id.user_emailid);
            cardView = itemView.findViewById(R.id.card_view);


        }
    }


    public static String convertToCamelCase(String input) {
      //  String input=input1.toLowerCase();
        StringBuilder camelCase = new StringBuilder();

        boolean capitalizeNext = false;

        for (char c : input.toCharArray()) {
            if (c == ' ') {
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    camelCase.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    camelCase.append(c);
                }
            }
        }

        return camelCase.toString();
    }
}
