package com.yesway.izhijia.fragment;

import com.yesway.izhijia.R;
import com.yesway.izhijia.activity.MainPanelActivity;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class BaseFragment extends Fragment implements OnClickListener{
	
	protected void initMenuButton(View view){
		ImageButton btn = (ImageButton)view.findViewById(R.id.slide_menu_btn);
		btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.slide_menu_btn){
			Activity activity = getActivity();
			
			if(activity instanceof MainPanelActivity){
				MainPanelActivity mainPanel = (MainPanelActivity)activity;
				
				mainPanel.openOrCloseDrawer();
			}
		}
		
	}

}
