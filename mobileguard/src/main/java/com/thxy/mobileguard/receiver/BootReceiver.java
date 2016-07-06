package com.thxy.mobileguard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import com.thxy.mobileguard.service.LostFindService;
import com.thxy.mobileguard.utils.EncryptTools;
import com.thxy.mobileguard.utils.MyConstants;
import com.thxy.mobileguard.utils.SpTools;

/**
 * 开机启动的广播接收者
 * Created by dongwei on 2016/6/25.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //手机启动完成，检测SIM卡是否发生变化
        //取出原来保存的SIM卡信息
        String oldsim = SpTools.getString(context, MyConstants.SIM, "");
        //获取当前手机SIM卡信息
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context
                .TELEPHONY_SERVICE);

        String simSerialNumber = tm.getSimSerialNumber();
        if (!oldsim.equals(simSerialNumber)) {
            //SIM卡发生变化，对安全号码发送短信，安全号码肯定存在
            String safeNumber = SpTools.getString(context, MyConstants.SAFENUMBER, "");
            //解密安全号码
            safeNumber = EncryptTools.decryption(safeNumber);
            SmsManager sm = SmsManager.getDefault();
            sm.sendTextMessage(safeNumber, "", "我是小偷", null, null);
        }

        if (SpTools.getBoolean(context, MyConstants.LOSTFIND, true)){
            Intent service = new Intent(context, LostFindService.class);
            context.startService(service);
        }
    }
}
