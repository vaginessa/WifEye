package wifeye.app.android.mahorad.com.wifeye.gui.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import wifeye.app.android.mahorad.com.wifeye.R;
import wifeye.app.android.mahorad.com.wifeye.app.MainApplication;
import wifeye.app.android.mahorad.com.wifeye.app.MainService;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.MainComponent;
import wifeye.app.android.mahorad.com.wifeye.app.events.LocationEvent;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Location;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;

import static wifeye.app.android.mahorad.com.wifeye.app.events.LocationEvent.create;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Location.ctid;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Location.date;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Location.known;

public class LocationView extends BoxView {

    public static final String TAG = LocationView.class.getSimpleName();

    private static final String HEADER = "S I G N A L";

    @Inject Utilities utils;
    private RippleView ripple;
    private int dark = ContextCompat.getColor(
            getContext(),
            R.color.colorMainBackground);
    private int lite = ContextCompat.getColor(
            getContext(),
            R.color.colorGreen);
    private Disposable disposable;
    private Disposable serviceDisposable;

    public LocationView(Context context) {
        super(context);
    }

    public LocationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LocationView(Context context, AttributeSet attrs, int defStyle) {
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

        serviceDisposable = MainService
                .observable()
                .subscribe(e -> {
                    if (e) enable();
                    else disable();
                });

        setHeader(HEADER);
        setupContents();
    }

    @Override
    public void enable() {
        disposable = Location
                .observable(getContext())
                .subscribe(e -> post(() -> updateView(e)));
    }

    @Override
    public void disable() {
        if (disposable == null)
            return;
        if (disposable.isDisposed())
            return;
        disposable.dispose();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        disable();
        serviceDisposable.dispose();
    }

    private void setupContents() {
        setupRipplingImage();
        setContents(ripple);
        setFact("Location identifier");
        setCaption("Cell Location ID");
    }

    private void setupRipplingImage() {
        if (ripple != null) return;
        ripple = new RippleView(getContext());
        ripple.setImage(R.drawable.tower);
        ripple.setCount(3);
        ripple.setInitialRadius(20f);
        ripple.setScale(10.0f);
        ripple.setDuration(2000);
        ripple.setStrokeStyle(0);
        ripple.setBackRippling(false);
    }

    private void updateView(LocationEvent e) {
        synchronized (this) {
            Log.d(TAG, "location: " + e.ctid() + " " + e.known());

            post(() -> {
                if (e.known())
                    ripple.setStrokeColor(lite);
                else
                    ripple.setStrokeColor(dark);
                ripple.startRippling();
            });
            String ago = utils.toAgo(
                    e.date(), getContext());
            setFact(ago);
            setCaption(e.ctid().equals("") ? "n/a" : e.ctid());
        }
    }
}
