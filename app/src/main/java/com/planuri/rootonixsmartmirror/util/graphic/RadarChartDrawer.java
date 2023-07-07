package com.planuri.rootonixsmartmirror.util.graphic;

import android.animation.ValueAnimator;
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

    private static final int MIN_SCORE = 1;     //점수 최소값
    private static final int MAX_SCORE = 20;    //점수 최대값

    // 점수 레이더 차트, 꼭지점 그릴 때 사용하는 페인트
    private Paint scorePaint = new Paint();
    private Paint pointPaint = new Paint();

    private boolean isDrawVertexPoints = true;  // 호출 시 꼭지점을 그릴지 여부
    public void setIsDrawVertexPoints(boolean isDrawVertexPoints) {
        this.isDrawVertexPoints = isDrawVertexPoints;
    }

    //차트의 중심점 위치 조정을 위한 내부 클래스
    public static class OffsetPoint {
        private float x;
        private float y;

        public OffsetPoint(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    //레이더차트 속성 내부 클래스
    public static class RadarAttribute {
        private float diameter;     //레이더 차트 지름
        private float vertexRadius; //레이더 차트 꼭지점 반지름(1보다 작으면 꼭지점 그리지 않음)
        private int vertexColor;    //레이더 차트 꼭지점 색(RGB)

        public RadarAttribute(float diameter, float vertexRadius, int vertexColor) {
            this.diameter = diameter;
            this.vertexRadius = vertexRadius;
            this.vertexColor = vertexColor;
        }
    }

    //생성자에서 Paint 설정
    public RadarChartDrawer() {
        scorePaint.setAntiAlias(true);
        scorePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        scorePaint.setColor(Color.argb(128, 0, 0, 255)); // Semi-transparent blue
        scorePaint.setStrokeWidth(2f);

        pointPaint.setAntiAlias(true);
    }

    //레이더 차트 그리기
    public void drawRadarChart(Canvas canvas, List<Integer> scores, RadarAttribute radarAttribute, OffsetPoint centerOffset) {
        if (scores == null || radarAttribute == null || centerOffset == null) {
            throw new IllegalArgumentException("scores, radarAttribute and centerOffset must not be null!!!");
        }

        // 입력된 점수 수 만큼의 다각형(현재 오각형)을 그릴 각도
        int numVertices = scores.size();
        float sliceDegrees = 360.0f / numVertices;

        float radius = radarAttribute.diameter / 2.0f;  // 반지름

        // 원의 중심 좌표
        float centerX = canvas.getWidth() / 2.0f + centerOffset.x;  // 원의 중심 X 좌표
        float centerY = canvas.getHeight() / 2.0f + centerOffset.y;  // 원의 중심 Y 좌표

        // 각 점수 위치에 대한 좌표를 저장할 리스트
        List<PointF> points = new ArrayList<>();

        // 각 점수에 대한 좌표를 계산하고 리스트에 추가
        for (int i = 0; i < numVertices; i++) {
            float score = scores.get(i);
            float normalizedScore = normalizeScore(score);
            float angleDegrees = calculateAngleDegrees(i, numVertices);

            PointF point = calculateScorePoint(centerX, centerY, radius, normalizedScore, angleDegrees);
            points.add(point);
        }

        pointPaint.setColor(radarAttribute.vertexColor);    // 꼭지점 색 지정
        // 다각형과 다각형의 꼭지점 그림
        drawPolygon(canvas, points);
        drawVertexPoints(canvas, points, radarAttribute.vertexRadius);
    }

    // 점수를 0~1 사이의 값으로 정규화 (1~20 사이의 값으로 조정)
    private float normalizeScore(float score) {
        if(score < MIN_SCORE) score = MIN_SCORE;
        else if(score > MAX_SCORE) score = MAX_SCORE;

        return (score - 1) / 19.0f;
    }

    // 다각형의 꼭지점 수에 따라 원을 돌며 각도 계산
    private float calculateAngleDegrees(int index, int totalVertices) {
        float sliceDegrees = 360.0f / totalVertices;
        float angleDegrees = 90 - (index * sliceDegrees);   // 각도를 시계 반대 방향으로 계산
        return (angleDegrees < 0) ? angleDegrees + 360 : angleDegrees;
    }

    // 그려질 다각형 꼭지점 좌표
    private PointF calculateScorePoint(float centerX, float centerY, float radius, float normalizedScore, float angleDegrees) {
        float angleRadians = (float) Math.toRadians(angleDegrees);
        float x = centerX + radius * normalizedScore * (float) Math.cos(angleRadians);
        float y = centerY - radius * normalizedScore * (float) Math.sin(angleRadians);
        return new PointF(x, y);
    }

    // Canvas 위에 다각형 그리기
    private void drawPolygon(Canvas canvas, List<PointF> points) {
        Path path = new Path();
        path.moveTo(points.get(0).x, points.get(0).y);
        for (int i = 1; i < points.size(); i++) {
            path.lineTo(points.get(i).x, points.get(i).y);
        }
        path.close();
        canvas.drawPath(path, scorePaint);
    }

    // 다각형의 꼭지점 그리기(반지름 1 이상이어야 그려짐)
    private void drawVertexPoints(Canvas canvas, List<PointF> points, float radius) {
        if (isDrawVertexPoints) {
            if (radius < 1.0f) {
                return;
            }
            for (PointF point : points) {
                canvas.drawCircle(point.x, point.y, radius, pointPaint);
            }
        }
    }
}
