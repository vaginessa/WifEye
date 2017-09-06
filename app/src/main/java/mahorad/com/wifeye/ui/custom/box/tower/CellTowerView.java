package mahorad.com.wifeye.ui.custom.box.tower;

import android.content.Context;
import android.util.AttributeSet;

import java.util.Date;

import io.reactivex.disposables.Disposable;
import mahorad.com.wifeye.R;
import mahorad.com.wifeye.publisher.event.service.RxEngineServiceMonitor;
import mahorad.com.wifeye.publisher.event.tower.RxCellTowerMonitor;
import mahorad.com.wifeye.ui.custom.box.AbstractBoxView;
import mahorad.com.wifeye.ui.custom.ripple.RippleView;
import timber.log.Timber;

import static android.support.v4.content.ContextCompat.getColor;
import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;
import static mahorad.com.wifeye.R.color.colorGreen;
import static mahorad.com.wifeye.R.color.colorMainBackground;
import static mahorad.com.wifeye.data.Persistence.getLatest;
import static mahorad.com.wifeye.publisher.event.persistence.EventType.CellTower;
import static mahorad.com.wifeye.util.Constants.BLANK;
import static mahorad.com.wifeye.util.Utils.toAgo;

/**
 * Created by mahan on 2017-09-06.
 */

public class CellTowerView extends AbstractBoxView {

    private static final String TAG = CellTowerView.class.getSimpleName();

    private static final String HEADER = "S I G N A L";

    private RippleView ripple;
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
    public void attachViewDisposables() {
        attachDisposable(cellTowerSignalDisposable());
        attachDisposable(engineServiceDisposable());
    }

    private Disposable engineServiceDisposable() {
        return RxEngineServiceMonitor
                .serviceStateChanges()
                .observeOn(mainThread())
                .doOnError(Timber::e)
                .subscribe(e -> { if (e) refresh(); });
    }

    private Disposable cellTowerSignalDisposable() {
        return RxCellTowerMonitor
                .cellTowerIdChanges(getContext())
                .observeOn(mainThread())
                .doOnError(Timber::e)
                .subscribe(e -> {
                    if (e.known())
                        ripple.setStrokeColor(lite);
                    else
                        ripple.setStrokeColor(dark);
                    ripple.startRippling();
                    setCaption(e.ctid().equals("") ? BLANK : e.ctid());
                    refresh();
                });
    }

    @Override
    protected String getHeader() {
        return HEADER;
    }

    @Override
    protected void setupContent() {
        ripple = new RippleView(getContext());
        ripple.setImage(R.drawable.tower);
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

    @Override
    protected void refreshHeader() {}

    @Override
    protected void refreshFact() {
        Date latest = getLatest(CellTower);
        String fact = (latest ==  null)
                ? BLANK
                : toAgo(latest, getContext());
        setFact(fact);
    }

    @Override
    protected void refreshContent() {}

    @Override
    protected void refreshCaption() {}
}
