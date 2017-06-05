package wifeye.app.android.mahorad.com.wifeye.gui.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import javax.inject.Inject;

import wifeye.app.android.mahorad.com.wifeye.R;
import wifeye.app.android.mahorad.com.wifeye.app.MainApplication;
import wifeye.app.android.mahorad.com.wifeye.app.constants.Constants;
import wifeye.app.android.mahorad.com.wifeye.app.consumers.IActionListener;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.MainComponent;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Action;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;
import wifeye.app.android.mahorad.com.wifeye.app.wifi.WifiHandler;

import static android.view.Gravity.CENTER;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Action.Type.DisablingMode;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Action.Type.Halt;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Action.Type.ObserveModeDisabling;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Action.Type.ObserveModeEnabling;

public class ActionView extends BoxView implements IActionListener {

    private static final String HEADER = "A C T I O N";

    @Inject Action action;
    @Inject Utilities utils;
    @Inject WifiHandler wifiHandler;

    private final Shimmer shimmer = new Shimmer();
    private ShimmerTextView shimmerText;
    private RoundBar roundBar;

    public ActionView(Context context) {
        super(context);
    }

    public ActionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ActionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        finishActionViewInflation();
    }

    private void finishActionViewInflation() {
        MainComponent mainComponent =
                MainApplication.mainComponent();
        if (mainComponent != null)
            mainComponent.inject(this);

        if (action != null)
            action.subscribe(this);
        setHeader(HEADER);
        setupContents();
        refresh();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (action != null)
            action.unsubscribe(this);
    }

    private void setupContents() {
        setupProgressBar();
        setupShimmerText();
        LinearLayout layout = getContentLayout();
        setContents(layout);
        layout.addView(roundBar);
        layout.addView(shimmerText);
    }

    private void setupProgressBar() {
        if (roundBar != null) return;
        roundBar = new RoundBar(getContext(), null);
        roundBar.setForegroundColor(ContextCompat.getColor(getContext(), R.color.boxActiveTextColor));
        roundBar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorMainBackground));
        float progressBarWidth = getResources().getDimension(R.dimen.progressBarWidth);
        roundBar.setForegroundWidth(progressBarWidth);
        float backgroundWidth = getResources().getDimension(R.dimen.backgroundProgressBarWidth);
        roundBar.setBackgroundWidth(backgroundWidth);
        roundBar.setLayoutParams(getProgressBarLayoutParams());
    }

    private void setupShimmerText() {
        if (shimmerText != null) return;
        shimmerText = new ShimmerTextView(getContext());
        int reflectColor = ContextCompat.getColor(getContext(), R.color.boxInfoTextColor);
        shimmerText.setReflectionColor(reflectColor);
        shimmerText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        shimmerText.setTextSize(20);
        shimmerText.setLayoutParams(getShimmerTextLayoutParams());
    }

    @NonNull
    private LinearLayout getContentLayout() {
        LinearLayout layout = new LinearLayout(getContext(), null);
        layout.setOrientation(VERTICAL);
        layout.setLayoutParams(new LayoutParams(MATCH_PARENT, MATCH_PARENT));
        return layout;
    }

    @NonNull
    private LinearLayout.LayoutParams getProgressBarLayoutParams() {
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        params.gravity = CENTER;
        params.weight = 1;
        return params;
    }

    @NonNull
    private LinearLayout.LayoutParams getShimmerTextLayoutParams() {
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        params.gravity = CENTER;
        return params;
    }

    @Override
    public void refresh() {
        if (action == null)
            return;
        post(this::updateView);
    }

    @Override
    public synchronized void onActionChanged(Action.Type type) {
        post(this::updateView);
    }

    private synchronized void updateView() {
        String ago = utils.toAgo(
                Action.date(), getContext());
        setCaption("since ".concat(ago));
        if (Action.type() == Halt) {
            shimmerStop();
            progressStop();
        } else {
            shimmerStart();
            progressStart();
        }
    }

    public synchronized void shimmerStart() {
        shimmerText.setText(Action.type().title());
        shimmer.start(shimmerText);
        int boxBackground = ContextCompat.getColor(getContext(), R.color.boxBackground);
        shimmerText.setTextColor(boxBackground);
    }

    private synchronized void shimmerStop() {
        shimmerText.setText(Halt.title());
        shimmer.cancel();
        int textColor = ContextCompat.getColor(getContext(), R.color.boxInfoTextColor);
        shimmerText.setTextColor(textColor);
    }

    public synchronized void progressStart() {
        int progress = (int) wifiHandler.elapsed();
        int total = getTotalDuration();
        int remained = (total - progress) * 1000;
        int percent = (100 * progress) / total;
        roundBar.setProgress(percent);
        roundBar.setProgressWithAnimation(100, remained);
    }

    public synchronized void progressStop() {
        roundBar.stop();
        roundBar.setProgress(0);
    }

    private int getTotalDuration() {
        if (Action.type() == DisablingMode
                || Action.type() == ObserveModeDisabling)
            return Constants.WIFI_DISABLE_TIMEOUT;
        if (Action.type() == ObserveModeEnabling)
            return Constants.WIFI_ENABLE_TIMEOUT;
        return 1;
    }
}
