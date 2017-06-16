package smc.lmz.weatherforcast;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

public class UIHelper {
	/**
	 * ��ʾtoast
	 * 
	 * @param context
	 *            �����Ķ���
	 * @param msg
	 *            ��ʾ���ı�����
	 */
	public static void showToastMsg(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	/**
	 * ��ʾtoast
	 * 
	 * @param context
	 *            �����Ķ���
	 * @param msg
	 *            ��strings.xml�ж�����ַ���Դid
	 */
	public static void showToastMsg(Context context, int resStrId) {
		Toast.makeText(context, resStrId, Toast.LENGTH_SHORT).show();
	}
	/**
	 * ��������Ƿ�Ϊ��
	 * 
	 * @return
	 */
	public static boolean checkInputIsEmpty(EditText et){
		if ("".equals(et.getText().toString().trim())) {
			return true;
		}
		return false;
	}
	/**
	 * ��ʾdialog
	 */
	public static void showProDialog(ProgressDialog dialog) {
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setTitle("��ʾ");
		dialog.setMessage("���Եȣ����ڲ�ѯ...");
		dialog.setIndeterminate(false);
		dialog.setCancelable(true);
		dialog.show();
	}
}
