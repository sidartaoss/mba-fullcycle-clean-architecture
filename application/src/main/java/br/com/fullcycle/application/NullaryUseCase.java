package br.com.fullcycle.application;

public abstract class NullaryUseCase<OUTPUT> {

    public abstract OUTPUT execute();

    public <T> T execute(Presenter<OUTPUT, T> presenter) {
        try {
            return presenter.present(execute());
        } catch (Throwable t) {
            return presenter.present(t);
        }
    }
}
