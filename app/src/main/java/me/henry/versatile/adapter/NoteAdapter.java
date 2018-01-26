package me.henry.versatile.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.henry.versatile.R;
import me.henry.versatile.model.note.Note;
import me.henry.versatile.model.note.Punch;
import me.henry.versatile.model.note.analyze.AnParticle;
import me.henry.versatile.model.note.analyze.DisplayModel;
import me.henry.versatile.model.note.analyze.PunchAnalyzer;

/**
 * Created by henry on 2018/1/12.
 */

public class NoteAdapter extends BaseQuickAdapter<Note, NoteAdapter.NoteViewHolder> {
    private ArrayList<Punch> mShowPunches = new ArrayList<>();
    private ArrayList<Punch> punches;
    public static final int FORM_SIZE = 8;

    public NoteAdapter(int layoutResId, @Nullable List<Note> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(NoteViewHolder helper, Note item) {
        //清空数据
        helper.tv_title.setText(item.getName());
        helper.cv_note_icon.setImageResource(getRandomImg());
        helper.addOnClickListener(R.id.ll_total_time);
        helper.addOnClickListener(R.id.iv_punch);

        //关于图标的----
        mShowPunches.clear();
        punches = item.getPunches();
        if (punches != null) {
            //先把数据顺序排列好
            sortData();
            int dataSize = mShowPunches.size();
            if (dataSize >= 0) {
                helper.tv_total_time.setText(String.valueOf(punches.size()));
                DisplayModel displayModel = new PunchAnalyzer(mShowPunches).getDisplayModel();
                helper.tv_maxValue.setText(displayModel.maxStr);
                helper.tv_minValue.setText(displayModel.minStr);
                LineChart mChart = helper.mChart;
                //设置chart
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
                mChart.setNoDataText("喵喵喵?");


                //设置limitline----------------
                LimitLine ll1 = new LimitLine(displayModel.maxMin, "");
                ll1.setLineWidth(0.8f);
                ll1.setLineColor(Color.WHITE);

                LimitLine ll2 = new LimitLine(displayModel.minMin, "");
                ll2.setLineWidth(0.8f);
                ll2.setLineColor(Color.WHITE);


                //设置y轴------------------------------------------------------------
                YAxis yAxis = mChart.getAxisRight();
                yAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines

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
//                xAxis.setAxisMaximum(punches.size());
//                xAxis.setAxisMinimum(punches.size()-dataSize+1);
                //xAxis.setAxisLineColor(Color.WHITE);
                //xAxis.setAxisLineWidth(1f);
                xAxis.setTextColor(Color.WHITE);
                xAxis.setLabelCount(8, true);
                //设置数据
                ArrayList<Entry> values = new ArrayList<Entry>();
                AnParticle[] datas = displayModel.datas;
                for (int i = 0; i < FORM_SIZE; i++) {

                    if (datas.length - i > 0) {
                        values.add(new Entry(punches.size() - dataSize + i + 1, datas[i].totalMin));

                    } else {
                        values.add(new Entry(punches.size() - dataSize + i + 1, displayModel.minMin));
                    }
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
                    set1.setLineWidth(1f);
                    set1.setDrawCircles(true);
                    set1.setCircleColor(Color.WHITE);//圆圈颜色
                    set1.setCircleRadius(3f);
                    set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);//横向贝塞尔
                    set1.setCubicIntensity(0.3f);//曲线弯曲系数
                    set1.setDrawCircleHole(false);//不空心
                    set1.setDrawValues(false);//数值点

                    //set1.setValueTextColor(Color.WHITE);
                    set1.setDrawFilled(false);//填充
                    set1.setHighlightEnabled(false);//准星
                    //图表渐变
//                    Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.fade_red);
//                    set1.setFillDrawable(drawable);//填充颜色
                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(set1); // add the datasets

                    // create a data object with the datasets
                    LineData data = new LineData(dataSets);

                    // set data
                    mChart.setData(data);
                }

            }


        } else {

            helper.mChart.setNoDataText("喵喵喵?");
        }


    }

    private void sortData() {
        int size = punches.size();
        if (size > FORM_SIZE) {
            for (int i = size - FORM_SIZE; i < size; i++) {
                mShowPunches.add(punches.get(i));
            }
        } else {
            mShowPunches.addAll(punches);
        }
    }

    public static final int[] girlRes = new int[]{R.mipmap.icon_test_girl1,
            R.mipmap.icon_test_girl2,
            R.mipmap.icon_test_girl3,
            R.mipmap.icon_test_girl4,
            R.mipmap.icon_test_girl5,
            R.mipmap.icon_test_girl6};

    private int getRandomImg() {
        //0-5
        int res = (int) (Math.random() * 6);
        return girlRes[res];
    }

    class NoteViewHolder extends BaseViewHolder {
        CircleImageView cv_note_icon;
        TextView tv_title;
        TextView tv_create_time;
        ImageView iv_punch;
        LinearLayout ll_total_time;
        LineChart mChart;
        TextView tv_maxValue;
        TextView tv_minValue;
        TextView tv_total_time;

        public NoteViewHolder(View view) {
            super(view);
            cv_note_icon = view.findViewById(R.id.cv_note_icon);
            tv_title = view.findViewById(R.id.tv_title);
            tv_create_time = view.findViewById(R.id.tv_create_time);
            iv_punch = view.findViewById(R.id.iv_punch);
            ll_total_time = view.findViewById(R.id.ll_total_time);
            mChart = view.findViewById(R.id.mChart);
            tv_maxValue = view.findViewById(R.id.tv_maxValue);
            tv_minValue = view.findViewById(R.id.tv_minValue);
            tv_total_time = view.findViewById(R.id.tv_total_time);
        }
    }
}
