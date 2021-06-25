package com.e.ceshi.ui.home;

import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.adapter.FragmentViewHolder;
import androidx.viewpager2.widget.ViewPager2;

import com.e.ceshi.R;
import com.e.ceshi.ui.BlankFragment;
import com.e.ceshi.ui.FragmentAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.lang.reflect.Array;
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

    // 初始化 tabTitles 和 fragments
    for (int i = 0; i < 15; i++) {
      tabTitles.add(String.valueOf(i));
      fragments.add(BlankFragment.newInstance(tabTitles.get(i)));
    }

    // 关闭预加载
    viewPager2.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);  // 可以不设置 因为默认是 -1 默认不进行预加载
    // 这个必须设置 不然仍然会启用预加载
    ((RecyclerView)viewPager2.getChildAt(0)).getLayoutManager().setItemPrefetchEnabled(false);
    // 设置缓存数量，对应 RecyclerView 中的 mCachedViews，即屏幕外的视图数量
    ((RecyclerView)viewPager2.getChildAt(0)).setItemViewCacheSize(0);

    // 第一种方法 (可能会内存泄漏)
    //FragmentAdapter adapter = new FragmentAdapter(this, fragments);
    //viewPager2.setAdapter(adapter);

    // 这里使用第二种方法
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
    // viewPager2 滑动监听
    viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
      @Override
      public void onPageSelected(int position) {
        super.onPageSelected(position);

      }
    });

    // 这里第四个参数一定要设置为false  如果设置为true时 我们在滑动时 BlankFragment的创建 和 销毁 都很正常
    // 一旦 我们通过 点击tabLayout时 如果两个tab距离过远  那么所有划过的tabLayout 都会创建和销毁BlankFragment 这显然不是我们想要的

    // tabLayout 和 viewPager 联动
    new TabLayoutMediator(tabLayout, viewPager2,true,false, new TabLayoutMediator.TabConfigurationStrategy() {
      @Override
      public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        tab.setText(tabTitles.get(position));
      }
    }).attach();


  }

}