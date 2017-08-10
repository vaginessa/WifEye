package mahorad.com.wifeye.engine.state;

import mahorad.com.wifeye.engine.Engine;

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