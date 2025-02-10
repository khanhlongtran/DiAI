package com.example.diai_app.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.diai_app.Fragments.ItemCaloriesFragments.BreakfastFragment;
import com.example.diai_app.Fragments.ItemCaloriesFragments.DinnerFragment;
import com.example.diai_app.Fragments.ItemCaloriesFragments.LunchFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new BreakfastFragment();
            case 1:
                return new LunchFragment();
            case 2:
                return new DinnerFragment();
            default:
                return null; // Or handle the default case appropriately
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Breakfast";
            case 1:
                return "Lunch";
            case 2:
                return "Dinner";
            default:
                return null;
        }
    }
}
