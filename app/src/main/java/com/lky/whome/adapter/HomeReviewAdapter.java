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
import com.lky.whome.vo.HomeReviewVo;

import java.util.ArrayList;
import java.util.List;

public class HomeReviewAdapter extends RecyclerView.Adapter<HomeReviewAdapter.HomeReviewHolder> {

    private List<HomeReviewVo> reviewList = new ArrayList<>();
    private Context mContext;

    public HomeReviewAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public HomeReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_review_item, parent, false);
        return new HomeReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeReviewHolder holder, int position) {
        holder.onBind(reviewList.get(position));
    }

    public void addItems(List<HomeReviewVo> list){this.reviewList = list;}

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class HomeReviewHolder extends RecyclerView.ViewHolder{

        private ImageView review_user_img;
        private TextView review_user_id;
        private TextView review_insert_date;
        private TextView review_comm;

        public HomeReviewHolder(@NonNull View itemView) {
            super(itemView);

            review_user_img = itemView.findViewById(R.id.review_user_img);
            review_user_id = itemView.findViewById(R.id.review_user_id);
            review_insert_date = itemView.findViewById(R.id.review_insert_date);
            review_comm = itemView.findViewById(R.id.review_comm);
        }

        public void onBind(HomeReviewVo review){

            Glide.with(mContext)
                 .load(review.getUserImg())
                 .into(review_user_img);

            review_user_id.setText(review.getUserId());
            review_comm.setText(review.getComm());
            review_insert_date.setText(review.getInsertDate());
        }
    }
}
