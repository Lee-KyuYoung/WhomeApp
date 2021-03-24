package com.lky.whome.kakaomap;

import android.content.Context;
import android.os.ParcelFormatException;
import android.view.ViewGroup;

import com.lky.whome.R;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class KakaoMap {

    private static MapView mMapView;
    private static ViewGroup container;

    //카카오맵 초기화
    public static void initMapView(String lat, String lng, Context context, ViewGroup mapContainer){

        if(mMapView == null){
            mMapView = new MapView(context);
            container = mapContainer;
        }
        try {
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(Double.valueOf(lat), Double.valueOf(lng));
            mMapView.setMapCenterPointAndZoomLevel(mapPoint, 4,true);
            mMapView.addPOIItem(setMapMarker(mapPoint));
        }catch (NullPointerException | ParcelFormatException e){
            e.printStackTrace();
        }
        mapContainer.addView(mMapView);
    }

    public static MapPOIItem setMapMarker(MapPoint mapPoint){
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("");
        marker.setTag(0);
        marker.setMapPoint(mapPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        marker.setCustomImageResourceId(R.drawable.map_marker_96);
//        marker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
//        marker.setCustomImageAnchor(0.5f, 0.5f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
        return marker;
    }

    public static void close(){
        if(mMapView != null) {
            container.removeView(mMapView);
            mMapView = null;
        }
    }
}
