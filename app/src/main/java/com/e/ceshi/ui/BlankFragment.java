package com.e.ceshi.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.e.ceshi.R;

import org.w3c.dom.Text;

import java.util.logging.LoggingMXBean;


public class BlankFragment extends Fragment {

  private String mParam1;
  private TextView mTextView;

  public static BlankFragment newInstance(String param1) {
    BlankFragment fragment = new BlankFragment();
    Bundle args = new Bundle();
    args.putString("param1", param1);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onPause() {
    super.onPause();
    Log.i("TAG=======onPause", mParam1);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString("param1");
    }

    Log.i("TAG=======onCreate", mParam1);
    Toast.makeText(getContext(), mParam1, Toast.LENGTH_SHORT).show();
  }



  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.i("TAG=======onDestroy", mParam1);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View root = inflater.inflate(R.layout.fragment_blank, container, false);
    mTextView = root.findViewById(R.id.text1);
    mTextView.setText(mParam1);
    Log.i("TAG=======onCreateView", mParam1);
    return root;
  }

  // 对外提供一个刷新Ui的方法
  public void refreshUi() {
    if (getArguments() != null) {
      mParam1 = getArguments().getString("param1");
      mTextView.setText(mParam1 + "ddd");
    }

  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    Log.i("TAG======onDestroyView", mParam1);
  }
}