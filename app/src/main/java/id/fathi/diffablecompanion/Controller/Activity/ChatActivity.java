package id.fathi.diffablecompanion.Controller.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.fathi.diffablecompanion.Model.Chat;
import id.fathi.diffablecompanion.R;
import id.fathi.diffablecompanion.Controller.Adapter.MessageAdapter;

public class ChatActivity extends AppCompatActivity {
    Button btnkirim;
    EditText input;
    DatabaseReference databaseReferenceChat, databaseReferenceDifabel, databaseReferencePendamping;
    RecyclerView recyclerView;
    MessageAdapter messageAdapter;
    List<Chat> mChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final String uidreceiver = getIntent().getStringExtra("uid");

        btnkirim = (Button) findViewById(R.id.kirim);
        input = (EditText) findViewById(R.id.textchat);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        databaseReferenceChat = FirebaseDatabase.getInstance().getReference("Chat");
        databaseReferenceDifabel = FirebaseDatabase.getInstance().getReference("Difabel");
        databaseReferencePendamping = FirebaseDatabase.getInstance().getReference("Pendamping");

        btnkirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(), uidreceiver, input.getText().toString().trim());
                                input.setText("");
            }
        });

        readMessages(FirebaseAuth.getInstance().getCurrentUser().getUid(), uidreceiver);

    }

    private void sendMessage(String sender, String receiver, String message){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        databaseReferenceChat.push().setValue(hashMap);
    }

    private void readMessages(final String myId, final String userId){
        mChat = new ArrayList<>();

        databaseReferenceChat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myId) && chat.getSender().equals(userId) ||
                    chat.getReceiver().equals(userId) && chat.getSender().equals(myId)){
                        mChat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(ChatActivity.this, mChat);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
