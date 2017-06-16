package wifeye.app.android.mahorad.com.wifeye.gui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import wifeye.app.android.mahorad.com.wifeye.R;
import wifeye.app.android.mahorad.com.wifeye.app.events.InternetEvent;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Internet;
import wifeye.app.android.mahorad.com.wifeye.app.state.IState;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;

public class InternetView extends AbstractBoxView {

    private static final String TAG = InternetView.class.getSimpleName();
    private static final String HEADER = "H O T S P O T";

    @Inject Utilities utils;
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
    protected void inject() {
        mainComponent.inject(this);
    }

    @Override
    public void attachViewDisposables() {
        Disposable internetDisposable = Internet
                .observable(getContext())
                .subscribe(e -> post(() -> updateView(e)));
        attachDisposable(internetDisposable);
    }

    @Override
    public void reset() {
        setHeader(HEADER);
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
