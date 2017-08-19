package mahorad.com.wifeye.base;

/**
 * Created by mahan on 2017-08-17.
 */

public interface BaseView {

    void onSetup();

    void refresh(Object event);

    void onReset();

}
