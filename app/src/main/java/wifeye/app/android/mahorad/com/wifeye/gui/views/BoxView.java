package wifeye.app.android.mahorad.com.wifeye.gui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import wifeye.app.android.mahorad.com.wifeye.R;

/*
----------------------
-    H E A D E R     -
----------------------
-                    -
-                    -
-                    -
-   C O N T E N T    -
-                    -
-                    -
-                    -
----------------------
-                    -
-     F A C T S      -
-                    -
----------------------
-   C A P T I O N    -
----------------------
*/
public class BoxView extends RelativeLayout {

    private static final CharSequence DEFAULT_EMPTY_STRING = "";
    private static final int DEFAULT_TEXT_COLOR = Color.parseColor("#C7C7C7");
    private static final int DEFAULT_BACKGROUND_COLOR = Color.parseColor("#041727");
    private static final int DEFAULT_BORDER_COLOR = Color.parseColor("#00889C");
    private static final float DEFAULT_BORDER_RADIUS = 0;
    private static final int DEFAULT_BORDER_WIDTH = 2;
    private static final int DEFAULT_PADDING_SIZE = 20;
    private static final int DEFAULT_SIDE_LENGTH = 150;

    private static final float DEFAULT_LETTER_SPACING = 0.2f;
    private static final float DEFAULT_HEADER_SIZE = 11;
    private static final int DEFAULT_HEADER_COLOR = Color.parseColor("#233E55");

    private static final float DEFAULT_FACT_SIZE = 20;

    private static final float DEFAULT_CAPTION_SIZE = 12;

    /* GENERAL PROPERTIES */
    private int paddingTop = DEFAULT_PADDING_SIZE;
    private int paddingBottom = DEFAULT_PADDING_SIZE;
    private int width;
    private int height;
    private int minLength;
    private boolean isSquare;
    private final GradientDrawable background = new GradientDrawable();
    private int borderColor = DEFAULT_BORDER_COLOR;
    private int borderWidth = DEFAULT_BORDER_WIDTH;
    private float borderRadius = DEFAULT_BORDER_RADIUS;
    private int backgroundColor = DEFAULT_BACKGROUND_COLOR;

    /* HEADER PROPERTIES */
    private TextView header;
    private boolean hasHeader;
    private CharSequence headerText = DEFAULT_EMPTY_STRING;
    private int headerColor = DEFAULT_HEADER_COLOR;
    private float headerSize = DEFAULT_HEADER_SIZE;
    private float headerSpacing = DEFAULT_LETTER_SPACING;

    /* CONTENTS PROPERTIES */
    private ViewStub stub;
    private FrameLayout contentFrame;

    /* FACTS PROPERTIES */
    private TextView fact;
    private boolean hasFact;
    private CharSequence factText = DEFAULT_EMPTY_STRING;
    private int factColor = DEFAULT_TEXT_COLOR;
    private float factSize = DEFAULT_FACT_SIZE;

    /* CAPTION PROPERTIES */
    private TextView caption;
    private boolean hasCaption;
    private CharSequence captionText = DEFAULT_EMPTY_STRING;
    private int captionColor = DEFAULT_TEXT_COLOR;
    private float captionSize = DEFAULT_CAPTION_SIZE;

    /**
     * constructor
     * @param context
     */
    public BoxView(Context context) {
        super(context);
        initializeViews(context);
    }

