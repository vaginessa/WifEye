package mahorad.com.wifeye.ui.custom.box.engine;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.Date;

import io.reactivex.disposables.Disposable;
import mahorad.com.wifeye.R;
import mahorad.com.wifeye.di.qualifier.engine.InitialState;
import mahorad.com.wifeye.engine.state.StateType;
import mahorad.com.wifeye.publisher.event.engine.RxEngineStateMonitor;
import mahorad.com.wifeye.publisher.event.service.RxEngineServiceMonitor;
import mahorad.com.wifeye.ui.custom.box.AbstractBoxView;
import timber.log.Timber;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;
import static mahorad.com.wifeye.R.drawable.has_ssid;
import static mahorad.com.wifeye.R.drawable.no_ssid;
import static mahorad.com.wifeye.R.drawable.state_initial;
import static mahorad.com.wifeye.R.drawable.tower_off;
import static mahorad.com.wifeye.R.drawable.tower_on;
import static mahorad.com.wifeye.data.Persistence.getLatest;
import static mahorad.com.wifeye.engine.state.StateType.Initial;
import static mahorad.com.wifeye.publisher.event.persistence.EventType.EngineState;
import static mahorad.com.wifeye.util.Constants.BLANK;
import static mahorad.com.wifeye.util.Utils.toAgo;

/**
 * Created by mahan on 2017-09-07.
 */

public class EngineView extends AbstractBoxView {

    private static final String TAG = EngineView.class.getSimpleName();

    private static final String HEADER = "E V A L U A T I O N";

    private ImageView stateIcon;
    private Disposable disposable;

    public EngineView(Context context) {
        super(context);
    }

    public EngineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EngineView(Context context, AttributeSet attrs, int defStyle) {
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

    private void update(Boolean e) {
        updateStateIcon(e);
        updateDisposable(e);
    }

    private void updateStateIcon(boolean enabled) {
        if (enabled) return;
        stateIcon.setImageResource(state_initial);
        setFact(Initial.title());
    }

    private void updateDisposable(Boolean enabled) {
        if (enabled)
            disposable = engineDisposable();
        else if (disposable != null)
            disposable.dispose();
    }

    private Disposable engineDisposable() {
        return RxEngineStateMonitor
                .engineStateChanges()
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
        stateIcon.setImageResource(state_initial);
    }

    @Override
    protected void setupFact() {
        setFact(Initial.title());
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
        StateType e = (StateType) event;
        setFact(e.title());
    }

    @Override
    protected void refreshContent(Object event) {
        StateType e = (StateType) event;
        stateIcon.setImageResource(getIcon(e));
    }

    public int getIcon(StateType type) {
        switch (type) {
            case Connected:
                return R.drawable.state_connected;
            case Disconnected:
                return R.drawable.state_disconnected;
            case NearbyArea:
                return R.drawable.state_nearby;
            case CloseRange:
                return R.drawable.state_router;
            case RemoteArea:
                return R.drawable.state_remote;
            default:
                return state_initial;
        }
    }

    @Override
    protected void refreshCaption(Object event) {
        Date latest = getLatest(EngineState);
        String date = (latest ==  null)
                ? BLANK
                : toAgo(latest, getContext());
        setCaption(date);
    }
}
