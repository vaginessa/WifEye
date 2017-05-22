package wifeye.app.android.mahorad.com.wifeye.gui.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import wifeye.app.android.mahorad.com.wifeye.MainApplication;
import wifeye.app.android.mahorad.com.wifeye.R;
import wifeye.app.android.mahorad.com.wifeye.constants.Constants;
import wifeye.app.android.mahorad.com.wifeye.consumers.IOngoingActionConsumer;
import wifeye.app.android.mahorad.com.wifeye.publishers.Action;
import wifeye.app.android.mahorad.com.wifeye.publishers.OngoingActionPublisher;
import wifeye.app.android.mahorad.com.wifeye.gui.BoxView;

import static android.view.Gravity.CENTER;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;
import static wifeye.app.android.mahorad.com.wifeye.publishers.Action.DisablingMode;
import static wifeye.app.android.mahorad.com.wifeye.publishers.Action.Halt;
import static wifeye.app.android.mahorad.com.wifeye.publishers.Action.ObserveModeDisabling;
import static wifeye.app.android.mahorad.com.wifeye.publishers.Action.ObserveModeEnabling;

public class ActionView extends BoxView implements IOngoingActionConsumer {

    private static final String HEADER = "A C T I O N";

    @Inject OngoingActionPublisher actionPublisher;

    private Action action;
    private Date date;
    private final Shimmer shimmer = new Shimmer();
    private ShimmerTextView shimmerText;
    private CircularProgressBar progressBar;
    private int progress;

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
        actionPublisher = MainApplication
                .mainComponent()
                .actionPublisher();
        actionPublisher.subscribe(this);
        setHeader(HEADER);
        setupContents();
        refresh();
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
        action = actionPublisher.action();
        date = actionPublisher.date();
        updateView();
    }

    @Override
    public void onActionChanged(Action action) {
        this.action = action;
        date = Calendar.getInstance().getTime();
        updateView();
    }

    private void updateView() {
//        setFact("15:33 Today");
//        setCaption("occurrence date");
        if (action == Halt) {
            shimmerStop();
        } else {
            shimmerStart();
        }
    }

    private void shimmerStop() {
        shimmerText.setText("Stopped");
        shimmer.cancel();
        int textColor = ContextCompat.getColor(getContext(), R.color.boxInfoTextColor);
        shimmerText.setTextColor(textColor);
    }

    public void shimmerStart() {
        shimmerText.setText(action.toString());
        shimmer.start(shimmerText);
        int boxBackground = ContextCompat.getColor(getContext(), R.color.boxBackground);
        shimmerText.setTextColor(boxBackground);
    }

    public void progressStart() {
        int progress = getProgressSeconds();
        int total = getTotalDuration();
        int remained = total - progress;
        int percent = (100 * progress) / total;
        progressBar.setProgress(percent);
        progressBar.setProgressWithAnimation(100, remained);
    }

    private int getTotalDuration() {
        if (action == DisablingMode
                || action == ObserveModeDisabling)
            return Constants.WIFI_DISABLE_TIMEOUT;
        if (action == ObserveModeEnabling)
            return Constants.WIFI_ENABLE_TIMEOUT;
        return 1;
    }

    public int getProgressSeconds() {
        // todo how much timer counted in seconds
        return 0;
    }
}