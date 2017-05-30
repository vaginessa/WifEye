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
    private String ctid;
    int dark = ContextCompat.getColor(
            getContext(),
            R.color.colorMainBackground);
    int lite = ContextCompat.getColor(
            getContext(),
            R.color.boxAccentBlue);

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
        setContents(ripple);
        setFact("Tower identifier");
        setCaption("Cell Tower ID");
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

    public void refresh() {
        ctid = towerIdPublisher.ctid();
        date = towerIdPublisher.date();
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

    private void onReceivedCellTowerId(String ctid) {
        this.ctid = ctid;
        this.date = towerIdPublisher.date();
        post(this::updateView);
        post(() -> ripple.startRippling());
    }

    private void updateView() {
        this.ctid = towerIdPublisher.ctid();
        this.date = towerIdPublisher.date();
        String ago = utils
                .toAgo(date, getContext());
        setFact(ago);
        setCaption(ctid == null ? "n/a" : ctid);
    }

}
