package me.henry.versatile.model.note.analyze;

import java.util.ArrayList;
import java.util.zip.Adler32;

import me.henry.versatile.model.note.Punch;

/**
 * Created by henry on 2018/1/12.
 *
 */

public class PunchAnalyzer {
    private DisplayModel model;
    private ArrayList<Punch> mDatas;

    public PunchAnalyzer(ArrayList<Punch> data) {
        model = new DisplayModel();
        mDatas = data;
        setExtreme();
    }

    public DisplayModel getDisplayModel() {

        return model;
    }

    private void setExtreme() {
        AnParticle[] anParticles = new AnParticle[mDatas.size()];
        AnParticle ap;
        Punch punch;
        for (int i = 0; i < mDatas.size(); i++) {
            ap = new AnParticle();
            punch = mDatas.get(i);
            ap.hour = punch.getHour();
            ap.min = punch.getMinutes();
            ap.totalMin = ap.hour * 60 + ap.min;
            anParticles[i] = (ap);
        }
        //排序之前先赋值
        model.datas = anParticles;
        //从小到大排序
        AnParticle[] data = sortX_D(anParticles);
        AnParticle max;
        AnParticle min;
        if (data.length == 1) {
            max = data[data.length - 1];
            min = new AnParticle();
            min.totalMin = 0;
            min.hour = 0;
            min.min = 0;
        } else {
            max = data[data.length - 1];
            min = data[0];
        }

        model.maxMin = max.totalMin;
        model.minMin = min.totalMin;
        model.maxStr = judgeInt(max.hour) + ":" + judgeInt(max.min);
        model.minStr = judgeInt(min.hour) + ":" + judgeInt(min.min);
    }

    private String judgeInt(int data) {
        if (data > 9) {
            return data + "";
        } else {
            return "0" + data;
        }
    }

    /**
     * 从小到大
     *
     * @param data
     */
    public static AnParticle[] sortX_D(AnParticle[] data) {
        AnParticle[] array = data.clone();
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j].totalMin > array[j + 1].totalMin) {
                    AnParticle temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
        return array;
    }
}
