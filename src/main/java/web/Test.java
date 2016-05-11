package web;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Test {
	static String[] param={"?id1=2147152072&id2=189831743","?id1=2251253715&id2=2180737804"};
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		// 测试并发对MyHttpServer的影响
		for (int i = 0; i < 20; i++) {
			Runnable run = new Runnable() {
				public void run() {
					try {
						String pString=null;
						double i=Math.random();
						if (i<0.5) {
							pString=param[0];
						}
						else {
							pString=param[1];
						}
						startWork(pString);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			exec.execute(run);
		}
		exec.shutdown();// 关闭线程池
	}

	public static void startWork(String param) throws IOException {
		URL url = new URL("http://127.0.0.1:8080/"+param);
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		urlConn.setDoOutput(true);
		urlConn.setDoInput(true);
		urlConn.setRequestMethod("GET");
		// 测试内容包
		
		
		while (urlConn.getContentLength() != -1) {
			if (urlConn.getResponseCode() == 200) {
				InputStream in = urlConn.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String temp = "";
				while ((temp = reader.readLine()) != null) {
					System.err.println("server response:" + temp);// 打印收到的信息
				}
				reader.close();
				in.close();
				urlConn.disconnect();
			}
		}
	}
}

