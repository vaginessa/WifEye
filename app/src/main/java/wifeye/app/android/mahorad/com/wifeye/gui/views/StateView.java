package wifeye.app.android.mahorad.com.wifeye.gui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import javax.inject.Inject;

import wifeye.app.android.mahorad.com.wifeye.R;
import wifeye.app.android.mahorad.com.wifeye.app.MainApplication;
import wifeye.app.android.mahorad.com.wifeye.app.consumers.ISystemStateConsumer;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.MainComponent;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.SystemStatePublisher;
import wifeye.app.android.mahorad.com.wifeye.app.state.IState;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;

public class StateView extends BoxView implements ISystemStateConsumer {

    private static final String HEADER = "S I G N A L";

    @Inject
    SystemStatePublisher statePublisher;

    @Inject
    Utilities utils;

    private IState.Type state;
    private ImageView stateIcon;

    public StateView(Context context) {
        super(context);
    }

    public StateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StateView(Context context, AttributeSet attrs, int defStyle) {
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

        if (statePublisher != null)
            statePublisher.subscribe(this);
        setHeader(HEADER);
        setupContents();
        refresh();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (statePublisher != null)
            statePublisher.unsubscribe(this);
    }

    private void setupContents() {
        setupStateIconView();
        setContents(stateIcon);
        setFact("Tower identifier");
        setCaption("Cell Tower ID");
    }

    private void setupStateIconView() {
        stateIcon = new ImageView(getContext());
        stateIcon.setImageResource(R.drawable.state_initial);
    }

    @Override
    public void refresh() {
        if (statePublisher == null)
            return;
        state = statePublisher.state();
        post(this::updateView);
    }

    @Override
    public void onStateChanged(IState.Type state) {
        this.state = state;
        post(this::updateView);
    }

    private void updateView() {
        setFact(state.title());
        String ago = utils.toAgo(
                statePublisher.date(), getContext());
        setCaption(ago);
        stateIcon.setImageResource(getIcon());
    }

    public int getIcon() {
        switch (state) {
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
