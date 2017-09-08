package mahorad.com.wifeye.ui.custom.box.wifi.state;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.Date;

import io.reactivex.disposables.Disposable;
import mahorad.com.wifeye.publisher.event.service.RxEngineServiceMonitor;
import mahorad.com.wifeye.publisher.event.wifi.RxWifiStateMonitor;
import mahorad.com.wifeye.ui.custom.box.AbstractBoxView;
import timber.log.Timber;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;
import static mahorad.com.wifeye.R.drawable.wifi_off;
import static mahorad.com.wifeye.R.drawable.wifi_on;
import static mahorad.com.wifeye.data.Persistence.getLatest;
import static mahorad.com.wifeye.publisher.event.persistence.EventType.WifiState;
import static mahorad.com.wifeye.util.Constants.BLANK;
import static mahorad.com.wifeye.util.Utils.toAgo;

/**
 * Created by mahan on 2017-09-07.
 */

public class WifiStateView extends AbstractBoxView {

    private static final String TAG = WifiStateView.class.getSimpleName();

    private static final String HEADER = "W I F I";

    private ImageView stateIcon;
    private Disposable disposable;

    public WifiStateView(Context context) {
        super(context);
    }

    public WifiStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WifiStateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onClicked(Object o) {}

    @Override
    protected void attachViewDisposables() {
        attachDisposable(engineServiceDisposable());
    }

    private Disposable engineServiceDisposable() {
        return RxEngineServiceMonitor
                .serviceStateChanges()
                .observeOn(mainThread())
                .doOnError(Timber::e)
                .subscribe(this::onServiceStateChanged);
    }

    private void onServiceStateChanged(Boolean e) {
        updateView(e);
        handleDisposables(e);
    }

    private void updateView(boolean enabled) {
        if (enabled) return;
        stateIcon.setImageResource(wifi_off);
    }

    private void handleDisposables(Boolean enabled) {
        if (enabled)
            disposable = wifiStateDisposable();
        else if (disposable != null)
            disposable.dispose();
    }

    private Disposable wifiStateDisposable() {
        return RxWifiStateMonitor
                .wifiStateChanges(getContext())
                .observeOn(mainThread())
                .doOnError(Timber::e)
                .subscribe(this::refresh);
    }

    /********** SETUP **********/

    @Override
    protected String getHeader() {
        return HEADER;
    }

    @Override
    protected void setupContent() {
        setupStateIconView();
        setContents(stateIcon);
    }

    private void setupStateIconView() {
        stateIcon = new ImageView(getContext());
        stateIcon.setImageResource(wifi_off);
    }

    @Override
    protected void setupFact() {
        setFact(BLANK);
    }

    @Override
    protected void setupCaption() {
        setCaption(BLANK);
    }

    /********** REFRESH **********/

    @Override
    protected void refreshHeader(Object event) {}

    @Override
    protected void refreshFact(Object event) {
        boolean enabled = (Boolean) event;
        setFact(enabled ? "enabled" : "disabled");
    }

    @Override
    protected void refreshContent(Object event) {
        boolean enabled = (Boolean) event;
        stateIcon.setImageResource(enabled ? wifi_on : wifi_off);
    }

    @Override
    protected void refreshCaption(Object event) {
        Date latest = getLatest(WifiState);
        String date = (latest ==  null)
                ? BLANK
                : toAgo(latest, getContext());
        setCaption(date);
    }
}
