package com.example.appthuongmaidientu.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appthuongmaidientu.R;
import com.example.appthuongmaidientu.activity.EditUserActivity;
import com.example.appthuongmaidientu.model.NotifyList;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.ViewHolder> {
    List<NotifyList> notifyLists;
    Context context;

    public NotifyAdapter(List<NotifyList> notifyLists, Context context) {
        this.notifyLists = notifyLists;
        this.context = context;
    }

    @NonNull
    @Override
    public NotifyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.notify_item_layout,null));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull NotifyAdapter.ViewHolder holder, int position) {
        NotifyList notifyList = notifyLists.get(position);

        holder.content.setText(notifyList.getContent());
        holder.itemlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_notify_custom);
                dialog.show();
                Button button = dialog.findViewById(R.id.btn_saveStatusNotify);
                Spinner spinner = dialog.findViewById(R.id.statusNotify);
                ArrayAdapter<String> adapter;
                List<String> dmmmm=new ArrayList<>();
                dmmmm.add("Xác nhận đơn");
                dmmmm.add("Đang chuẩn bị hàng");
                dmmmm.add("Đã giao hàng cho vận chuyển");
                dmmmm.add("Vận chuyển đã báo giao hàng thành công");
                adapter  = new ArrayAdapter(context, android.R.layout.simple_spinner_item,dmmmm);
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                spinner.setAdapter(adapter);
                Activity activity =(Activity) context;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("ThongBao").child(activity.getIntent().getStringExtra("mobile")).child(notifyList.getIdTB()).child(notifyList.getId()).child((notifyList.getId())).child("status").setValue(spinner.getSelectedItem());
                        databaseReference.child("DonDatHang").child(notifyList.getIdKH()).child(notifyList.getIdSP()).child("status").setValue(spinner.getSelectedItem());
                        dialog.dismiss();
                    }
                });
            }
        });
    }
    public void update(List<NotifyList> notifyLists){
        this.notifyLists=notifyLists;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return notifyLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout itemlayout;
        TextView content,time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemlayout=itemView.findViewById(R.id.layout_notifyitem);
            content=itemView.findViewById(R.id.ndNotify);
            time=itemView.findViewById(R.id.timeNotify);
        }
    }
}
