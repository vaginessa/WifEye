package wifeye.app.android.mahorad.com.wifeye.gui.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import wifeye.app.android.mahorad.com.wifeye.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class RippleBackground extends RelativeLayout {

    private static final int DEFAULT_RIPPLE_COUNT = 6;
    private static final int DEFAULT_DURATION_TIME = 3000;
    private static final float DEFAULT_SCALE = 6.0f;
    private static final int DEFAULT_FILL_TYPE = 0;

    private int strokeColor = ContextCompat.getColor(getContext(), R.color.rippleColor);
    private float strokeWidth = getResources().getDimension(R.dimen.rippleStrokeWidth);
    private float radius = getResources().getDimension(R.dimen.rippleRadius);
    private int duration = DEFAULT_DURATION_TIME;
    private int count = DEFAULT_RIPPLE_COUNT;
    private int delay = 100;
    private float scale = DEFAULT_SCALE;
    private int type = DEFAULT_FILL_TYPE;
    private final Paint paint = new Paint();
    private boolean isRippling;
    private List<RippleView> ripples = new ArrayList<>();
    private AnimatorSet animatorSet;

    public RippleBackground(Context context) {
        super(context);
    }

    public RippleBackground(Context context, AttributeSet attrs) {
        super(context, attrs);
        attrs(context, attrs);
    }

    public RippleBackground(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        attrs(context, attrs);
    }

    private void attrs(final Context context, final AttributeSet attrs) {
        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RippleBackground, 0, 0);
        try {
            strokeColor = array.getColor(
                    R.styleable.RippleBackground_rb_color, getResources().getColor(R.color.rippleColor));
            strokeWidth = array.getDimension(
                    R.styleable.RippleBackground_rb_strokeWidth, getResources().getDimension(R.dimen.rippleStrokeWidth));
            radius = array.getDimension(
                    R.styleable.RippleBackground_rb_radius, getResources().getDimension(R.dimen.rippleRadius));
            duration = array.getInt(
                    R.styleable.RippleBackground_rb_duration, DEFAULT_DURATION_TIME);
            count = array.getInt(
                    R.styleable.RippleBackground_rb_rippleAmount, DEFAULT_RIPPLE_COUNT);
            scale = array.getFloat(
                    R.styleable.RippleBackground_rb_scale, DEFAULT_SCALE);
            type = array.getInt(
                    R.styleable.RippleBackground_rb_type, DEFAULT_FILL_TYPE);
        } finally {
            array.recycle();
        }
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setStrokeColor(int color) {
        this.strokeColor = color;
    }

    public void setStrokeWidth(float width) {
        this.strokeWidth = width;
    }

    public void setInitialRadius(float radius) {
        this.radius = radius;
    }

    public void setCount(int count) {
        this.count = count;
        setDelay();
    }

    public void setDuration(int duration) {
        this.duration = duration;
        setDelay();
    }

    private void setDelay() {
        delay = duration / count;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void startRippling() {
//        if (isRippling()) return;
        initializeView();
        for (RippleView rippleView : ripples) {
            addView(rippleView, layoutParams());
            rippleView.setVisibility(VISIBLE);
        }
        animatorSet.start();
        isRippling = true;
    }

    private LayoutParams layoutParams() {
        LayoutParams params = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        params.addRule(CENTER_IN_PARENT, TRUE);
        return params;
    }

    public void stopRippling() {
        if (!isRippling())
            return;
        animatorSet.end();
        isRippling = false;
    }

    public boolean isRippling() {
        return isRippling;
    }

    private void initializeView() {
        setupPaint();
        createViews();
        setupAnimatorSet();
    }

    private void setupPaint() {
        paint.setAntiAlias(true);
        if (type == DEFAULT_FILL_TYPE) {
            strokeWidth = 0;
            paint.setStyle(Paint.Style.FILL);
        } else {
            paint.setStyle(Paint.Style.STROKE);
        }
        paint.setColor(strokeColor);
    }

    private void setupAnimatorSet() {
        animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        List<Animator> animators = createAnimators();
        animatorSet.playTogether(animators);
    }

    private void createViews() {
        ripples.clear();
//        removeAllViews();
        for (int index = 0; index < count; index++) {
            ripples.add(new RippleView(getContext()));
        }
    }

    private List<Animator> createAnimators() {
        List<Animator> animators = new ArrayList<>();
        int index = 0;
        for (RippleView ripple : ripples) {
            animators.addAll(getAnimators(ripple, index++));
        }
        return animators;
    }

    private List<ObjectAnimator> getAnimators(final RippleView ripple, final int index) {
        return new ArrayList<ObjectAnimator>() {{
            add(getScaleXAnimator(ripple, index));
            add(getScaleYAnimator(ripple, index));
            add(getAlphaAnimator(ripple, index));
        }};
    }

    private ObjectAnimator getScaleXAnimator(RippleView ripple, int index) {
        final ObjectAnimator animator =
                ObjectAnimator.ofFloat(ripple, "ScaleX", 1.0f, scale);
        setAnimatorProperties(animator, index);
        return animator;
    }

    private ObjectAnimator getScaleYAnimator(RippleView ripple, int index) {
        final ObjectAnimator animator =
                ObjectAnimator.ofFloat(ripple, "ScaleY", 1.0f, scale);
        setAnimatorProperties(animator, index);
        return animator;
    }

    private ObjectAnimator getAlphaAnimator(RippleView ripple, int index) {
        ObjectAnimator animator =
                ObjectAnimator.ofFloat(ripple, "Alpha", 1.0f, 0f);
        setAnimatorProperties(animator, index);
        return animator;
    }

    private void setAnimatorProperties(ObjectAnimator animator, int index) {
//        animator.setRepeatCount(ObjectAnimator.INFINITE);
//        animator.setRepeatMode(ObjectAnimator.RESTART);
        animator.setDuration(duration);
        animator.setStartDelay(index * delay);
    }

    /**
     * The Ripple View
     */
    private class RippleView extends View {

        public RippleView(Context context) {
            super(context);
            this.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawCircle(getWidth()  / 2, getHeight() / 2, radius, paint);
        }
    }
}
