package com.example.appthuongmaidientu.UIFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appthuongmaidientu.Adapter.CartAdapter;
import com.example.appthuongmaidientu.Adapter.DonDatHangAdapter;
import com.example.appthuongmaidientu.Adapter.SanPhamAdapter;
import com.example.appthuongmaidientu.R;
import com.example.appthuongmaidientu.activity.CartActivity;
import com.example.appthuongmaidientu.model.CartList;
import com.example.appthuongmaidientu.model.SanPham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {

    DonDatHangAdapter donDatHangAdapter;
    ArrayList<CartList> cartLists=new ArrayList<>();
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    RecyclerView recyclerView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
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
        super.onViewCreated(view, savedInstanceState);
        cartLists = new ArrayList<>();
        recyclerView=view.findViewById(R.id.recyclerDDH);
        donDatHangAdapter = new DonDatHangAdapter(cartLists,getContext());
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false);
        recyclerView.setAdapter(donDatHangAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        databaseReference.child("DonDatHang").child(getActivity().getIntent().getStringExtra("mobile")).addValueEventListener(new ValueEventListener() {
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
                    String status=dataSnapshot.child("status").getValue(String.class);
                    CartList cartList = new CartList(maSP,tenSP,soLuongMua,check,gia,uid,hinhanh);
                    cartList.setStatus(status);
                    cartLists.add(cartList);
                    i++;
                }
                donDatHangAdapter.updateDDH(cartLists);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
    }
}