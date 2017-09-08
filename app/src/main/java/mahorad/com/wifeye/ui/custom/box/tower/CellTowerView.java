package mahorad.com.wifeye.ui.custom.box.tower;

import android.content.Context;
import android.util.AttributeSet;

import java.util.Date;

import io.reactivex.disposables.Disposable;
import mahorad.com.wifeye.publisher.event.service.RxEngineServiceMonitor;
import mahorad.com.wifeye.publisher.event.tower.CellTowerIdChangedEvent;
import mahorad.com.wifeye.publisher.event.tower.RxCellTowerMonitor;
import mahorad.com.wifeye.ui.custom.box.AbstractBoxView;
import mahorad.com.wifeye.ui.custom.ripple.RippleView;
import timber.log.Timber;

import static android.support.v4.content.ContextCompat.getColor;
import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;
import static mahorad.com.wifeye.R.color.colorGreen;
import static mahorad.com.wifeye.R.color.colorMainBackground;
import static mahorad.com.wifeye.R.drawable.tower_off;
import static mahorad.com.wifeye.R.drawable.tower_on;
import static mahorad.com.wifeye.data.Persistence.getLatest;
import static mahorad.com.wifeye.publisher.event.persistence.EventType.CellTower;
import static mahorad.com.wifeye.util.Constants.BLANK;
import static mahorad.com.wifeye.util.Utils.toAgo;

/**
 * Created by mahan on 2017-09-06.
 */

public class CellTowerView extends AbstractBoxView {

    private static final String TAG = CellTowerView.class.getSimpleName();

    private static final String HEADER = "T O W E R S";

    private RippleView ripple;
    private boolean firstEvent = true;
    private Disposable disposable;
    private int dark = getColor(getContext(), colorMainBackground);
    private int lite = getColor(getContext(), colorGreen);

    public CellTowerView(Context context) {
        super(context);
    }

    public CellTowerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CellTowerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onClicked(Object o) { refreshFact(""); }

    @Override
    public void attachViewDisposables() {
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
        int image = enabled
                ? tower_on
                : tower_off;
        ripple.setImage(image);
    }

    private void handleDisposables(boolean enabled) {
        if (enabled)
            disposable = cellTowerSignalDisposable();
        else if (disposable != null)
            disposable.dispose();
    }

    private Disposable cellTowerSignalDisposable() {
        return RxCellTowerMonitor
                .cellTowerIdChanges(getContext())
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
        ripple = new RippleView(getContext());
        ripple.setImage(tower_off);
        ripple.setCount(3);
        ripple.setInitialRadius(20f);
        ripple.setScale(10.0f);
        ripple.setDuration(2000);
        ripple.setStrokeStyle(0);
        ripple.setBackRippling(false);
        setContents(ripple);
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
        Date latest = getLatest(CellTower);
        String date = (latest ==  null)
                ? BLANK
                : toAgo(latest, getContext());
        setFact(date);
    }

    @Override
    protected void refreshContent(Object event) {
        if (firstEvent) { firstEvent = false; return; }
        CellTowerIdChangedEvent e = (CellTowerIdChangedEvent) event;
        ripple.setStrokeColor(e.known() ? lite : dark);
        ripple.startRippling();
    }

    @Override
    protected void refreshCaption(Object event) {
        CellTowerIdChangedEvent e = (CellTowerIdChangedEvent) event;
        setCaption(e.ctid().equals("") ? BLANK : e.ctid());
    }
}
