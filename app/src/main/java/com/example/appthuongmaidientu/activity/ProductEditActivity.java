package com.example.appthuongmaidientu.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appthuongmaidientu.R;
import com.example.appthuongmaidientu.model.SanPham;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProductEditActivity extends AppCompatActivity {

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference= firebaseStorage.getReference();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    ArrayAdapter<String> adapter;
    List<String> dmmmm=new ArrayList<>();
    String maSP;
    private Spinner spnCategory;
    TextView name,gia,slc,mota;
    ImageView imgedt;
    Button btnSave,btnPick;
    ProgressDialog progressDialog;
    String mobile;
    boolean ktraa=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);
        AnhXa();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");


        maSP = getIntent().getStringExtra("maSP");

        databaseReference.child("DanhMucSanPham").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dmmmm.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String asd=dataSnapshot.child("id").getValue(String.class)+" - "+dataSnapshot.child("ten").getValue(String.class);
                    if (Integer.parseInt(dataSnapshot.child("id").getValue(String.class))>0)
                        dmmmm.add(asd);
                    System.out.println(asd);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter  = new ArrayAdapter(this, android.R.layout.simple_spinner_item,dmmmm);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnCategory.setAdapter(adapter);
        
        btnPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickIMG();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check()) {
                    if (ktraa){
                        progressDialog.show();
                    Calendar calendar = Calendar.getInstance();
                    storageReference.child("image" + calendar.getTimeInMillis() + ".png");
                    imgedt.setDrawingCacheEnabled(true);
                    imgedt.buildDrawingCache();
                    Bitmap bitmap = imgedt.getDrawingCache();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] data = baos.toByteArray();
                    UploadTask uploadTask = storageReference.child("image" + calendar.getTimeInMillis() + ".png").putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (taskSnapshot.getMetadata() != null) {
                                if (taskSnapshot.getMetadata().getReference() != null) {
                                    Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String imageUrl = uri.toString();
                                            if (imageUrl.isEmpty()) {
                                                imageUrl = getIntent().getStringExtra("imgsp");
                                            }
                                            SanPham sanPham = new SanPham(name.getText().toString(), slc.getText().toString(), gia.getText().toString(), mota.getText().toString(), imageUrl, spnCategory.getSelectedItem().toString().split("")[0]);
                                            final String currentTimeStamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);
                                            sanPham.setMaSP(getIntent().getStringExtra("maSP"));
                                            sanPham.setDaBan(getIntent().getStringExtra("daBan"));
                                            System.out.println(getIntent().getStringExtra("mobile"));
                                            databaseReference.child("SanPham").child(getIntent().getStringExtra("mobile")).child(getIntent().getStringExtra("maSP")).setValue(sanPham);
                                            Toast.makeText(ProductEditActivity.this, "Đã cập nhật.", Toast.LENGTH_SHORT).show();
                                            name.setText("");
                                            slc.setText("");
                                            gia.setText("");
                                            mota.setText("");
                                            spnCategory.setSelection(0);
                                            imgedt.setImageDrawable(null);
                                            progressDialog.dismiss();
                                        }
                                    });
                                }
                            }
                        }
                    });
                    ktraa=false;
                }else {
                    String imageUrl = getIntent().getStringExtra("imgSP");
                        System.out.println(imageUrl);
                    SanPham sanPham = new SanPham(name.getText().toString(), slc.getText().toString(), gia.getText().toString(), mota.getText().toString(), imageUrl, spnCategory.getSelectedItem().toString().split(" ")[0]);
                    final String currentTimeStamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);
                    sanPham.setMaSP(getIntent().getStringExtra("maSP"));
                    sanPham.setDaBan(getIntent().getStringExtra("daBan"));
                    sanPham.setImg(imageUrl);
                    databaseReference.child("SanPham").child(getIntent().getStringExtra("mobile")).child(getIntent().getStringExtra("maSP")).setValue(sanPham);
                    Toast.makeText(ProductEditActivity.this, "Đã cập nhật.", Toast.LENGTH_SHORT).show();
                    name.setText("");
                    slc.setText("");
                    gia.setText("");
                    mota.setText("");
                    spnCategory.setSelection(0);
                    imgedt.setImageDrawable(null);
                    progressDialog.dismiss();
                    ktraa=false;
                    }
                }else
                    Toast.makeText(ProductEditActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void PickIMG() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==100 && data !=null && data.getData()!=null){
            Uri uriimgt=data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(ProductEditActivity.this.getContentResolver(), uriimgt);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imgedt.setImageBitmap(bitmap);
            ktraa=true;

        }
    }

    private void AnhXa() {
        spnCategory = findViewById(R.id.edt_danhmucedtSP);
        name=findViewById(R.id.edt_tenedtSP);
        gia=findViewById(R.id.edt_giaedtSP);
        slc=findViewById(R.id.edt_slcedtSP);
        mota= findViewById(R.id.edt_motaedtSP);
        imgedt= findViewById(R.id.edt_imgedtSP);
        btnSave=findViewById(R.id.btn_SaveedtSP);
        btnPick=findViewById(R.id.btn_pickedtSP);

        name.setText(getIntent().getStringExtra("nameSP"));
        gia.setText(getIntent().getStringExtra("giaSP"));
        slc.setText(getIntent().getStringExtra("slcSP"));
        mota.setText(getIntent().getStringExtra("motaSP"));


    }
    private boolean check(){
        if (name.getText().toString().isEmpty() || gia.getText().toString().isEmpty()||mota.getText().toString().isEmpty()||slc.getText().toString().isEmpty()){
            return false;
        }else return true;
    }
}