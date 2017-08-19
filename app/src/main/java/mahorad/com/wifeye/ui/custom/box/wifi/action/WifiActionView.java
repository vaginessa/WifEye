package mahorad.com.wifeye.ui.custom.box.wifi.action;


import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import mahorad.com.wifeye.R;
import mahorad.com.wifeye.engine.wifi.WifiAction;
import mahorad.com.wifeye.publisher.event.wifi.RxWifiActionMonitor;
import mahorad.com.wifeye.publisher.event.wifi.RxWifiActionTimerMonitor;
import mahorad.com.wifeye.ui.custom.box.AbstractBoxView;
import mahorad.com.wifeye.ui.custom.progress.RoundBar;

import static android.view.Gravity.CENTER;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;
import static mahorad.com.wifeye.data.Persistence.getLatest;
import static mahorad.com.wifeye.engine.wifi.WifiAction.DisablingMode;
import static mahorad.com.wifeye.engine.wifi.WifiAction.Halt;
import static mahorad.com.wifeye.engine.wifi.WifiAction.ObserveModeDisabling;
import static mahorad.com.wifeye.engine.wifi.WifiAction.ObserveModeEnabling;
import static mahorad.com.wifeye.publisher.event.persistence.EventType.WifiAction;
import static mahorad.com.wifeye.util.Constants.BLANK;
import static mahorad.com.wifeye.util.Constants.WIFI_DISABLE_TIMEOUT;
import static mahorad.com.wifeye.util.Constants.WIFI_ENABLE_TIMEOUT;
import static mahorad.com.wifeye.util.Utils.toAgo;

/**
 * Created by Mahan Rad on 2017-08-17.
 */

public class WifiActionView extends AbstractBoxView {

    public static final String TAG = WifiActionView.class.getSimpleName();

    private static final String HEADER = "action";

    WifiActionViewModel viewModel;

    private WifiAction wifiAction;

    private LinearLayout layout;
    private final Shimmer shimmer = new Shimmer();
    private ShimmerTextView shimmerText;
    private RoundBar roundBar;

    private int activeTextColor = ContextCompat.getColor(getContext(), R.color.boxActiveTextColor);
    private int mainBackground = ContextCompat.getColor(getContext(), R.color.colorMainBackground);
    private int activeRedColor = ContextCompat.getColor(getContext(), R.color.boxAccentRed);
    private int textColorIdling = ContextCompat.getColor(getContext(), R.color.boxInfoTextColor);

    public WifiActionView(Context context) {
        super(context);
    }

    public WifiActionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WifiActionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void attachViewDisposables() {
        attachDisposable(wifiActionDisposable());
        attachDisposable(actionTimerDisposable());
    }

    @NonNull
    private Disposable actionTimerDisposable() {
        return RxWifiActionTimerMonitor
                .timerTickChanges()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(tick -> {

                });
    }

    @NonNull
    private Disposable wifiActionDisposable() {
        return RxWifiActionMonitor
                .wifiActionChanges()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> {
                    wifiAction = e;
                    refresh(e);
                });
    }

    @Override
    protected void onClick(Object o) {

    }

    /* SETUP */

    @Override
    protected void setupHeader() {
    }

    @Override
    protected void setupContent() {
        if (roundBar != null) return;
        roundBar = new RoundBar(getContext(), null);
        roundBar.setForegroundColor(activeTextColor);
        roundBar.setBackgroundColor(mainBackground);
        float progressBarWidth = getResources().getDimension(R.dimen.progressBarWidth);
        roundBar.setForegroundWidth(progressBarWidth);
        float backgroundWidth = getResources().getDimension(R.dimen.backgroundProgressBarWidth);
        roundBar.setBackgroundWidth(backgroundWidth);
        roundBar.setLayoutParams(getProgressBarLayoutParams());
    }

    @NonNull
    private LinearLayout.LayoutParams getProgressBarLayoutParams() {
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        params.gravity = CENTER;
        params.weight = 1;
        return params;
    }

    @Override
    protected void setupFact() {
        shimmerText = new ShimmerTextView(getContext());
        shimmerText.setText(BLANK);
        shimmerText.setTextColor(mainBackground);
        shimmerText.setReflectionColor(activeTextColor);
        shimmerText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        shimmerText.setTextSize(18);
        shimmerText.setTypeface(null, Typeface.BOLD);
        shimmerText.setLayoutParams(getShimmerTextLayoutParams());
    }

    @NonNull
    private LinearLayout.LayoutParams getShimmerTextLayoutParams() {
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        params.gravity = CENTER;
        return params;
    }

    @Override
    protected void setupCaption() {

    }

    @Override
    protected void setupLayout() {

    }

    /* REFRESH */

    @Override
    protected void refreshHeader(Object event) {
    }

    @Override
    protected void refreshContent(Object event) {
        WifiAction action = (WifiAction) event;

    }

    private void progressStart(int elapsed) {
        int total = getTotalDuration();
        int duration = (total - elapsed) * 1000;
        int percentage = (100 * elapsed) / total;
        roundBar.setForegroundColor(actionColor());
        roundBar.start(percentage, 100, duration);
    }

    private int getTotalDuration() {
        switch (wifiAction) {
            case ObserveModeEnabling:
                return WIFI_ENABLE_TIMEOUT;
            case DisablingMode:
            case ObserveModeDisabling:
                return WIFI_DISABLE_TIMEOUT;
            default:
                return 1;
        }
    }

    public synchronized void progressStop() {
        roundBar.stop();
        roundBar.setProgress(0);
    }


    @Override
    protected void refreshFact(Object event) {
        WifiAction action = (WifiAction) event;
    }

    private void shimmerStart(WifiAction e) {
        shimmerText.setText(e.title());
        shimmerText.setPrimaryColor(mainBackground);
        shimmerText.setTextColor(mainBackground);
        shimmerText.setReflectionColor(actionColor());
        shimmer.start(shimmerText);
    }

    private int actionColor() {
        return (wifiAction == ObserveModeEnabling)
                ? activeTextColor
                : activeRedColor;
    }

    private void shimmerStop() {
        shimmerText.setText(Halt.title());
        shimmerText.setTextColor(textColorIdling);
        shimmer.cancel();
    }

    @Override
    protected void refreshCaption(Object event) {
        String ago = toAgo(getLatest(WifiAction), getContext());
        setCaption("since ".concat(ago));
    }

    /* RESET */

    @Override
    protected void resetHeader() {
        setHeader(HEADER);
    }

    @Override
    protected void resetContent() {
    }

    @Override
    protected void resetFact() {
    }

    @Override
    protected void resetLayout() {
        if (layout != null)
            layout.removeAllViews();
        layout = createLayout();
    }

    @NonNull
    private LinearLayout createLayout() {
        LinearLayout layout = new LinearLayout(getContext(), null);
        layout.setOrientation(VERTICAL);
        layout.setLayoutParams(new LayoutParams(MATCH_PARENT, MATCH_PARENT));
        return layout;
    }

    @Override
    protected void resetCaption() {
        setCaption("n/a");
    }
}
