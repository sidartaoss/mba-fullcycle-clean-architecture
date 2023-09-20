package br.com.fullcycle.application;

public abstract class UnitUseCase<INPUT> {

    public abstract void execute(INPUT input);
}
