package mahorad.com.wifeye.ui.custom.box;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.util.AttributeSet;

import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import mahorad.com.wifeye.base.BaseView;
import timber.log.Timber;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;

/**
 * Created by mahan on 2017-08-18.
 */

public abstract class AbstractBoxView extends BoxView implements BaseView {

    private static final String TAG = AbstractBoxView.class.getSimpleName();

    private CompositeDisposable disposables;

    public AbstractBoxView(Context context) {
        super(context);
    }

    public AbstractBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AbstractBoxView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        onSetup();
        if (disposables != null) return;
        Timber.tag(TAG).i("layout inflated");
        disposables = new CompositeDisposable();
        attachDisposables();
    }

    private void attachDisposables() {
        attachViewDisposables();
        RxView.clicks(this)
                .observeOn(mainThread())
                .subscribe(this::onClicked);
    }

    protected abstract void onClicked(Object o);

    protected abstract void attachViewDisposables();

    protected void attachDisposable(Disposable d) {
        disposables.add(d);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Timber.tag(TAG).i("detached from window");
        detachDisposables();
    }

    private void detachDisposables() {
        if (disposables == null)
            return;
        Timber.tag(TAG).i("disposing disposables");
        disposables.clear();
        disposables = null;
    }

    @Override
    public void onSetup() {
        setupHeader();
        setupContent();
        setupFact();
        setupCaption();
    }

    protected void setupHeader() {
        setHeader(getHeader());
    }

    protected abstract String getHeader();

    protected abstract void setupContent();

    protected abstract void setupFact();

    protected abstract void setupCaption();

    @Override
    @CallSuper
    public void refresh(Object event) {
        refreshHeader(event);
        refreshContent(event);
        refreshFact(event);
        refreshCaption(event);
    }

    protected abstract void refreshHeader(Object event);

    protected abstract void refreshFact(Object event);

    protected abstract void refreshContent(Object event);

    protected abstract void refreshCaption(Object event);

}
