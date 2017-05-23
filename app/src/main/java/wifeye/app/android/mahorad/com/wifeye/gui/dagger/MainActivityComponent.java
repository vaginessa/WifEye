package wifeye.app.android.mahorad.com.wifeye.gui.dagger;

import dagger.Component;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.ApplicationComponent;
import wifeye.app.android.mahorad.com.wifeye.gui.MainActivity;

@MainActivityScope
@Component
        (
        modules = MainActivityModule.class,
        dependencies = ApplicationComponent.class
        )
public interface MainActivityComponent {

        MainActivity mainActivity();
}
