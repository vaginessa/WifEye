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
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import wifeye.app.android.mahorad.com.wifeye.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class RippleView extends RelativeLayout {

    private static final int DEFAULT_RIPPLE_COUNT = 6;
    private static final int DEFAULT_DURATION_TIME = 3000;
    private static final float DEFAULT_SCALE = 6.0f;
    private static final int DEFAULT_FILL_TYPE = 0;

    private int strokeColor = ContextCompat.getColor(getContext(), R.color.rippleColor);
    private float strokeWidth = getResources().getDimension(R.dimen.rippleStrokeWidth);
    private int strokeStyle = DEFAULT_FILL_TYPE;

    private float radius = getResources().getDimension(R.dimen.rippleRadius);
    private boolean backRippling = true;
    private int duration = DEFAULT_DURATION_TIME;
    private int count = DEFAULT_RIPPLE_COUNT;
    private float scale = DEFAULT_SCALE;
    private int delay = 100;

    private final Paint paint = new Paint();
    private List<RippleCircle> ripples = new ArrayList<>();
    private List<Animator> animators = new ArrayList<>();
    private AnimatorSet animatorSet;
    private ImageView image = new ImageView(getContext());

    /**
     *
     * @param context
     */
    public RippleView(Context context) {
        super(context);
        initiate();
    }

    /**
     *
     * @param context
     * @param attrs
     */
    public RippleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        attrs(context, attrs);
        initiate();
    }

    /**
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public RippleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        attrs(context, attrs);
        initiate();
    }

    private void attrs(final Context context, final AttributeSet attrs) {
        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RippleView, 0, 0);
        try {
            strokeColor = array.getColor(
                    R.styleable.RippleView_rb_color, ContextCompat.getColor(getContext(), R.color.rippleColor));
            strokeWidth = array.getDimension(
                    R.styleable.RippleView_rb_strokeWidth, getResources().getDimension(R.dimen.rippleStrokeWidth));
            strokeStyle = array.getInt(
                    R.styleable.RippleView_rb_type, DEFAULT_FILL_TYPE);

            radius = array.getDimension(
                    R.styleable.RippleView_rb_radius, getResources().getDimension(R.dimen.rippleRadius));
            duration = array.getInt(
                    R.styleable.RippleView_rb_duration, DEFAULT_DURATION_TIME);
            count = array.getInt(
                    R.styleable.RippleView_rb_rippleAmount, DEFAULT_RIPPLE_COUNT);
            scale = array.getFloat(
                    R.styleable.RippleView_rb_scale, DEFAULT_SCALE);
        } finally {
            array.recycle();
        }
    }

    private void initiate() {
        setupViewPaint();
        initializeView();
    }

    private void setupViewPaint() {
        paint.setAntiAlias(true);
        setStrokeColor(strokeColor);
        setStrokeWidth(strokeWidth);
        setStrokeStyle(strokeStyle);
    }

    // PAINT
    public void setStrokeColor(int color) {
        this.strokeColor = color;
        paint.setColor(color);
    }

    public void setStrokeWidth(float width) {
        this.strokeWidth = width;
        paint.setStrokeWidth(width);
    }

    public void setStrokeStyle(int style) {
        if (style == DEFAULT_FILL_TYPE) {
            paint.setStyle(Paint.Style.FILL);
            setStrokeWidth(0);
        } else {
            paint.setStyle(Paint.Style.STROKE);
        }
    }

    // RIPPLE PROPERTIES
    public void setBackRippling(boolean value) {
        this.backRippling = value;
        initializeView();
    }

    public void setInitialRadius(float radius) {
        this.radius = radius;
    }

    public void setDuration(int duration) {
        this.duration = duration;
        calculateDelay();
    }

    public void setCount(int count) {
        if (this.count == count)
            return;
        this.count = count;
        calculateDelay();
        initializeView();
    }

    private void calculateDelay() {
        delay = duration / count;
    }

    private void initializeView() {
        removeAllChildViews();
        createRippleViews();
        addRipplesAndImage();
        configureAnimators();
    }

    private void removeAllChildViews() {
        ripples.clear();
        removeAllViews();
    }

    private void createRippleViews() {
        for (int index = 0; index < count; index++) {
            RippleCircle ripple = new RippleCircle(getContext());
            ripples.add(ripple);
        }
    }

    private void addRipplesAndImage() {
        if (!backRippling) {
            addView(image, layoutParams());
        }
        for (RippleCircle ripple : ripples) {
            addView(ripple, layoutParams());
        }
        if (backRippling) {
            addView(image, layoutParams());
        }
    }

    private LayoutParams layoutParams() {
        LayoutParams params = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        params.addRule(CENTER_IN_PARENT, TRUE);
        return params;
    }

    public void setScale(float scale) {
        this.scale = scale;
        configureAnimators();
    }

    // ANIMATION
    private void configureAnimators() {
        animators = createRippleAnimators();
        animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.playTogether(animators);
    }

    private List<Animator> createRippleAnimators() {
        List<Animator> animators = new ArrayList<>();
        int index = 0;
        for (RippleCircle ripple : ripples) {
            animators.addAll(getAnimators(ripple, index++));
        }
        return animators;
    }

    private List<ObjectAnimator> getAnimators(
            final RippleCircle ripple, final int index) {
        return new ArrayList<ObjectAnimator>() {{
            add(getScaleXAnimator(ripple, index));
            add(getScaleYAnimator(ripple, index));
            add(getAlphaAnimator(ripple, index));
        }};
    }

    private ObjectAnimator getScaleXAnimator(RippleCircle ripple, int index) {
        final ObjectAnimator animator =
                ObjectAnimator.ofFloat(ripple, "ScaleX", 1.0f, scale);
        setAnimatorProperties(animator, index);
        return animator;
    }

    private ObjectAnimator getScaleYAnimator(RippleCircle ripple, int index) {
        final ObjectAnimator animator =
                ObjectAnimator.ofFloat(ripple, "ScaleY", 1.0f, scale);
        setAnimatorProperties(animator, index);
        return animator;
    }

    private ObjectAnimator getAlphaAnimator(RippleCircle ripple, int index) {
        ObjectAnimator animator =
                ObjectAnimator.ofFloat(ripple, "Alpha", 1.0f, 0f);
        setAnimatorProperties(animator, index);
        return animator;
    }

    private void setAnimatorProperties(ObjectAnimator animator, int index) {
        animator.setDuration(duration);
        animator.setStartDelay(index * delay);
    }

    public void setImage(int drawable) {
        image.setImageResource(drawable);
    }

    /**
     * start rippling
     */
    public void startRippling() {
        for (RippleCircle rippleCircle : ripples) {
            rippleCircle.setVisibility(VISIBLE);
        }
        animatorSet.start();
    }

    /**
     * stop rippling
     */
    public void stopRippling() {
        animatorSet.end();
    }

    /**
     * The Ripple View
     */
    private class RippleCircle extends View {
        public RippleCircle(Context context) {
            super(context);
            this.setVisibility(View.INVISIBLE);
        }
        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawCircle(getWidth()  / 2, getHeight() / 2, radius, paint);
        }
    }
}
