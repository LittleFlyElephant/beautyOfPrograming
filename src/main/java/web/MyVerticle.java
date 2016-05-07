package web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

/**
* beautyOfPrograming/web/MyVerticle.java
* @author cxworks
* 2016年5月7日 下午6:25:05
*/
public class MyVerticle extends AbstractVerticle {
	@Override
	public void start(){
		HttpServer httpServer=vertx.createHttpServer();
		Router router=Router.router(vertx);
		router.route("/cal").handler(new Handler());
		
		
		//
		httpServer.requestHandler(router::accept).listen(8020);
	}
}
