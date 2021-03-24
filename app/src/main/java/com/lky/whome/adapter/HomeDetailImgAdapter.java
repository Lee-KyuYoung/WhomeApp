package com.lky.whome.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.lky.whome.R;
import com.lky.whome.fragment.ImgGalleryFragment;
import java.util.ArrayList;
import java.util.List;


public class HomeDetailImgAdapter extends RecyclerView.Adapter<HomeDetailImgAdapter.HomeDetailImgHolder>{

    private final Context mContext;
    private final List<String> imgList = new ArrayList<>();

    public HomeDetailImgAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public HomeDetailImgHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_detail_viewpager_item, parent, false);
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

        private final ImageView home_image;
        private final ImgGalleryFragment imgGalleryFragment = new ImgGalleryFragment();

        public HomeDetailImgHolder(@NonNull View itemView) {
            super(itemView);

            home_image = itemView.findViewById(R.id.home_detail_img);

            itemView.setOnClickListener(v -> {

                ((AppCompatActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_framelayout, imgGalleryFragment).commit();

                Bundle bundle = new Bundle();
                bundle.putStringArrayList("imgList",(ArrayList)imgList);
                bundle.putInt("position",getAdapterPosition());

                imgGalleryFragment.setArguments(bundle);
            });
        }

        void onBind(String imgURl) {
            Glide.with(mContext).load(imgURl).into(home_image);
        }
    }
}