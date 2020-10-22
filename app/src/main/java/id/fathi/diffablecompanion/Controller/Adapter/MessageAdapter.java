package id.fathi.diffablecompanion.Controller.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import id.fathi.diffablecompanion.Model.Chat;
import id.fathi.diffablecompanion.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.RecycleViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    Context mContext;
    List<Chat> mChat;
    FirebaseUser firebaseUser;

    public MessageAdapter(Context context, List<Chat> chat) {
        this.mContext = context;
        this.mChat = chat;
    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MSG_TYPE_RIGHT) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.RecycleViewHolder(v);
        }else {
            View v = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.RecycleViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecycleViewHolder holder, final int position) {

        Chat chat = mChat.get(position);

        holder.show_message.setText(chat.getMessage());
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;

        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = (TextView) itemView.findViewById(R.id.show_message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
