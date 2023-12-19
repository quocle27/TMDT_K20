package com.example.appthuongmaidientu.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.appthuongmaidientu.Adapter.SanPhamAdapter;
import com.example.appthuongmaidientu.R;
import com.example.appthuongmaidientu.model.SanPham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class QuanLySanPhamActivity extends AppCompatActivity {

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference= firebaseStorage.getReference();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    ImageView imageView;
    CardView cardView;

    RecyclerView recyclerView;

    ArrayList<SanPham> sanPhams;
    SanPhamAdapter sanPhamAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_san_pham);
        cardView = findViewById(R.id.qlsp_add);
        recyclerView =findViewById(R.id.list_sanphamQLSP);
        sanPhams=new ArrayList<>();

        sanPhamAdapter=new SanPhamAdapter(sanPhams,this );
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this,2,RecyclerView.VERTICAL,false);
        recyclerView.setAdapter(sanPhamAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        databaseReference.child("SanPham").child(getIntent().getStringExtra("mobile")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sanPhams.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String ten= dataSnapshot.child("ten").getValue(String.class);
                    SanPham sanPham = new SanPham(ten);
                    sanPham.setImg(dataSnapshot.child("img").getValue(String.class));
                    sanPham.setMaSP(dataSnapshot.getKey());
                    sanPham.setUID(getIntent().getStringExtra("mobile"));
                    sanPham.setMota(dataSnapshot.child("mota").getValue(String.class));
                    sanPham.setGia(dataSnapshot.child("gia").getValue(String.class));
                    sanPham.setDaBan(dataSnapshot.child("daBan").getValue(String.class));
                    sanPhams.add(sanPham);
                    sanPhamAdapter.updateSanPham(sanPhams);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuanLySanPhamActivity.this, ThemSanPhamActivity.class);
                intent.putExtra("email",getIntent().getStringExtra("email"));
                intent.putExtra("mobile",getIntent().getStringExtra("mobile"));
                intent.putExtra("name",getIntent().getStringExtra("name"));
                startActivity(intent);
            }
        });

    }
}