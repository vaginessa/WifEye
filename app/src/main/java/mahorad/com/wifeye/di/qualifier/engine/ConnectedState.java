package mahorad.com.wifeye.di.qualifier.engine;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by mahan on 2017-08-13.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ConnectedState {
}
