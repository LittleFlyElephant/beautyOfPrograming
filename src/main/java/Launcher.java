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

                    request.response().end(APIHelper.getJson("Id=2140251882","Id,AA.AuId,AA.AfId").encode());
                })
                .listen(8080);
    }

}
