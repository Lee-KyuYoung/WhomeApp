package com.lky.whome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.lky.whome.R;
import com.lky.whome.activity.HomeDetailActivity;
import java.util.ArrayList;
import java.util.List;


public class GallerylImgAdapter extends RecyclerView.Adapter<GallerylImgAdapter.HomeDetailImgHolder>{

    private final Context mContext;
    private final List<String> imgList = new ArrayList<>();

    public GallerylImgAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public HomeDetailImgHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_viewpager_item, parent, false);
        return new HomeDetailImgHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeDetailImgHolder holder, int position) {
        holder.onBind(imgList.get(position));
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    public void addItems(List<String> list){
        imgList.addAll(list);
    }

    class HomeDetailImgHolder extends RecyclerView.ViewHolder {

        private final ImageView gallery_img;
        private final ImageView gallery_img_viewer;
        private final TextView gallery_position;

        public HomeDetailImgHolder(@NonNull View itemView) {
            super(itemView);

            gallery_img = itemView.findViewById(R.id.gallery_img);
            gallery_img_viewer = ((HomeDetailActivity)mContext).findViewById(R.id.gallery_img_viewer);
            gallery_position = ((HomeDetailActivity)mContext).findViewById(R.id.gallery_position);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                int total = getItemCount();

                gallery_position.setText((pos+1)+" / "+total);

                Glide.with(mContext)
                     .load(imgList.get(pos))
                     .into(gallery_img_viewer);
            });
        }
        void onBind(String imgURl) {
            Glide.with(mContext)
                 .load(imgURl)
                 .into(gallery_img);
        }
    }
}