package com.nna88.voz.root;

public class ShellOnMainThreadException extends RuntimeException {
    public ShellOnMainThreadException() {
        super("Application attempted to run a shell command from the main thread");
    }
}
