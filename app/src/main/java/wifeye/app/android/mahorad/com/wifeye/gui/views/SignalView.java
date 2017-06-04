package wifeye.app.android.mahorad.com.wifeye.gui.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import javax.inject.Inject;

import wifeye.app.android.mahorad.com.wifeye.R;
import wifeye.app.android.mahorad.com.wifeye.app.MainApplication;
import wifeye.app.android.mahorad.com.wifeye.app.consumers.ISignalListener;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.MainComponent;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Signal;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;

public class SignalView extends BoxView implements ISignalListener {

    private static final String HEADER = "S I G N A L";

    @Inject
    Signal signalIdPublisher;

    @Inject
    Utilities utils;

    private String ctid;

    int dark = ContextCompat.getColor(
            getContext(),
            R.color.colorMainBackground);
    int lite = ContextCompat.getColor(
            getContext(),
            R.color.colorGreen);

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
        finishSignalViewInflation();
    }

    private void finishSignalViewInflation() {
        MainComponent mainComponent =
                MainApplication.mainComponent();
        if (mainComponent != null)
                mainComponent.inject(this);

        if (signalIdPublisher != null)
            signalIdPublisher.subscribe(this);
        setHeader(HEADER);
        setupContents();
        refresh();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (signalIdPublisher != null)
            signalIdPublisher.unsubscribe(this);
    }

    private void setupContents() {
        setupRipplingImage();
        setContents(ripple);
        setFact("Signal identifier");
        setCaption("Cell Signal ID");
    }

    private void setupRipplingImage() {
        if (ripple != null) return;
        ripple = new RippleBackground(getContext());
        ripple.setImage(R.drawable.tower);
        ripple.setCount(3);
        ripple.setInitialRadius(20f);
        ripple.setScale(10.0f);
        ripple.setDuration(2000);
        ripple.setStrokeStyle(0);
        ripple.setBackRippling(false);
    }

    @Override
    public void refresh() {
        if (signalIdPublisher == null)
            return;
        this.ctid = signalIdPublisher.ctid();
        post(this::updateView);
    }

    @Override
    public void onReceivedKnownTowerId(String ctid) {
        ripple.setStrokeColor(lite);
        onReceivedCellTowerId(ctid);
    }

    @Override
    public void onReceivedUnknownTowerId(String ctid) {
        ripple.setStrokeColor(dark);
        onReceivedCellTowerId(ctid);
    }

    private synchronized void onReceivedCellTowerId(String ctid) {
        this.ctid = ctid;
        post(this::updateView);
        post(() -> ripple.startRippling());
    }

    private void updateView() {
        String ago = utils.toAgo(
                signalIdPublisher.date(), getContext());
        setFact(ago);
        setCaption(ctid == null ? "n/a" : ctid);
    }
}
