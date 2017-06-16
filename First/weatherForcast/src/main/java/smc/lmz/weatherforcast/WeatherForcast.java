package smc.lmz.weatherforcast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WeatherForcast extends Activity {
	private EditText et_city=null;
	private Button btn_query=null;
	private TextView tv_show=null;
	private ProgressDialog dialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.et_city=(EditText) super.findViewById(R.id.edt_city);
		this.btn_query=(Button) super.findViewById(R.id.query_btn);
		this.tv_show=(TextView) super.findViewById(R.id.show_info);
		this.dialog=new ProgressDialog(WeatherForcast.this);
		this.btn_query.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!UIHelper.checkInputIsEmpty(et_city)){
					if (NetUtils.getNetworkIsAvailable(WeatherForcast.this)){
						new QueryTask().execute(et_city.getText().toString().trim());
					}else{
						UIHelper.showToastMsg(WeatherForcast.this,
								R.string.str_neterror);
					}
				} else {
					UIHelper.showToastMsg(WeatherForcast.this,
							R.string.str_inputwaring);
				}
				
			}
			
		});
		showWeatherPage();//��ʾ��������
    }
    /**
	 * ��ʾ��������
	 */
	private void showWeatherPage() {
		// ��ʾ�Զ����ؿ�
		// ��λ���м�ͨ����ͼ��λ
		// ��������ĸ�������е�ʡ��
		et_city.setHint(R.string.str_hint_weather);
		btn_query.setText(R.string.str_submit);
	}

	/**
	 * �첽��ѯ
	 * 
	 * @author simaben
	 * 
	 */
    class QueryTask extends AsyncTask<String, Void, Integer>{
    	private final String NAMESPACE =WeatherForcast.this.getResources().getString(R.string.weather_namespace);
    	// WebService��ַ
    	private static final String URL = "http://www.webxml.com.cn/webservices/weatherwebservice.asmx";
    	// ��������᷵�ز�ѯ���������ڵ�����״������Ϣ
    	private static final String METHOD_NAME = "getWeatherbyCityName";	
    	private static final String SOAP_ACTION = "http://WebXml.com.cn/getWeatherbyCityName";
    	private String weatherToday;
		@Override
		protected Integer doInBackground(String... params) {
			getWebServiceInfos(params[0]);
			return null;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			UIHelper.showProDialog(dialog);
		}
		@Override
		protected void onPostExecute(Integer result){
			super.onPostExecute(result);
			sendMsg(1);
			tv_show.setText(weatherToday);
		}
		/**
		 * �õ�webservice��Ϣ
		 * 
		 * @param str
		 */
		private void getWebServiceInfos(String cityName){
			// �ж�����ĳ����Ƿ�Ϊ�й���ʡ������
			if (!isCityNameInList(cityName)){
				try {
					// ��һ��ʵ����SoapObject
					// ����ָ��webService�������ռ䣨�����WSDL�ĵ��п��Բ鿴�����ռ䣩���Լ����÷�������
					SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
					// �ڶ��������跽���в����Ļ�,���õ��÷�������
					rpc.addProperty("theCityName", cityName);
					// ������������SOAP������Ϣ(��������ΪSOAPЭ��汾�ţ�����Ҫ���õ�webService�а汾��һ��)
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11);
					// ���Ĳ���ע��Envelope
					envelope.bodyOut = rpc;
					envelope.dotNet = true;
					envelope.setOutputSoapObject(rpc);
					// ���岽������������󣬲�ָ��WSDL�ĵ�URL
					HttpTransportSE ht = new HttpTransportSE(URL);
					ht.debug = true;
					// ������:����WebService(���в���Ϊ1�������ռ�+�������ƣ�2��Envelope����)
					ht.call(SOAP_ACTION, envelope);
					SoapObject detail = (SoapObject) envelope.getResponse();
					// ���߲���������������
					parseWeather(detail);

				} catch (SoapFault e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				}
			} else {
				weatherToday = getResources().getString(
						R.string.str_citynonsupport);
			}
				
		}
		/**
		 * ���������theCityName = ������������(������п���Ӣ��)����д���(������Ĭ��Ϊ�Ϻ���)���磺�Ϻ� ��
		 * 58367�����г��������ظ���ʹ�ó��д����ѯ(��ͨ�� getSupportCity �� getSupportDataSet
		 * ���)���������ݣ� һ��һά���� String(22)������23��Ԫ�ء� String(0) ��
		 * String(4)��ʡ�ݣ����У����д��룬����ͼƬ���ƣ�������ʱ�䡣String(5) �� String(11)�������
		 * ���£��ſ�������ͷ���
		 * ���������ƿ�ʼͼƬ����(���³ƣ�ͼ��һ)���������ƽ���ͼƬ����(���³ƣ�ͼ���)�����ڵ�����ʵ��������������ָ����String(12) ��
		 * String(16)���ڶ���� ���£��ſ�������ͷ�����ͼ��һ��ͼ�����String(17) �� String(21)���������
		 * ���£��ſ�������ͷ�����ͼ��һ��ͼ�����String(22) ����ѯ�ĳ��л�����Ľ���
		 * 
		 * @param detail
		 * @throws UnsupportedEncodingException
		 */
		private void parseWeather(SoapObject detail)
				throws UnsupportedEncodingException {
			System.out.println(detail.toString());
			// ������ݲ�ͬ�Ľ�ڷ��ص�����Ҳ�᲻ͬ������Ҫ�ο�webservice���ĵ�
			// ���ݲ�ͬ�ķŻؽ���õ��Ĳ���ҲҪ����

			String date = detail.getProperty(6).toString();
			weatherToday = "���죺" + date.split(" ")[0];
			weatherToday = weatherToday + "\n������" + date.split(" ")[1];
			weatherToday = weatherToday + "\n���£�"
					+ detail.getProperty(5).toString();
			weatherToday = weatherToday + "\n������"
					+ detail.getProperty(7).toString() + "\n";

			// ����ָ��
			String listState = detail.getProperty(11).toString();
			weatherToday = weatherToday + "\n" + listState + "\n";

			// ������ʱ��
			String lastUpdateTime = detail.getProperty(4).toString();
			weatherToday = weatherToday + "\n������ʱ�䣺" + lastUpdateTime + "\n";
		}
    }
		/**
		 * �ж��û�������Ƿ�Ϊ�й���ʡ������
		 * 
		 * @param cityName
		 * @return ����������ʡ����true
		 */
		private boolean isCityNameInList(String cityName) {

			String[] allCitys = getResources()
					.getStringArray(R.array.province_item);
			for (String city : allCitys) {
				if (city.equals(cityName)) {
					return true;
				}
			}
			return false;
		}
		public void sendMsg(int i) {
			Message msg = new Message();
			msg.what = i;
			handler.sendMessage(msg);
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				dialog.dismiss();
			};
		};
    }
