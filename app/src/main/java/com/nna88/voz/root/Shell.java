package com.nna88.voz.root;

import android.os.Looper;
import android.support.v4.view.MotionEventCompat;
import com.nna88.voz.main.BuildConfig;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.IOUtils;

public class Shell {

    public static class SH {
        public static List<String> run(String str) {
            return Shell.run("sh", new String[]{str}, false);
        }

        public static List<String> run(List<String> list) {
            return Shell.run("sh", (String[]) list.toArray(new String[list.size()]), false);
        }

        public static List<String> run(String[] strArr) {
            return Shell.run("sh", strArr, false);
        }
    }

    public static class SU {
        public static boolean available() {
            List<String> run = run(new String[]{"id", "echo -EOC-"});
            if (run != null) {
                for (String str : run) {
                    if (str.contains("uid=")) {
                        return str.contains("uid=0");
                    }
                    if (str.contains("-EOC-")) {
                        return true;
                    }
                }
            }
            return false;
        }

        public static List<String> run(String str) {
            return Shell.run("su", new String[]{str}, false);
        }

        public static List<String> run(List<String> list) {
            return Shell.run("su", (String[]) list.toArray(new String[list.size()]), false);
        }

        public static List<String> run(String[] strArr) {
            return Shell.run("su", strArr, false);
        }

        public static String version(boolean z) {
            String str = z ? "su -V" : "su -v";
            List<String> run = Shell.run("sh", new String[]{str, "exit"}, false);
            if (run == null) {
                return null;
            }
            for (String str2 : run) {
                if (z) {
                    try {
                        if (Integer.parseInt(str2) > 0) {
                            return str2;
                        }
                    } catch (NumberFormatException e) {
                    }
                } else if (str2.contains(".")) {
                    return str2;
                }
            }
            return null;
        }
    }

    public static List<String> run(String str, String[] strArr, boolean z) {
        String toUpperCase = str.toUpperCase();
        if (BuildConfig.DEBUG) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Debug.log("Application attempted to run a shell command from the main thread");
                throw new ShellOnMainThreadException();
            }
            Debug.log(String.format("[%s%%] START", new Object[]{toUpperCase}));
        }
        List<String> synchronizedList = Collections.synchronizedList(new ArrayList());
        try {
            Process exec = Runtime.getRuntime().exec(str);
            DataOutputStream dataOutputStream = new DataOutputStream(exec.getOutputStream());
            StreamGobbler streamGobbler = new StreamGobbler(toUpperCase + "-", exec.getInputStream(), synchronizedList);
            StreamGobbler streamGobbler2 = new StreamGobbler(toUpperCase + "*", exec.getErrorStream(), z ? synchronizedList : null);
            streamGobbler.start();
            streamGobbler2.start();
            int length = strArr.length;
            int i = 0;
            while (i < length) {
                String str2 = strArr[i];
                try {
                    if (BuildConfig.DEBUG) {
                        Debug.log(String.format("[%s+] %s", new Object[]{toUpperCase, str2}));
                    }
                    dataOutputStream.writeBytes(str2 + IOUtils.LINE_SEPARATOR_UNIX);
                    dataOutputStream.flush();
                    i++;
                } catch (Exception e) {
                    synchronizedList = null;
                }
            }
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            exec.waitFor();
            dataOutputStream.close();
            streamGobbler.join();
            streamGobbler2.join();
            exec.destroy();
            if (str.equals("su") && exec.exitValue() == MotionEventCompat.ACTION_MASK) {
                synchronizedList = null;
            }
            if (BuildConfig.DEBUG) {
                Debug.log(String.format("[%s%%] END", new Object[]{str.toUpperCase()}));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return synchronizedList;
    }
}
