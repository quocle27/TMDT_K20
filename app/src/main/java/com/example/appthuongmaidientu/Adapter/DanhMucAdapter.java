package com.example.appthuongmaidientu.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appthuongmaidientu.R;
import com.example.appthuongmaidientu.model.DanhMuc;
import com.example.appthuongmaidientu.model.SanPham;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DanhMucAdapter extends RecyclerView.Adapter<DanhMucAdapter.ViewHolder> {
    List<DanhMuc> danhMucList;
    private Context mContext;
    ImageView img_cate;

    public DanhMucAdapter(List<DanhMuc> danhMucList, Context mContext) {
        this.danhMucList = danhMucList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.danhmucitemlayout, parent, false);
        return new ViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DanhMuc computer =danhMucList.get(position);
        holder.tenDanhMuc.setText(computer.getName());
        Picasso.get().load(computer.getHinhanh()).into(img_cate);
    }
    public void updateDanhMuc(List<DanhMuc> danhMucs){
        this.danhMucList=danhMucs;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return danhMucList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private View itemview;
        public TextView tenDanhMuc;
        public Button detail_button;


        public ViewHolder(View itemView) {
            super(itemView);
            itemview = itemView;
            tenDanhMuc = itemView.findViewById(R.id.studentname);
            img_cate=itemView.findViewById(R.id.DMItemIMG);

            //Xử lý khi nút Chi tiết được bấm
        }
    }
}
