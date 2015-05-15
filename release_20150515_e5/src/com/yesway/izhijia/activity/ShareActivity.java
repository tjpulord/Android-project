package com.yesway.izhijia.activity;

import java.util.ArrayList;
import java.util.HashMap;

import com.yesway.izhijia.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;



public class ShareActivity extends BaseActivity implements OnClickListener {
	
	private int[] imageId = new int[] {R.drawable.ic_sina_web, R.drawable.ic_tecent_web, R.drawable.ic_wechat_friends, R.drawable.ic_wechat_moments};
	private int[] textId = new int[]{R.string.SINA_BLOG, R.string.TENCENT_BLOG, R.string.WECHAT_FRIEND, R.string.WECHAT_MOMENTS};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		
		findViewById(R.id.back_btn).setOnClickListener(this);
		
		ListView appList = (ListView) findViewById(R.id.share_list);
		
		String[] from = new String[]{"image", "text"};
		int[] to = new int[]{R.id.car_function_icon, R.id.car_function_name};
		
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>(3);
				
		for (int i = 0 ; i < 4; i++){
			HashMap<String, Object> entry = new HashMap<String, Object>();
			entry.put(from[0], imageId[i]);
			entry.put(from[1], getString(textId[i]));
			data.add(entry);
		}
		
		SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.list_item_my_car, from, to);
		appList.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.back_btn){
			finish();
		}
	}
}
