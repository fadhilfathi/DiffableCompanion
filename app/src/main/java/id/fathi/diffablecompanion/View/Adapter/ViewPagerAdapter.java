package id.fathi.diffablecompanion.View.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import id.fathi.diffablecompanion.View.Fragment.CariFragment;
import id.fathi.diffablecompanion.View.Fragment.CompanionFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private int numOfTabs;
    private String tabTitles[] = new String[]{"Cari", "Companion"};

    public ViewPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new CariFragment();
            case 1:
                return new CompanionFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
