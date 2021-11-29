package com.example.instagramclone.home.rooms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagramclone.R;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.MyViewHolder> {

    private Context context;
    private List<RoomData> roomDataList;

    public RoomAdapter(Context context, List<RoomData> roomDataList) {
        this.context = context;
        this.roomDataList = roomDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.room_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RoomData data = roomDataList.get(position);
        holder.personAccountName.setText(data.getPersonAccountName());
        holder.personName.setText(data.getPersonName());
        try {
            if (data.getImage() != -1){
                Glide.with(context).load(data.getImage()).into(holder.roomProfileIV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return roomDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView roomProfileIV;
        private TextView personAccountName, personName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            roomProfileIV = itemView.findViewById(R.id.roomProfileIV);
            personAccountName = itemView.findViewById(R.id.personAccountName);
            personName = itemView.findViewById(R.id.personName);
        }
    }
}
