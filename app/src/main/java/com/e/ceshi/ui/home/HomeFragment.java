package com.e.ceshi.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.e.ceshi.R;
import com.e.ceshi.ui.BlankFragment;
import com.e.ceshi.ui.TabLayoutMediators;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

  private HomeViewModel homeViewModel;
  private TabLayout tabLayout;
  private ViewPager2 viewPager2;

  private final ArrayList<String> tabTitles = new ArrayList<>();

  // 用List 存放fragment 不推荐使用 用不好 导致内存泄漏
  private ArrayList<BlankFragment> fragments = new ArrayList<>();


  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    View root = inflater.inflate(R.layout.fragment_home, container, false);

    tabLayout = root.findViewById(R.id.tabs);
    viewPager2 = root.findViewById(R.id.viewPager);
    return root;
  }


  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // 初始化公共的设置
    initTabAndViewPager();
    // 设置tab 和 viewPager 联动  (3个方法 各有利弊)
    firstMethod();
    //secondMethod();
    //thirdMethod();
  }

  /**
   * 设置一些公共的处理
   */
  private void initTabAndViewPager() {
    // 初始化 tabTitles 和 fragments
    for (int i = 0; i < 15; i++) {
      tabTitles.add(String.valueOf(i));
      fragments.add(BlankFragment.newInstance(tabTitles.get(i)));
    }

    // 关闭预加载
    viewPager2.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);  // 可以不设置 因为默认是 -1 默认不进行预加载
    // 这个必须设置 不然仍然会启用预加载
    ((RecyclerView) viewPager2.getChildAt(0)).getLayoutManager().setItemPrefetchEnabled(false);
    // 设置缓存数量，对应 RecyclerView 中的 mCachedViews，即屏幕外的视图数量
    ((RecyclerView) viewPager2.getChildAt(0)).setItemViewCacheSize(0);

    // 滑动到边界的阴影 设置为无阴影
    ((RecyclerView) viewPager2.getChildAt(0)).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

    // 可以设置 滑动动画效果
    //viewPager2.setPageTransformer(new DepthPageTransformer());

    // 给viewPager2 设置adapter (如果使用List存放 fragment 可能会内存泄漏)
    //FragmentAdapter adapter = new FragmentAdapter(this, fragments);
    //viewPager2.setAdapter(adapter);

    // 这里我们使用这个
    viewPager2.setAdapter(new FragmentStateAdapter(this) {
      @NonNull
      @Override
      public Fragment createFragment(int position) {
        return BlankFragment.newInstance(tabTitles.get(position));
      }

      @Override
      public int getItemCount() {
        return tabTitles.size();
      }

    });
  }

  // 下面是联动的方法


  /**
   * (推荐使用)
   * 第一种方法 不使用官方的 TabLayoutMediator 联动方法,  我们自己手动设置联动效果
   * 优点 : 滑动viewPager2 有动画 ，并且点击tabLayout 从0点到 14 也有动画 并且 不会创建多余的fragment 实现了懒加载
   */
  private void firstMethod() {

    // 设置 tabLayout 标题
    for (int i = 0; i < tabTitles.size(); i++) {
      tabLayout.addTab(tabLayout.newTab().setText(tabTitles.get(i)));
    }
    // 监听tabLayout事件 设置选中的viewPager2
    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {
        viewPager2.setCurrentItem(tab.getPosition(), false);
      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab) {

      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {

      }
    });

    // viewPager2 滑动监听 设置tab选中
    viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
      @Override
      public void onPageSelected(int position) {
        tabLayout.selectTab(tabLayout.getTabAt(position));
      }
    });

  }

  /**
   * 很坑  如果对点击tab和滑动页面没有缓慢滑动动画要求的话 可以使用这个
   * 使用官方的联动方法 TabLayoutMediator
   * 如果smoothScroll 设置为 true: 点击tabLayout时 如果两个tab距离过远  那么所有划过的tabLayout 都会创建和销毁BlankFragment
   * 如果smoothScroll 设置为 false: 滑动也好 点击也好 都没有切换效果 很生硬 但是 确实是懒加载了 点哪个tab就去创建哪个fragment
   */
  private void secondMethod() {

    // 这里第四个参数一定要设置为false  如果设置为true时 我们在滑动时 BlankFragment的创建 和 销毁 都很正常
    // 一旦 我们通过 点击tabLayout时 如果两个tab距离过远  那么所有划过的tabLayout 都会创建和销毁BlankFragment 这显然不是我们想要的
    // tabLayout 和 viewPager 联动
    new TabLayoutMediator(tabLayout, viewPager2, true, true, new TabLayoutMediator.TabConfigurationStrategy() {
      @Override
      public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        tab.setText(tabTitles.get(position));
      }
    }).attach();
  }


  /**
   * 还行  如果要求 点击tab没有缓慢滑动动画 滑动页面时有动画 的话 可以使用这个
   * copy google官方的代码 修改了源代码 使用我们修改的联动方法
   * 点击tab时无动画 滑动时有动画 防止了fragment的多次创建 实现了懒加载
   */
  private void thirdMethod() {
    // 这里我们将 google官方的代码copy下来做一下修改 点击tab时无动画 滑动时有动画 防止fragment 多次创建
    new TabLayoutMediators(tabLayout, viewPager2, new TabLayoutMediators.TabConfigurationStrategy() {
      @Override
      public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        tab.setText(tabTitles.get(position));
      }
    }).attach();
  }


}