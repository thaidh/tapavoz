package com.whoami.voz.main;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build.VERSION;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.TextView;

public class Global {
    public static int ADS_ADMOD = 0;
    public static int ADS_ADMOD_AIRPUSH = 0;
    public static int ADS_ADMOD_AIRPUSH2 = 0;
    public static int ADS_AIRPUSH = 0;
    public static int ADS_AIRPUSH_ADMOD = 0;
    public static int ADS_AIRPUSH_ADMOD2 = 0;
    public static final Boolean DEBUG;
    public static final double FONTSCALE_MAX = 2.5d;
    public static final double FONTSCALE_MIN = 0.2d;
    public static final int FONT_MAX = 25;
    public static final int GO_FIRST = 4;
    public static final int GO_INDEXPAGE = 0;
    public static final int GO_LAST = 5;
    public static final int GO_NEXT = 2;
    public static final int GO_PREVIOUS = 3;
    public static final int GO_REFRESH = 1;
    public static final String HISTORY_FILE = "history.json";
    public static final Boolean OFFILNE;
    public static final int PAGE_1 = 1;
    public static final int PAGE_2 = 2;
    public static final int PAGE_3 = 3;
    public static final int PAGE_3HTML = 14;
    public static final int PAGE_BOOKMARK = 5;
    public static final int PAGE_CP = 4;
    public static final int PAGE_NEWPOST = 12;
    public static final int PAGE_PMDETAIL = 11;
    public static final int PAGE_QUOTE = 7;
    public static final int PAGE_RECENTPOST = 8;
    public static final int PAGE_RECENTTHREAD = 9;
    public static final int PAGE_SEARCH = 13;
    public static final int REDUCE1 = 1;
    public static final int REDUCE2 = 2;
    public static final int REDUCE4 = 4;
    public static final int REDUCE8 = 8;
    public static final int REDUCENONE = 0;
    public static final int REQUEST_CHOOSE_AN_IMAGE = 8;
    public static final int REQUEST_DELETE = 3;
    public static final int REQUEST_EDIT = 2;
    public static final int REQUEST_INTENTFILTER = 4;
    public static final int REQUEST_NEWTHREAD = 5;
    public static final int REQUEST_PMINSERT = 6;
    public static final int REQUEST_PMNEW = 7;
    public static final int REQUEST_QUOTE = 1;
    public static final int REQUEST_REPLY = 0;
    public static final String TAG = "vozForums";
    public static final int TASK_ADVANCESEARCH = 13;
    public static final int TASK_GETDOC = 0;
    public static final int TASK_GETQUOTE = 3;
    public static final int TASK_LOGIN = 1;
    public static final int TASK_LOGOUT = 2;
    public static final int TASK_NEWTHREAD = 9;
    public static final int TASK_PM = 15;
    public static final int TASK_PMNEW = 17;
    public static final int TASK_PMREPLY = 16;
    public static final int TASK_POSTDELETE = 6;
    public static final int TASK_POSTEDIT = 5;
    public static final int TASK_POSTREPLY = 4;
    public static final int TASK_RECENTPOST = 11;
    public static final int TASK_RECENTTHREAD = 12;
    public static final int TASK_SEARCH = 14;
    public static final int TASK_SEARCHQUOTE = 10;
    public static final int TASK_SUBSCRIBE = 7;
    public static final int TASK_UNSUBSCRIBE = 8;
    public static final int THEME_BLACK = 1;
    public static final int THEME_COLOR1 = 3;
    public static final int THEME_COLOR2 = 4;
    public static final int THEME_COLOR3 = 5;
    public static final int THEME_COLOR4 = 6;
    public static final int THEME_COLOR5 = 7;
    public static final int THEME_CUSTOM = 8;
    public static final int THEME_VOZ = 0;
    public static final int THEME_WOOD = 2;
    public static final String URL = "https://vozforums.com/";
    public static final String URL2 = "http://www.vozforums.com/";
    public static boolean bClickAd = false;
    public static boolean bDevider = false;
    public static boolean bEffect = false;
    public static boolean bFullScreen = false;
    public static boolean bIconRefresh = false;
    public static boolean bNotifQuote = false;
    public static boolean bNotifSubscribe = false;
    public static boolean bScrolling = false;
    public static boolean bSign = false;
    public static boolean bSwipe = false;
    public static boolean bTopicHeader = false;
    public static boolean bVibrate = false;
    public static final int bg = 0;
    public static final int bgfocus = 1;
    public static final int bgmain = 8;
    public static final int bgtitle = 2;
    public static int height = 0;
    public static float iDensity = 0.0f;
    public static int iHome = 0;
    public static int iNotifminutes = 0;
    public static String iNumQuickLink = null;
    public static int iNumQuote = 0;
    public static float iSize = 0.0f;
    public static int iSizeImage = 0;
    public static int iTheme = 0;
    public static int mCusThemeBg = 0;
    public static int mCusThemeBgFocus = 0;
    public static int mCusThemeQuicklink = 0;
    public static int mCusThemeTitleBg = 0;
    public static int mCusThemeTxt1 = 0;
    public static int mCusThemeTxt2 = 0;
    public static int mCusThemeTxtTitle = 0;
    public static String mSavePath = null;
    public static final int quicklink = 6;
    public static String sYourDevice = null;
    public static final int stick = 7;
    public static int[][] themeColor = null;
    public static String[][] themeColor2 = null;
    public static final int txColor1 = 4;
    public static final int txColor2 = 5;
    public static final int txtitle = 3;
    public static int width;

