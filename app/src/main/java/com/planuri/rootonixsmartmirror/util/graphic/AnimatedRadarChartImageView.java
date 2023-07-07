package com.planuri.rootonixsmartmirror.util.graphic;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import androidx.appcompat.widget.AppCompatImageView;

import java.util.ArrayList;
import java.util.List;

public class AnimatedRadarChartImageView extends AppCompatImageView {
    private static final int ANIMATION_DURATION = 600;

    private RadarChartDrawer chartDrawer;
    private List<Integer> originalScores;
    private RadarChartDrawer.RadarAttribute radarAttribute;
    private RadarChartDrawer.OffsetPoint centerOffset;
    private ValueAnimator animator;

    private boolean isAnimationEnd = false; //애니메이션 종료 후 꼭지점을 그리기 위해
    
    public AnimatedRadarChartImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        chartDrawer = new RadarChartDrawer();
        animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(ANIMATION_DURATION);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());  // 속도가 점점 느려지게

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimationEnd = true;  //애니메이션 종료 플래그 (종료 후 꼭지점을 그리기 위해)
                invalidate();  // 마지막 프레임이 안 그려질 경우가 있으므로 강제로 그리기
            }

            @Override
            public void onAnimationStart(Animator animation) {
                isAnimationEnd = false;  // 애니메이션 시작시 플래그 해제
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (originalScores != null && radarAttribute != null && centerOffset != null) {
            List<Integer> scores = new ArrayList<>(originalScores);
            if (animator.isStarted()) {
                float animatedFraction = animator.getAnimatedFraction();
                for (int i = 0; i < scores.size(); i++) {
                    scores.set(i, (int) (scores.get(i) * animatedFraction));
                }
            }
            if (isAnimationEnd) {
                chartDrawer.setIsDrawVertexPoints(true);
            } else {
                chartDrawer.setIsDrawVertexPoints(false);
            }
            chartDrawer.drawRadarChart(canvas, scores, radarAttribute, centerOffset);
        }
    }

    public void setChartData(List<Integer> scores, RadarChartDrawer.RadarAttribute radarAttribute, RadarChartDrawer.OffsetPoint centerOffset) {
        this.originalScores = scores;
        this.radarAttribute = radarAttribute;
        this.centerOffset = centerOffset;
    }

    public void startAnimation() {
        if (animator.isStarted()) {
            animator.cancel();
        }
        animator.start();
    }
}