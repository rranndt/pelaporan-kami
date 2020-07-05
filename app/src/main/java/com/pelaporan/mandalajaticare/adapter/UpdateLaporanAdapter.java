package com.pelaporan.mandalajaticare.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pelaporan.mandalajaticare.R;
import com.pelaporan.mandalajaticare.model.UpdateLaporanModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class UpdateLaporanAdapter extends RecyclerView.Adapter<UpdateLaporanAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<UpdateLaporanModel> dataModel;

    public UpdateLaporanAdapter(Context context, ArrayList<UpdateLaporanModel> dataModel) {
        inflater = LayoutInflater.from(context);
        this.dataModel = dataModel;
    }

    public UpdateLaporanAdapter(ArrayList<UpdateLaporanModel> list) {
        this.dataModel = list;
    }

    @NonNull
    @Override
    public UpdateLaporanAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.from(parent.getContext()).inflate(R.layout.rv_update_petugas, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(dataModel.get(position).getImgURL())
                .into(holder.imgUrl);
        holder.username.setText(dataModel.get(position).getUsername());
        holder.detail.setText(dataModel.get(position).getDetail());
        holder.aksi.setText(dataModel.get(position).getAksi());
        if (dataModel.get(position).getAksi().equals("Belum Ditindak"))
        {
            holder.aksi.setTextColor(Color.parseColor("#FFFF0000")); // Merah
        } else if (dataModel.get(position).getAksi().equals("Sedang di Proses"))
        {
            holder.aksi.setTextColor(Color.parseColor("#FF00A5FF")); // Biru
        } else if (dataModel.get(position).getAksi().equals("Sudah Ditangani"))
        {
            holder.aksi.setTextColor(Color.parseColor("#FF2DFF00")); // Hijau
        }
    }

    @Override
    public int getItemCount() {
        return dataModel.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView username, detail, aksi;
        ImageView imgUrl;

        public MyViewHolder(View itemView) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.tvUsername);
            detail = (TextView) itemView.findViewById(R.id.tvDetail);
            aksi = (TextView) itemView.findViewById(R.id.tvTindakLanjut);
            imgUrl = (ImageView) itemView.findViewById(R.id.imgUrl);
        }
    }
}
