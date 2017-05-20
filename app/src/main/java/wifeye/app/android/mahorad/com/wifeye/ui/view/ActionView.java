package wifeye.app.android.mahorad.com.wifeye.ui.view;

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

import wifeye.app.android.mahorad.com.wifeye.R;
import wifeye.app.android.mahorad.com.wifeye.consumers.IOngoingActionConsumer;
import wifeye.app.android.mahorad.com.wifeye.publishers.Action;
import wifeye.app.android.mahorad.com.wifeye.publishers.OngoingActionPublisher;
import wifeye.app.android.mahorad.com.wifeye.ui.BoxView;

import static android.view.Gravity.CENTER;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;

public class ActionView extends BoxView implements IOngoingActionConsumer {

    @Inject OngoingActionPublisher actionPublisher;

    private Action action;
    private Date date;

    private ShimmerTextView shimmerText;
    private CircularProgressBar progressBar;
//    private CircleProgressBar progressBar;
    private static final String HEADER = "ACTIONS";
    private static final String FACT = "fact";
    private static final String CAPTION = "caption";


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
        //        actionPublisher.subscribe(this);
        setupProgressBar();
        setupShimmerText();
        setupContents();
    }

    private void setupContents() {
        LinearLayout layout = getContentLayout();
//        layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.boxAccentYellow));
        setContents(layout);

        layout.addView(progressBar);
        progressBar.setLayoutParams(getProgressBarLayoutParams());
        layout.addView(shimmerText);
        shimmerText.setLayoutParams(getShimmerTextLayoutParams());

        setFact(FACT);
    }

    public void startShimmerText() {
        Shimmer shimmer = new Shimmer();
        shimmer.start(shimmerText);
    }

    public void startProgressBar() {
        int animationDuration = 25000; // 2500ms = 2,5s
        progressBar.setProgress(55);
        progressBar.setProgressWithAnimation(100, animationDuration); // Default duration = 1500ms
    }

    private void setupProgressBar() {
        if (progressBar != null) return;/**/
        progressBar = new CircularProgressBar(getContext(), null);
        progressBar.setColor(ContextCompat.getColor(getContext(), R.color.boxAccentBlue));
        progressBar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorMainBackground));
        progressBar.setProgressBarWidth(getResources().getDimension(R.dimen.progressBarWidth));
        progressBar.setBackgroundProgressBarWidth(getResources().getDimension(R.dimen.backgroundProgressBarWidth));
        progressBar.setProgressWithAnimation(100, 10000); // Default duration = 1500ms

    }

    private void setupShimmerText() {
        if (shimmerText != null) return;
        shimmerText = new ShimmerTextView(getContext());
        shimmerText.setText(shimmerText.getTextSize() + " dp");
        shimmerText.setTextColor(ContextCompat.getColor(getContext(), R.color.colorMainBackground));
        shimmerText.setReflectionColor(ContextCompat.getColor(getContext(), R.color.boxInfoTextColor));
        shimmerText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//        shimmerText.setGravity(Gravity.CENTER);
    }

    @NonNull
    private LinearLayout getContentLayout() {
        LinearLayout layout = new LinearLayout(getContext(), null);
        layout.setOrientation(VERTICAL);
        LayoutParams params = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
//        layout.setGravity(Gravity.CENTER);
        layout.setLayoutParams(params);
        return layout;
    }

    @NonNull
    private LinearLayout.LayoutParams getProgressBarLayoutParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        params.gravity = CENTER;
        params.weight = 1;
        return params;
    }

    @NonNull
    private LinearLayout.LayoutParams getShimmerTextLayoutParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
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

    }

}
