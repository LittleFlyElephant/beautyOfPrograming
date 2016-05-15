package calculate;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.http.impl.client.HttpClients;
import po.AA;
import po.Entity;
import utility.APIHelper;
import utility.FileHelper;
import utility.SearchType;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by raychen on 16/5/14.
 */
public class Logic implements CalService{

    private Node getInputNode(String id){
        Long id_long = Long.parseLong(id);
        List<Entity> e = getFromWeb(id_long, SearchType.AUID, 1000, "Id,AA.AuId,AA.AfId");
        if (e.size() == 0) {
            Node newNode = new Node(id_long, SearchType.ID);
            newNode.hop1 = getFromWeb(id_long, SearchType.ID, 1, "RId,AA.AuId,C.CId,F.FId,J.JId");
            return newNode;
        }
        else {
            Node newNode = new Node(id_long, SearchType.AUID);
            newNode.hop1 = e;
            return newNode;
        }
    }

    private List<Entity> getFromWeb(Long id, SearchType type, int count, String attr){
        APIHelper.client = HttpClients.createDefault();
        //generate expr
        String expr = "Composite("+type.toString()+"="+id+")";
        if (type == SearchType.RID || type == SearchType.ID) expr = type.toString()+"="+id;
        //get from api
        JsonObject jsonWeb = APIHelper.getJson(expr, attr, count);
        while (jsonWeb == null) jsonWeb = APIHelper.getJson(expr, attr, count); // 防止timeout
        //json to entities
        JsonArray entities_json = jsonWeb.getJsonArray("entities");
        List<Entity> entities = new LinkedList<>();
        entities_json.forEach(e -> {
            JsonObject entity_json = (JsonObject) e;
            entities.add(FileHelper.convertToEntity(entity_json));
        });
        return entities;
    }

    private List<Node> getFirstHop(Node node, boolean isEnd){
        List<Node> first_hop = new LinkedList<>();
        List<Entity> related_entities;
        if (node.type == SearchType.AUID){
            related_entities = node.hop1;
            related_entities.forEach(e -> {
                //--> id
                Node newNode = new Node(e.getId(), SearchType.ID);
                newNode.hop1 = getFromWeb(e.getId(), SearchType.ID, 1, "RId,AA.AuId,C.CId,F.FId,J.JId");
                first_hop.add(newNode);
                //--> afid
                List<AA> authors = e.getAas();
                authors.forEach(a -> {
                    if (a.auid.equals(node.id) && a.afid!=null)
//                    if (a.afid!=null) //change
                        first_hop.add(new Node(a.afid, SearchType.AFID));
                });
            });
        }
        else if (node.type == SearchType.ID){
            related_entities = node.hop1;
            related_entities.forEach(e -> {
                //--> id
                if (!isEnd){ //start --> rid
                    List<Long> rids = e.getRids();
                    rids.forEach(r -> {
                        Node newNode = new Node(r, SearchType.ID);
                        newNode.hop1 = getFromWeb(r, SearchType.ID, 1, "RId,AA.AuId,C.CId,F.FId,J.JId");
                        first_hop.add(newNode);
                    });
                }else { //end rid -->
                    List<Entity> ids = getFromWeb(node.id, SearchType.RID, 10000, "Id");
                    ids.forEach(id -> {
                        Node newNode = new Node(id.getId(), SearchType.ID);
                        newNode.hop1 = getFromWeb(id.getId(), SearchType.ID, 1, "RId,AA.AuId,C.CId,F.FId,J.JId");
                        first_hop.add(newNode);
                    });
                }
                //--> auid
                List<AA> authors = e.getAas();
                authors.forEach(a -> {
                    Node newNode = new Node(a.auid, SearchType.AUID);
                    newNode.hop1 = getFromWeb(a.auid, SearchType.AUID, 1000, "Id,AA.AuId,AA.AfId");
                    first_hop.add(newNode);
                });
                //--> fid
                List<Long> fids = e.getFids();
                fids.forEach(f -> first_hop.add(new Node(f, SearchType.FID)));
                //--> cid
                if (e.getCid() != null) first_hop.add(new Node(e.getCid(), SearchType.CID));
                //--> jid
                if (e.getJid() != null) first_hop.add(new Node(e.getJid(), SearchType.JID));
            });
        }

        return first_hop;
    }

