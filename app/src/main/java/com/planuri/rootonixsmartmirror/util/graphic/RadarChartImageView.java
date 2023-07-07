package com.planuri.rootonixsmartmirror.util.graphic;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.planuri.rootonixsmartmirror.R;

import java.util.ArrayList;

public class RadarChartImageView extends androidx.appcompat.widget.AppCompatImageView {
    private ArrayList<Integer> scores = new ArrayList<>();
    private RadarChartDrawer.RadarAttribute radarAttribute;
    private RadarChartDrawer.OffsetPoint offsetPoint;
    private RadarChartDrawer radarChartDrawer;

    public RadarChartImageView(Context context) {
        super(context);
        this.radarChartDrawer = new RadarChartDrawer();
    }

    public RadarChartImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.radarChartDrawer = new RadarChartDrawer();
    }

    public RadarChartImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.radarChartDrawer = new RadarChartDrawer();
    }

    public void setScores(ArrayList<Integer> scores) {
        this.scores = scores;
        invalidate(); // 뷰를 다시 그리도록 요청
    }

    public void setRadarAttribute(RadarChartDrawer.RadarAttribute radarAttribute) {
        this.radarAttribute = radarAttribute;
    }

    public void setOffsetPoint(RadarChartDrawer.OffsetPoint offsetPoint) {
        this.offsetPoint = offsetPoint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (scores != null && scores.size() > 0 && radarAttribute != null && offsetPoint != null) {
            radarChartDrawer.drawRadarChart(canvas, scores, radarAttribute, offsetPoint);
        }
    }
}