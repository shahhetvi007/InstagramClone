package com.example.instagramclone.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagramclone.R;
import com.example.instagramclone.search.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.MyViewHolder> {

    private Context context;
    private List<StoryData> storyDataList;

    public StoryAdapter(Context context, List<StoryData> storyDataList) {
        this.context = context;
        this.storyDataList = storyDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.add_story_item, parent, false);
            return new MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.story_item_layout, parent, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        StoryData data = storyDataList.get(position);
        userInfo(holder, data.getUserId(), position);

        if (holder.getAdapterPosition() != 0){
            seenStory(holder, data.getUserId());
        }

        if (holder.getAdapterPosition() == 0){
            myStory(holder.add_story_text, holder.story_plus, false);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.getAdapterPosition() == 0){
                    myStory(holder.add_story_text, holder.story_plus, true);
                } else {
                    //open story activity
                    Intent intent = new Intent(context, StoryActivity.class);
                    intent.putExtra("userId", data.getUserId());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return storyDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView story_photo, story_plus, story_photo_seen;
        private TextView story_username, add_story_text;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            story_photo = itemView.findViewById(R.id.story_photo);
            story_plus = itemView.findViewById(R.id.story_plus);
            story_photo_seen = itemView.findViewById(R.id.story_photo_seen);
            story_username = itemView.findViewById(R.id.story_username);
            add_story_text = itemView.findViewById(R.id.add_story_text);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    private void userInfo(MyViewHolder holder, String userId, int position){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData user = snapshot.getValue(UserData.class);
                Glide.with(context).load(user.getImageUrl()).into(holder.story_photo);
                if (position != 0){
                    Glide.with(context).load(user.getImageUrl()).into(holder.story_photo_seen);
                    holder.story_username.setText(user.getUsername());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void seenStory(MyViewHolder holder, String userId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    //check if any story view is remaining
                    if (!snapshot.child("Views").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()
                    && System.currentTimeMillis() < dataSnapshot.getValue(StoryData.class).getTimeEnd()){
                        i++;
                    }
                }

                if (i>0){
                    //if story view is remaining set as pink background photo
                    holder.story_photo.setVisibility(View.VISIBLE);
                    holder.story_photo_seen.setVisibility(View.GONE);
                } else {
                    holder.story_photo.setVisibility(View.GONE);
                    holder.story_photo_seen.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void myStory(TextView textView, ImageView imageView, boolean click){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                long currentTime = System.currentTimeMillis();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    StoryData story = dataSnapshot.getValue(StoryData.class);
                    if (currentTime > story.getTimeStart() && currentTime < story.getTimeEnd()){
                        count++;
                    }
                }
                if (click){
                    if (count > 0){
                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "View Story", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //open story activity
                                Intent intent = new Intent(context, StoryActivity.class);
                                intent.putExtra("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                context.startActivity(intent);
                                dialog.dismiss();
                            }
                        });

                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add Story", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //open add story activity
                                Intent intent = new Intent(context, AddStoryActivity.class);
                                context.startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    } else {
                        //open add story activity
                        Intent intent = new Intent(context, AddStoryActivity.class);
                        context.startActivity(intent);

                    }
                } else {
                    if (count > 0){
                        textView.setText("My Story");
                        imageView.setVisibility(View.GONE);
                    } else {
                        textView.setText("Add Story");
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
