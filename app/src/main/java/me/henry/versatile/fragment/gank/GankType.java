package me.henry.versatile.fragment.gank;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by henry on 2017/11/28.
 */

public class GankType implements Serializable{
    public static final int Android=22;
    public static final int IOS=33;
    public static final int Web=44;
    public static final int MeiZhi=55;

    public static String getTypeDes(int dataType){
        switch (dataType){
            case GankType.Android:
                return "Android";
            case GankType.IOS:
                return "iOS";
            case GankType.Web:
                return "前端";
            case GankType.MeiZhi:
                return "福利";
        }
        return null;
    }

}
