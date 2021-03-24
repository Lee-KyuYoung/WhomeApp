package com.lky.whome.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.lky.whome.activity.MainActivity;
import com.lky.whome.network.VolleyNetwork;
import com.lky.whome.vo.WhomeVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.MainViewHolder> {

    private final List<WhomeVO> mWhomeLists = new ArrayList<>();
    private final Context mContext;

    public MainRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recyclerview_item, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainRecyclerViewAdapter.MainViewHolder holder, int position) {
        holder.onBind(mWhomeLists.get(position));
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return mWhomeLists.size();
    }

    public void addItem(WhomeVO item){mWhomeLists.add(item);}

    public void clearItem(){
        mWhomeLists.clear();
    }

    public void addItems(List<WhomeVO> list){
        mWhomeLists.addAll(list);
    }

    class MainViewHolder extends RecyclerView.ViewHolder {

        private final String TAG = MainViewHolder.class.getSimpleName();
        private final ImageView home_list_img;
        private final TextView home_list_type;
        private final TextView home_list_bed;
        private final TextView home_list_rate;
        private final TextView home_list_title;
        private final TextView home_list_pay;
        private final TextView home_list_max_guest;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);

            home_list_img = itemView.findViewById(R.id.home_list_img);
            home_list_type = itemView.findViewById(R.id.home_list_type);
            home_list_bed = itemView.findViewById(R.id.home_list_bed);
            home_list_rate = itemView.findViewById(R.id.home_list_rate);
            home_list_title = itemView.findViewById(R.id.home_list_title);
            home_list_pay = itemView.findViewById(R.id.home_list_pay);
            home_list_max_guest = itemView.findViewById(R.id.home_list_max_guest);

            itemView.setOnClickListener((v -> {

                int pos = getAdapterPosition();

                if(pos != RecyclerView.NO_POSITION){
                    WhomeVO homeInfo = mWhomeLists.get(pos);
                    String homeId = homeInfo.getHomeId();

                    if(!((MainActivity)mContext).isFinishing()) {
                        getWhomeDetail(homeId);
                    }
                }
            }));
        }

        @SuppressLint("SetTextI18n")
        void onBind(WhomeVO list){

            Glide.with(mContext).load(list.getHomeImg()).into(home_list_img);

            home_list_type.setText(list.getHomeType());
            home_list_bed.setText("침대 "+list.getHomeBedCount()+"개");
            home_list_pay.setText("₩"+list.getHomePay()+" / 1박");
            home_list_rate.setText(list.getHomeRate());
            home_list_title.setText(list.getHomeTitle());
            home_list_max_guest.setText("최대 "+list.getHomeMaxGuest()+"명");
        }

        void getWhomeDetail(String homeId){

            StringBuilder url = new StringBuilder(mContext.getResources().getString(R.string.REAL_WHOME_REST_URL))
                    .append(mContext.getResources().getString(R.string.GET_HOME_DETAIL))
                    .append("/"+homeId);

            new VolleyNetwork(mContext).requestGet(url.toString(),null, (response)->{

                try {
                    WhomeVO whomeVO = new WhomeVO();
                    JSONObject object = new JSONObject(response);
                    JSONObject whomeInfo = object.getJSONObject("homeDetail");

                    whomeVO.setHomeId(whomeInfo.getString("homeId"));
                    whomeVO.setUserId(whomeInfo.getString("userId"));
                    whomeVO.setUserImg(whomeInfo.getString("userImg"));
                    whomeVO.setUserIntro(whomeInfo.getString("userIntro"));
                    whomeVO.setHomeTitle(whomeInfo.getString("homeTitle"));
                    whomeVO.setHomeAddr1(whomeInfo.getString("homeAddr1"));
                    whomeVO.setHomePay(whomeInfo.getString("homePay"));
                    whomeVO.setHomeRevStart(whomeInfo.getString("homeRevStart"));
                    whomeVO.setHomeRevEnd(whomeInfo.getString("homeRevEnd"));
                    whomeVO.setHomeMaxGuest(whomeInfo.getString("maxGuest"));
                    whomeVO.setHomeBedCount(whomeInfo.getString("bedCnt"));
                    whomeVO.setHomeBathCount(whomeInfo.getString("bathroomCnt"));
                    whomeVO.setHomeComm(whomeInfo.getString("homeComm"));
                    whomeVO.setHomeRange(whomeInfo.getString("homeRange"));
                    whomeVO.setHomeRateCount(whomeInfo.getString("homeRateCount"));
                    whomeVO.setUserJoinDate(whomeInfo.getString("userJoinDate"));
                    whomeVO.setHomeRate(whomeInfo.getString("homeRate"));
                    whomeVO.setLongitude(whomeInfo.getString("longitude"));
                    whomeVO.setLatitude(whomeInfo.getString("latitude"));

                    whomeVO.setHomeImgList(new ArrayList<>());
                    whomeVO.setHomeFacilityList(new ArrayList<>());
                    whomeVO.setHomePrecautionList(new ArrayList<>());
                    whomeVO.setHomeTypeList(new ArrayList<>());
                    whomeVO.setHomeGuestRuleList(new ArrayList<>());

                    //집 이미지
                    JSONArray imgList = whomeInfo.getJSONArray("homeImgPath");
                    for(int i = 0; i < imgList.length(); i++){
                        if(i == 0){
                            whomeVO.setHomeImg(imgList.getString(i));
                        }
                        whomeVO.getHomeImgList().add(imgList.getString(i));
                    }

                    //편의 시설
                    JSONArray facilityList = whomeInfo.getJSONArray("homeFacilityList");
                    for(int i = 0; i < facilityList.length(); i++){
                        whomeVO.getHomeFacilityList().add(facilityList.getString(i));
                    }

                    //집 사용시 주의사항
                    JSONArray precautionList = whomeInfo.getJSONArray("homePrecautionList");
                    for(int i = 0; i < precautionList.length(); i++){
                        whomeVO.getHomePrecautionList().add(precautionList.getString(i));
                    }

                    //집 타입
                    JSONArray homeType = whomeInfo.getJSONArray("homeTypeList");
                    for(int i = 0; i < homeType.length(); i++){
                        whomeVO.getHomeTypeList().add(homeType.getString(i));
                    }

                    //게스트가 알아야 할 사항
                    JSONArray guestRule = whomeInfo.getJSONArray("homeGuestRuleList");
                    for(int i = 0; i < guestRule.length(); i++){
                        whomeVO.getHomeGuestRuleList().add(guestRule.getString(i));
                    }
                    Intent intent = new Intent(mContext, HomeDetailActivity.class);
                    intent.putExtra("whomeVO",whomeVO);
                    mContext.startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
