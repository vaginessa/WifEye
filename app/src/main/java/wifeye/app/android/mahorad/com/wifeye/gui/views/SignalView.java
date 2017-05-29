package wifeye.app.android.mahorad.com.wifeye.gui.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import java.util.Date;

import javax.inject.Inject;

import wifeye.app.android.mahorad.com.wifeye.R;
import wifeye.app.android.mahorad.com.wifeye.app.MainApplication;
import wifeye.app.android.mahorad.com.wifeye.app.consumers.ICellTowerIdConsumer;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.CellTowerIdPublisher;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;

public class SignalView extends BoxView implements ICellTowerIdConsumer {

    private static final String HEADER = "S I G N A L";

    @Inject
    CellTowerIdPublisher towerIdPublisher;

    @Inject
    Utilities utils;

    private Date date;

    private RippleBackground ripple;

    public SignalView(Context context) {
        super(context);
    }

    public SignalView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SignalView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        MainApplication
                .mainComponent()
                .inject(this);

        towerIdPublisher.subscribe(this);
        setHeader(HEADER);
        setupContents();
        refresh();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        towerIdPublisher.unsubscribe(this);
    }

    private void setupContents() {
        setupRipplingImage();
//        LinearLayout layout = getContentLayout();
        setContents(ripple);
//        layout.addView(ripple);
        setCaption("Cell Tower ID");
    }

    private void setupRipplingImage() {
        if (ripple != null) return;
        ripple = new RippleBackground(getContext());
        ripple.setStrokeColor(ContextCompat.getColor(getContext(), R.color.colorMainBackground));
        ripple.setCount(3);
        ripple.setInitialRadius(50f);
        ripple.setScale(5.0f);
        ripple.setDuration(2000);
        ripple.setType(0);

        setOnClickListener((d) -> {
            ripple.startRippling();
        });
//        ripple.setLayoutParams(getRippleLayoutParams());
    }

//    private void setupProgressBar() {
//        if (progressBar != null) return;
//        progressBar = new CircularProgressBar(getContext(), null);
//        progressBar.setColor(ContextCompat.getColor(getContext(), R.color.boxAccentBlue));
//        progressBar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorMainBackground));
//        float progressBarWidth = getResources().getDimension(R.dimen.progressBarWidth);
//        progressBar.setProgressBarWidth(progressBarWidth);
//        float backgroundWidth = getResources().getDimension(R.dimen.backgroundProgressBarWidth);
//        progressBar.setBackgroundProgressBarWidth(backgroundWidth);
//        progressBar.setLayoutParams(getProgressBarLayoutParams());
//    }

//    @NonNull
//    private LinearLayout getContentLayout() {
//        LinearLayout layout = new LinearLayout(getContext(), null);
//        layout.setOrientation(VERTICAL);
//        layout.setLayoutParams(new LayoutParams(MATCH_PARENT, MATCH_PARENT));
//        return layout;
//    }

//    @NonNull
//    private LinearLayout.LayoutParams getRippleLayoutParams() {
//        LinearLayout.LayoutParams params =
//                new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
//        params.gravity = CENTER;
//        params.weight = 1;
//        return params;
//    }

    public void refresh() {
//        action = towerIdPublisher.action();
//        date = towerIdPublisher.date();
        post(this::updateView);
    }

    @Override
    public void onReceivedKnownTowerId(String ctid) {

    }

    @Override
    public void onReceivedUnknownTowerId(String ctid) {

    }

    private void updateView() {
//        String ago = utils
//                .toAgo(date, getContext());
//        setCaption(ago);
//        if (action == Halt) {
//            shimmerStop();
//            progressStop();
//        } else {
//            shimmerStart();
//            progressStart();
//        }
    }

}
