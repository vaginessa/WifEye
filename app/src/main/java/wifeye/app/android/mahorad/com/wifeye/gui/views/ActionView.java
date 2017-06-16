package wifeye.app.android.mahorad.com.wifeye.gui.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import wifeye.app.android.mahorad.com.wifeye.R;
import wifeye.app.android.mahorad.com.wifeye.app.events.ActionEvent;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Action;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;

import static android.view.Gravity.CENTER;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;
import static wifeye.app.android.mahorad.com.wifeye.app.constants.Constants.WIFI_DISABLE_TIMEOUT;
import static wifeye.app.android.mahorad.com.wifeye.app.constants.Constants.WIFI_ENABLE_TIMEOUT;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Action.Type.DisablingMode;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Action.Type.Halt;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Action.Type.ObserveModeDisabling;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Action.Type.ObserveModeEnabling;

public class ActionView extends AbstractBoxView {

    public static final String TAG = ActionView.class.getSimpleName();

    private static final String HEADER = "A C T I O N";

    @Inject Action action;
    @Inject Utilities utils;

    private LinearLayout layout;
    private final Shimmer shimmer = new Shimmer();
    private ShimmerTextView shimmerText;
    private RoundBar roundBar;

    private int activeTextColor = ContextCompat.getColor(getContext(), R.color.boxActiveTextColor);
    private int mainBackground = ContextCompat.getColor(getContext(), R.color.colorMainBackground);
    private int activeRedColor = ContextCompat.getColor(getContext(), R.color.boxAccentRed);
    private int textColorIdling = ContextCompat.getColor(getContext(), R.color.boxInfoTextColor);

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
    protected void inject() {
        mainComponent.inject(this);
    }

    @Override
    public void attachViewDisposables() {
        Disposable actionDisposable = Action
                .observable()
                .subscribe(e -> post(() -> updateView(e)));
        attachDisposable(actionDisposable);
    }

    @Override
    public void reset() {
        synchronized (this) {
            setHeader(HEADER);
            setupProgressBar();
            setupShimmerText();
            shimmerStop();
            progressStop();
            if (layout != null)
                layout.removeAllViews();
            layout = createLayout();
            clearContents();
            setContents(layout);
            layout.addView(roundBar);
            layout.addView(shimmerText);
            setCaption("n/a");
        }
    }

    @NonNull
    private LinearLayout createLayout() {
        LinearLayout layout = new LinearLayout(getContext(), null);
        layout.setOrientation(VERTICAL);
        layout.setLayoutParams(new LayoutParams(MATCH_PARENT, MATCH_PARENT));
        return layout;
    }

    private void setupProgressBar() {
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

    private void setupShimmerText() {
        if (shimmerText != null) return;
        shimmerText = new ShimmerTextView(getContext());
        shimmerText.setText("-");
        shimmerText.setTextColor(mainBackground);
        shimmerText.setReflectionColor(activeTextColor);
        shimmerText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        shimmerText.setTextSize(18);
        shimmerText.setTypeface(null, Typeface.BOLD);
        shimmerText.setLayoutParams(getShimmerTextLayoutParams());
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

    private synchronized void updateView(ActionEvent e) {
        synchronized (this) {
            Log.d(TAG, "action: " + e.type());
            String ago = utils.toAgo(e.date(), getContext());
            setCaption("since ".concat(ago));
            if (e.type() == Halt) {
                shimmerStop();
                progressStop();
            } else {
                progressStart(e);
                shimmerStart(e);
            }
        }
    }

    private void shimmerStart(ActionEvent e) {
        shimmerText.setText(e.type().title());
        shimmerText.setPrimaryColor(mainBackground);
        shimmerText.setTextColor(mainBackground);
        shimmerText.setReflectionColor(actionColor(e));
        shimmer.start(shimmerText);
    }

    private int actionColor(ActionEvent e) {
        if (e.type() == ObserveModeEnabling)
            return activeTextColor;
        else
            return activeRedColor;
    }

    private void shimmerStop() {
        shimmerText.setText(Halt.title());
        shimmerText.setTextColor(textColorIdling);
        shimmer.cancel();
    }

    private void progressStart(ActionEvent e) {
        int progress = (int) action.elapsed();
        int total = getTotalDuration(e);
        int duration = (total - progress) * 1000;
        int passed = (100 * progress) / total;
        roundBar.setForegroundColor(actionColor(e));
        roundBar.start(passed, 100, duration);
    }

    public synchronized void progressStop() {
        roundBar.stop();
        roundBar.setProgress(0);
    }

    private int getTotalDuration(ActionEvent e) {
        if (e.type() == DisablingMode
                || e.type() == ObserveModeDisabling)
            return WIFI_DISABLE_TIMEOUT;
        if (e.type() == ObserveModeEnabling)
            return WIFI_ENABLE_TIMEOUT;
        return 1;
    }
}
