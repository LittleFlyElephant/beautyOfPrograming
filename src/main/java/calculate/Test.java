package calculate;

import dataservice.DataService;
import io.vertx.core.json.JsonArray;
import po.AA;
import po.Entity;
import utility.FileHelper;
import utility.SearchType;
import web.Handler;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by raychen on 16/5/10.
 */
public class Test implements CalService {

    DataService service;
    JsonArray ansDFS;

    public Test(DataService dataService) {
        service = dataService;
    }

    private void addToNodes(List<Long> toAdd, SearchType type, List<Node> nodes){
        for (Long l: toAdd) {
            Node n = new Node(l, type);
            n.hop1 = service.getEntities(l, type);
            nodes.add(n);
        }
    }

    private List<Node> getNextHop(Long id, SearchType type, boolean isEnd){
        List<Node> nodes = new LinkedList<>();
        List<Entity> entities = service.getEntities(id, type);
        if (type == SearchType.AUID){
            for (Entity e: entities) {
                Node n = new Node(e.getId(), SearchType.ID);
                n.hop1 = service.getEntities(e.getId(), SearchType.ID);
                nodes.add(n);
                for (AA a: e.getAas()) {
                    if (a.auid==id && a.afid != null) {
                        Node node = new Node(a.afid, SearchType.AFID);
                        node.aa = a;
                        node.hop1 = service.getEntities(a.afid, SearchType.AFID);
                        nodes.add(node);
                    }
                }
            }
        }else if (type == SearchType.ID){
            for (Entity e: entities) {
                if (e.getCid()!=null) {
                    Node n = new Node(e.getCid(), SearchType.CID);
                    n.hop1 = service.getEntities(e.getCid(), SearchType.CID);
                    nodes.add(n);
                }
                if (e.getJid()!=null) {
                    Node n = new Node(e.getJid(), SearchType.JID);
                    n.hop1 = service.getEntities(e.getJid(), SearchType.JID);
                    nodes.add(n);
                }
                if (!isEnd) {
                    addToNodes(e.getRids(), SearchType.ID, nodes);
                }else {
                    List<Entity> entities_2 = service.getEntities(id, SearchType.RID);
                    for (Entity e2: entities_2) {
                        Node n = new Node(e2.getId(), SearchType.ID);
                        n.hop1 = service.getEntities(e2.getId(), SearchType.ID);
                        nodes.add(n);
                    }
                }
                addToNodes(e.getFids(), SearchType.FID, nodes);
                for (AA a: e.getAas()) {
                    Node node = new Node(a.auid, SearchType.AUID);
                    node.aa = a;
                    node.hop1 = service.getEntities(a.auid, SearchType.AUID);
                    nodes.add(node);
                }
            }
        }
        return nodes;
    }

    private SearchType getIdType(Long id){
        List<Entity> entities = service.getEntities(id, SearchType.AUID);
        if (entities.size()==0 || (entities.size()==1 && entities.get(0).getAas().size() == 0)){
            return SearchType.ID;
        }
        return SearchType.AUID;
    }

    private boolean hasRelation(Node n1, Node n2){
        List<Entity> e1 = n1.hop1;
        switch (n1.type){
            case ID:
                Entity e1_id = e1.get(0);
                switch (n2.type){
                    case ID: if (e1_id.getRids().contains(n2.id)) return true;break;
                    case FID: if (e1_id.getFids().contains(n2.id)) return true;break;
                    case JID: if (e1_id.getJid()!=null && e1_id.getJid().equals(n2.id)) return true;break;
                    case CID: if (e1_id.getCid()!=null && e1_id.getCid().equals(n2.id)) return true;break;
                    case AUID:
                        for (AA aa: e1_id.getAas()) {
                            if (aa.auid.equals(n2.id)) return true;
                        }
                        break;
                    default:break;
                }
                break;
            case AUID:
                switch (n2.type){
                    case ID:
                        for (Entity e: e1) {
                            if (e.getId().equals(n2.id)) return true;
                        }
                        break;
                    case AFID:
                        for (Entity e: e1) {
                            for (AA aa: e.getAas()) {
                                if (aa.afid!=null && aa.auid.equals(n1.id) && aa.afid.equals(n2.id)) return true;
                            }
                        }
                        break;
                    default:break;
                }
                break;
            case AFID:
                if (n2.type == SearchType.AUID){
                    List<Entity> e2 = n2.hop1;
                    for (Entity e: e2) {
                        for (AA aa: e.getAas()) {
                            if (aa.afid!=null && aa.auid.equals(n2.id) && aa.afid.equals(n1.id)) return true;
                        }
                    }
                }
                break;
            default:
                if (n2.type == SearchType.ID){
                    Entity e2 = n2.hop1.get(0);
                    if (n1.type==SearchType.FID && e2.getFids().contains(n1.id)) return true;
                    else if (e2.getCid()!=null && n1.type==SearchType.CID && e2.getCid().equals(n1.id)) return true;
                    else if (e2.getJid()!=null && n1.type==SearchType.JID && e2.getJid().equals(n1.id)) return true;
                }
                break;
        }
        return false;
    }

