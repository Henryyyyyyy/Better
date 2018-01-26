package me.henry.versatile.activity;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;

import me.henry.versatile.R;

public class CharttActivity extends AppCompatActivity {
    private LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_note);
        mChart = findViewById(R.id.mChart);

        //屏蔽一切关于手势的
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        // 没有描述text
        mChart.getDescription().setEnabled(false);
        mChart.animateY(1500);
        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        mChart.getLegend().setEnabled(false);//取消图例
        mChart.setBackgroundColor(Color.TRANSPARENT);


        //设置limitline----------------
        LimitLine ll1 = new LimitLine(24, "");
        ll1.setLineWidth(0.8f);
        ll1.setLineColor(Color.WHITE);

        LimitLine ll2 = new LimitLine(0, "");
        ll2.setLineWidth(0.8f);
        ll2.setLineColor(Color.WHITE);


        //设置y轴------------------------------------------------------------
        YAxis yAxis = mChart.getAxisRight();
        yAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        yAxis.setAxisMaximum(24);
        yAxis.setAxisMinimum(0);
        yAxis.addLimitLine(ll1);
        yAxis.addLimitLine(ll2);
        yAxis.setTextColor(Color.parseColor("#00ffffff"));
        yAxis.setDrawAxisLine(false);//设置y轴边缘线
        yAxis.setDrawGridLines(false);//设置y轴网格线
        mChart.getAxisLeft().setEnabled(false);


        //设置x轴--------------------------------------------------
        XAxis xAxis = mChart.getXAxis();
        xAxis.removeAllLimitLines();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);//设置x轴网格线
        xAxis.setDrawAxisLine(false);//设置x轴边缘线
//        xAxis.setAxisLineColor(Color.WHITE);
//        xAxis.setAxisLineWidth(1f);
        xAxis.setTextColor(Color.WHITE);

        //设置数据--------------------------------------------------
        setData(8, 24);

        //  dont forget to refresh the drawing
        //  mChart.invalidate();
    }

    private void setData(int count, int range) {

        ArrayList<Entry> values = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {

            int val = (int) (Math.random() * range);
            values.add(new Entry(i, val));
        }

        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, null);//新建一个不带图标的数据集

            set1.setColor(Color.WHITE);//线的颜色
            set1.setCircleColor(Color.WHITE);//圆圈颜色
            set1.setLineWidth(1f);
            set1.setCircleRadius(4f);
            set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);//横向贝塞尔
            set1.setCubicIntensity(0.3f);//曲线弯曲系数
            set1.setDrawCircleHole(false);//不空心
            set1.setDrawValues(false);//数值点
            //set1.setValueTextColor(Color.WHITE);
            set1.setDrawFilled(true);//填充
            set1.setHighlightEnabled(false);//准星
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
            set1.setFillDrawable(drawable);//填充颜色
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            mChart.setData(data);
        }
    }
}
