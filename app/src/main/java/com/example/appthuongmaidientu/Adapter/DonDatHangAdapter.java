package com.example.appthuongmaidientu.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appthuongmaidientu.R;
import com.example.appthuongmaidientu.model.CartList;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class DonDatHangAdapter extends RecyclerView.Adapter<DonDatHangAdapter.ViewHolder> {
    List<CartList> cartLists;
    Context context;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public DonDatHangAdapter(List<CartList> cartLists, Context context) {
        this.cartLists = cartLists;
        this.context = context;
    }
    @NonNull
    @Override
    public DonDatHangAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ddh_item_layout,null));
    }

    @Override
    public void onBindViewHolder(@NonNull DonDatHangAdapter.ViewHolder holder, int position) {
        CartList cartList = cartLists.get(position);
        holder.name.setText(cartList.getTenSP());
        holder.price.setText(String.valueOf(Integer.parseInt(cartList.getGia())*Integer.parseInt(cartList.getSoLuongMua())));
        holder.solg.setText(cartList.getSoLuongMua());
        holder.status.setText(cartList.getStatus());
        Picasso.get().load(cartList.getHinhanh()).into(holder.img);

    }
    public void updateDDH(List<CartList> cartLists){
        this.cartLists=cartLists;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return cartLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,price,solg,status;
        ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tenspDH);
            price=itemView.findViewById(R.id.totalMoneyDH);
            solg=itemView.findViewById(R.id.edt_slgdh);
            status=itemView.findViewById(R.id.statusDH);
            img=itemView.findViewById(R.id.imgSPDH);
        }
    }
}
