package dataservice;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.http.impl.client.HttpClients;
import po.Entity;
import utility.APIHelper;
import utility.FileHelper;
import utility.SearchType;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by raychen on 16/5/7.
 */
public class DataImpl implements DataService {

    //public static List<Entity> allEntities = FileHelper.getFromFile("papers.txt");

    public DataImpl() {

    }

    //opt
    @Override
    public List<Entity> getEntities(Long id, SearchType type) {
        List<Entity> ret = new LinkedList<>();
//        for (Entity e : allEntities) {
//            switch (type) {
//                case ID:
//                    if (e.getId().equals(id)) ret.add(e);
//                    break;
//                case FID:
//                    if (e.getFids().contains(id)) ret.add(e);
//                    break;
//                case CID:
//                    if (e.getCid()!=null && e.getCid().equals(id)) ret.add(e);
//                    break;
//                case JID:
//                    if (e.getJid()!=null && e.getJid().equals(id)) ret.add(e);
//                    break;
//                case AFID:
//                    if (e.getAfids().contains(id)) ret.add(e);
//                    break;
//                case AUID:
//                    if (e.getAuids().contains(id)) ret.add(e);
//                    break;
//                default:
//                    break;
//            }
//        }
//        if (ret.size() == 0)
        ret = getFromWeb(id, type);
//        System.out.println("id: "+id+" "+type.toString()+" "+"size:"+ret.size());
        return ret;
    }

    public static List<Entity> getFromWeb(Long id, SearchType type) {
//        System.out.println("from web: "+id+" "+type.toString());
        String base = "Id,RId,AA.AuId,AA.AfId,F.FId,J.JId,C.CId";
        String expr_base = "Composite(" + type.toString() + "=" + id + ")";
        APIHelper.client = HttpClients.createDefault();
        if (type == SearchType.ID) expr_base = "Id=" + id;
        if (type == SearchType.RID) expr_base = "RId="+ id;
        List<Entity> ret = new LinkedList<>();
        JsonObject obj = APIHelper.getJson(expr_base, base);
        if (obj == null) return ret;
        JsonArray array = obj.getJsonArray("entities");
        if (array == null) return ret;
        for (int i = 0; i < array.size(); i++) {
            JsonObject objs = array.getJsonObject(i);
            ret.add(FileHelper.convertToEntity(objs));
        }
        try {
            APIHelper.client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private void addMore(Long id, SearchType type) {
        String base = "Id,RId,AA.AuId,AA.AfId,F.FId,J.JId,C.CId";
        String expr_base = "Composite(" + type.toString() + "=" + id + ")";
        if (type == SearchType.ID) expr_base = "Id=" + id;
        JsonObject obj = APIHelper.getJson(expr_base, base);
        JsonArray array = obj.getJsonArray("entities");
        for (int i = 0; i < array.size(); i++) {
            JsonObject objs = array.getJsonObject(i);
            JsonArray aa = objs.getJsonArray("AA");
            if (aa != null)
                for (int j = 0; j < aa.size(); j++) {
                    JsonObject aaSingle = aa.getJsonObject(j);
                    if (aaSingle != null) {
                        APIHelper.spideFromAuId(aaSingle.getLong("AuId"));
                    }
                }
        }
    }

    public static void main(String[] args) {
//        DataService service = new DataImpl(FileHelper.getFromFile("src/main/resources/papers.txt"));
//        long before = new Date().getTime();
//        List<Entity> relatives = service.getEntities(Long.parseLong("1910645507"), SearchType.FID);
//        System.out.println(new Date().getTime() - before);
//        for (Entity e : relatives) {
//            System.out.println(e.getId());
//        }
        List<Entity> entities = getFromWeb(Long.parseLong("1910645507"), SearchType.ID);
        System.out.println(entities.size());
//        JsonObject obj = APIHelper.getJson("Composite(AA.AuId=2304075454)","Id,RId,AA.AuId,AA.AfId,F.FId,J.JId,C.CId");
//        System.out.println(obj.encode());
    }

}
