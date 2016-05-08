package dataservice;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import po.Entity;
import utility.APIHelper;
import utility.FileHelper;
import utility.SearchType;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by raychen on 16/5/7.
 */
public class DataImpl implements DataService {

    private List<Entity> allEntities;

    public DataImpl(List<Entity> allEntities) {
        this.allEntities = allEntities;
    }

    //opt
    @Override
    public List<Entity> getEntities(Long id, SearchType type) {
        List<Entity> ret = new LinkedList<>();
        for (Entity e : allEntities) {
            switch (type) {
                case ID:
                    if (e.getId().equals(id)) ret.add(e);
                    break;
                case FID:
                    if (e.getFids().contains(id)) ret.add(e);
                    break;
                case CID:
                    if (e.getCid()!=null && e.getCid().equals(id)) ret.add(e);
                    break;
                case JID:
                    if (e.getJid()!=null && e.getJid().equals(id)) ret.add(e);
                    break;
                case AFID:
                    if (e.getAfids().contains(id)) ret.add(e);
                    break;
                case AUID:
                    if (e.getAuids().contains(id)) ret.add(e);
                    break;
                default:
                    break;
            }
        }
        //if (ret.size() == 0) ret = getFromWeb(id, type);
        return ret;
    }

    private List<Entity> getFromWeb(Long id, SearchType type) {
        String base = "AA.AuId";
        String expr_base = "Composite(" + type.toString() + "=" + id + ")";
        if (type == SearchType.ID) expr_base = "Id=" + id;
        List<Entity> ret = new LinkedList<>();
        JsonObject obj = APIHelper.getJson(expr_base, base);
        if (obj == null) return ret;
        JsonArray array = obj.getJsonArray("entities");
        for (int i = 0; i < array.size(); i++) {
            JsonObject objs = array.getJsonObject(i);
            ret.add(FileHelper.convertToEntity(objs));
        }
        return ret;
    }

    private void addMore(Long id, SearchType type) {
        String base = "AA.AuId";
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

//    public static void main(String[] args) {
//        DataService service = new DataImpl(FileHelper.getFromFile("src/main/resources/papers.txt"));
//        long before = new Date().getTime();
//        List<Entity> relatives = service.getEntities(Long.parseLong("97744766"), SearchType.FID);
//        System.out.println(new Date().getTime() - before);
//        for (Entity e : relatives) {
//            System.out.println(e.getId());
//        }
//    }

}
