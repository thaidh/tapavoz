package com.nna88.voz.root;

import com.nna88.voz.main.BuildConfig;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class StreamGobbler extends Thread {
    private BufferedReader reader;
    private String shell;
    private List<String> writer;

    public StreamGobbler(String str, InputStream inputStream, List<String> list) {
        this.shell = null;
        this.reader = null;
        this.writer = null;
        this.shell = str;
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
        this.writer = list;
    }

    public void run() {
        try {
            while (true) {
                try {
                    String readLine = this.reader.readLine();
                    if (readLine != null) {
                        if (BuildConfig.DEBUG) {
                            Debug.log(String.format("[%s] %s", new Object[]{this.shell, readLine}));
                        }
                        if (this.writer != null) {
                            this.writer.add(readLine);
                        }
                    }
                } catch (Exception e) {
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                this.reader.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }

    }
}
