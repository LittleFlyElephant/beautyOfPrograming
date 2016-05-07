package utility;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by raychen on 16/5/6.
 */
public class APIHelper {

    private static CloseableHttpClient client;

    public static String getGithubAPI(String url){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
//                .addHeader("authorization", "Basic Y2hlbm11ZW46Q2hlbm11ZW41ODE3Mjgx")
//                .addHeader("cache-control", "no-cache")
//                .addHeader("postman-token", "4de9b8c2-dbfd-75ee-e597-da21968aa417")
                .build();
        Response response = null;
        try {
            long before = new Date().getTime();
            response = client.newCall(request).execute();
            System.out.println(new Date().getTime()-before);
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JsonObject getJson(String expr, String attr){
        try {
            URIBuilder builder = new URIBuilder("https://oxfordhk.azure-api.net/academic/v1.0/evaluate");
            builder.setParameter("expr", expr);
            builder.setParameter("attributes", attr);
            builder.setParameter("subscription-key","f7cc29509a8443c5b3a5e56b0e38b5a6");

            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);

            //long before = new Date().getTime();
            HttpResponse response = client.execute(request);
            //System.out.println(new Date().getTime()-before);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                JsonObject obj = new JsonObject(EntityUtils.toString(entity));
                return obj;
            }
            else return null;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void spideFromAuId(Long auid){
        FileHelper.getExistedIdTable();
        String base = "Id,AA.AuId,AA.AfId,F.FId,J.JId,C.CId";
        String expr_base = "Composite(AA.AuId=";
        List<Long> queue = new LinkedList<>();
        List<Long> existed = FileHelper.auidTable;
        queue.add(auid);
        int head = 0; int rear = 0;
        while (head <= rear){
            Long toGet = queue.get(head);
            String expr = expr_base+toGet+")";
            JsonObject obj = getJson(expr, base);
            JsonArray array = obj.getJsonArray("entities");
            if (array!=null){
                FileHelper.saveToFile(array, "src/main/resources/papers.txt");
                for (int i = 0; i < array.size(); i++) {
                    JsonObject single = array.getJsonObject(i);
                    JsonArray aa = single.getJsonArray("AA");
                    if (aa!=null){
                        for (int j = 0; j < aa.size(); j++) {
                            JsonObject aaSingle = aa.getJsonObject(j);
                            if (aaSingle!=null){
                                Long singleAuid = aaSingle.getLong("AuId");
                                if (!existed.contains(singleAuid)){
                                    existed.add(singleAuid);
                                    queue.add(singleAuid);
                                    rear++;
                                }
                            }
                        }
                    }
                }
                FileHelper.updateTables(FileHelper.idTable, existed);
            }
            head++;
        }
    }

    public static void main(String[] args) {
        client = HttpClients.createDefault();
//        String base = "Id,AA.AuId,AA.AfId,F.FId,J.JId,C.CId";
//        JsonObject obj1 = getJson("Composite(AA.AuId=2239559232)", base);
//        System.out.println(obj1.encode());
//        JsonArray array = obj1.getJsonArray("entities");
//////        System.out.println(array.encode());
//        for (int i = 0; i < array.size(); i++) {
//            JsonObject obj = array.getJsonObject(i);
//            JsonArray aa = obj.getJsonArray("AA");
//            if (aa!=null)
//                for (int j = 0; j < aa.size(); j++) {
//                    JsonObject aaSingle = aa.getJsonObject(j);
//                    if (aaSingle!=null){
//                        System.out.println(aaSingle.getLong("AuId"));
//                    }
//                }
//        }
        //FileHelper.saveToFile(array, "src/main/resources/papers.txt");
        spideFromAuId(Long.parseLong("2313280611"));

        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
