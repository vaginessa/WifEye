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
import mahorad.com.wifeye.ui.custom.progress.RoundBar;
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
import static mahorad.com.wifeye.util.Utils.toAgo;

/**
 * Created by Mahan Rad on 2017-08-17.
 */

public class WifiActionView extends AbstractBoxView {

    public static final String TAG = WifiActionView.class.getSimpleName();

    private static final String HEADER = "A C T I O N";

    private WifiAction action = Halt;
    private LinearLayout contentsLayout;
    private final Shimmer shimmer = new Shimmer();
    private ShimmerTextView shimmerText;
    private RoundBar progressBar;

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
                .subscribe(this::startProgressBar);
    }

    @NonNull
    private Disposable wifiActionDisposable() {
        return RxWifiActionMonitor
                .wifiActionChanges()
                .observeOn(mainThread())
                .doOnError(Timber::e)
                .subscribe(e -> {
                    action = e;
                    refresh();
                });
    }

    @Override
    protected void onClick(Object o) {
        stopProgressBar();
    }

    /********** SETUP **********/

    @Override
    protected void setupHeader() {
        setHeader(HEADER);
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
        progressBar = new RoundBar(getContext(), null);
        progressBar.setForegroundColor(activeTextColor);
        progressBar.setBackgroundColor(mainBackground);
        float progressBarWidth = getResources().getDimension(R.dimen.progressBarWidth);
        progressBar.setForegroundWidth(progressBarWidth);
        float backgroundWidth = getResources().getDimension(R.dimen.backgroundProgressBarWidth);
        progressBar.setBackgroundWidth(backgroundWidth);
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
    protected void refreshHeader() {
        setHeader(HEADER);
    }

    @Override
    protected void refreshContent() {
        shimmerText.setText(action.title());
        if (action == Halt) {
            stopProgressBar();
            stopShimmerText();
        } else {
            startShimmerText();
        }
    }

    public void stopProgressBar() {
        Timber.tag(TAG).v("stopping progress bar");
        progressBar.stop();
        progressBar.clearAnimation();
        progressBar.animate().cancel();
        progressBar.setProgress(0);
    }

    private void stopShimmerText() {
        Timber.tag(TAG).v("stopping shimmer text");
        shimmerText.setTextColor(textColorIdling);
        shimmer.cancel();
    }

    private void startProgressBar(Long passed) {
        if (progressBar.isRunning()) return;
        int elapsed = passed.intValue();
        int total = getTotalActionDuration();
        int remainder = (total - elapsed) * 1000;
        int percentage = (100 * elapsed) / total;
        progressBar.setForegroundColor(actionColor());
        Timber.tag(TAG).w("starting progress bar");
        progressBar.start(percentage, 100, remainder);
    }

    private int getTotalActionDuration() {
        switch (action) {
            case ObserveModeEnabling:
                return WIFI_ENABLE_TIMEOUT;
            case DisablingMode:
            case ObserveModeDisabling:
                return WIFI_DISABLE_TIMEOUT;
            default:
                return 1;
        }
    }

    private void startShimmerText() {
        shimmerText.setText(action.title());
        shimmerText.setPrimaryColor(mainBackground);
        shimmerText.setTextColor(mainBackground);
        shimmerText.setReflectionColor(actionColor());
        shimmer.start(shimmerText);
    }

    private int actionColor() {
        return (action == ObserveModeEnabling)
                ? activeTextColor
                : activeRedColor;
    }

    @Override
    protected void refreshFact() {}

    @Override
    protected void refreshCaption() {
        Date latest = getLatest(WifiAction);
        if (latest == null)
            setCaption(BLANK);
        else {
            String ago = toAgo(latest, getContext());
            setCaption("since ".concat(ago));
        }
    }

}
