package etiennedesticourt.makurajapanese;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SkillPagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_PAGES = 1;

    public SkillPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new SkillsFragment();
        /*
        if (position == 0) {
            return new SkillsFragment();
            //return new HiraganaFragment();
        }
        else if (position == 1) {
            return new SkillsFragment();
        }
        else {
            return new SkillsFragment();
            //return new RadicalsFragment();
        }
        */
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
