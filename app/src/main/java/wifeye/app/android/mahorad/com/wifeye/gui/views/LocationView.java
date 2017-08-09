package wifeye.app.android.mahorad.com.wifeye.gui.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import wifeye.app.android.mahorad.com.wifeye.R;
import wifeye.app.android.mahorad.com.wifeye.app.events.LocationEvent;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Location;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;

public class LocationView extends AbstractBoxView {

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
    protected void inject() {
        mainComponent.inject(this);
    }

    @Override
    public void attachViewDisposables() {
        Disposable locationDisposable = Location
                .observable(getContext())
                .subscribe(e -> post(() -> updateView(e)));
        attachDisposable(locationDisposable);
    }

    @Override
    public void reset() {
        setHeader(HEADER);
        setupRipplingImage();
        setContents(ripple);
        setFact("-");
        setCaption("n/a");
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
            if (e.known())
                ripple.setStrokeColor(lite);
            else
                ripple.setStrokeColor(dark);

            post(() -> ripple.startRippling());
            String ago = utils.toAgo(Location.date(), getContext());
            setFact(ago);
            setCaption(e.ctid().equals("") ? "n/a" : e.ctid());
        }
    }
}
