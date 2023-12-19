package com.example.appthuongmaidientu.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.appthuongmaidientu.UIFragment.OrderFragment;
import com.example.appthuongmaidientu.UIFragment.FavouriteFragment;
import com.example.appthuongmaidientu.UIFragment.HomeFragment;
import com.example.appthuongmaidientu.UIFragment.NotifiFragment;
import com.example.appthuongmaidientu.UIFragment.ProfileFragment;

public class ViewPageAdapter extends FragmentStatePagerAdapter {
    public ViewPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public ViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new OrderFragment();
            case 2:
                return new FavouriteFragment();
            case 3:
                return new NotifiFragment();
            case 4:
                return new ProfileFragment();
            default: return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
