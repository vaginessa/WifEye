package mahorad.com.wifeye.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import mahorad.com.wifeye.di.component.ActivityComponent;
import mahorad.com.wifeye.di.component.DaggerActivityComponent;
import mahorad.com.wifeye.di.module.ActivityModule;
import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;

public abstract class BaseActivity extends ActivityManagePermission {

    private ActivityComponent component;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeComponent();
        injectDependencies();
    }

    protected void initializeComponent() {
        component = DaggerActivityComponent
                .builder()
                .activityModule(new ActivityModule(this))
                .build();
    }

    public ActivityComponent component() {
        return component;
    }

    protected abstract void injectDependencies();
}
