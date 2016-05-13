package main;

import dataservice.DataImpl;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.impl.VertxFactoryImpl;
import io.vertx.core.spi.VertxFactory;

/**
* beautyOfPrograming/main/Main.java
* @author cxworks
* 2016年5月7日 下午6:42:35
*/
public class Main {

	public static void main(String[] args) {

		VertxOptions options=new VertxOptions();
		options.setBlockedThreadCheckInterval(300000);
		options.setEventLoopPoolSize(10);
		VertxFactory factory=new VertxFactoryImpl();
		Vertx vertx=factory.vertx(options);
		DeploymentOptions deploymentOptions=new DeploymentOptions();
//		deploymentOptions.setInstances(10);
		vertx.deployVerticle("web.MyVerticle", deploymentOptions);
	}
	

}
