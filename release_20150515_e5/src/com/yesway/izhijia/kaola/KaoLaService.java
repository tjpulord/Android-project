package com.yesway.izhijia.kaola;

import com.yesway.izhijia.R.string;
import com.yesway.izhijia.kaola.KaoLaModel.ActivateListener;
import com.yesway.izhijia.kaola.KaoLaModel.KaoLaInformationDownloadListener;
import com.yesway.izhijia.kaola.internal.KaoLaServiceImpl;
import com.yesway.izhijia.model.BaseService;

public abstract class KaoLaService extends BaseService {	
	public abstract void activateDevice(final String deviceId, final ActivateListener listener);
	public abstract void getAllCategories(final String deviceId, final String openId, final KaoLaInformationDownloadListener listener);
	public abstract void getContentListByCategoryId(final String deviceId, final String openId, final String categoryId, final KaoLaInformationDownloadListener listener);
	public abstract void getRadioPlayList(final String deviceId, final String openId, final String radioId, final String clockId, final KaoLaInformationDownloadListener listener);
	public static KaoLaService createService(){
		return new KaoLaServiceImpl();
	}
}
