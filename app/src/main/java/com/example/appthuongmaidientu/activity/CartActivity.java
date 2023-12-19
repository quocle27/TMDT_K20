package com.example.appthuongmaidientu.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appthuongmaidientu.Adapter.CartAdapter;
import com.example.appthuongmaidientu.Adapter.SanPhamAdapter;
import com.example.appthuongmaidientu.R;
import com.example.appthuongmaidientu.control.MemoryData;
import com.example.appthuongmaidientu.model.CartList;
import com.example.appthuongmaidientu.model.SanPham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    CartAdapter cartAdapter;
    List<CartList> cartLists= new ArrayList<>();
    RecyclerView recyclerView;
    public static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    public static TextView dachon,totalMoney;
    public static CheckBox checkBoxTotal;
    public static String mobile;
    Button btn_DatHang;
    String m_Text;
    String diaChi;
    Intent intentMain;
    String daban,soluongban;
    ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView = findViewById(R.id.rvCart);
        btn_DatHang=findViewById(R.id.btn_DatHang);
        cartAdapter = new CartAdapter(cartLists, CartActivity.this);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(CartActivity.this, 1, RecyclerView.VERTICAL, false);
        recyclerView.setAdapter(cartAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        dachon = findViewById(R.id.dachon);
        totalMoney=findViewById(R.id.totalMoney);
        checkBoxTotal=findViewById(R.id.checkboxTotal);
        mobile=getIntent().getStringExtra("mobile");
        dachon.setText("Đã chọn: 0");
        totalMoney.setText("Tổng tiền: 0");

        GetIntent();

        mobile=getIntent().getStringExtra("mobile");
        databaseReference.child("users").child(mobile).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                diaChi = snapshot.child("diaChi").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        btn_DatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (diaChi.isEmpty()){
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setMessage("Hệ thống nhận thấy bạn chưa cập nhật thông tin địa chỉ. Vui lòng cập nhật và thử lại")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(CartActivity.this,EditUserActivity.class);
                                        intent.putExtra("mobile",mobile);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog


                                    }
                                });
                        builder.create();
                        builder.show();

                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                    builder.setTitle("Nhập ghi chú cho shop");

