package com.example.diai_app.fragments;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.example.diai_app.R;
import com.example.diai_app.adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class RecipesFragment extends BaseFragment {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recipes;
    }

    @Override
    protected void bindView(View view) {
        tabLayout = view.findViewById(R.id.tabs);
        viewPager = view.findViewById(R.id.view_pager);

        // Thiết lập Adapter cho ViewPager
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
