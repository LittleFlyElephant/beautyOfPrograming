package web;

import calculate.CalImpl;
import calculate.CalService;
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
		
		String id1 = map.get("id1");
		String id2 = map.get("id2");
		System.out.println(id1+" "+id2);
		//do
		CalService calService = new CalImpl();
		event.response().end(calService.calculate(id1,id2));
		//
	}

}
