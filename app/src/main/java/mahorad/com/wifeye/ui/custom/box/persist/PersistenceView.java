package mahorad.com.wifeye.ui.custom.box.persist;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import io.reactivex.disposables.Disposable;
import mahorad.com.wifeye.publisher.event.persistence.PersistenceChangedEvent;
import mahorad.com.wifeye.publisher.event.persistence.RxPersistenceMonitor;
import mahorad.com.wifeye.publisher.event.service.RxEngineServiceMonitor;
import mahorad.com.wifeye.ui.custom.box.AbstractBoxView;
import timber.log.Timber;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;
import static java.lang.String.format;
import static mahorad.com.wifeye.R.drawable.disk_off;
import static mahorad.com.wifeye.R.drawable.disk_on;
import static mahorad.com.wifeye.data.Persistence.towersOf;
import static mahorad.com.wifeye.util.Constants.BLANK;

/**
 * Created by mahan on 2017-09-07.
 */

public class PersistenceView extends AbstractBoxView {

    private static final String TAG = PersistenceView.class.getSimpleName();

    private static final String HEADER = "P E R S I S T E N C E";

    private ImageView stateIcon;
    private Disposable disposable;

    public PersistenceView(Context context) {
        super(context);
    }

    public PersistenceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PersistenceView(Context context, AttributeSet attrs, int defStyle) {
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
        if (enabled)
            stateIcon.setImageResource(disk_on);
        else
            stateIcon.setImageResource(disk_off);
    }

    private void handleDisposables(Boolean enabled) {
        if (enabled)
            disposable = persistenceDisposable();
        else if (disposable != null)
            disposable.dispose();
    }

    private Disposable persistenceDisposable() {
        return RxPersistenceMonitor
                .persistenceChanges()
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
        stateIcon.setImageResource(disk_off);
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
        PersistenceChangedEvent e = (PersistenceChangedEvent) event;
        int towers = towersOf(e.ssid()).size();
        setFact(format("%d-towers (%s)", towers, e.ssid()));
    }

    @Override
    protected void refreshContent(Object event) {}

    @Override
    protected void refreshCaption(Object event) {
        PersistenceChangedEvent e = (PersistenceChangedEvent) event;
        setCaption(e.ctid());
    }
}
