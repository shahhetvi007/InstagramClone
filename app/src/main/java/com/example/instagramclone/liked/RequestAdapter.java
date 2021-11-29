package com.example.instagramclone.liked;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagramclone.R;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {

    private Context context;
    private List<RequestData> requestData;

    public RequestAdapter(Context context, List<RequestData> requestData) {
        this.context = context;
        this.requestData = requestData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RequestData data = requestData.get(position);
        holder.requestTV.setText(data.getRequest());
        try {
            if (data.getProfile() != -1){
                Glide.with(context).load(data.getProfile()).into(holder.requestIV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Confirm!", Toast.LENGTH_SHORT).show();
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView requestIV;
        private TextView requestTV;
        private Button confirmBtn, deleteBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            requestIV = itemView.findViewById(R.id.requestIV);
            requestTV = itemView.findViewById(R.id.requestTV);
            confirmBtn = itemView.findViewById(R.id.confirmBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}
