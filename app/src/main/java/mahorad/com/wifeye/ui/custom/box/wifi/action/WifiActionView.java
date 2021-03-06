package mahorad.com.wifeye.ui.custom.box.wifi.action;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.Date;

import io.reactivex.disposables.Disposable;
import mahorad.com.wifeye.R;
import mahorad.com.wifeye.engine.wifi.WifiAction;
import mahorad.com.wifeye.publisher.event.wifi.RxWifiActionMonitor;
import mahorad.com.wifeye.publisher.event.wifi.RxWifiActionTimerMonitor;
import mahorad.com.wifeye.ui.custom.box.AbstractBoxView;
import mahorad.com.wifeye.ui.custom.progress.CircleProgressBar;
import timber.log.Timber;

import static android.graphics.Typeface.BOLD;
import static android.support.v4.content.ContextCompat.getColor;
import static android.view.Gravity.CENTER;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;
import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;
import static mahorad.com.wifeye.data.Persistence.getLatest;
import static mahorad.com.wifeye.engine.wifi.WifiAction.Halt;
import static mahorad.com.wifeye.engine.wifi.WifiAction.ObserveModeEnabling;
import static mahorad.com.wifeye.publisher.event.persistence.EventType.WifiAction;
import static mahorad.com.wifeye.util.Constants.BLANK;
import static mahorad.com.wifeye.util.Constants.WIFI_DISABLE_TIMEOUT;
import static mahorad.com.wifeye.util.Constants.WIFI_ENABLE_TIMEOUT;
import static mahorad.com.wifeye.util.Constants.WIFI_HALT_DURATION;
import static mahorad.com.wifeye.util.Utils.toAgo;

/**
 * Created by Mahan Rad on 2017-08-17.
 */

public class WifiActionView extends AbstractBoxView {

    public static final String TAG = WifiActionView.class.getSimpleName();

    private static final String HEADER = "A C T I O N";

    private LinearLayout contentsLayout;
    private final Shimmer shimmer = new Shimmer();
    private ShimmerTextView shimmerText;
    private CircleProgressBar progressBar;

    private int actionDuration;
    private int actionFillColor;
    private String actionTitle;

    private int activeTextColor = getColor(getContext(), R.color.boxActiveTextColor);
    private int mainBackground = getColor(getContext(), R.color.colorMainBackground);
    private int activeRedColor = getColor(getContext(), R.color.boxAccentRed);
    private int textColorIdling = getColor(getContext(), R.color.boxInfoTextColor);

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
    protected void onClicked(Object o) {
        refreshCaption("");
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
                .observeOn(mainThread())
                .doOnError(Timber::e)
                .subscribe(l -> setProgress(l + 2));
    }

    @NonNull
    private Disposable wifiActionDisposable() {
        return RxWifiActionMonitor
                .wifiActionChanges()
                .observeOn(mainThread())
                .doOnError(Timber::e)
                .subscribe(e -> {
                    Timber.tag(TAG).v("received action %s", e);
                    refresh(e);
                });
    }

    /********** SETUP **********/

    @Override
    protected String getHeader() {
        return HEADER;
    }

    @Override
    protected void setupContent() {
        contentsLayout = createContentsLayout();
        setContents(contentsLayout);
        setupProgressBar();
        setupShimmerText();
        contentsLayout.addView(progressBar);
        contentsLayout.addView(shimmerText);
    }

    private void setupProgressBar() {
        if (progressBar != null) return;
        progressBar = new CircleProgressBar(getContext(), null);
        progressBar.setBackgroundColor(mainBackground);
        float progressBarWidth = getResources().getDimension(R.dimen.progressBarWidth);
        progressBar.setMinimumWidth((int) progressBarWidth);
        progressBar.setStrokeWidth((int) progressBarWidth);
        progressBar.setLayoutParams(getProgressBarLayoutParams());
    }

    @NonNull
    private LinearLayout.LayoutParams getProgressBarLayoutParams() {
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        params.gravity = CENTER;
        params.weight = 1;
        return params;
    }

    private void setupShimmerText() {
        if (shimmerText != null) return;
        shimmerText = new ShimmerTextView(getContext());
        shimmerText.setText(BLANK);
        shimmerText.setTextColor(mainBackground);
        shimmerText.setReflectionColor(activeTextColor);
        shimmerText.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        shimmerText.setTextSize(18);
        shimmerText.setTypeface(null, BOLD);
        shimmerText.setLayoutParams(getShimmerTextLayoutParams());
    }

    @NonNull
    private LinearLayout createContentsLayout() {
        LinearLayout layout = new LinearLayout(getContext(), null);
        layout.setOrientation(VERTICAL);
        layout.setLayoutParams(new LayoutParams(MATCH_PARENT, MATCH_PARENT));
        return layout;
    }

    @NonNull
    private LinearLayout.LayoutParams getShimmerTextLayoutParams() {
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        params.gravity = CENTER;
        return params;
    }

    @Override
    protected void setupFact() {}

    @Override
    protected void setupCaption() {
        setCaption(BLANK);
    }

    /********** REFRESH **********/

    @Override
    protected void refreshHeader(Object event) { }

    @Override
    protected void refreshContent(Object event) {
        WifiAction action = (WifiAction) event;
        actionDuration = actionDuration(action);
        actionFillColor = actionFillColor(action);
        actionTitle = action.title();
        shimmerText.setText(action.title());
        if (action == Halt) {
            stopProgressBar();
            stopShimmerText();
        } else {
            setProgress(1L);
            startShimmerText();
        }
    }

    private int actionFillColor(WifiAction action) {
        return (action == ObserveModeEnabling)
                ? activeTextColor
                : activeRedColor;
    }

    public void stopProgressBar() {
        Timber.tag(TAG).v("stopping progress bar");
        progressBar.setProgressWithAnimation(0);
        progressBar.clearAnimation();
        progressBar.animate().cancel();
    }

    private void setProgress(Long passed) {
        progressBar.setStrokeColor(actionFillColor);
        int progress = calculateProgress(passed);
        progressBar.setProgressWithAnimation(progress);
    }

    private int calculateProgress(long passed) {
        if (actionDuration == WIFI_HALT_DURATION) return 0;
        int progress = (100 * (int) passed) / actionDuration;
        Timber.tag(TAG).v("--> passed %d, progress %d", passed, progress);
        return progress;
    }

    private void stopShimmerText() {
        if (!shimmerText.isShimmering()) return;
        Timber.tag(TAG).v("stopping shimmer text");
        shimmerText.setTextColor(textColorIdling);
        shimmer.cancel();
    }

    private int actionDuration(WifiAction action) {
        switch (action) {
            case ObserveModeEnabling:
                return WIFI_ENABLE_TIMEOUT;
            case DisablingMode:
            case ObserveModeDisabling:
                return WIFI_DISABLE_TIMEOUT;
            default:
                return WIFI_HALT_DURATION;
        }
    }

    private void startShimmerText() {
        Timber.tag(TAG).v("starting shimmer text");
        shimmerText.setText(actionTitle);
        shimmerText.setPrimaryColor(mainBackground);
        shimmerText.setTextColor(mainBackground);
        shimmerText.setReflectionColor(actionFillColor);
        shimmer.start(shimmerText);
    }

    @Override
    protected void refreshFact(Object event) {}

    @Override
    protected void refreshCaption(Object event) {
        Date latest = getLatest(WifiAction);
        String date = (latest ==  null)
                ? BLANK
                : "since ".concat(toAgo(latest, getContext()));
        setCaption(date);
    }

}
