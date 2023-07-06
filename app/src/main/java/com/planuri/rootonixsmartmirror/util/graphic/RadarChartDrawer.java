package com.planuri.rootonixsmartmirror.util.graphic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class RadarChartDrawer {

    private static final float FLOAT_MIN_SCORE = 1.0f;     //점수 최소값
    private static final float FLOAT_MAX_SCORE = 20.0f;    //점수 최대값

    public static class OffsetPoint {   //차트의 중심점 위치 조정
        float x;
        float y;

        public OffsetPoint(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class RadarAttribute {
        float diameter;     //레이더 차트 지름
        float vertexRadius; //레이더 차트 꼭지점 반지름(1보다 작으면 꼭지점 그리지 않음)
        String vertexColor; //레이더 차트 꼭지점 색(RGB HEX)

        public RadarAttribute(float diameter, float vertexRadius, String vertexColor) {
            this.diameter = diameter;
            this.vertexRadius = vertexRadius;
            this.vertexColor = vertexColor;
        }
    }

    public static void drawRadarChart(Canvas canvas, List<Integer> scores, RadarAttribute radarAttribute, OffsetPoint centerOffset) {
        // 점수별로 차트를 그림
        int numVertices = scores.size();
        float sliceDegrees = 360.0f / numVertices;

        float radius = radarAttribute.diameter / 2.0f;  // 반지름

        // 원의 중심 좌표
        float centerX = canvas.getWidth() / 2.0f + centerOffset.x;  // 원의 중심 X 좌표
        float centerY = canvas.getHeight() / 2.0f + centerOffset.y;  // 원의 중심 Y 좌표

        // 점수를 그릴 때 사용하는 페인트 설정
        Paint scorePaint = new Paint();
        scorePaint.setAntiAlias(true);
        scorePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        scorePaint.setColor(Color.argb(128, 0, 0, 255)); // Semi-transparent blue
        scorePaint.setStrokeWidth(2f);


        // 꼭지점을 그릴 때 사용하는 페인트 설정
        Paint pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setColor(Color.parseColor(radarAttribute.vertexColor));

        // 각 점수 위치에 대한 좌표를 저장할 리스트
        List<PointF> points = new ArrayList<>();

        // 각 점수에 대한 좌표를 계산하고 리스트에 추가
        for (int i = 0; i < numVertices; i++) {
            float score = scores.get(i);
            if(score < FLOAT_MIN_SCORE) score = FLOAT_MIN_SCORE;
            else if(score > FLOAT_MAX_SCORE) score = FLOAT_MAX_SCORE;

            float normalizedScore = (score - 1) / 19.0f; // 점수를 0~1 사이의 값으로 정규화 (1~20 사이의 값으로 조정)
            float angleDegrees = 90 - (i * sliceDegrees);   // 각도를 시계 반대 방향으로 계산
            if (angleDegrees < 0) {
                angleDegrees += 360;
            }
            float angleRadians = (float) Math.toRadians(angleDegrees);

            float x = centerX + radius * normalizedScore * (float) Math.cos(angleRadians);
            float y = centerY - radius * normalizedScore * (float) Math.sin(angleRadians);
            //Log.e("Points", "angleRadians:"+angleRadians+", angleDegrees:"+angleDegrees+", normalizedScore:"+normalizedScore+", point:"+x + ", " + y);
            points.add(new PointF(x, y));
        }

        // 점수별로 선과 다각형을 그림
        Path path = new Path();
        path.moveTo(points.get(0).x, points.get(0).y);
        for (int i = 1; i < numVertices; i++) {
            path.lineTo(points.get(i).x, points.get(i).y);
        }
        path.close(); // 다각형을 완성하기 위해 마지막 점과 처음 점을 연결
        canvas.drawPath(path, scorePaint);

        // 각 점수 위치에 점을 그림
        if(radarAttribute.vertexRadius >= 1.0f) {
            for (int i = 0; i < numVertices; i++) {
                PointF point = points.get(i);
                float angleDegrees = 90 - (i * sliceDegrees); // 각도를 시계 반대 방향으로 계산
                if (angleDegrees < 0) {
                    angleDegrees += 360;
                }
                float angleRadians = (float) Math.toRadians(angleDegrees);

                float shiftAmount = radarAttribute.vertexRadius;  // 원의 중심을 이동할 양 (픽셀 단위)
                float shiftX = shiftAmount * (float) Math.cos(angleRadians);
                float shiftY = shiftAmount * (float) Math.sin(angleRadians);
                canvas.drawCircle(point.x + shiftX, point.y - shiftY, radarAttribute.vertexRadius, pointPaint);
            }
        }
    }
}
