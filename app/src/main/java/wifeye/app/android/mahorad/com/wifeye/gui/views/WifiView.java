package wifeye.app.android.mahorad.com.wifeye.gui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import wifeye.app.android.mahorad.com.wifeye.R;
import wifeye.app.android.mahorad.com.wifeye.app.events.WifiEvent;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Wifi;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;

import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Wifi.State.Enabled;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Wifi.date;

public class WifiView extends AbstractBoxView {

    public static final String TAG = WifiView.class.getSimpleName();

    private static final String HEADER = "W I F I";

    @Inject Utilities utils;
    private ImageView stateIcon;

    public WifiView(Context context) {
        super(context);
    }

    public WifiView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WifiView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void inject() {
        mainComponent.inject(this);
    }

    @Override
    public void attachViewDisposables() {
        Disposable wifiDisposable = Wifi
                .observable(getContext())
                .subscribe(e -> post(() -> updateView(e)));
        attachDisposable(wifiDisposable);
    }

    @Override
    public void reset() {
        setHeader(HEADER);
        setupStateIconView();
        setContents(stateIcon);
        setFact("-");
        setCaption("n/a");
    }

    private void setupStateIconView() {
        stateIcon = new ImageView(getContext());
        stateIcon.setImageResource(R.drawable.wifi_off);
    }

    private void updateView(WifiEvent e) {
        synchronized (this) {
            Log.d(TAG, "wifi: " + e.state());
            boolean enabled = (e.state() == Enabled);
            setFact(enabled ? "enabled" : "disabled");
            String ago = utils.toAgo(Wifi.date(), getContext());
            setCaption(ago);
            int icon = enabled
                    ? R.drawable.wifi_on
                    : R.drawable.wifi_off;
            stateIcon.setImageResource(icon);
        }
    }

}
