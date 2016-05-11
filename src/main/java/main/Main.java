package main;

import java.io.IOException;

import dataservice.DataImpl;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.impl.VertxFactoryImpl;
import io.vertx.core.spi.VertxFactory;
import web.MyHttpServer;

/**
* beautyOfPrograming/main/Main.java
* @author cxworks
* 2016年5月7日 下午6:42:35
*/
public class Main {

	public static void main(String[] args) {
		try {
			MyHttpServer httpServer=new MyHttpServer();
			httpServer.httpserverService();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