    private JsonArray method1(String id1, String id2){
        Long id1_t = Long.parseLong(id1);
        Long id2_t = Long.parseLong(id2);
        SearchType id1_type = getIdType(id1_t);
        SearchType id2_type = getIdType(id2_t);
        long before = new Date().getTime();
        List<Node> id1_first = getNextHop(id1_t, id1_type, false);
        List<Node> id2_first = getNextHop(id2_t, id2_type, true);
        long after1 = new Date().getTime();
        System.out.println("end1: "+(after1-before));
        JsonArray ans = new JsonArray();

        for (int i = 0; i < id1_first.size(); i++) {
            Node s_id1 = id1_first.get(i);
            if (s_id1.id.equals(id2_t) && s_id1.type == id2_type){ // 1-hop
                JsonArray s_ans = new JsonArray();
                s_ans.add(id1_t);
                s_ans.add(id2_t);
                System.out.println("find one: "+s_ans.encode());
                ans.add(s_ans);
            }else{
                for (int j = 0; j < id2_first.size(); j++) {
                    Node s_id2 = id2_first.get(j);
                    if (s_id2.id.equals(id1_t) && s_id2.type == id1_type) continue;
                    if (s_id1.id.equals(s_id2.id) && s_id1.type == s_id2.type){ // 2-hop
                        JsonArray s_ans = new JsonArray();
                        s_ans.add(id1_t);
                        s_ans.add(s_id1.id);
                        s_ans.add(id2_t);
                        System.out.println("find one: "+s_ans.encode());
                        ans.add(s_ans);
                    } else if (hasRelation(s_id1, s_id2)){ // 3-hop
                        JsonArray s_ans = new JsonArray();
                        s_ans.add(id1_t);
                        s_ans.add(s_id1.id);
                        s_ans.add(s_id2.id);
                        s_ans.add(id2_t);
                        System.out.println("find one: "+s_ans.encode());
                        ans.add(s_ans);
                    }
                }
            }
        }
        System.out.println("loop: "+(new Date().getTime()-after1));
        return ans;
    }

    private void addTo(List<Long> toAdd, SearchType type, List<Node> nodes){
        for (Long l: toAdd) {
            Node n = new Node(l, type);
            nodes.add(n);
        }
    }

    private List<Node> getDfsNextHop(Node node){
        List<Node> nextHop = new LinkedList<>();
        List<Entity> webs = service.getEntities(node.id, node.type);
        switch (node.type){
            case ID:
                for (Entity e: webs) {
                    addTo(e.getRids(), SearchType.ID, nextHop);
                    addTo(e.getFids(), SearchType.FID, nextHop);
                    for (AA aa: e.getAas()) {
                        nextHop.add(new Node(aa.auid, SearchType.AUID));
                    }
                    if (e.getCid()!=null) nextHop.add(new Node(e.getCid(), SearchType.CID));
                    if (e.getJid()!=null) nextHop.add(new Node(e.getJid(), SearchType.JID));
                }
                break;
            case AUID:
                for (Entity e: webs) {
                    nextHop.add(new Node(e.getId(), SearchType.ID));
                    for (AA aa: e.getAas()) {
                        if (aa.auid.equals(node.id) && aa.afid!=null)
                            nextHop.add(new Node(aa.afid, SearchType.AFID));
                    }
                }
                break;
            case AFID:
                for (Entity e : webs) {
                    for (AA aa: e.getAas()) {
                        if (aa.afid!=null && aa.afid.equals(node.id))
                            nextHop.add(new Node(aa.auid, SearchType.AUID));
                    }
                }
                break;
            default:
                for (Entity e: webs) {
                    nextHop.add(new Node(e.getId(), SearchType.ID));
                }
                break;
        }
        return nextHop;
    }

    private void dfs(Node node, Node end, int hop, JsonArray path){
//        System.out.println("dfs: "+path.encode());
        if (node.id.equals(end.id) && node.type == end.type) {
            JsonArray array = new JsonArray();
            for (Object l: path) {
                array.add((Long) l);
            }
            ansDFS.add(array);
            System.out.println("find one: "+path.encode());
        }
        else if (hop < 3){
            List<Node> nexts = getDfsNextHop(node);
            for (Node next: nexts) {
                if (!path.contains(next.id)) {
                    path.add(next.id);
                    dfs(next, end, hop+1, path);
                    path.remove(path.size()-1);
                }
            }
        }
    }

    @Override
    public String calculate(String id1, String id2) {
        Long id1_t = Long.parseLong(id1);
        Long id2_t = Long.parseLong(id2);
        SearchType id1_type = getIdType(id1_t);
        SearchType id2_type = getIdType(id2_t);

//        Node newNode = new Node(id1_t, id1_type);
//        Node endNode = new Node(id2_t, id2_type);
//
//        ansDFS = new JsonArray();
//        JsonArray path = new JsonArray();
//        path.add(id1_t);
//        System.out.println("start: ");
//        dfs(newNode, endNode, 0, path);
//        System.out.println("end!");
        String ans = method1(id1, id2).encode();
        FileHelper.saveSingleAns(ans, Handler.ji);
        return ans;
    }
}
