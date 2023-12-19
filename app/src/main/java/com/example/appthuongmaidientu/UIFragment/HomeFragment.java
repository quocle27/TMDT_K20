package com.example.appthuongmaidientu.UIFragment;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ScrollView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.appthuongmaidientu.Adapter.DMSPAdapter;
import com.example.appthuongmaidientu.Adapter.DanhMucAdapter;
import com.example.appthuongmaidientu.Adapter.PhotoAdapter;
import com.example.appthuongmaidientu.Adapter.SanPhamAdapter;
import com.example.appthuongmaidientu.R;
import com.example.appthuongmaidientu.model.DMSP;
import com.example.appthuongmaidientu.model.DanhMuc;
import com.example.appthuongmaidientu.model.Photo;
import com.example.appthuongmaidientu.model.SanPham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    ViewPager viewPager;
    CircleIndicator circleIndicator,circleIndicator1;
    PhotoAdapter photoAdapter;
    List<Photo> mListPhoto;
    DMSPAdapter dmspAdapter;
    Timer mTimer;
    RecyclerView recyclerView,recyclerViewSP, recyclerViewDMSP;
    DanhMucAdapter danhMucAdapter;
    ArrayList<DanhMuc> danhMucs;
    public static ArrayList<SanPham> sanPhams;
    ArrayList<DMSP> dmsps;
    public static SanPhamAdapter sanPhamAdapter;
    GestureDetector gestureDetector;
    ScrollView scrollView;
    CardView topCV;
    SearchView topSV;
    public static ProgressDialog progressDialog;
    public static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getActivity().getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        }
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        viewPager = view.findViewById(R.id.ViewPager    );
        circleIndicator = view.findViewById(R.id.circleindicator);
        mListPhoto = getListPhoto();
        photoAdapter = new PhotoAdapter(getContext(),mListPhoto);
        viewPager.setAdapter(photoAdapter);
        circleIndicator.setViewPager(viewPager);
        recyclerView = view.findViewById(R.id.list_danhmuc);
        recyclerViewSP=view.findViewById(R.id.list_sanpham);
        recyclerViewDMSP=view.findViewById(R.id.list_danhmucsanpham);
        scrollView = view.findViewById(R.id.scrollHomeFg);
        topCV = getActivity().findViewById(R.id.topnav);
        topSV = getActivity().findViewById(R.id.topSearchView);
        recyclerView.setHorizontalScrollBarEnabled(true);


        danhMucs = new ArrayList<DanhMuc>();
        databaseReference.child("DanhMuc").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                danhMucs.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    danhMucs.add(new DanhMuc(dataSnapshot.child("tenDM").getValue(String.class),dataSnapshot.child("hinhanhDM").getValue(String.class),dataSnapshot.child("idDM").getValue(String.class)));
                }
                danhMucAdapter.updateDanhMuc(danhMucs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dmsps = new ArrayList<DMSP>();
        databaseReference.child("DanhMucSanPham").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dmsps.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    dmsps.add(new DMSP(dataSnapshot.child("ten").getValue(String.class),dataSnapshot.child("hinhanh").getValue(String.class),dataSnapshot.child("id").getValue(String.class)));
                }
                dmspAdapter.updateDanhMuc(dmsps);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        sanPhams=new ArrayList<>();
        databaseReference.child("SanPham").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sanPhams.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        String ten= dataSnapshot1.child("ten").getValue(String.class);
                        SanPham sanPham = new SanPham(ten);
                        sanPham.setImg(dataSnapshot1.child("img").getValue(String.class));
                        sanPham.setMaSP(dataSnapshot1.getKey());
                        sanPham.setUID(dataSnapshot.getKey());
                        sanPham.setMota(dataSnapshot1.child("mota").getValue(String.class));
                        sanPham.setGia(dataSnapshot1.child("gia").getValue(String.class));
                        sanPham.setDaBan(dataSnapshot1.child("daBan").getValue(String.class));
                        sanPham.setSoluongban(dataSnapshot1.child("soluongban").getValue(String.class));
                        sanPhams.add(sanPham);
                    }
                    sanPhamAdapter.updateSanPham(sanPhams);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        EventScroll();
        danhMucAdapter = new DanhMucAdapter(danhMucs, getContext());
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getContext(),1,RecyclerView.HORIZONTAL,false);
        recyclerView.setAdapter(danhMucAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        sanPhamAdapter=new SanPhamAdapter(sanPhams,getContext());
        linearLayoutManager = new GridLayoutManager(getContext(),2,RecyclerView.VERTICAL,false);
        recyclerViewSP.setAdapter(sanPhamAdapter);
        recyclerViewSP.setLayoutManager(linearLayoutManager);


        dmspAdapter = new DMSPAdapter(dmsps, getContext());
        linearLayoutManager = new GridLayoutManager(getContext(),2,RecyclerView.HORIZONTAL,false);
        recyclerViewDMSP.setAdapter(dmspAdapter);
        recyclerViewDMSP.setLayoutManager(linearLayoutManager);



        autoSildeImages();
        super.onViewCreated(view, savedInstanceState);
    }

    private void EventScroll() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    int color = R.color.white10;
                    if (i1<20)
                        color=R.color.pri00;
                    else if(i1<40)
                        color=R.color.pri10;
                    else if(i1<60)
                        color=R.color.pri20;
                    else if(i1<80)
                        color=R.color.pri30;
                    else if (i1<100)
                        color=R.color.pri40;
                    else if (i1<120)
                        color=R.color.pri50;
                    else if (i1<140)
                        color=R.color.pri60;
                    else if(i1<160)
                        color=R.color.pri70;
                    else if (i1<180)
                        color=R.color.pri80;
                    else if (i1<200)
                        color=R.color.pri90;
                    else
                        color=R.color.pri100;
                    Window window = getActivity().getWindow();
                    window.setStatusBarColor(getActivity().getResources().getColor(color));
                    topCV.setCardBackgroundColor(getActivity().getResources().getColor(color));
                    //topSV.setBackgroundColor(getActivity().getResources().getColor(color));
                }
            });
        }
    }


    private List<Photo> getListPhoto() {
        List<Photo> photoList = new ArrayList<Photo>();
        photoList.add(new Photo(R.drawable.banner));
        photoList.add(new Photo(R.drawable.banner1));
        photoList.add(new Photo(R.drawable.banner2));
        photoList.add(new Photo(R.drawable.banner3));
        return photoList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    public void autoSildeImages(){
        if (mListPhoto ==null || mListPhoto.isEmpty() || viewPager == null){
            return;
        }
        if (mTimer==null){
            mTimer= new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager.getCurrentItem();
                        int totalItem = mListPhoto.size() - 1;
                        if (currentItem < totalItem){
                            currentItem++;
                            viewPager.setCurrentItem(currentItem);
                        }else viewPager.setCurrentItem(0);
                    }
                });
            }
        },500,3000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer!=null) {
            mTimer.cancel();
            mTimer = null;
        }
    }
    public static void update(int pos){
        progressDialog.show();
        sanPhams=new ArrayList<>();
        databaseReference.child("SanPham").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sanPhams.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        if(Integer.parseInt(dataSnapshot1.child("loai").getValue(String.class))==pos){
                                String ten= dataSnapshot1.child("ten").getValue(String.class);
                                SanPham sanPham = new SanPham(ten);
                                sanPham.setImg(dataSnapshot1.child("img").getValue(String.class));
                                sanPham.setMaSP(dataSnapshot1.getKey());
                                sanPham.setUID(dataSnapshot.getKey());
                                sanPham.setMota(dataSnapshot1.child("mota").getValue(String.class));
                                sanPham.setGia(dataSnapshot1.child("gia").getValue(String.class));
                                sanPham.setDaBan(dataSnapshot1.child("daBan").getValue(String.class));
                                sanPham.setSoluongban(dataSnapshot1.child("soluongban").getValue(String.class));
                                sanPhams.add(sanPham);
                            }
                            sanPhamAdapter.updateSanPham(sanPhams);
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();

            }
        });
    }
    public static void update(){
        progressDialog.show();
        sanPhams=new ArrayList<>();
        databaseReference.child("SanPham").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sanPhams.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            String ten= dataSnapshot1.child("ten").getValue(String.class);
                            SanPham sanPham = new SanPham(ten);
                            sanPham.setImg(dataSnapshot1.child("img").getValue(String.class));
                            sanPham.setMaSP(dataSnapshot1.getKey());
                            sanPham.setUID(dataSnapshot.getKey());
                            sanPham.setMota(dataSnapshot1.child("mota").getValue(String.class));
                            sanPham.setGia(dataSnapshot1.child("gia").getValue(String.class));
                            sanPham.setDaBan(dataSnapshot1.child("daBan").getValue(String.class));
                            sanPham.setSoluongban(dataSnapshot1.child("soluongban").getValue(String.class));
                            sanPhams.add(sanPham);
                        }
                        sanPhamAdapter.updateSanPham(sanPhams);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
    }
}