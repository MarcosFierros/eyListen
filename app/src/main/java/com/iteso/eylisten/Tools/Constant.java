package com.iteso.eylisten.Tools;

import com.iteso.eylisten.ActivityMain;

public class Constant {
    public static final int TYPE_MAC = 0;
    public static final int TYPE_ALIENWARE = 1;
    public static final int TYPE_SHEETS = 2;
    public static final int TYPE_PILLOW = 3;
    public static final int TYPE_REFRIGERATOR = 4;
    public static final int TYPE_MICRO = 5;

    public static final String EXTRA_PRODUCT = "PRODUCT";
    public static final String EXTRA_FRAGMENT = "FRAGMENT";
    public static final int FRAGMENT_TECHNOLOGY = 0;
    public static final int FRAGMENT_HOME = 1;
    public static final int FRAGMENT_ELECTRONICS = 2;

    public static final int ACTIVITY_DETAIL = 9999;

    public static final String USER_PREFERENCES = "com.iteso.USER_PREFERENCES";

    public static final int REQUEST_READ_EXTERNAL = 9998;
    public static final int REQUEST_SELECT_PIC = 9997;

    public static final String BUNDLE_LOCALFILES = "localfiles";
    public static final String BUNDLE_ONLINEFILES = "onlinefiles";
    public static final String BUNDLE_USERPLAYLISTS = "userplaylists";
    public static final String BUNDLE_LOCALADAPTER = "localadapter";
    public static final String BUNDLE_ONLINEADAPTER = "onlineadapter";
    public static final String BUNDLE_USERPLAYLISTSADAPTER = "userplaylistsadapter";

    public static final String EXTRAS_PLAYLIST = "PLAYLIST";

    public static int preset=0;

    public static int getPreset() {
        return preset;
    }

    public static void setPreset(int preset) {
        Constant.preset = preset;
        ActivityMain.UpdateEqualizer();
    }

}
