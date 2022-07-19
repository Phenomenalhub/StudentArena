package com.example.studentarena.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.studentarena.utilities.GetRelativeTime;
import com.example.studentarena.R;
import com.example.studentarena.model.Message;
import com.example.studentarena.model.User;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<Message> chats;
    private Context mContext;
    private static final int MESSAGE_OUTGOING = 123;
    private static final int MESSAGE_INCOMING = 321;
    private String mUserId = ParseUser.getCurrentUser().getObjectId();

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        if (viewType == MESSAGE_INCOMING) {
            View contactView = inflater.inflate(R.layout.message_incoming, parent, false);
            return new IncomingChatViewHolder(contactView);
        } else if (viewType == MESSAGE_OUTGOING) {
            View contactView = inflater.inflate(R.layout.message_outgoing, parent, false);
            return new OutgoingChatViewHolder(contactView);
        } else {
            throw new IllegalArgumentException("Unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Message message = chats.get(position);
        holder.bindMessage(message);
    }

    public ChatAdapter(Context context, String userId, List<Message> messages) {
        chats = messages;
        mContext = context;
        this.mUserId = userId;
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isMe(position)) {
            return MESSAGE_OUTGOING;
        } else {
            return MESSAGE_INCOMING;
        }
    }

    private boolean isMe(int position) {
        Message message = chats.get(position);
        return message.getSender() != null && message.getSender().getObjectId().equals(ParseUser.getCurrentUser().getObjectId());
    }

    public abstract class ChatViewHolder extends RecyclerView.ViewHolder {
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        abstract void bindMessage(Message message);
    }

    public class IncomingChatViewHolder extends ChatViewHolder {
        ImageView imageOther;
        TextView body;
        TextView tvTimestamp_other;
        TextView tvUser_other;
        private Context mContext;

        public IncomingChatViewHolder(View itemView) {
            super(itemView);
            imageOther = (ImageView) itemView.findViewById(R.id.ivProfile_other);
            body = (TextView) itemView.findViewById(R.id.tvMessage_other);
            tvUser_other = (TextView) itemView.findViewById(R.id.tvUser_other);
            tvTimestamp_other = (TextView) itemView.findViewById(R.id.tvTimestamp_other);
        }

        @Override
        public void bindMessage(Message message) {
            User otherUser = (User) new User();
            try{
                otherUser = (User) message.getSender().fetchIfNeeded();
            } catch (ParseException e){

            }
            body.setText(message.getBody());
            tvUser_other.setText(message.getSender().getUsername());
            tvTimestamp_other.setText(GetRelativeTime.getSimpleTime(message.getCreatedAt()));
        }
    }

    public class OutgoingChatViewHolder extends ChatViewHolder {
        TextView body;
        TextView tvTimestamp_me;

        public OutgoingChatViewHolder(View itemView) {
            super(itemView);
            body = (TextView) itemView.findViewById(R.id.tvMessage_me);
            tvTimestamp_me = (TextView) itemView.findViewById(R.id.tvTimestamp_me);
        }

        @Override
        public void bindMessage(Message message) {
            User otherMe = (User) new User();
            try{
                otherMe = (User) message.getSender().fetchIfNeeded();
            } catch (ParseException e){

            }
            body.setText(message.getBody());
            tvTimestamp_me.setText(GetRelativeTime.getSimpleTime(message.getCreatedAt()));
        }
    }
}
