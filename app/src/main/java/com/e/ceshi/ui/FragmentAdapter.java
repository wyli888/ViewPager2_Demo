package com.e.ceshi.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class FragmentAdapter extends FragmentStateAdapter {

  private ArrayList<BlankFragment> fragments;

  public FragmentAdapter(@NonNull Fragment fragment, ArrayList<BlankFragment> fragments) {
    super(fragment);
    this.fragments = fragments;
  }

  @NonNull
  @Override
  public Fragment createFragment(int position) {
    return fragments.get(position);
  }

  @Override
  public int getItemCount() {
    return fragments.size();
  }
}
