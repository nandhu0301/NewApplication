package com.smiligence.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class EmployeeDetailAdapter extends  RecyclerView.Adapter<EmployeeDetailAdapter.EmployeedetailViewHolder>
        {
            Context mcontext;
            List<Usetdetails> musetdetails;

            public EmployeeDetailAdapter(Context context, List<Usetdetails>  usetdetails){
                this.mcontext =context;
                this.musetdetails = usetdetails;
            }

            @NonNull
            @Override
            public EmployeeDetailAdapter.EmployeedetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(mcontext).inflate(R.layout.employeeldetail_cardview,parent,false);
                EmployeeDetailAdapter.EmployeedetailViewHolder viewHolder = new EmployeeDetailAdapter.EmployeedetailViewHolder(view);
                return viewHolder;
            }

            @Override
            public void onBindViewHolder(@NonNull EmployeeDetailAdapter.EmployeedetailViewHolder holder, int position) {
                String camelname =  convertToCamelCase(musetdetails.get(position).getName());

                holder.employee_Name.setText(camelname);
                holder.Employee_Phone.setText(musetdetails.get(position).getPhone());
                holder.employee_email.setText(musetdetails.get(position).getEmail());


                holder.employee_email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                        emailIntent.setData(Uri.parse("mailto:" +musetdetails.get(position).getEmail()));
                        mcontext.startActivity(emailIntent);
                    }
                });

                holder.Employee_Phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                        dialIntent.setData(Uri.parse("tel:" +musetdetails.get(position).getPhone()));
                        mcontext.startActivity(dialIntent);
                    }
                });




            }

            @Override
            public int getItemCount() {
                return musetdetails.size();
            }

            public class EmployeedetailViewHolder  extends RecyclerView.ViewHolder{


                TextView employee_Name,Employee_Phone,employee_email;
                public EmployeedetailViewHolder(@NonNull View itemView) {
                    super(itemView);

                    employee_Name = itemView.findViewById(R.id.emp_name);
                    Employee_Phone = itemView.findViewById(R.id.emp_phoneNumber);
                    employee_email = itemView.findViewById(R.id.emp_emailid);
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