    /**
     * constructor
     * @param context
     * @param attrs
     */
    public BoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        attrs(attrs, context);
        initializeViews(context);
    }

    /**
     * constructor
     * @param context
     * @param attrs
     * @param defStyle
     */
    public BoxView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        attrs(attrs, context);
        initializeViews(context);
    }

    private void attrs(AttributeSet attrs, Context context) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BoxView, 0, 0);
        try {
            isSquare = array.getBoolean(
                    R.styleable.BoxView_isSquare, false);
            backgroundColor = array.getColor(
                    R.styleable.BoxView_backgroundColor, DEFAULT_BACKGROUND_COLOR);
            borderRadius = array.getFloat(
                    R.styleable.BoxView_borderRadius, DEFAULT_BORDER_RADIUS);
            borderColor = array.getColor(
                    R.styleable.BoxView_borderColor, DEFAULT_BORDER_COLOR);
            borderWidth = array.getInteger(
                    R.styleable.BoxView_borderThickness, DEFAULT_BORDER_WIDTH);
            headerColor = array.getColor(
                    R.styleable.BoxView_headerColor, DEFAULT_HEADER_COLOR);
            headerSpacing = array.getFloat(
                    R.styleable.BoxView_headerSpacing, DEFAULT_LETTER_SPACING);
            factColor = array.getColor(
                    R.styleable.BoxView_factColor, DEFAULT_TEXT_COLOR);
            captionColor = array.getColor(
                    R.styleable.BoxView_captionColor, DEFAULT_TEXT_COLOR);
        } finally {
            array.recycle();
        }
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_box, this);
        setupBoxComponents();
    }

    private void setupBoxComponents() {
        stub = (ViewStub) findViewById(R.id.stub);
        header = (TextView) findViewById(R.id.header);
        fact = (TextView) findViewById(R.id.facts);
        caption = (TextView) findViewById(R.id.caption);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        finishBoxViewInflation();
    }

    private void finishBoxViewInflation() {
        setupBackground();
        setupHeader();
        setupContents();
        setupFacts();
        setupCaption();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = getMeasuredHeight() / 3;
        width = getMeasuredWidth() / 3;

        minLength = (width == 0 || height == 0)
                ? Math.max(width, height)
                : Math.min(width, height);

        scaleTextSizes();
        scaleBoxPadding();

        if (!isSquare) return;
        int minMeasureSpec = getMinMeasureSpec(
                widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(minMeasureSpec, minMeasureSpec);
    }

    private void scaleTextSizes() {
        setHeaderSize(minLength * DEFAULT_HEADER_SIZE / DEFAULT_SIDE_LENGTH);
        setFactSize(minLength * DEFAULT_FACT_SIZE / DEFAULT_SIDE_LENGTH);
        setCaptionSize(minLength * DEFAULT_CAPTION_SIZE / DEFAULT_SIDE_LENGTH);
    }

    private void scaleBoxPadding() {
        paddingTop = (minLength * DEFAULT_PADDING_SIZE / DEFAULT_SIDE_LENGTH);
        paddingBottom = (minLength * DEFAULT_PADDING_SIZE / DEFAULT_SIDE_LENGTH);
        setPadding(
                DEFAULT_PADDING_SIZE,
                paddingTop,
                DEFAULT_PADDING_SIZE,
                paddingBottom);
    }

    private int getMinMeasureSpec(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int minSize = Math.min(widthSize, heightSize);
        return MeasureSpec.makeMeasureSpec(minSize, MeasureSpec.EXACTLY);
    }

    private void setupBackground() {
        setBgColor(backgroundColor);
        setBorderColor(borderColor);
        setBorderWidth(borderWidth);
        setRadius(borderRadius);
        setBackground(background);
    }

    private void setupHeader() {
        setHeaderColor(headerColor);
        setHeaderSize(headerSize);
        setHeaderLetterSpacing(headerSpacing);
        setHasHeader(hasHeader);
        header.setTypeface(null, Typeface.BOLD);
    }

    private void setupContents() {
        stub.setLayoutResource(R.layout.view_place_holder);
        contentFrame = (FrameLayout) stub.inflate();
    }

    private void setupFacts() {
        setFactColor(factColor);
        setFactSize(factSize);
        setHasFact(hasFact);
        fact.setTypeface(null, Typeface.BOLD);
    }

    private void setupCaption() {
        setCaptionColor(captionColor);
        setCaptionSize(captionSize);
        setHasCaption(hasCaption);
    }

    /* BACKGROUND */
    public void setBgColor(int color) {
        backgroundColor = color;
        background.setColor(backgroundColor);
    }

    public void setBorderWidth(int width) {
        borderWidth = width;
        background.setStroke(borderWidth, borderColor);
    }

    public void setBorderColor(int color) {
        borderColor = color;
        background.setStroke(borderWidth, borderColor);
    }

    public void setRadius(float radius) {
        borderRadius = radius;
        background.setCornerRadius(borderRadius);
    }

    // TODO setOnClickListener(listener)

    /* HEADER */
    public void setHeader(CharSequence text) {
        if (text == null || text == "") {
            setHasHeader(false);
            return;
        }
        setHasHeader(true);
        headerText = text;
        header.setText(headerText);
    }

    public void setHeaderColor(int color) {
        headerColor = color;
        header.setTextColor(headerColor);
    }


    public void setHasHeader(boolean value) {
        hasHeader = value;
        header.setVisibility(hasHeader ? VISIBLE : GONE);
        adjustStubPosition(value);
    }

    private void adjustStubPosition(boolean value) {
        LayoutParams params =
                (LayoutParams) stub.getLayoutParams();
        if (value)
            params.addRule(BELOW, R.id.header);
        else
            params.removeRule(BELOW);
    }

    public void setHeaderSize(float size) {
        headerSize = size;
        header.setTextSize(headerSize);
    }

    public void setHeaderLetterSpacing(float spacing) {
        headerSpacing = spacing;
        header.setLetterSpacing(headerSpacing);
    }

    /* CONTENTS */
    public void setContents(View view) {
        if (view == null)
            return;
        clearContents();
        contentFrame.addView(view);
    }

    public void clearContents() {
        contentFrame.removeAllViews();
        contentFrame.removeAllViewsInLayout();
    }

    /* FACT */

    public void setHasFact(boolean value) {
        hasFact = value;
        fact.setVisibility(hasFact ? VISIBLE : GONE);
    }

    public void setFact(CharSequence text) {
        if (text == null || text == "") {
            setHasFact(false);
            return;
        }
        setHasFact(true);
        factText = text;
        int start = text.toString().indexOf(" ");
        int stop = text.length();
        Spannable span = null;
        if (start > 0)
            span = toSpan(factText, start, stop, .6f);
        fact.setText(span == null ? text : span);
    }

    private Spannable toSpan(CharSequence text, int start, int stop, float ratio) {
        Spannable span = new SpannableString(text);
        RelativeSizeSpan relative = new RelativeSizeSpan(ratio);
        int exclusive = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE;
        span.setSpan(relative, start, stop, exclusive);
        return span;
    }

    public void setFactColor(int color) {
        factColor = color;
        fact.setTextColor(factColor);
    }

    public void setFactSize(float size) {
        factSize = size;
        fact.setTextSize(size);
    }

    /* CAPTION */
    public void setCaption(CharSequence text) {
        if (text == null || text == "") {
            setHasCaption(false);
            return;
        }
        setHasCaption(true);
        captionText = text;
        caption.setText(captionText);
    }

    public void setHasCaption(boolean value) {
        hasCaption = value;
        caption.setVisibility(hasCaption ? VISIBLE : GONE);
        adjustFactPosition(value);
    }

    private void adjustFactPosition(boolean value) {
        LayoutParams params =
                (LayoutParams) fact.getLayoutParams();
        if (value)
            params.removeRule(ALIGN_PARENT_BOTTOM);
        else
            params.addRule(ALIGN_PARENT_BOTTOM);
    }

    public void setCaptionColor(int color) {
        captionColor = color;
        caption.setTextColor(captionColor);
    }

    public void setCaptionSize(float size) {
        captionSize = size;
        caption.setTextSize(captionSize);
    }

    public void refresh() {}

}
