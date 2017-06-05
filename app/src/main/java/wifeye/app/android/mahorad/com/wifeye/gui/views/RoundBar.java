package wifeye.app.android.mahorad.com.wifeye.gui.views;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import wifeye.app.android.mahorad.com.wifeye.R;

public class RoundBar extends View {

    // Properties
    private float progress = 0;
    private float foregroundWidth = getResources().getDimension(R.dimen.default_stroke_width);
    private float backgroundWidth = getResources().getDimension(R.dimen.default_background_stroke_width);
    private int foregroundColor = Color.BLACK;
    private int backgroundColor = Color.GRAY;

    // Object used to draw
    private int startAngle = -90;
    private RectF rectF;
    private Paint backgroundPaint;
    private Paint foregroundPaint;
    private ObjectAnimator objectAnimator;

    /**
     * creates a round bar
     * @param context
     * @param attrs
     */
    public RoundBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        rectF = new RectF();
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundBar, 0, 0);
        //Reading values from the XML layout
        try {
            // Value
            progress = typedArray.getFloat(R.styleable.RoundBar_roundBarProgress, progress);
            // StrokeWidth
            foregroundWidth = typedArray.getDimension(R.styleable.RoundBar_roundBarBgWidth, foregroundWidth);
            backgroundWidth = typedArray.getDimension(R.styleable.RoundBar_roundBarBgWidth, backgroundWidth);
            // Color
            foregroundColor = typedArray.getInt(R.styleable.RoundBar_roundBarFgColor, foregroundColor);
            backgroundColor = typedArray.getInt(R.styleable.RoundBar_roundBarBgColor, backgroundColor);
        } finally {
            typedArray.recycle();
        }

        // Init Background
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(backgroundWidth);

        // Init Foreground
        foregroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        foregroundPaint.setColor(foregroundColor);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        foregroundPaint.setStrokeWidth(foregroundWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawOval(rectF, backgroundPaint);
        float angle = 360 * progress / 100;
        canvas.drawArc(rectF, startAngle, angle, false, foregroundPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        float highStroke = (foregroundWidth > backgroundWidth) ? foregroundWidth : backgroundWidth;
        rectF.set(0 + highStroke / 2, 0 + highStroke / 2, min - highStroke / 2, min - highStroke / 2);
    }

    //region Method Get/Set
    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = (progress<=100) ? progress : 100;
        invalidate();
    }

    public float getForegroundWidth() {
        return foregroundWidth;
    }

    public void setForegroundWidth(float strokeWidth) {
        this.foregroundWidth = strokeWidth;
        foregroundPaint.setStrokeWidth(strokeWidth);
        requestLayout();//Because it should recalculate its bounds
        invalidate();
    }

    public float getBackgroundWidth() {
        return backgroundWidth;
    }

    public void setBackgroundWidth(float backgroundStrokeWidth) {
        this.backgroundWidth = backgroundStrokeWidth;
        backgroundPaint.setStrokeWidth(backgroundStrokeWidth);
        requestLayout();//Because it should recalculate its bounds
        invalidate();
    }

    public int getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(int foregroundColor) {
        this.foregroundColor = foregroundColor;
        foregroundPaint.setColor(foregroundColor);
        invalidate();
        requestLayout();
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        backgroundPaint.setColor(backgroundColor);
        invalidate();
        requestLayout();
    }

    public boolean isAnimating() {
        if (objectAnimator == null)
            return false;
        boolean started = objectAnimator.isStarted();
        boolean running = objectAnimator.isRunning();
        return started && running;
    }
    //endregion

    //region Other Method
    /**
     * Set the progress with an animation.
     * Note that the {@link ObjectAnimator} Class automatically set the progress
     * so don't call the {@link RoundBar#setProgress(float)} directly within this method.
     *
     * @param progress The progress it should animate to it.
     */
    public void setProgressWithAnimation(float progress) {
        setProgressWithAnimation(progress, 1500);
    }

    /**
     * Set the progress with an animation.
     * Note that the {@link ObjectAnimator} Class automatically set the progress
     * so don't call the {@link RoundBar#setProgress(float)} directly within this method.
     *
     * @param progress The progress it should animate to it.
     * @param duration The length of the animation, in milliseconds.
     */
    public void setProgressWithAnimation(float progress, int duration) {
        objectAnimator = ObjectAnimator.ofFloat(this, "progress", progress);
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();
    }

    public void stop() {
        if (objectAnimator == null)
            return;
        objectAnimator.removeAllListeners();
        objectAnimator.end();
        objectAnimator.cancel();
        objectAnimator = null;
    }
    //endregion
}
