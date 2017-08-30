package mahorad.com.wifeye.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import mahorad.com.wifeye.base.activity.LifecycleActivity;
import mahorad.com.wifeye.navigate.Navigator;

/**
 * Created by Mahan Rad on 2017-08-24.
 */

public abstract class BaseActivity extends LifecycleActivity {

    @Inject
    protected Navigator navigator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

}
