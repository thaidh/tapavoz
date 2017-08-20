package com.whoami.voz.worker;

public abstract class FetchHtmlTask {
    public abstract void doTask();

    public boolean isImportant() {
        return false;
    }

}