    static {
        DEBUG = Boolean.valueOf(true);
        OFFILNE = Boolean.valueOf(false);
        int[][] iArr = new int[TASK_NEWTHREAD][];
        iArr[bg] = new int[]{-657921, -13057804, -9798736, ViewCompat.MEASURED_STATE_MASK, -9798736, ViewCompat.MEASURED_STATE_MASK, -872415232, SupportMenu.CATEGORY_MASK, -2302758};
        iArr[bgfocus] = new int[]{ViewCompat.MEASURED_STATE_MASK, -14653571, -9467737, -1, -13388315, -1118482, -872415232, SupportMenu.CATEGORY_MASK, -15000805};
        iArr[bgtitle] = new int[]{-1300, -3921, -2336226, -1, -5736128, ViewCompat.MEASURED_STATE_MASK, -872415232, SupportMenu.CATEGORY_MASK, -2302758};
        iArr[txtitle] = new int[]{ViewCompat.MEASURED_STATE_MASK, -14653571, -6075996, ViewCompat.MEASURED_STATE_MASK, -6075996, -1118482, -872415232, SupportMenu.CATEGORY_MASK, -10066330};
        iArr[txColor1] = new int[]{-526345, -14653571, -6075996, ViewCompat.MEASURED_STATE_MASK, -6075996, ViewCompat.MEASURED_STATE_MASK, -872415232, SupportMenu.CATEGORY_MASK, -2302758};
        iArr[txColor2] = new int[]{ViewCompat.MEASURED_STATE_MASK, -14653571, -4593875, ViewCompat.MEASURED_STATE_MASK, -4593875, -1118482, -872415232, SupportMenu.CATEGORY_MASK, -10066330};
        iArr[quicklink] = new int[]{-526345, -14653571, -4593875, ViewCompat.MEASURED_STATE_MASK, -4593875, ViewCompat.MEASURED_STATE_MASK, -872415232, SupportMenu.CATEGORY_MASK, -2302758};
        iArr[stick] = new int[]{ViewCompat.MEASURED_STATE_MASK, -13057804, -9467737, -1, -16735512, -1118482, -872415232, SupportMenu.CATEGORY_MASK, -10066330};
//        iArr[bgmain] = new int[]{-1300, -3921, -2336226, -1, QuickAction.WOOD_TEXT_TITLE, QuickAction.WOOD_TEXT_TITLE, -872415232, SupportMenu.CATEGORY_MASK, -2302758};
        themeColor = iArr;
        String[] strArr = new String[TASK_NEWTHREAD];
        strArr[bg] = "000000";
        strArr[bgfocus] = "20677D";
        strArr[bgtitle] = "6F88A7";
        strArr[txtitle] = "FFFFFF";
        strArr[txColor1] = "33B5E5";
        strArr[txColor2] = "EEEEEE";
        strArr[quicklink] = "000000";
        strArr[stick] = "FF0000";
        strArr[bgmain] = "1B1B1B";
        String[] strArr2 = new String[TASK_NEWTHREAD];
        strArr2[bg] = "000000";
        strArr2[bgfocus] = "20677D";
        strArr2[bgtitle] = "A349A4";
        strArr2[txtitle] = "000000";
        strArr2[txColor1] = "A349A4";
        strArr2[txColor2] = "EEEEEE";
        strArr2[quicklink] = "000000";
        strArr2[stick] = "FF0000";
        strArr2[bgmain] = "666666";
        String[] strArr3 = new String[TASK_NEWTHREAD];
        strArr3[bg] = "F7F7F7";
        strArr3[bgfocus] = "20677D";
        strArr3[bgtitle] = "B9E72D";
        strArr3[txtitle] = "000000";
        strArr3[txColor1] = "B9E72D";
        strArr3[txColor2] = "000000";
        strArr3[quicklink] = "000000";
        strArr3[stick] = "FF0000";
        strArr3[bgmain] = "DCDCDA";
        String[] strArr4 = new String[TASK_NEWTHREAD];
        strArr4[bg] = "000000";
        strArr4[bgfocus] = "38C0F4";
        strArr4[bgtitle] = "6F88A7";
        strArr4[txtitle] = "FFFFFF";
        strArr4[txColor1] = "00A2E8";
        strArr4[txColor2] = "EEEEEE";
        strArr4[quicklink] = "000000";
        strArr4[stick] = "FF0000";
        strArr4[bgmain] = "666666";
        String[] strArr5 = new String[TASK_NEWTHREAD];
        strArr5[bg] = "FFFAEC";
        strArr5[bgfocus] = "FFF0AF";
        strArr5[bgtitle] = "DC5A1E";
        strArr5[txtitle] = "FFFFFF";
        strArr5[txColor1] = "888888";
        strArr5[txColor2] = "888888";
        strArr5[quicklink] = "000000";
        strArr5[stick] = "FF0000";
        strArr5[bgmain] = "DCDCDA";
        String[][] strArr6 = new String[TASK_NEWTHREAD][];
        String[] strArr7 = new String[TASK_NEWTHREAD];
        strArr7[bg] = "F5F5FF";
        strArr7[bgfocus] = "38C0F4";
        strArr7[bgtitle] = "6A7BB0";
        strArr7[txtitle] = "000000";
        strArr7[txColor1] = "6A7BB0";
        strArr7[txColor2] = "000000";
        strArr7[quicklink] = "000000";
        strArr7[stick] = "FF0000";
        strArr7[bgmain] = "DCDCDA";
        strArr6[bg] = strArr7;
        strArr6[bgfocus] = strArr;
        strArr = new String[TASK_NEWTHREAD];
        strArr[bg] = "FFFAEC";
        strArr[bgfocus] = "FFF0AF";
        strArr[bgtitle] = "DC5A1E";
        strArr[txtitle] = "FFFFFF";
        strArr[txColor1] = "A87940";
        strArr[txColor2] = "000000";
        strArr[quicklink] = "000000";
        strArr[stick] = "FF0000";
        strArr[bgmain] = "DCDCDA";
        strArr6[bgtitle] = strArr;
        strArr6[txtitle] = strArr2;
        strArr2 = new String[TASK_NEWTHREAD];
        strArr2[bg] = "F7F7F7";
        strArr2[bgfocus] = "20677D";
        strArr2[bgtitle] = "A349A4";
        strArr2[txtitle] = "000000";
        strArr2[txColor1] = "A349A4";
        strArr2[txColor2] = "000000";
        strArr2[quicklink] = "000000";
        strArr2[stick] = "FF0000";
        strArr2[bgmain] = "DCDCDA";
        strArr6[txColor1] = strArr2;
        strArr2 = new String[TASK_NEWTHREAD];
        strArr2[bg] = "000000";
        strArr2[bgfocus] = "20677D";
        strArr2[bgtitle] = "B9E72D";
        strArr2[txtitle] = "000000";
        strArr2[txColor1] = "B9E72D";
        strArr2[txColor2] = "EEEEEE";
        strArr2[quicklink] = "000000";
        strArr2[stick] = "FF0000";
        strArr2[bgmain] = "666666";
        strArr6[txColor2] = strArr2;
        strArr6[quicklink] = strArr3;
        strArr6[stick] = strArr4;
        strArr6[bgmain] = strArr5;
        themeColor2 = strArr6;
        ADS_ADMOD = bgfocus;
        ADS_AIRPUSH = bgtitle;
        ADS_ADMOD_AIRPUSH = txtitle;
        ADS_AIRPUSH_ADMOD = txColor1;
        ADS_ADMOD_AIRPUSH2 = txColor2;
        ADS_AIRPUSH_ADMOD2 = quicklink;
    }

