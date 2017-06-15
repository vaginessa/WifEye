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
import wifeye.app.android.mahorad.com.wifeye.app.events.PersistenceEvent;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Persistence;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;

public class PersistView extends BoxView {

    public static final String TAG = PersistView.class.getSimpleName();

    private static final String HEADER = "P E R S I S T";

    @Inject Utilities utils;
    private ImageView stateIcon;
    private Disposable disposable;
    private Disposable serviceDisposable;

    public PersistView(Context context) {
        super(context);
    }

    public PersistView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PersistView(Context context, AttributeSet attrs, int defStyle) {
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
        disposable = Persistence
                .observable()
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
        setFact("-");
        setCaption("n/a");
    }

    private void setupStateIconView() {
        stateIcon = new ImageView(getContext());
        stateIcon.setImageResource(R.drawable.save);
    }

    private void updateView(PersistenceEvent e) {
        synchronized (this) {
            Log.d(TAG, "persist: " + e.ssid() + " " + e.ctid());
            setFact(facts(e));
            String caption = String.format("%s: %s", e.ssid(), e.ctid());
            setCaption(caption);
        }
    }

    private String facts(PersistenceEvent e) {
        if (Utilities.isNullOrEmpty(e.ssid()))
            return "-";
        int towers = Persistence
                .towersOf(e.ssid())
                .size();
        String plural = towers > 1 ? "s" : "";
        String ago = utils.toAgo(
                e.date(), getContext());
        return String.format("%d Tower%s (%s)", towers, plural, ago);
    }
}
