package com.example.appthuongmaidientu.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appthuongmaidientu.R;
import com.example.appthuongmaidientu.UIFragment.HomeFragment;
import com.example.appthuongmaidientu.model.DMSP;
import com.example.appthuongmaidientu.model.DanhMuc;
import com.example.appthuongmaidientu.model.SanPham;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DMSPAdapter extends RecyclerView.Adapter<DMSPAdapter.ViewHolder> {
    List<DMSP> danhMucList;
    private Context mContext;
    ImageView img_cate;

    public DMSPAdapter(List<DMSP> danhMucList, Context mContext) {
        this.danhMucList = danhMucList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dmsp_item, parent, false);
        return new ViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int i=position;
        DMSP computer =danhMucList.get(position);
        holder.tenDanhMuc.setText(computer.getName());
        Picasso.get().load(computer.getHinhanh()).into(img_cate);
        holder.layoutdmsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(computer.getId())>0)
                    HomeFragment.update(Integer.parseInt(computer.getId()));
                else HomeFragment.update();
            }
        });
    }
    public void updateDanhMuc(List<DMSP> danhMucs){
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
        LinearLayout layoutdmsp;


        public ViewHolder(View itemView) {
            super(itemView);
            itemview = itemView;
            tenDanhMuc = itemView.findViewById(R.id.name_cate);
            img_cate = itemView.findViewById(R.id.img_cate);
            layoutdmsp=itemView.findViewById(R.id.layoutdmsp);

            //Xử lý khi nút Chi tiết được bấm
        }
    }
}
