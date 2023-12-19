package com.example.appthuongmaidientu.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ThemSanPhamActivity extends AppCompatActivity {
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference= firebaseStorage.getReference();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    ImageView imageView;
    TextView tenSP,moTa,gia,soLuong;
    Spinner loaiSP;
    ProgressDialog progressDialog;
    String mobile;
    ArrayAdapter<String> adapter;
    List<String> dmmmm=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_san_pham);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        AnhXa();
        Button pick = (Button) findViewById(R.id.upLoad);
        Button save=(Button) findViewById(R.id.saveQLSP);
        imageView = (ImageView) findViewById(R.id.qlspIMG);
        mobile=getIntent().getStringExtra("mobile");

        databaseReference.child("DanhMucSanPham").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dmmmm.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String asd=dataSnapshot.child("id").getValue(String.class)+" - "+dataSnapshot.child("ten").getValue(String.class);
                    if (Integer.parseInt(asd.split(" ")[0])>0){
                        dmmmm.add(asd);
                    }
                    System.out.println(asd.split(" ")[0]);
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
        loaiSP.setAdapter(adapter);
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectIMG();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check()) {
                    progressDialog.show();
                    Calendar calendar = Calendar.getInstance();
                    storageReference.child("image" + calendar.getTimeInMillis() + ".png");
                    imageView.setDrawingCacheEnabled(true);
                    imageView.buildDrawingCache();
                    Bitmap bitmap = imageView.getDrawingCache();
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
                                                imageUrl = "https://firebasestorage.googleapis.com/v0/b/demotmdt-26982.appspot.com/o/error-image-generic.png?alt=media&token=dbfe9456-ba97-458f-8abf-dfd6e644dd25";
                                            }
                                            SanPham sanPham = new SanPham(tenSP.getText().toString(), soLuong.getText().toString(), gia.getText().toString(), moTa.getText().toString(), imageUrl, loaiSP.getSelectedItem().toString().split(" ")[0]);
                                            final String currentTimeStamp = String.valueOf(System.currentTimeMillis());
                                            sanPham.setMaSP(currentTimeStamp);
                                            sanPham.setDaBan("0");
                                            databaseReference.child("SanPham").child(mobile).child(currentTimeStamp).setValue(sanPham);
//                                            Intent intent = new Intent(ThemSanPhamActivity.this, QuanLySanPhamActivity.class);
//                                            intent.putExtra("email",getIntent().getStringExtra("email"));
//                                            intent.putExtra("mobile",getIntent().getStringExtra("mobile"));
//                                            intent.putExtra("name",getIntent().getStringExtra("name"));
//                                            startActivity(intent);
                                            Toast.makeText(ThemSanPhamActivity.this, "Đã thêm.", Toast.LENGTH_SHORT).show();
                                            tenSP.setText("");
                                            soLuong.setText("");
                                            gia.setText("");
                                            moTa.setText("");
                                            loaiSP.setSelection(0);
                                            imageView.setImageDrawable(null);

                                            progressDialog.dismiss();
                                        }
                                    });
                                }
                            }
                        }
                    });
                }else
                    Toast.makeText(ThemSanPhamActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AnhXa() {
        tenSP =(TextView) findViewById(R.id.addSP_Ten);
        gia =(TextView) findViewById(R.id.addSP_Gia);
        loaiSP =(Spinner) findViewById(R.id.addSP_DanhMuc);
        moTa =(TextView) findViewById(R.id.addSP_Mota);
        soLuong =(TextView) findViewById(R.id.addSP_SoLuong);
    }
    private boolean check(){
        if (tenSP.getText().toString().isEmpty() || gia.getText().toString().isEmpty()||moTa.getText().toString().isEmpty()||soLuong.getText().toString().isEmpty()){
            return false;
        }else return true;
    }

    private void selectIMG() {
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
                bitmap = MediaStore.Images.Media.getBitmap(ThemSanPhamActivity.this.getContentResolver(), uriimgt);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(bitmap);

        }
    }
}