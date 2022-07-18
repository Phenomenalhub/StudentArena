package com.example.studentarena.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.studentarena.ChatActivity;
import com.example.studentarena.databinding.ItemMessageBinding;
import com.example.studentarena.model.Message;
import com.example.studentarena.model.User;
import com.parse.ParseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> messages;
    private Context context;

    public MessageAdapter(List<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMessageBinding binding = ItemMessageBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ItemMessageBinding itemMessageBinding;
//        GetRelativeTime getRelativeTime = new GetRelativeTime();

        public ViewHolder(ItemMessageBinding binding) {
            super(binding.getRoot());
            itemMessageBinding = binding;
            itemMessageBinding.getRoot().setOnClickListener(this);
        }

        public void bind(Message message) {

            if (message.getSender().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                itemMessageBinding.tvMessageSenderName.setText(message.getReceiver().getUsername());
                if (message.getReceiver().getProfileImage() != null) {
                    Glide.with(context).load(message.getReceiver().getProfileImage().getUrl()).circleCrop().into(itemMessageBinding.ivMessageSender);
                }
            } else {
                itemMessageBinding.tvMessageSenderName.setText(message.getSender().getUsername());
                if (message.getSender().getProfileImage() != null) {
                    Glide.with(context).load(message.getSender().getProfileImage().getUrl()).circleCrop().into(itemMessageBinding.ivMessageSender);
                }
            }

//            itemMessageBinding.tvMessageSent.setText((message.getMessage()));
//            String dateSent = getRelativeTime.getRelativeTimeAgo(message.getCreatedAt().toString());
//            itemMessageBinding.tvMessageDateSent.setText(dateSent);

        }

        @Override
        public void onClick(View v) {

            Intent i = new Intent(context, ChatActivity.class);
            User otherUser;
            String userId = ParseUser.getCurrentUser().getObjectId().toString();
            String messageSenderId = messages.get(getAdapterPosition()).getSender().getObjectId().toString();
            if (userId.equals(messageSenderId))
            {
                otherUser = messages.get(getAdapterPosition()).getReceiver();
            } else {
                otherUser = messages.get(getAdapterPosition()).getSender();
            }
            i.putExtra("otherUser", otherUser);
            context.startActivity(i);
        }
    }
}
