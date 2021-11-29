package com.example.instagramclone.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagramclone.FollowersActivity;
import com.example.instagramclone.PostDetailFragmentFragment;
import com.example.instagramclone.R;
import com.example.instagramclone.profile.ProfileFragment;
import com.example.instagramclone.search.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialTextView;

import java.util.HashMap;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    private Context context;
    private List<PostData> postDataList;
    private FirebaseUser firebaseUser;

    public PostAdapter(Context context, List<PostData> postDataList) {
        this.context = context;
        this.postDataList = postDataList;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_post_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final PostData postData = postDataList.get(position);
        Glide.with(context).load(Uri.parse(postData.getImageUrl())).into(holder.personalPostIV);
        holder.captionTV.setText("Caption  " + postData.getCaption());

        FirebaseDatabase.getInstance().getReference().child("Users").child(postData.getPublisher())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserData userData = snapshot.getValue(UserData.class);
                        if (userData.getImageUrl().equals("default")) {
                            holder.personalAccountIV.setImageResource(R.mipmap.ic_launcher);
                        } else {
                            Glide.with(context).load(userData.getImageUrl()).into(holder.personalAccountIV);
                        }
                        holder.username.setText(userData.getUsername());
                        holder.name.setText(userData.getName());
                        holder.publisher.setText(userData.getUsername());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        isLiked(postData.getPostId(), holder.like);
        noOfLikes(postData.getPostId(), holder.no_of_likes);
        noOfComments(postData.getPostId(), holder.no_of_comments);
        isSaved(postData.getPostId(), holder.save);

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.like.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(postData.getPostId())
                            .child(firebaseUser.getUid()).setValue(true);
                    addNotification(postData.getPostId(), postData.getPublisher());
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(postData.getPostId())
                            .child(firebaseUser.getUid()).removeValue();
                }
            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("postId", postData.getPostId());
                intent.putExtra("authorId", postData.getPublisher());
                context.startActivity(intent);
            }
        });

        holder.no_of_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("postId", postData.getPostId());
                intent.putExtra("authorId", postData.getPublisher());
                context.startActivity(intent);
            }
        });

        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.save.getTag().equals("Save")){
                    FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid())
                            .child(postData.getPostId()).setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid())
                            .child(postData.getPostId()).removeValue();
                }
            }
        });

        holder.personalAccountIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.getSharedPreferences("PROFILE", Context.MODE_PRIVATE)
                        .edit().putString("profileId", postData.getPublisher()).apply();

                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_layout, new ProfileFragment()).commit();
            }
        });

        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.getSharedPreferences("PROFILE", Context.MODE_PRIVATE)
                        .edit().putString("profileId", postData.getPublisher()).apply();

                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_layout, new ProfileFragment()).commit();
            }
        });

        holder.publisher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.getSharedPreferences("PROFILE", Context.MODE_PRIVATE)
                        .edit().putString("profileId", postData.getPublisher()).apply();

                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_layout, new ProfileFragment()).commit();
            }
        });

        holder.personalPostIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                        .putString("postId", postData.getPostId()).apply();
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_layout, new PostDetailFragmentFragment()).commit();
            }
        });

        holder.no_of_likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FollowersActivity.class);
                intent.putExtra("id", postData.getPostId());
                intent.putExtra("title", "Likes");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView personalAccountIV, more_menu, personalPostIV, like, comment, save;
        private TextView username, name, no_of_likes, publisher, no_of_comments;
        private SocialTextView captionTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            personalAccountIV = itemView.findViewById(R.id.personalAccountIV);
            more_menu = itemView.findViewById(R.id.more_menu);
            personalPostIV = itemView.findViewById(R.id.personalPostIV);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            save = itemView.findViewById(R.id.save);

            username = itemView.findViewById(R.id.username);
            name = itemView.findViewById(R.id.name);
            no_of_likes = itemView.findViewById(R.id.no_of_likes);
            publisher = itemView.findViewById(R.id.publisher);
            no_of_comments = itemView.findViewById(R.id.no_of_comments);

            captionTV = itemView.findViewById(R.id.captionTV);
        }
    }

    private void addNotification(String postId, String publisherId){
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", publisherId);
        map.put("postId", postId);
        map.put("text", "liked your post. ");
        map.put("isPost", true);

        FirebaseDatabase.getInstance().getReference().child("Notifications").child(firebaseUser.getUid()).push().setValue(map);
    }

    private void isSaved(String postId, ImageView save) {
        FirebaseDatabase.getInstance().getReference().child("Saves").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(postId).exists()){
                            save.setImageResource(R.drawable.ic_saved);
                            save.setTag("Saved");
                        } else {
                            save.setImageResource(R.drawable.ic_save);
                            save.setTag("Save");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void isLiked(String postId, ImageView imageView){
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.ic__red_heart);
                    imageView.setTag("liked");
                } else {
                    imageView.setImageResource(R.drawable.ic_heart);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void noOfLikes(String postId, TextView text){
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                text.setText(snapshot.getChildrenCount() + " likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void noOfComments(String postId, TextView text){
        FirebaseDatabase.getInstance().getReference().child("Comments").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                text.setText("View all " + snapshot.getChildrenCount() + " comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
