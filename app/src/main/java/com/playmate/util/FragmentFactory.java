package com.playmate.util;

import android.app.Fragment;

import com.playmate.fragment.FindFragment;
import com.playmate.fragment.MeFragment;
import com.playmate.fragment.MessageFragment;
import com.playmate.fragment.RankingFragment;

public class FragmentFactory {

    private static final int RANKING = 0;
    private static final int FIND = 1;
    private static final int MESSAGE = 2;
    private static final int ME = 3;

    public static Fragment getInstanceByIndex(int index) {
        Fragment fragment = null;
        switch (index) {
            case RANKING:
                fragment = new RankingFragment();
                break;
            case FIND:
                fragment = new FindFragment();
                break;
            case MESSAGE:
                fragment = new MessageFragment();
                break;
            case ME:
                fragment = new MeFragment();
                break;
        }
        return fragment;
    }
}