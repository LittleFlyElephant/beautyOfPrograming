package demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

/**
 * Created by raychen on 16/5/5.
 */
public class Helloworld extends AbstractVerticle{

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        vertx
                .createHttpServer()
                .requestHandler(r -> {
                    r.response().end("hello world!");
                })
                .listen(8080, result -> {
                   if (result.succeeded()){
                       startFuture.complete();
                   }else {
                       startFuture.fail(result.cause());
                   }
                });
    }
}
