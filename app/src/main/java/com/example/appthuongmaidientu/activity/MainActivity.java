package com.example.appthuongmaidientu.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.appthuongmaidientu.Adapter.CartAdapter;
import com.example.appthuongmaidientu.Adapter.SanPhamAdapter;
import com.example.appthuongmaidientu.Adapter.ViewPageAdapter;
import com.example.appthuongmaidientu.R;
import com.example.appthuongmaidientu.model.CartList;
import com.example.appthuongmaidientu.model.MessengerList;
import com.example.appthuongmaidientu.model.SanPham;
import com.example.appthuongmaidientu.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;
    ViewPageAdapter viewPageAdapter;
    ImageView iconMess, iconCart;
    SearchView searchView;
    RecyclerView recyclerViewSearch;
    CardView cardViewRecy,topCV;
    public static CardView unSeenMain;
    public static TextView textUnSeenMain;
    public static CardView slgCart;
    public static TextView textSlgCart;
    ArrayList<SanPham> sanPhams = new ArrayList<>();
    public static List<CartList> cartLists = new ArrayList<>();
    CartAdapter cartAdapter;
    SanPhamAdapter sanPhamAdapter;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        topCV = findViewById(R.id.topnav);
        unSeenMain = (CardView) findViewById(R.id.unseenMain);
        textUnSeenMain = (TextView) findViewById(R.id.textUnseenMain);
        slgCart = findViewById(R.id.slCartMain);
        textSlgCart = findViewById(R.id.textSLCart);
        viewPager = (ViewPager) findViewById(R.id.viewpg);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navnav);
        iconMess = (ImageView) findViewById(R.id.topnavMess);
        iconCart = (ImageView) findViewById(R.id.topnavCart);
        searchView = (SearchView) findViewById(R.id.topSearchView);
        recyclerViewSearch = (RecyclerView) findViewById(R.id.recycleViewMainSearch);
        cardViewRecy = (CardView) findViewById(R.id.cardrecycler);
        sanPhamAdapter = new SanPhamAdapter(sanPhams, MainActivity.this);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(MainActivity.this, 2, RecyclerView.VERTICAL, false);
        recyclerViewSearch.setAdapter(sanPhamAdapter);
        recyclerViewSearch.setLayoutManager(linearLayoutManager);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                callQuery(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                callQuery(s);
                System.out.println(s);
                if (s.isEmpty())
                    cardViewRecy.setVisibility(View.GONE);
                else cardViewRecy.setVisibility(View.VISIBLE);
                return false;
            }
        });

        iconMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MessengerActivity.class);
                intent.putExtra("mobile", getIntent().getStringExtra("mobile"));
                startActivity(intent);
            }
        });
        iconCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CartActivity.class);
                intent.putExtra("mobile",getIntent().getStringExtra("mobile"));
                startActivity(intent);
            }
        });

        databaseReference.child("GioHang").child(getIntent().getStringExtra("mobile")).addValueEventListener(new ValueEventListener() {
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
                    String hinhanh=dataSnapshot.child("soLuongMua").getValue(String.class);
                    CartList cartList = new CartList(maSP,tenSP,soLuongMua,check,gia,uid,hinhanh);
                    cartLists.add(cartList);
                    i++;
                }
                if (i > 0) {
                    slgCart.setVisibility(View.VISIBLE);
                    textSlgCart.setText(String.valueOf(i));
                } else {
                    slgCart.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navhome:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.navorder:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.navfavourite:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.navThongBao:
                        viewPager.setCurrentItem(3);
                        break;
                    case R.id.navprofile:
                        viewPager.setCurrentItem(4);
                        break;
                    default:
                        viewPager.setCurrentItem(0);
                        break;
                }
                Window window = MainActivity.this.getWindow();
                window.setStatusBarColor(MainActivity.this.getResources().getColor(R.color.pri100));
                topCV.setCardBackgroundColor(MainActivity.this.getResources().getColor(R.color.pri100));
                return true;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.navhome).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.navorder).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.navfavourite).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.navThongBao).setChecked(true);
                        break;
                    case 4:
                        bottomNavigationView.getMenu().findItem(R.id.navprofile).setChecked(true);
                        break;
                }
                Window window = MainActivity.this.getWindow();
                window.setStatusBarColor(MainActivity.this.getResources().getColor(R.color.pri100));
                topCV.setCardBackgroundColor(MainActivity.this.getResources().getColor(R.color.pri100));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPageAdapter);
        if (getIntent().getStringExtra("acti").equals("cart")){
            viewPager.setCurrentItem(1);
            bottomNavigationView.setSelectedItemId(R.id.navorder);
        }
    }

    private void callQuery(String s) {
        databaseReference.child("SanPham").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sanPhams.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String name=dataSnapshot1.child("ten").getValue(String.class).toUpperCase();
                        String name1=s.toUpperCase();
                        if (name.contains(name1)){
                            System.out.println(dataSnapshot1.getRef() + " acd");
                            String ten = dataSnapshot1.child("ten").getValue(String.class);
                            SanPham sanPham = new SanPham(ten);
                            sanPham.setImg(dataSnapshot1.child("img").getValue(String.class));
                            sanPham.setMaSP(dataSnapshot1.getKey());
                            sanPham.setUID(dataSnapshot.getKey());
                            sanPham.setMota(dataSnapshot1.child("mota").getValue(String.class));
                            sanPham.setGia(dataSnapshot1.child("gia").getValue(String.class));
                            sanPham.setDaBan(dataSnapshot1.child("daBan").getValue(String.class));
                            sanPhams.add(sanPham);
                        }
                    }
                    sanPhamAdapter.updateSanPham(sanPhams);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recyclerViewSearch.setAdapter(sanPhamAdapter);
    }

    public static void updateUnSeen(List<MessengerList> messengerLists) {
        int i = 0;
        for (MessengerList messengerList : messengerLists) {
            if (messengerList.getUnseenMessenger() > 0) {
                i++;
            }
        }
        if (i > 0) {
            unSeenMain.setVisibility(View.VISIBLE);
            textUnSeenMain.setText(String.valueOf(i));
        } else {
            unSeenMain.setVisibility(View.GONE);
        }
    }
}