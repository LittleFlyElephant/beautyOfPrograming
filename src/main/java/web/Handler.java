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
		
		long start=Long.parseLong(map.get("id1"));
		long end=Long.parseLong(map.get("id2"));
		//do 
		//
	}

}
