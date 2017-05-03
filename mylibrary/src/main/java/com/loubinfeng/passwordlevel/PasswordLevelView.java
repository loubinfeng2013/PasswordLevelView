package com.loubinfeng.passwordlevel;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by loubinfeng on 2017/5/3.
 */

public class PasswordLevelView extends View {

    //draw paint
    private Paint mPaint;
    //draw path
    private Path mDrawPath;
    //all path
    private Path mAllPath;
    //measure path
    private PathMeasure mPathMeasure;
    //path length
    private float mPathLength;
    //current level
    private int mLevel;
    //draw color array
    private int[] mColorArray;
    //value change
    private ValueAnimator mAesValueAnimator;
    private ValueAnimator mDescValueAnimator;
    private float mMoveP;

    public PasswordLevelView(Context context) {
        this(context, null);
    }

    public PasswordLevelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PasswordLevelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * init work
     */
    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mDrawPath = new Path();
        mAllPath = new Path();
        mColorArray = new int[]{Color.RED, Color.YELLOW, Color.GREEN};
        mAesValueAnimator = ValueAnimator.ofFloat(0, 1);
        mAesValueAnimator.setDuration(800);
        mAesValueAnimator.setInterpolator(new BounceInterpolator());
        mAesValueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mDescValueAnimator = ValueAnimator.ofFloat(1, 0);
        mDescValueAnimator.setDuration(500);
        mDescValueAnimator.setInterpolator(new LinearInterpolator());
        mDescValueAnimator.setRepeatMode(ValueAnimator.REVERSE);
    }

    /**
     * set level color,just get top 3
     *
     * @param colorArray color list
     */
    public void setColorArray(int[] colorArray) {
        if (colorArray.length < 3)
            return;
        this.mColorArray = colorArray;
    }

    /**
     * set current level
     *
     * @param level level
     */
    public void setLevel(final int level) {
        if (this.mLevel == level)
            return;
        if (level == 0) {
            this.mLevel = level;
            invalidate();
            return;
        }
        if (this.mLevel < level)
        {
            mAesValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float value = (Float) valueAnimator.getAnimatedValue();
                    mMoveP = value* mPathLength / 3 + (level - 1) * mPathLength / 3;
                    invalidate();
                }
            });
            mDescValueAnimator.cancel();
            mAesValueAnimator.start();
            mLevel = level;
        }
        else
        {
            mDescValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float value = (Float) valueAnimator.getAnimatedValue();
                    mMoveP = value * mPathLength / 3 + level * mPathLength / 3;
                    invalidate();
                    if (value == 0){
                        mLevel = level;
                    }
                }
            });
            mAesValueAnimator.cancel();
            mDescValueAnimator.start();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPaint.setStrokeWidth(h);
        mAllPath.lineTo(w, 0);
        mPathMeasure = new PathMeasure(mAllPath, false);
        mPathLength = mPathMeasure.getLength();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mLevel == 0) {
            mDrawPath.reset();
            mDrawPath.lineTo(0, 0);
            canvas.drawPath(mDrawPath, mPaint);
        }
        if (mLevel >= 1) {//draw first step
            mDrawPath.reset();
            mDrawPath.lineTo(0, 0);
            mPaint.setColor(mColorArray[0]);
            if (mLevel == 1)
                mPathMeasure.getSegment(0, mMoveP, mDrawPath, true);
            else
                mPathMeasure.getSegment(0, mPathLength / 3, mDrawPath, true);
            canvas.drawPath(mDrawPath, mPaint);
        }
        if (mLevel >= 2) {
            mDrawPath.reset();
            mDrawPath.lineTo(0, 0);
            mPaint.setColor(mColorArray[1]);
            if (mLevel == 2)
                mPathMeasure.getSegment(mPathLength / 3, mMoveP, mDrawPath, true);
            else
                mPathMeasure.getSegment(mPathLength / 3, mPathLength * 2 / 3, mDrawPath, true);
            canvas.drawPath(mDrawPath, mPaint);
        }
        if (mLevel >= 3) {
            mDrawPath.reset();
            mDrawPath.lineTo(0, 0);
            mPaint.setColor(mColorArray[2]);
            mPathMeasure.getSegment(mPathLength * 2 / 3, mMoveP, mDrawPath, true);
            canvas.drawPath(mDrawPath, mPaint);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAesValueAnimator != null){
            mAesValueAnimator.cancel();
            mAesValueAnimator = null;
        }
        if (mDescValueAnimator != null){
            mDescValueAnimator.cancel();
            mDescValueAnimator = null;
        }
        mPaint = null;
        mDrawPath = null;
        mAllPath = null;
        mPathMeasure = null;
        mColorArray = null;
    }
}
