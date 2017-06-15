package wifeye.app.android.mahorad.com.wifeye.app.state;

public abstract class State implements IState {

    protected Engine engine;

    public State(Engine engine) {
        this.engine = engine;
    }

    @Override
    public String toString() {
        return type().title();
    }
}