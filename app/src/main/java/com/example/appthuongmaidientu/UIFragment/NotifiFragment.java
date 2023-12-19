package com.example.appthuongmaidientu.UIFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appthuongmaidientu.Adapter.NotifyAdapter;
import com.example.appthuongmaidientu.Adapter.SanPhamAdapter;
import com.example.appthuongmaidientu.R;
import com.example.appthuongmaidientu.model.NotifyList;
import com.google.common.base.StandardSystemProperty;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotifiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotifiFragment extends Fragment {

    RecyclerView recyclerView;
    List<NotifyList> notifyLists=new ArrayList<>();
    ArrayAdapter<NotifyList> arrayAdapter;
    NotifyAdapter notifyAdapter;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotifiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotifiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotifiFragment newInstance(String param1, String param2) {
        NotifiFragment fragment = new NotifiFragment();
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

        recyclerView = view.findViewById(R.id.lvNotify);
        notifyAdapter=new NotifyAdapter(notifyLists,getContext());
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getContext(),1, RecyclerView.VERTICAL,false);
        recyclerView.setAdapter(notifyAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        databaseReference.child("ThongBao").child(getActivity().getIntent().getStringExtra("mobile")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notifyLists.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        if (dataSnapshot1.child("content").getValue(String.class)!=null)
                            notifyLists.add(new NotifyList(dataSnapshot1.child("id").getValue(String.class),dataSnapshot1.child("status").getValue(String.class),dataSnapshot1.child("content").getValue(String.class),dataSnapshot1.child("idKH").getValue(String.class),dataSnapshot1.child("idTB").getValue(String.class),dataSnapshot1.child("idSP").getValue(String.class)));
                    }
                }
                notifyAdapter.update(notifyLists);
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
        return inflater.inflate(R.layout.fragment_notifi, container, false);
    }
}