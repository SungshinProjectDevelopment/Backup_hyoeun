package org.androidtown.muksujung;

import net.daum.mf.map.api.MapPOIItem;

/**
 * Created by hyon1001 on 2018-05-14.
 */

public class GeoItem {
    public GeoItem() {
        this.title = null;
        this.geolati = 0.0;
        this.geolongi = 0.0;
        this.marker = null;
    }

    public GeoItem(String title, double geolati, double geolongi, MapPOIItem marker) {
        this.title = title;
        this.geolati = geolati;
        this.geolongi = geolongi;
        this.marker = marker;
    }

    String title = null;
    double geolati;
    double geolongi;
    MapPOIItem marker;

    @Override
    public String toString() {
        return "GeoItem{" +
                "title='" + title + '\'' +
                ", geolati=" + geolati +
                ", geolongi=" + geolongi +
                ", marker=" + marker +
                '}';
    }
}
    //
//    final double lat = arg1.getMapPoint().getMapPointGeoCoord().latitude;   /* 해당 마커가 찍힌 곳에 위도를 저장 */
//    final double log = arg1.getMapPoint().getMapPointGeoCoord().longitude;   /* 해당 마커가 찍힌 곳에 경도를 저장 */
//    AlertDialog.Builder bulider = new AlertDialog.Builder(this);   /* Alert 객체 생성 */
//bulider.setTitle(Maker_Name).setCancelable(false).setPositiveButton("자동차 길찾기",new DialogInterface.OnClickListener(){
//@Override   /* TODO Auto-generated method stub */
//public     void onClick(DialogInterface dialog,int which){
//        StringBuffer result=new StringBuffer("daummaps://route?sp=");
//        result.append(MapX);result.append(",");result.append(MapY);   /* 시작점 */
//        result.append("&ep=");result.append(lat);result.append(",");result.append(log);   /* 도착점 */
//        result.append("&by=CAR");
//        Uri uri=Uri.parse(result.toString());
//        Intent intent=new Intent();
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.setData(uri);
//        startActivity(intent);
//        }
//        }).setNegativeButton("취소",new DialogInterface.OnClickListener(){
//@Override   /* TODO Auto-generated method stub */
//public     void onClick(DialogInterface dialog,int which){dialog.cancel();}}).show();