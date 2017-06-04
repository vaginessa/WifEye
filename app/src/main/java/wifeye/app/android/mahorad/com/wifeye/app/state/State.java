package wifeye.app.android.mahorad.com.wifeye.app.state;

public abstract class State implements IState {

    protected StateMachine stateMachine;

    public State(StateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    @Override
    public String toString() {
        return type().title();
    }
}