    public static int getTextClolor2() {
        return themeColor[iTheme][txColor2];
    }

    public static void setBackgoundMain(View view) {
        setColorBackground(view, themeColor[iTheme][bgmain], themeColor[iTheme][bgmain]);
    }

    public static void setBackgroundItemThread(View view) {
        setColorBackground(view, themeColor[iTheme][bg], themeColor[iTheme][bgfocus]);
    }

    public static void setBackgroundItemThreadMultiQuote(View view) {
        setColorBackground(view, themeColor[iTheme][bgfocus], themeColor[iTheme][bgfocus]);
    }

    public static void setBackgroundMenuLogo(View view) {
        setColorBackground(view, themeColor[iTheme][bgtitle], themeColor[iTheme][bgfocus]);
    }

    public static void setBackgroundQuickAction(View view) {
        setColorBackground(view, themeColor[iTheme][bg], themeColor[iTheme][bgfocus]);
    }

    public static void setBackgroundQuickLink(View view) {
        setColorBackground(view, themeColor[iTheme][quicklink], themeColor[iTheme][bgfocus]);
    }

    public static void setColorBackground(View view, int i, int i2) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        ColorDrawable colorDrawable = new ColorDrawable(i);
        ColorDrawable colorDrawable2 = new ColorDrawable(i2);
        stateListDrawable.addState(new int[] {android.R.attr.stateNotNeeded}, colorDrawable);
        stateListDrawable.addState(new int[] {android.R.attr.state_enabled}, colorDrawable);
        stateListDrawable.addState(new int[] {android.R.attr.state_pressed}, colorDrawable2);
        stateListDrawable.addState(new int[] {android.R.attr.state_pressed}, colorDrawable2);
        if (view != null) {
            if (VERSION.SDK_INT >= 16) {
                view.setBackground(stateListDrawable);
            } else {
                view.setBackgroundDrawable(stateListDrawable);
            }
        }
    }

    public static void setTextColor1(TextView textView) {
        textView.setTextColor(themeColor[iTheme][txColor1]);
    }

    public static void setTextColor2(TextView textView) {
        textView.setTextColor(themeColor[iTheme][txColor2]);
    }

    public static void setTextContain(TextView textView) {
        textView.setTextColor(themeColor[iTheme][txColor2]);
    }

    public static void setTextMenuTitle(TextView textView) {
        textView.setTextColor(themeColor[iTheme][txtitle]);
    }

    public static void setTextSticky(TextView textView) {
        textView.setTextColor(themeColor[iTheme][stick]);
    }
}
