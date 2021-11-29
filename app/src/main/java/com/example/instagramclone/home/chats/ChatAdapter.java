package com.example.instagramclone.home.chats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagramclone.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private Context context;
    private List<ChatData> chatDataList;

    public ChatAdapter(Context context, List<ChatData> chatDataList) {
        this.context = context;
        this.chatDataList = chatDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ChatData chatData = chatDataList.get(position);
        try {
            if (chatData.getImage() != -1){
                Glide.with(context).load(chatData.getImage()).into(holder.chatProfileIV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.personName.setText(chatData.getName());
        holder.messageTV.setText(chatData.getMessage());
        holder.cameraIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Camera", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView chatProfileIV, cameraIV;
        private TextView personName, messageTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            chatProfileIV = itemView.findViewById(R.id.chatProfileIV);
            personName = itemView.findViewById(R.id.personName);
            messageTV = itemView.findViewById(R.id.messageTV);
            cameraIV = itemView.findViewById(R.id.cameraIV);
        }
    }
}