    private boolean hasRelations(Node start, Node end){
        switch (start.type){
            case ID:
                Entity hop1 = start.hop1.get(0);
                switch (end.type){
                    case ID: if (hop1.getRids().contains(end.id)) return true;break;
                    case AUID:
                        for (AA author: hop1.getAas()) {
                            if (author.auid.equals(end.id)) return true;
                        }break;
                    case FID: if (hop1.getFids().contains(end.id)) return true;break;
                    case JID: if (hop1.getJid()!=null && hop1.getJid().equals(end.id)) return true;break;
                    case CID: if (hop1.getCid()!=null && hop1.getCid().equals(end.id)) return true;break;
                    default: break;
                }
                break;
            case AUID:
                List<Entity> hop1_list = start.hop1;
                switch (end.type){
                    case ID:
                        Entity hop2 = end.hop1.get(0);
                        for (AA author: hop2.getAas()) {
                            if (author.auid.equals(start.id)) return true;
                        }break;
                    case AFID:
                        for (Entity e: hop1_list) {
                            for (AA author: e.getAas()) {
                                if (author.auid.equals(start.id) && author.afid!=null && author.afid.equals(end.id)) return true;
//                                if (author.afid!=null && author.afid.equals(end.id)) return true;
                            }
                        }break;
                    default:break;
                }
                break;
            case AFID:
                if (end.type == SearchType.AUID) return hasRelations(end, start);
                break;
            default:
                if (end.type == SearchType.ID) return hasRelations(end, start);
                break;
        }
        return false;
    }

    @Override
    public String calculate(String id1, String id2) {
        Node start = getInputNode(id1);
        Node end = getInputNode(id2);
        System.out.println("end of to node --");
        //both 1-hop
        List<Node> start_hop = getFirstHop(start, false);
        System.out.println("end of start first hop -- "+start_hop.size());
        List<Node> end_hop = getFirstHop(end, true);
        System.out.println("end of first hop"+end_hop.size());
        //get answer
        JsonArray answer = new JsonArray();
        List<JsonArray> answer_list = new LinkedList<>();
        start_hop.forEach(node_start -> {
            if (node_start.id.equals(end.id) && node_start.type == end.type){//hop-1
                JsonArray single = new JsonArray();
                single.add(start.id);
                single.add(end.id);
                answer_list.add(single);
            }
            {
                end_hop.forEach(node_end -> {
                    if (node_start.id.equals(node_end.id) && node_start.type == node_end.type){//hop-2
                        JsonArray single = new JsonArray();
                        single.add(start.id);
                        single.add(node_start.id);
                        single.add(end.id);
                        answer_list.add(single);
                    }
                    {
//                        if (!node_end.id.equals(start.id) && //change
                        if (hasRelations(node_start, node_end)){//hop-3
                            JsonArray single = new JsonArray();
                            single.add(start.id);
                            single.add(node_start.id);
                            single.add(node_end.id);
                            single.add(end.id);
                            answer_list.add(single);
                        }
                    }
                });
            }
        });

        List<JsonArray> answer_distinct = answer_list.stream().distinct().collect(Collectors.toList());
        Collections.sort(answer_distinct, ((o1, o2) -> o1.size() - o2.size()));
        answer_distinct.forEach(single -> {
            System.out.println("path: "+single.encode());
            answer.add(single);
        });
//        System.out.println(answer_distinct.size());

//        FileHelper.saveSingleAns(answer.encode(), Handler.ji);
        return answer.encode();
    }

}
