package com.e.ceshi.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.e.ceshi.R;
import com.e.ceshi.ui.BlankFragment;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_DRAGGING;
import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_IDLE;

public class DashboardFragment extends Fragment {

  private DashboardViewModel dashboardViewModel;
  private TabLayout tabLayout;
  private ViewPager viewPager;

  private ArrayList<String> titles = new ArrayList<>();
  private BlankFragment blankFragment;

  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
    View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

    tabLayout = root.findViewById(R.id.tabs);
    viewPager = root.findViewById(R.id.viewPager);
    titles.add("推荐");
    titles.add("我的");
    titles.add("小书包");
    titles.add("哈哈");
    titles.add("有意思");
    titles.add("没意思");
    titles.add("真的");
    titles.add("立即");
    titles.add("地方");
    titles.add("阿斯蒂芬");
    titles.add("多个");
    titles.add("二号个");
    titles.add("欧尼");
    titles.add("地方");
    titles.add("格式地方");


    tabLayout.setupWithViewPager(viewPager);

    // 解决tabLayout 回弹问题
    // 先清空所有的pageChangeListener，会移除tabLayout添加进去的那个，要在setupWithViewPager后面调用
    viewPager.clearOnPageChangeListeners();
   // 先清空所有的pageChangeListener，根据上面的反射的那个思路，如果不是手动触发的滚动，会移除tabLayout添加进去的那个
    viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout) {

      boolean isTouchState;

      @Override
      public void onPageScrollStateChanged(int state) {
        super.onPageScrollStateChanged(state);
        if (state == SCROLL_STATE_DRAGGING) {
          isTouchState = true;
        } else if (state == SCROLL_STATE_IDLE) {
          isTouchState = false;
        }
      }

      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (isTouchState) {
          super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

      }
    });

    viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
      @NonNull
      @Override
      public Fragment getItem(int position) {
        blankFragment =  BlankFragment.newInstance(titles.get(position));
        return  blankFragment;
      }

      @Override
      public int getCount() {
        return titles.size();
      }

      @Nullable
      @Override
      public CharSequence getPageTitle(int position) {

        return titles.get(position);
      }
    });



    return root;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);


  }




}