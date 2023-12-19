package com.example.appthuongmaidientu.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appthuongmaidientu.R;
import com.example.appthuongmaidientu.activity.CartActivity;
import com.example.appthuongmaidientu.control.MemoryData;
import com.example.appthuongmaidientu.model.CartList;
import com.example.appthuongmaidientu.model.ChatList;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    List<CartList> cartLists;
    Context context;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public CartAdapter(List<CartList> cartLists, Context context) {
        this.cartLists = cartLists;
        this.context = context;
    }


    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_layout,null));
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        CartList cartList = cartLists.get(position);
        int i=position;
        Picasso.get().load(cartList.getHinhanh()).into(holder.imgCart);
        holder.tvGia.setText(cartList.getGia());
        holder.tvName.setText(cartList.getTenSP());
        holder.solg.setText(cartList.getSoLuongMua());
        if (cartList.getCheck().equals("1"))
            holder.checkBox.setChecked(true);
        else holder.checkBox.setChecked(false);
        long totalBill = 0,totalSP=0;
        for (int j=0;j<cartLists.size();j++){
            if (cartLists.get(j).getCheck().equals("1")){
                totalBill= totalBill+Long.parseLong(cartLists.get(j).getGia())*Long.parseLong(cartLists.get(j).getSoLuongMua());
                totalSP++;
            }
        }
        CartActivity.totalMoney.setText("Tổng tiền: "+totalBill);
        CartActivity.dachon.setText("Đã chọn: "+totalSP);
        if (totalSP==cartLists.size())
            CartActivity.checkBoxTotal.setChecked(true);
        else CartActivity.checkBoxTotal.setChecked(false);
        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartLists.get(i).setSoLuongMua(String.valueOf(Integer.parseInt(cartLists.get(i).getSoLuongMua())+1));
                holder.solg.setText(cartLists.get(i).getSoLuongMua());
                long totalBill = 0,totalSP=0;
                for (int j=0;j<cartLists.size();j++){
                    if (cartLists.get(j).getCheck().equals("1")){
                        totalBill= totalBill+Long.parseLong(cartLists.get(j).getGia())*Long.parseLong(cartLists.get(j).getSoLuongMua());
                        totalSP++;
                    }
                }
                CartActivity.totalMoney.setText("Tổng tiền: "+totalBill);
                CartActivity.dachon.setText("Đã chọn: "+totalSP);
                CartActivity.Update(cartLists);
            }
        });
        holder.btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(cartLists.get(i).getSoLuongMua())>0){
                    cartLists.get(i).setSoLuongMua(String.valueOf(Integer.parseInt(cartLists.get(i).getSoLuongMua())-1));
                    holder.solg.setText(cartLists.get(i).getSoLuongMua());
                    long totalBill = 0,totalSP=0;
                    for (int j=0;j<cartLists.size();j++){
                        if (cartLists.get(j).getCheck().equals("1")){
                            totalBill= totalBill+Long.parseLong(cartLists.get(j).getGia())*Long.parseLong(cartLists.get(j).getSoLuongMua());
                            totalSP++;
                        }
                    }
                    CartActivity.totalMoney.setText("Tổng tiền: "+totalBill);
                    CartActivity.dachon.setText("Đã chọn: "+totalSP);
                    CartActivity.Update(cartLists);
                }
            }
        });
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });
        System.out.println(cartList.getHinhanh());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked()){
                    System.out.println(i+" "+" acb");
                    cartLists.get(i).setCheck("1");
                }else cartLists.get(i).setCheck("0");
                long totalBill = 0,totalSP=0;
                for (int j=0;j<cartLists.size();j++){
                    if (cartLists.get(j).getCheck().equals("1")){
                        totalBill= totalBill+Long.parseLong(cartLists.get(j).getGia())*Long.parseLong(cartLists.get(j).getSoLuongMua());
                        totalSP++;
                    }
                }
                if (totalSP==cartLists.size())
                    CartActivity.checkBoxTotal.setChecked(true);
                else CartActivity.checkBoxTotal.setChecked(false);
                CartActivity.totalMoney.setText("Tổng tiền: "+totalBill);
                CartActivity.dachon.setText("Đã chọn: "+totalSP);
                CartActivity.Update(cartLists);

            }
        });
        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Bạn có muốn xóa sản phẩm này khỏi giỏ hàng ?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                CartActivity.Delete(cartLists.get(i));
                                cartLists.remove(i);
                                updateCart(cartLists);
                            }
                        })
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                builder.show();
            }
        });
    }
    public void updateCart(List<CartList> cartLists){
        this.cartLists=cartLists;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return cartLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCart,btnDel;
        TextView tvName,tvGia,solg;
        CheckBox checkBox;
        CardView btn_add,btn_remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox=itemView.findViewById(R.id.checkboxCartt);
            imgCart=itemView.findViewById(R.id.imgCartt);
            tvGia=itemView.findViewById(R.id.giaCartt);
            tvName=itemView.findViewById(R.id.nameCartt);
            solg = itemView.findViewById(R.id.edt_slCartt);
            btn_add=itemView.findViewById(R.id.btn_plusCartt);
            btn_remove=itemView.findViewById(R.id.btn_removeCartt);
            btnDel=itemView.findViewById(R.id.btn_delCartt);
        }
    }
}
