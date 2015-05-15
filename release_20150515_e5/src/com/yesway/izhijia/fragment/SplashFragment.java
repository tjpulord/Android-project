package com.yesway.izhijia.fragment;

import com.yesway.izhijia.R;
import com.yesway.izhijia.activity.LoginActivity;
import com.yesway.izhijia.engine.UiEngine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class SplashFragment extends Fragment implements OnClickListener {
	private int index = -1;
	public void init(int index){
		this.index = index;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
                R.layout.fragment_splash, container, false);
		
		TextView text = (TextView) view.findViewById(R.id.welcome_message);
		switch(index){
		case 0:
		{
			text.setText(R.string.AD_1);
			break;
		}
		case 1:
		{
			text.setText(R.string.AD_2);
			break;
		}
		case 2:
		{
			text.setText(R.string.AD_3);
			Button btn = (Button) view.findViewById(R.id.use_it);
			btn.setVisibility(View.VISIBLE);
			btn.setOnClickListener(this);
			break;
		}
		default:
			break;
			
		}
		return view;
	}

	@Override
	public void onClick(View v) {
		UiEngine.getInstance(getActivity()).getPreferenceEngine().setIsFreshBuild(false);
		Intent i = new Intent(getActivity(), LoginActivity.class);
		this.getActivity().startActivity(i);
		this.getActivity().finish();
	}
}
