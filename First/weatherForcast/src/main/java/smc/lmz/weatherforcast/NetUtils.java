package smc.lmz.weatherforcast;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtils {
	/**
	 * 检查网络是否可用
	 * 
	 * @return
	 */
	public static boolean getNetworkIsAvailable(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(context.CONNECTIVITY_SERVICE);//返回网络连接状态实例
		NetworkInfo info = manager.getActiveNetworkInfo();//得到当前网络连接的实例
		if (info == null || !info.isConnected()) {
			return false;
		}
		if (info.isRoaming()) {
			return true;
		}
		return true;
	}

}
