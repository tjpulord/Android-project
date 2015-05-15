package com.yesway.izhijia.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import com.yesway.izhijia.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MyCarFragment extends BaseFragment {

	private int[] imageId = new int[] {R.drawable.ic_lock_car, R.drawable.ic_close_window, R.drawable.ic_my_4s};
	private int[] textId = new int[]{R.string.LOCK_CAR, R.string.CLOSE_WINDOW, R.string.MY_4S};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_my_car, container, false);
		ListView appList = (ListView) view.findViewById(R.id.car_function_list);
		
		String[] from = new String[]{"image", "text"};
		int[] to = new int[]{R.id.car_function_icon, R.id.car_function_name};
		
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>(3);
				
		for (int i = 0 ; i < 3; i++){
			HashMap<String, Object> entry = new HashMap<String, Object>();
			entry.put(from[0], imageId[i]);
			entry.put(from[1], getString(textId[i]));
			data.add(entry);
		}
		
		SimpleAdapter adapter = new SimpleAdapter(getActivity(), data, R.layout.list_item_my_car, from, to);
		appList.setAdapter(adapter);
		initMenuButton(view);
		return view;
	}
}
