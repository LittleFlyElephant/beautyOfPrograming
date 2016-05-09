package utility;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import po.Entity;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by raychen on 16/5/6.
 */
public class APIHelper {

    public static CloseableHttpClient client;
    public static String root = "";

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
            System.out.println("api:"+e.getMessage());
            return null;
        }
    }

    public static void spideFromAuId(Long auid){
        //FileHelper.getExistedIdTable();
        String base = "Id,RId,AA.AuId,AA.AfId,F.FId,J.JId,C.CId";
        String expr_base = "Composite(AA.AuId=";
        List<Long> queue = new LinkedList<>();
        List<Long> existed = FileHelper.auidTable;
        queue.add(auid);
        int head = 0; int rear = 0;long times = 1;
        while (head <= rear){
            Long toGet = queue.get(head);
            String expr = expr_base+toGet+")";
            JsonObject obj = getJson(expr, base);
            System.out.println("get times:"+times);
            times++;
            if (obj == null) {
                head++;
                continue;
            }
            JsonArray array = obj.getJsonArray("entities");
            if (array!=null){
                FileHelper.saveToFile(array);
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
                FileHelper.updateTables(existed);
            }
            head++;
        }
    }

    public static void main(String[] args) {
//        String connectionUrl = "jdbc:sqlserver://zz1tqcs98b.database.chinacloudapi.cn:1433;database=njuccc;user=njuuser@zz1tqcs98b;password={此处为你的密码};encrypt=true;hostNameInCertificate=*.database.chinacloudapi.cn;loginTimeout=30;"
        Entity t = new Entity();
        System.out.println(t.getCid());
    }

//    public static void main(String[] args) {
//        client = HttpClients.createDefault();
////        String base = "Id,AA.AuId,AA.AfId,F.FId,J.JId,C.CId";
////        JsonObject obj1 = getJson("Composite(AA.AuId=2239559232)", base);
////        System.out.println(obj1.encode());
////        JsonArray array = obj1.getJsonArray("entities");
////////        System.out.println(array.encode());
////        for (int i = 0; i < array.size(); i++) {
////            JsonObject obj = array.getJsonObject(i);
////            JsonArray aa = obj.getJsonArray("AA");
////            if (aa!=null)
////                for (int j = 0; j < aa.size(); j++) {
////                    JsonObject aaSingle = aa.getJsonObject(j);
////                    if (aaSingle!=null){
////                        System.out.println(aaSingle.getLong("AuId"));
////                    }
////                }
////        }
//        //FileHelper.saveToFile(array, "src/main/resources/papers.txt");
//        FileHelper.getExistedIdTable();
//        List<Long> existed = FileHelper.auidTable;
//        Long i = new Long(0);
//        for (Long auid: existed) {
//            spideFromAuId(auid);
//            System.out.println(i+" yes+++++++++");
//            i++;
//        }
//        //spideFromAuId(Long.parseLong("1591454549"));
//
//        try {
//            client.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
