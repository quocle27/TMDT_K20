package com.example.appthuongmaidientu.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appthuongmaidientu.Adapter.ChatAdapter;
import com.example.appthuongmaidientu.R;
import com.example.appthuongmaidientu.control.MemoryData;
import com.example.appthuongmaidientu.model.ChatList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    ImageView btnBackChat,btnSendChat;
    CircleImageView profilePicChat;
    TextView nameChat;
    EditText edtChat;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    String chatKey;
    String getUserMobile;
    RecyclerView chattingRecyclerView;
    ChatAdapter chatAdapter;
    public static boolean loadingFirstTime= true;
    private final List<ChatList> chatLists = new ArrayList<>();
    ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        AnhXa();

        chattingRecyclerView.setHasFixedSize(true);
        chattingRecyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));

        chatAdapter = new ChatAdapter(chatLists,ChatActivity.this);
        chattingRecyclerView.setAdapter(chatAdapter);
        final String getName = getIntent().getStringExtra("name");
        final String getProfilePic = getIntent().getStringExtra("profilePic");
        chatKey = getIntent().getStringExtra("chatKey");
        final String getMobile= getIntent().getStringExtra("mobile");
        btn_back = findViewById(R.id.back_buttonChat);



        nameChat.setText(getName);
        getUserMobile= MemoryData.getData(ChatActivity.this);
        if (!getProfilePic.isEmpty()){
            Picasso.get().load(getProfilePic).into(profilePicChat);
        }
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ChatActivity.this,MessengerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("mobile",getUserMobile);
                startActivity(intent);
            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (chatKey.isEmpty()) {
                    chatKey = "1";
                    if (snapshot.hasChild("chat")) {
                        chatKey = String.valueOf(snapshot.child("chat").getChildrenCount() + 1);
                    }
                }

                if (snapshot.hasChild("chat")){
                    if (snapshot.child("chat").child(chatKey).hasChild("messenger")) {
                        chatLists.clear();

                        for(DataSnapshot chatSnapshot: snapshot.child("chat").child(chatKey).child("messenger").getChildren()){
                            if(chatSnapshot.hasChild("msg")&& chatSnapshot.hasChild("mobile")) {
                                final String messengerTimeStamps = chatSnapshot.getKey();
                                final String getMobile =chatSnapshot.child("mobile").getValue(String.class);
                                final String getMsg = chatSnapshot.child("msg").getValue(String.class);

                                Timestamp timestamp = new Timestamp(Long.parseLong(messengerTimeStamps));
                                System.out.println(timestamp);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm aa");
                                System.out.println(simpleDateFormat+"abc"+simpleTimeFormat);
                                ChatList chatList = new ChatList(getMobile,getName,getMsg,simpleDateFormat.format(timestamp),simpleTimeFormat.format(timestamp));
                                chatLists.add(chatList);

                                if (loadingFirstTime || Long.parseLong(messengerTimeStamps)> Long.parseLong(MemoryData.getLastMsgTs(ChatActivity.this,chatKey))){
                                    loadingFirstTime=false;
                                    // MemoryData.saveLastMsgTS(messengerTimeStamps,chatKey,Chat.this);
                                    chatAdapter.updateChat(chatLists);
                                };
                                chattingRecyclerView.scrollToPosition(chatLists.size()-1);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnSendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String getTxtMessenger = edtChat.getText().toString();
                final String currentTimeStamp= String.valueOf(System.currentTimeMillis());
                databaseReference.child("chat").child(chatKey).child("user_1").setValue(getUserMobile);
                databaseReference.child("chat").child(chatKey).child("user_2").setValue(getMobile);
                databaseReference.child("chat").child(chatKey).child("messenger").child(currentTimeStamp).child("msg").setValue(getTxtMessenger);
                databaseReference.child("chat").child(chatKey).child("messenger").child(currentTimeStamp).child("mobile").setValue(getUserMobile);
                MemoryData.saveLastMsgTS(currentTimeStamp,chatKey,ChatActivity.this);
                chatAdapter.updateChat(chatLists);
                edtChat.setText("");
            }
        });
    }

    private void AnhXa() {
        btnBackChat = (ImageView) findViewById(R.id.back_buttonChat);
        btnSendChat =(ImageView) findViewById(R.id.sendBtnChat);
        profilePicChat = (CircleImageView) findViewById(R.id.idProfilePicChat);
        nameChat = (TextView) findViewById(R.id.nameChat);
        edtChat = (EditText) findViewById(R.id.edtChat);
        chattingRecyclerView = (RecyclerView) findViewById(R.id.chatRecyclerView);
    }

}