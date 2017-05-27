package wifeye.app.android.mahorad.com.wifeye.gui.dagger;

import dagger.Component;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.MainComponent;
import wifeye.app.android.mahorad.com.wifeye.gui.HomeActivity;
import wifeye.app.android.mahorad.com.wifeye.gui.mvp.HomePresenter;

@HomeScope
@Component(modules = { HomeModule.class }, dependencies = MainComponent.class)
public interface HomeComponent {

    void inject(HomeActivity activity);

    void inject(HomePresenter presenter);
}