// Set up the input
                    final EditText input = new EditText(CartActivity.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

// Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String currentTimeStamp= String.valueOf(System.currentTimeMillis());
                            m_Text = input.getText().toString();
                            int slb;
                            for(int i=0;i<cartLists.size();i++){
                                if (cartLists.get(i).getCheck().equals("1")){
                                    databaseReference.child("DonDatHang").child(getIntent().getStringExtra("mobile")).child(cartLists.get(i).getMaSP()).setValue(cartLists.get(i));
                                    databaseReference.child("DonDatHang").child(getIntent().getStringExtra("mobile")).child(cartLists.get(i).getMaSP()).child("status").setValue("Chờ xác nhận");

                                    String nd= "Bạn cần giao "+cartLists.get(i).getSoLuongMua()+" sản phẩm: "+cartLists.get(i).getTenSP()+" đến địa chỉ: "+diaChi+" SDT khách hàng: "+getIntent().getStringExtra("mobile") +" và nhận được: "+Long.parseLong(cartLists.get(i).getGia())*Long.parseLong(cartLists.get(i).getSoLuongMua())+" VNĐ. Ghi chú của khách hàng: "+m_Text;
                                    databaseReference.child("ThongBao").child(cartLists.get(i).getUid()).child(currentTimeStamp).child(String.valueOf(i)).child("content").setValue(nd);
                                    databaseReference.child("ThongBao").child(cartLists.get(i).getUid()).child(currentTimeStamp).child(String.valueOf(i)).child("status").setValue("0");
                                    databaseReference.child("ThongBao").child(cartLists.get(i).getUid()).child(currentTimeStamp).child(String.valueOf(i)).child("id").setValue(String.valueOf(i));
                                    databaseReference.child("ThongBao").child(cartLists.get(i).getUid()).child(currentTimeStamp).child(String.valueOf(i)).child("idTB").setValue(currentTimeStamp);
                                    databaseReference.child("ThongBao").child(cartLists.get(i).getUid()).child(currentTimeStamp).child(String.valueOf(i)).child("idKH").setValue(getIntent().getStringExtra("mobile"));
                                    databaseReference.child("ThongBao").child(cartLists.get(i).getUid()).child(currentTimeStamp).child(String.valueOf(i)).child("idSP").setValue(cartLists.get(i).getMaSP());

                                    String maSP=cartLists.get(i).getMaSP();
                                    CartList cartList=cartLists.get(i);
                                    databaseReference.child("SanPham").child(cartLists.get(i).getUid()).child(cartLists.get(i).getMaSP()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            daban = snapshot.child("daBan").getValue(String.class);
                                            soluongban =snapshot.child("soluongban").getValue(String.class);
                                            System.out.println(daban);
                                            databaseReference.child("SanPham").child(cartList.getUid()).child(maSP).child("soluongban").setValue(String.valueOf(Integer.parseInt(soluongban)-Integer.parseInt(cartList.getSoLuongMua())));
                                            databaseReference.child("SanPham").child(cartList.getUid()).child(maSP).child("daBan").setValue(String.valueOf(Integer.parseInt(daban)+Integer.parseInt(cartList.getSoLuongMua())));
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
//
                                }
                            }
                            int i=0;
                            while (i<cartLists.size()){
                                if (cartLists.get(i).getCheck().equals("1")){
                                    Delete(cartLists.get(i));
                                    cartLists.remove(i);
                                    CartActivity.Update(cartLists);
                                    cartAdapter.updateCart(cartLists);
                                }else i++;
                            }
                            startActivity(intentMain);

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            }
        });
        btn_back=findViewById(R.id.btn_backMain);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentMain.putExtra("acti","login");
                startActivity(intentMain);

            }
        });



        checkBoxTotal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            }
        });
        checkBoxTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBoxTotal.isChecked()){
                    System.out.println("truee");
                    for (int i=0;i<cartLists.size();i++){
                        cartLists.get(i).setCheck("1");
                    }
                }else{
                    System.out.println("false");
                    for (int i=0;i<cartLists.size();i++){
                        cartLists.get(i).setCheck("0");
                    }
                    dachon.setText("Đã chọn: 0");
                    totalMoney.setText("Tổng tiền: 0");
                }
                cartAdapter.updateCart(cartLists);
                Update(cartLists);
            }
        });

        databaseReference.child("GioHang").child(getIntent().getStringExtra("mobile")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartLists.clear();
                int i=0;
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String tenSP=dataSnapshot.child("tenSP").getValue(String.class);
                    String gia=dataSnapshot.child("gia").getValue(String.class);
                    String check=dataSnapshot.child("check").getValue(String.class);
                    String name=dataSnapshot.child("name").getValue(String.class);
                    String maSP=dataSnapshot.child("maSP").getValue(String.class);
                    String uid=dataSnapshot.child("uid").getValue(String.class);
                    String soLuongMua=dataSnapshot.child("soLuongMua").getValue(String.class);
                    String hinhanh=dataSnapshot.child("hinhanh").getValue(String.class);
                    CartList cartList = new CartList(maSP,tenSP,soLuongMua,check,gia,uid,hinhanh);
                    cartLists.add(cartList);
                    i++;
                }
                cartAdapter.updateCart(cartLists);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static void Update(List<CartList> cartLists){
        for (int i=0;i<cartLists.size();i++)
            databaseReference.child("GioHang").child(mobile).child(cartLists.get(i).getMaSP()).setValue(cartLists.get(i));
    }
    public static void Delete(CartList cartLists){
            databaseReference.child("GioHang").child(mobile).child(cartLists.getMaSP()).removeValue();
    }
    public void GetIntent(){
        databaseReference.child("users").child(mobile).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(snapshot.child("matKhau").getValue(String.class));
                String email= snapshot.child("email").getValue(String.class);
                String mobile= snapshot.child("sdt").getValue(String.class);
                String name= snapshot.child("tenUser").getValue(String.class);
                String imgUS= snapshot.child("imgUS").getValue(String.class);
                String anhnen= snapshot.child("anhnen").getValue(String.class);
                intentMain = new Intent(CartActivity.this, MainActivity.class);
                intentMain.putExtra("name",name);
                intentMain.putExtra("email",email);
                intentMain.putExtra("imgUS",imgUS);
                intentMain.putExtra("anhnen",anhnen);
                intentMain.putExtra("mobile",mobile);
                intentMain.putExtra("acti","cart");
                intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}