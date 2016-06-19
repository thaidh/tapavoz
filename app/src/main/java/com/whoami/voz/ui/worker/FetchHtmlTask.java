package com.whoami.voz.ui.worker;

public abstract class FetchHtmlTask {
    public abstract void doTask();

    public boolean isImportant() {
        return false;
    }

}
