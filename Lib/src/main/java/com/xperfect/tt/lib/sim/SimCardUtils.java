package com.xperfect.tt.lib.sim;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Kyle on 2017/9/18.
 */
public class SimCardUtils {

    public static boolean isMultiSimCard(Context context) {

        return false;
    }

    public static int getSimCardCount(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Uri uri = Uri.parse("content://telephony/siminfo");
            Cursor cursor = null;
            ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
            //new String[]{"_id","icc_id","sim_id","display_name","carrier_name","name_source","color","number","display_number_format","data_roaming","mcc","mnc","sub_state","network_mode","enable_cmas_extreme_threat_alerts","enable_cmas_severe_threat_alerts","enable_cmas_amber_alerts","enable_emergency_alerts","alert_sound_duration","alert_reminder_interval","enable_alert_vibrate","enable_alert_speech","enable_etws_test_alerts","enable_channel_50_alerts","enable_cmas_test_alerts","show_cmas_opt_out_dialog","sim_provisioning_status"}
            cursor = contentResolver.query(uri, new String[]{}, "sim_id>=0",
                    new String[]{}, null);
            if (null != cursor) {
                while (cursor.moveToNext()) {
                    String iccId = cursor.getString(cursor.getColumnIndex("icc_id"));
                    String displayName = cursor.getString(cursor.getColumnIndex("display_name"));
                    int simId = cursor.getInt(cursor.getColumnIndex("sim_id"));
                    int id = cursor.getInt(cursor.getColumnIndex("_id"));

                    Log.e("Q_M", "icc_id--> " + iccId);
                    Log.e("Q_M", "sim_id--> " + simId);
                    Log.e("Q_M", "display_name-->   " + displayName);
                    Log.e("Q_M", "subId或者说是_id->    " + id);
                    Log.e("Q_M", "number->  " + id);
                    Log.e("Q_M", "Cursor->  " + JSON.toJSONString(cursor));
                    Log.e("Q_M", "---------------------------------");
                }
                Log.e("Q_M", "Cursor.getCount()     " + cursor.getCount());
                return cursor.getCount();
            }
        } else {
        }
        return 0;
    }

    public static String getImsi(Context context) {
        String imsi = "";
        try {   //普通方法获取imsi
            TelephonyManager tm = (TelephonyManager) context.
                    getSystemService(Context.TELEPHONY_SERVICE);
            imsi = tm.getSubscriberId();
            if (imsi == null || "".equals(imsi)) imsi = tm.getSimOperator();
            Class<?>[] resources = new Class<?>[]{int.class};
            Integer resourcesId = new Integer(1);
            if (imsi == null || "".equals(imsi)) {
                try {   //利用反射获取    MTK手机
                    Method addMethod = tm.getClass().getDeclaredMethod("getSubscriberIdGemini", resources);
                    addMethod.setAccessible(true);
                    imsi = (String) addMethod.invoke(tm, resourcesId);
                } catch (Exception e) {
                    imsi = null;
                }
            }
            if (imsi == null || "".equals(imsi)) {
                try {   //利用反射获取    展讯手机
                    Class<?> c = Class
                            .forName("com.android.internal.telephony.PhoneFactory");
                    Method m = c.getMethod("getServiceName", String.class, int.class);
                    String spreadTmService = (String) m.invoke(c, Context.TELEPHONY_SERVICE, 1);
                    TelephonyManager tm1 = (TelephonyManager) context.getSystemService(spreadTmService);
                    imsi = tm1.getSubscriberId();
                } catch (Exception e) {
                    imsi = null;
                }
            }
            if (imsi == null || "".equals(imsi)) {
                try {   //利用反射获取    高通手机
                    Method addMethod2 = tm.getClass().getDeclaredMethod("getSimSerialNumber", resources);
                    addMethod2.setAccessible(true);
                    imsi = (String) addMethod2.invoke(tm, resourcesId);
                } catch (Exception e) {
                    imsi = null;
                }
            }
            if (imsi == null || "".equals(imsi)) {
                imsi = "000000";
            }
            return imsi;
        } catch (Exception e) {
            return "000000";
        }
    }

    class SimCardInfo {

        private String imsi;
        private String cardName;
        private String tel;

        public SimCardInfo() {
        }

        public String getImsi() {
            return imsi;
        }

        public void setImsi(String imsi) {
            this.imsi = imsi;
        }

        public String getCardName() {
            return cardName;
        }

        public void setCardName(String cardName) {
            this.cardName = cardName;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }
    }
}