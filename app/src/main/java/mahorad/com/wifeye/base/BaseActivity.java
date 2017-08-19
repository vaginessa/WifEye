package mahorad.com.wifeye.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import mahorad.com.wifeye.di.component.ActivityComponent;
import mahorad.com.wifeye.di.component.DaggerActivityComponent;
import mahorad.com.wifeye.di.module.ActivityModule;
import mahorad.com.wifeye.di.qualifier.ApplicationContext;
import mahorad.com.wifeye.service.EngineService;
import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;

import static mahorad.com.wifeye.util.Constants.PERMISSIONS;
import static mahorad.com.wifeye.util.Utils.isRunning;
import static mahorad.com.wifeye.util.Utils.openPermissions;

public abstract class BaseActivity extends ActivityManagePermission
//        implements LifecycleProvider<ActivityEvent>
{

    // todo implement rxlifecycle
//
//    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();
//
//    @Override
//    @NonNull
//    @CheckResult
//    public final Observable<ActivityEvent> lifecycle() {
//        return lifecycleSubject.hide();
//    }
//
//    @Override
//    @NonNull
//    @CheckResult
//    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
//        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
//    }
//
//    @Override
//    @NonNull
//    @CheckResult
//    public final <T> LifecycleTransformer<T> bindToLifecycle() {
//        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
//    }
//
//    @Override
//    @CallSuper
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        lifecycleSubject.onNext(ActivityEvent.CREATE);
//    }
//
//    @Override
//    @CallSuper
//    protected void onStart() {
//        super.onStart();
//        lifecycleSubject.onNext(ActivityEvent.START);
//    }
//
//    @Override
//    @CallSuper
//    protected void onResume() {
//        super.onResume();
//        lifecycleSubject.onNext(ActivityEvent.RESUME);
//    }
//
//    @Override
//    @CallSuper
//    protected void onPause() {
//        lifecycleSubject.onNext(ActivityEvent.PAUSE);
//        super.onPause();
//    }
//
//    @Override
//    @CallSuper
//    protected void onStop() {
//        lifecycleSubject.onNext(ActivityEvent.STOP);
//        super.onStop();
//    }
//
//    @Override
//    @CallSuper
//    protected void onDestroy() {
//        lifecycleSubject.onNext(ActivityEvent.DESTROY);
//        super.onDestroy();
//    }









    private ActivityComponent component;

    @Inject
    @ApplicationContext
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeComponent();
        injectDependencies();
    }

    protected void initializeComponent() {
        component = DaggerActivityComponent
                .builder()
                .applicationComponent(BaseApplication.component())
                .activityModule(new ActivityModule(this))
                .build();
    }

    public ActivityComponent component() {
        return component;
    }

    protected abstract void injectDependencies();
    
    /************************************************/

    public void toggleService() {
        if (isRunning(context, EngineService.class)) {
            stopEngineService();
        } else {
            handlePermissions();
        }
    }

    public void handlePermissions() {
        askCompactPermissions(PERMISSIONS, new PermissionResult() {
            @Override
            public void permissionGranted() { startEngineService(); }
            @Override
            public void permissionDenied() { finish(); }
            @Override
            public void permissionForeverDenied() {
                openPermissions(context);
            }
        });
    }

    public void startEngineService() {
        Intent intent = new Intent(context, EngineService.class);
        startService(intent);
    }

    public void stopEngineService() {
        Intent intent = new Intent(context, EngineService.class);
        stopService(intent);
    }


}
