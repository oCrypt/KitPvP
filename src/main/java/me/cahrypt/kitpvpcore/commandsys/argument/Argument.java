package me.cahrypt.kitpvpcore.commandsys.argument;

public interface Argument<T> {
    boolean isArg(String arg);
    T getArg(String arg);
}
