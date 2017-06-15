package wifeye.app.android.mahorad.com.wifeye.gui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import wifeye.app.android.mahorad.com.wifeye.R;
import wifeye.app.android.mahorad.com.wifeye.app.MainApplication;
import wifeye.app.android.mahorad.com.wifeye.app.MainService;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.MainComponent;
import wifeye.app.android.mahorad.com.wifeye.app.events.InternetEvent;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Internet;
import wifeye.app.android.mahorad.com.wifeye.app.state.IState;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;

public class InternetView extends BoxView {

    private static final String TAG = InternetView.class.getSimpleName();
    private static final String HEADER = "H O T S P O T";

    @Inject Utilities utils;
    private ImageView stateIcon;
    private Disposable disposable;
    private Disposable serviceDisposable;

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
    protected void onFinishInflate() {
        super.onFinishInflate();
        onFinishedInflation();
    }

    private void onFinishedInflation() {
        MainComponent mainComponent =
                MainApplication.mainComponent();
        if (mainComponent != null)
                mainComponent.inject(this);

        serviceDisposable = MainService
                .observable()
                .subscribe(e -> {
                    if (e) enable();
                    else disable();
                });

        setHeader(HEADER);
        setupContents();
    }

    @Override
    public void enable() {
        disposable = Internet
                .observable(getContext())
                .subscribe(e -> post(() -> updateView(e)));
    }

    @Override
    public void disable() {
        if (disposable == null)
            return;
        if (disposable.isDisposed())
            return;
        disposable.dispose();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        disable();
        serviceDisposable.dispose();
    }

    private void setupContents() {
        setupStateIconView();
        setContents(stateIcon);
        setFact(IState.Type.Initial.title());
        setCaption("n/a");
    }

    private void setupStateIconView() {
        stateIcon = new ImageView(getContext());
        stateIcon.setImageResource(R.drawable.no_ssid);
    }

    private void updateView(InternetEvent e) {
        synchronized (this) {
            Log.d(TAG, e.ssid() + " " + e.connected());
            setFact(e.ssid());
            String ago = utils.toAgo(e.date(), getContext());
            setCaption(ago);
            int icon = e.connected()
                    ? R.drawable.has_ssid
                    : R.drawable.no_ssid;
            stateIcon.setImageResource(icon);
        }
    }

}
