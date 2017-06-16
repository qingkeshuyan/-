package smc.lmz.weatherforcast;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtils {
	/**
	 * ��������Ƿ����
	 * 
	 * @return
	 */
	public static boolean getNetworkIsAvailable(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(context.CONNECTIVITY_SERVICE);//������������״̬ʵ��
		NetworkInfo info = manager.getActiveNetworkInfo();//�õ���ǰ�������ӵ�ʵ��
		if (info == null || !info.isConnected()) {
			return false;
		}
		if (info.isRoaming()) {
			return true;
		}
		return true;
	}

}
