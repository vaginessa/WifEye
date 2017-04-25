package wifeye.app.android.mahorad.com.wifeye.engine;

public abstract class State implements IState {

    protected Engine engine;

    public State(Engine engine) {
        this.engine = engine;
    }

}