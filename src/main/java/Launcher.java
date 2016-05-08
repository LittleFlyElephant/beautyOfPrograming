import calculate.CalImpl;
import calculate.CalService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import utility.APIHelper;

/**
 * Created by raychen on 16/5/6.
 */
public class Launcher extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        vertx
                .createHttpServer()
                .requestHandler(request -> {
                    MultiMap params = request.params();
                    String id1 = params.get("id1");
                    String id2 = params.get("id2");
//                    CalService calService = new CalImpl();
//                    request.response().end(calService.calculate(id1,id2));
                })
                .listen(8020);
    }

}
