package mahorad.com.wifeye.ui.custom.box.internet;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.Date;

import io.reactivex.disposables.Disposable;
import mahorad.com.wifeye.R;
import mahorad.com.wifeye.publisher.event.internet.InternetStateChangedEvent;
import mahorad.com.wifeye.publisher.event.internet.RxInternetMonitor;
import mahorad.com.wifeye.publisher.event.service.RxEngineServiceMonitor;
import mahorad.com.wifeye.ui.custom.box.AbstractBoxView;
import timber.log.Timber;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;
import static mahorad.com.wifeye.R.drawable.has_ssid;
import static mahorad.com.wifeye.R.drawable.no_ssid;
import static mahorad.com.wifeye.R.drawable.tower_off;
import static mahorad.com.wifeye.R.drawable.tower_on;
import static mahorad.com.wifeye.data.Persistence.getLatest;
import static mahorad.com.wifeye.engine.state.StateType.Initial;
import static mahorad.com.wifeye.publisher.event.persistence.EventType.Internet;
import static mahorad.com.wifeye.util.Constants.BLANK;
import static mahorad.com.wifeye.util.Utils.toAgo;

/**
 * Created by mahan on 2017-09-07.
 */

public class InternetView extends AbstractBoxView {

    private static final String TAG = InternetView.class.getSimpleName();

    private static final String HEADER = "H O T S P O T";

    private Disposable netDisposable;
    private ImageView stateIcon;

    public InternetView(Context context) {
        super(context);
    }

    public InternetView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InternetView(Context context, AttributeSet attrs, int defStyle) {
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
                .subscribe(this::update);
    }

    private void update(Boolean enabled) {
        updateHotspotImage(enabled);
        updateNetDisposable(enabled);
    }

    private void updateHotspotImage(boolean enabled) {
        int image = enabled
                ? has_ssid
                : no_ssid;
        stateIcon.setImageResource(image);

    }

    private void updateNetDisposable(Boolean enabled) {
        if (enabled)
            netDisposable = internetDisposable();
        else if (netDisposable != null)
            netDisposable.dispose();
    }

    private Disposable internetDisposable() {
        return RxInternetMonitor
                .internetStateChanges(getContext())
                .observeOn(mainThread())
                .doOnError(Timber::e)
                .subscribe(this::refresh);
    }

    @Override
    protected String getHeader() {
        return HEADER;
    }

    @Override
    protected void setupContent() {
        stateIcon = new ImageView(getContext());
        stateIcon.setImageResource(no_ssid);
        setContents(stateIcon);
    }

    @Override
    protected void setupFact() {
        setFact(Initial.title());
    }

    @Override
    protected void setupCaption() {
        setCaption(BLANK);
    }

    @Override
    protected void refreshHeader(Object event) {}

    @Override
    protected void refreshFact(Object event) {
        InternetStateChangedEvent e = (InternetStateChangedEvent) event;
        setFact(e.ssid());
    }

    @Override
    protected void refreshContent(Object event) {
        InternetStateChangedEvent e = (InternetStateChangedEvent) event;
        int icon = e.connected()
                ? has_ssid
                : no_ssid;
        stateIcon.setImageResource(icon);
    }

    @Override
    protected void refreshCaption(Object event) {
        Date latest = getLatest(Internet);
        String date = (latest ==  null)
                ? BLANK
                : toAgo(latest, getContext());
        setCaption(date);
    }
}
