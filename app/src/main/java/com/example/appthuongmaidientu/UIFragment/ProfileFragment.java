package com.example.appthuongmaidientu.UIFragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.appthuongmaidientu.R;

import com.example.appthuongmaidientu.activity.EditUserActivity;
import com.example.appthuongmaidientu.activity.LoginActivity;
import com.example.appthuongmaidientu.activity.QuanLySanPhamActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    TextView prThongTin,prQLSP,prLSDH,nameProfile,Logout;
    ImageView imgProfile,imgBackground;
    CardView topCV;
    SearchView topSV;
    ScrollView scrollView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AnhXa(view);
        Picasso.get().load(getActivity().getIntent().getStringExtra("imgUS")).into(imgProfile);
        nameProfile.setText(getActivity().getIntent().getStringExtra("name"));
//        Picasso.get().load(getActivity().getIntent().getStringExtra("anhnen")).into(imgBackground);
        prThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditUserActivity.class);
                intent.putExtra("mobile",getActivity().getIntent().getStringExtra("mobile"));
                startActivity(intent);

            }
        });
        prQLSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), QuanLySanPhamActivity.class);
                intent.putExtra("email",getActivity().getIntent().getStringExtra("email"));
                intent.putExtra("mobile",getActivity().getIntent().getStringExtra("mobile"));
                intent.putExtra("name",getActivity().getIntent().getStringExtra("name"));
                startActivity(intent);
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    int color = R.color.white10;
                    if (i1<20)
                        color=R.color.pri10;
                    else if(i1<40)
                        color=R.color.pri20;
                    else if(i1<60)
                        color=R.color.pri30;
                    else if(i1<80)
                        color=R.color.pri40;
                    else if (i1<100)
                        color=R.color.pri50;
                    else if (i1<120)
                        color=R.color.pri60;
                    else if (i1<140)
                        color=R.color.pri70;
                    else if(i1<160)
                        color=R.color.pri80;
                    else if (i1<180)
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

    private void AnhXa(View view) {
        prThongTin = (TextView)  view.findViewById(R.id.prThongtin);
        prQLSP=(TextView) view.findViewById(R.id.prQLSP);
        prLSDH=(TextView) view.findViewById(R.id.prLSDH);
        nameProfile = (TextView) view.findViewById(R.id.profile_fullname);
        imgProfile=(ImageView) view.findViewById(R.id.imgProfile);
        Logout =(TextView) view.findViewById(R.id.logOut);
        topCV = getActivity().findViewById(R.id.topnav);
        topSV = getActivity().findViewById(R.id.topSearchView);
        scrollView = view.findViewById(R.id.scrollViewProfile);
//         imgBackground=view.findViewById(R.id.imgBackgroundFrProfile);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}