package wifeye.app.android.mahorad.com.wifeye.gui.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.Date;

import javax.inject.Inject;

import wifeye.app.android.mahorad.com.wifeye.R;
import wifeye.app.android.mahorad.com.wifeye.app.MainApplication;
import wifeye.app.android.mahorad.com.wifeye.app.constants.Constants;
import wifeye.app.android.mahorad.com.wifeye.app.consumers.IOngoingActionConsumer;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.MainComponent;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Action;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.OngoingActionPublisher;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;
import wifeye.app.android.mahorad.com.wifeye.app.wifi.WifiDevice;

import static android.view.Gravity.CENTER;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Action.DisablingMode;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Action.Halt;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Action.ObserveModeDisabling;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Action.ObserveModeEnabling;

public class ActionView extends BoxView implements IOngoingActionConsumer {

    private static final String HEADER = "A C T I O N";

    @Inject
    OngoingActionPublisher actionPublisher;

    @Inject
    Utilities utils;

    @Inject
    WifiDevice wifiDevice;

    private Action action;
    private Date date;
    private final Shimmer shimmer = new Shimmer();
    private ShimmerTextView shimmerText;
    private CircularProgressBar progressBar;

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

        if (actionPublisher != null)
            actionPublisher.subscribe(this);
        setHeader(HEADER);
        setupContents();
        refresh();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (actionPublisher != null)
            actionPublisher.unsubscribe(this);
    }

    private void setupContents() {
        setupProgressBar();
        setupShimmerText();
        LinearLayout layout = getContentLayout();
        setContents(layout);
        layout.addView(progressBar);
        layout.addView(shimmerText);
    }

    private void setupProgressBar() {
        if (progressBar != null) return;
        progressBar = new CircularProgressBar(getContext(), null);
        progressBar.setColor(ContextCompat.getColor(getContext(), R.color.boxAccentBlue));
        progressBar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorMainBackground));
        float progressBarWidth = getResources().getDimension(R.dimen.progressBarWidth);
        progressBar.setProgressBarWidth(progressBarWidth);
        float backgroundWidth = getResources().getDimension(R.dimen.backgroundProgressBarWidth);
        progressBar.setBackgroundProgressBarWidth(backgroundWidth);
        progressBar.setLayoutParams(getProgressBarLayoutParams());
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

    public void refresh() {
        if (actionPublisher == null)
            return;
        action = actionPublisher.action();
        date = actionPublisher.date();
        post(this::updateView);
    }

    @Override
    public void onActionChanged(Action action) {
        this.action = action;
        date = actionPublisher.date();
        post(this::updateView);
    }

    private void updateView() {
        String ago = utils
                .toAgo(date, getContext());
        setCaption("since ".concat(ago));
        if (action == Halt) {
            shimmerStop();
            progressStop();
        } else {
            shimmerStart();
            progressStart();
        }
    }

    public void shimmerStart() {
        shimmerText.setText(action.title());
        shimmer.start(shimmerText);
        int boxBackground = ContextCompat.getColor(getContext(), R.color.boxBackground);
        shimmerText.setTextColor(boxBackground);
    }

    private void shimmerStop() {
        shimmerText.setText(Action.Halt.title());
        shimmer.cancel();
        int textColor = ContextCompat.getColor(getContext(), R.color.boxInfoTextColor);
        shimmerText.setTextColor(textColor);
    }

    public void progressStart() {
        int progress = (int) wifiDevice.elapsed();
        int total = getTotalDuration();
        int remained = (total - progress) * 2000;
        int percent = (100 * progress) / total;
        progressBar.setProgress(percent);
        progressBar.setProgressWithAnimation(100, remained);
    }

    public void progressStop() {
        progressBar.stop();
        progressBar.setProgress(0);
    }

    private int getTotalDuration() {
        if (action == DisablingMode
                || action == ObserveModeDisabling)
            return Constants.WIFI_DISABLE_TIMEOUT;
        if (action == ObserveModeEnabling)
            return Constants.WIFI_ENABLE_TIMEOUT;
        return 1;
    }

}
