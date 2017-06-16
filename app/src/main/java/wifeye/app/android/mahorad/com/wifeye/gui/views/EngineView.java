package wifeye.app.android.mahorad.com.wifeye.gui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import wifeye.app.android.mahorad.com.wifeye.R;
import wifeye.app.android.mahorad.com.wifeye.app.events.EngineEvent;
import wifeye.app.android.mahorad.com.wifeye.app.state.Engine;
import wifeye.app.android.mahorad.com.wifeye.app.state.IState.Type;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;

public class EngineView extends AbstractBoxView {

    private static final String HEADER = "E V A L U A T I O N";

    @Inject Utilities utils;
    private ImageView stateIcon;

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
    protected void inject() {
        mainComponent.inject(this);
    }

    @Override
    public void attachViewDisposables() {
        Disposable engineDisposable = Engine
                .observable()
                .subscribe(e -> post(() -> updateView(e)));
        attachDisposable(engineDisposable);
    }

    @Override
    public void reset() {
        setHeader(HEADER);
        setupStateIconView();
        setContents(stateIcon);
        setFact(Type.Initial.title());
        setCaption("n/a");
    }

    private void setupStateIconView() {
        stateIcon = new ImageView(getContext());
        stateIcon.setImageResource(R.drawable.state_initial);
    }

    private void updateView(EngineEvent e) {
        synchronized (this) {
            setFact(e.type().title());
            String ago = utils.toAgo(e.date(), getContext());
            setCaption(ago);
            stateIcon.setImageResource(getIcon(e.type()));
        }
    }

    public int getIcon(Type type) {
        switch (type) {
            case Connected:
                return R.drawable.state_connected;
            case DisConnected:
                return R.drawable.state_disconnected;
            case KnownArea:
                return R.drawable.state_known;
            case RouterArea:
                return R.drawable.state_router;
            case UnknownArea:
                return R.drawable.state_unknown;
            default:
                return R.drawable.state_initial;
        }
    }
}
