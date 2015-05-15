package com.example.animationsample;

import java.util.ArrayList;
import java.util.HashMap;







import java.util.Map;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ShareActivity extends Activity implements OnClickListener, OnItemClickListener{
	private int[] imageId = new int[] {R.drawable.ic_sina_web, R.drawable.ic_tecent_web, R.drawable.ic_wechat_friends, R.drawable.ic_wechat_moments};
	private int[] textId = new int[]{R.string.SINA_BLOG, R.string.TENCENT_BLOG, R.string.WECHAT_FRIEND, R.string.WECHAT_MOMENTS};
	ListView appList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_activity);
		
		findViewById(R.id.back_btn).setOnClickListener(this);
		
		appList = (ListView) findViewById(R.id.share_list);
		//GridView appList = (GridView) findViewById(R.id.share_list);
		
		String[] from = new String[]{"image", "text"};
		int[] to = new int[]{R.id.menu_image, R.id.menu_name};
		
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>(5);
				
		for (int i = 0 ; i < 4; i++){
			HashMap<String, Object> entry = new HashMap<String, Object>();
			entry.put(from[0], imageId[i]);
			entry.put(from[1], getString(textId[i]));
			data.add(entry);
		}
		
		SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.menu_info, from, to);
		appList.setAdapter(adapter);
		appList.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.back_btn){
			finish();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		HashMap<String, Object> map = (HashMap<String, Object>) this.appList.getItemAtPosition(position);
		String name = (String) map.get("text");
		switch (position) {
		case 0:
			Toast.makeText(this, "share name="+name+"postion: 0", Toast.LENGTH_SHORT).show();
			break;
		case 1:
			Toast.makeText(this, "share name="+name+"postion: 1", Toast.LENGTH_SHORT).show();
			break;
		case 2:
			Toast.makeText(this, "share name="+name+"postion: 2", Toast.LENGTH_SHORT).show();
			break;
		case 3:
			Intent intent = new Intent();
			ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
			intent.setComponent(comp);
			intent.setAction("android.intent.action.SEND");
			intent.setType("image/*");
			startActivity(intent);
			break;

		default:
			break;
		}
		//int img = map.
		
	}
}
