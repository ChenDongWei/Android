package com.thxy.mobileguard.utils;

/**
 * Created by dongwei on 2016/6/23.
 */
public interface MyConstants {
    String SPFILE = "config";//sp的配置文件名
    String PASSWORD = "password";//手机防盗的密码
    String ISSETUP = "issetup";//是否进入设置向导界面
    String SIM = "sim";//SIM卡信息
    String SAFENUMBER = "safenumber";//安全号码
    int MUSIC = 120;//安全号码的加密种子(为迷惑而取名为MUSIC)
    String LOSTFIND = "lostfind";//开机是否开启手机防盗
    String LOSTFINDNAME = "lostfindname";//手机防盗名
    String AUTOUPDATE = "autoupdate";//自动更新设置
    String TOASTX = "toastx";//自定义吐司X坐标
    String TOASTY = "toasty";//自定义吐司Y坐标
    String STYLEBGINDEX = "styleindex";//来电归属地背景样式
    String SHOWSYSTEM = "showsystem";//显示系统进程
    String VIRUSVERSIONURL = "http://10.0.2.2:8080/VirusServer/servlet/getversion";//获取病毒库版本的url
    String GETVIRUSVDATASURL = "http://10.0.2.2:8080/VirusServer/servlet/getviruses";//获取病毒库数据的url
}
