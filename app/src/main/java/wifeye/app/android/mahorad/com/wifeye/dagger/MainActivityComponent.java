package wifeye.app.android.mahorad.com.wifeye.dagger;

import dagger.Component;
import wifeye.app.android.mahorad.com.wifeye.dagger.annotations.MainActivityScope;

@MainActivityScope
@Component
        (
        modules = MainActivityModule.class,
        dependencies = AppComponent.class
        )
public interface MainActivityComponent {

    String something();
}
