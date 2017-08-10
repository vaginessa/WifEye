package mahorad.com.wifeye.ui.home;

import android.content.Intent;
import android.os.Bundle;

import mahorad.com.wifeye.R;
import mahorad.com.wifeye.base.BaseActivity;
import mahorad.com.wifeye.service.EngineService;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = new Intent(this, EngineService.class);
        startService(intent);
    }

    @Override
    protected void injectDependencies() {
        component().inject(this);
    }
}
