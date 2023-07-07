package com.planuri.rootonixsmartmirror.util.graphic;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class AnimatedRadarChartDrawer extends RadarChartDrawer {

    private static final int ANIMATION_DURATION = 1000;  // 애니메이션 지속 시간 (밀리초)
    private ValueAnimator animator;
    private ImageView chartImageView;

    public AnimatedRadarChartDrawer(ImageView chartImageView) {
        this.chartImageView = chartImageView;
        this.animator = ValueAnimator.ofFloat(0, 1);  // 애니메이션 시작, 끝 값
        this.animator.setDuration(ANIMATION_DURATION);
        this.animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                chartImageView.invalidate();
            }
        });
    }

    @Override
    public void drawRadarChart(Canvas canvas, List<Integer> scores, RadarAttribute radarAttribute, OffsetPoint centerOffset) {
        if (animator.isStarted()) {
            float animatedFraction = animator.getAnimatedFraction();
            List<Integer> animatedScores = new ArrayList<>();
            for (Integer score : scores) {
                animatedScores.add((int) (score * animatedFraction));
            }
            scores = animatedScores;
        }
        super.drawRadarChart(canvas, scores, radarAttribute, centerOffset);
    }

    public void startAnimation() {
        if (animator.isStarted()) {
            animator.cancel();
        }
        animator.start();
    }
}