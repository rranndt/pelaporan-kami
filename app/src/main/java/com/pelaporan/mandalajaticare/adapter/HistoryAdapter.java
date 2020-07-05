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
import com.pelaporan.mandalajaticare.model.HistoryModel;
//import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<HistoryModel> dataModel;

    public HistoryAdapter(Context context, ArrayList<HistoryModel> dataModel) {
        inflater = LayoutInflater.from(context);
        this.dataModel = dataModel;
    }

//    public HistoryAdapter(ArrayList<HistoryModel> list) {
//        this.dataModel = list;
//    }

    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = inflater.inflate(R.layout.rv_history, parent, false);
        View view = inflater.from(parent.getContext()).inflate(R.layout.rv_history, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.MyViewHolder holder, int position) {
//        Picasso.get().load(dataModel.get(position).getImgURL()).into(holder.ivImg);
        Glide.with(holder.itemView.getContext())
                .load(dataModel.get(position).getImgURL())
                .into(holder.ivImg);
//        holder.pelapor.setText(dataModel.get(position).getPelapor());
        holder.jenis.setText(dataModel.get(position).getJenis());
        holder.detail.setText(dataModel.get(position).getDetail());
//        holder.alamat.setText(dataModel.get(position).getAlamat());
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

        TextView pelapor, jenis, detail,  alamat, aksi;
        ImageView ivImg;

        public MyViewHolder(View itemView) {
            super(itemView);

//            pelapor = (TextView) itemView.findViewById(R.id.tvPelapor);
            jenis = (TextView) itemView.findViewById(R.id.tvJenis);
            detail = (TextView) itemView.findViewById(R.id.tvDetail);
//            alamat = (TextView) itemView.findViewById(R.id.tvAlamat);
            aksi = (TextView) itemView.findViewById(R.id.tvAksi);
            ivImg = (ImageView) itemView.findViewById(R.id.ivImg);
        }
    }
}
