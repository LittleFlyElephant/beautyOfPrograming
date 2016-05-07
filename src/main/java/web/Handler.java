package web;

import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;

/**
* beautyOfPrograming/web/Handler.java
* @author cxworks
* 2016年5月7日 下午6:37:26
*/
public class Handler implements io.vertx.core.Handler<RoutingContext> {

	@Override
	public void handle(RoutingContext event) {
		MultiMap map=event.request().params();
		
		int start=Integer.parseInt(map.get("id1"));
		int end=Integer.parseInt(map.get("id2"));
		//do 
		//
	}

}
