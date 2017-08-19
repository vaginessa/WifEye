package mahorad.com.wifeye.ui.custom.box;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.util.AttributeSet;

import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import mahorad.com.wifeye.base.BaseView;
import mahorad.com.wifeye.service.EngineService;

/**
 * Created by mahan on 2017-08-18.
 */

public abstract class AbstractBoxView extends BoxView implements BaseView {

    private CompositeDisposable boxViewDisposables = new CompositeDisposable();
    private Disposable serviceStateChanges;

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
        RxView.clicks(this).subscribe(this::onClick);
        boxViewDisposables.add(serviceStateDisposable());
        onSetup();
    }

    protected Disposable serviceStateDisposable() {
        return EngineService
                .stateChanges()
                .subscribe(this::onServiceStateChanged);
    }

    private void onServiceStateChanged(boolean started) {
        if (started)
            attachViewDisposables();
        else {
            detachViewDisposables();
            onReset();
        }
    }

    protected abstract void onClick(Object o);

    public abstract void attachViewDisposables();

    protected void attachDisposable(Disposable d) {
        boxViewDisposables.add(d);
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        detachViewDisposables();
    }

    private void detachViewDisposables() {
        boxViewDisposables.clear();
    }

    @Override
    public void onSetup() {
        setupHeader();
        setupContent();
        setupFact();
        setupCaption();
        setupLayout();
    }

    protected abstract void setupHeader();

    protected abstract void setupContent();

    protected abstract void setupFact();

    protected abstract void setupCaption();

    protected abstract void setupLayout();

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

    @Override
    @CallSuper
    public void onReset() {
        resetHeader();
        resetContent();
        resetFact();
        resetCaption();
        resetLayout();
    }

    protected abstract void resetHeader();

    protected abstract void resetContent();

    protected abstract void resetFact();

    protected abstract void resetCaption();

    protected abstract void resetLayout();


}
