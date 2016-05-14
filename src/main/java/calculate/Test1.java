package calculate;

import dataservice.DataImpl;
import dataservice.DataService;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import po.AA;
import po.Entity;
import sun.corba.EncapsInputStreamFactory;
import utility.SearchType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 2016/5/14.
 */
public class Test1 implements CalService{
    DataService service = new DataImpl();

    private SearchType getIdType(Long id){
        List<Entity> entities = service.getEntities(id, SearchType.AUID);
        if (entities.size()==0 || (entities.size()==1 && entities.get(0).getAas().size() == 0)){
            return SearchType.ID;
        }
        return SearchType.AUID;
    }


    private boolean hasRelation(Node n1,Node n2){
        if ((n1.type == SearchType.ID) && (n2.type == SearchType.CID)){
            for (Entity e : n1.hop1){
                if (e.getCid().equals(n2.id)) return true;
            }
        }else if ((n1.type == SearchType.CID) && (n2.type == SearchType.ID)){
            for (Entity e : n2.hop1){
                if (e.getCid().equals(n1.id)) return true;
            }
        }else if ((n1.type == SearchType.ID) && (n2.type == SearchType.JID)){
            for (Entity e : n1.hop1){
                if (e.getJid().equals(n2.id)) return true;
            }
        }else if ((n1.type == SearchType.JID) && (n2.type == SearchType.ID)){
            for (Entity e : n2.hop1){
                if (e.getJid().equals(n1.id)) return true;
            }
        }else if ((n1.type == SearchType.ID) && (n2.type == SearchType.FID)){
            for (Entity e : n1.hop1){
                for (Long fid : e.getFids()){
                    if (fid.equals(n2.id)) return true;
                }
            }
        }else if ((n1.type == SearchType.FID) && (n2.type == SearchType.ID)){
            for (Entity e : n2.hop1){
                for (Long fid : e.getFids()){
                    if (fid.equals(n1.id)) return true;
                }
            }
        }else if ((n1.type == SearchType.ID) && (n2.type == SearchType.ID)){
            if (n1.hop1.size()>1) System.out.println("something wrong!!!!!!!!!!!!!!!!!!!!!!!!!!");
            for (Long rid : n1.hop1.get(0).getRids()){
                if (rid.equals(n2.id)) return true;
            }
        }else if ((n1.type == SearchType.ID) && (n2.type == SearchType.AUID)){
            for (Entity e : n2.hop1){
                if (e.getId().equals(n1.id)) return true;
            }
        }else if ((n1.type == SearchType.AUID) && (n2.type == SearchType.ID)){
            for (Entity e : n1.hop1){
                if (e.getId().equals(n2.id)) return true;
            }
        }else if ((n1.type == SearchType.AUID) && (n2.type == SearchType.AFID)){
            if (n1.aa.afid.equals(n2.id)) return true;
        }else if ((n1.type == SearchType.AFID) && (n2.type == SearchType.AUID)){
            if (n2.aa.afid.equals(n1.id)) return true;
        }
        return false;
    }

    private List<Node> getNextHop(Node n1,SearchType type){
        List<Entity> es = n1.hop1;
        List<Node> nodes = new ArrayList<Node>();
        if (type == SearchType.ID){
                if (n1.hop1.size()>1) System.out.println("!!!!!!!!!!something wrong");
                Entity e = n1.hop1.get(0);
                if (e.getCid() != null){
                    Node node = new Node(e.getCid(),SearchType.CID);
                    node.hop1 = service.getEntities(e.getCid(),SearchType.CID);
                    nodes.add(node);
                }
                if (e.getJid() != null){
                    Node node = new Node(e.getJid(),SearchType.JID);
                    node.hop1 = service.getEntities(e.getJid(),SearchType.JID);
                    nodes.add(node);
                }
                if (e.getFids() != null){
                    for (Long fid : e.getFids()){
                        Node node = new Node(fid,SearchType.FID);
                        node.hop1 = service.getEntities(fid,SearchType.FID);
                        nodes.add(node);
                    }
                }
                if (e.getRids() != null){
                    for (Long rid : e.getRids()){
                        Node node = new Node(rid,SearchType.ID);
                        node.hop1 = service.getEntities(rid,SearchType.RID);
                        nodes.add(node);
                    }
                }
                if (e.getAas() != null){
                    for (AA aa : e.getAas()){
                        Node node = new Node(aa.auid,SearchType.AUID);
                        node.hop1 = service.getEntities(aa.auid,SearchType.AUID);
                        nodes.add(node);
                    }
                }
        }else if (type == SearchType.AUID){
            boolean findFid = false;
            for (Entity e : es){
                Node node1 = new Node(e.getId(),SearchType.ID);
                node1.hop1 = service.getEntities(e.getId(),SearchType.ID);
                nodes.add(node1);
                if (!findFid){
                    for (AA aa : e.getAas()){
                        if (aa.auid.equals(n1.id)){
                            Node node = new Node(aa.afid,SearchType.AFID);
                            node.hop1 = service.getEntities(aa.afid,SearchType.FID);
                            nodes.add(node);
                            findFid = true;
                            break;
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public String calculate(String start, String end) {
        Long id1 = Long.parseLong(start);
        Long id2 = Long.parseLong(end);
        JsonArray array = new JsonArray();

        SearchType t1 = getIdType(id1);
        SearchType t2 = getIdType(id2);

        List<Entity> e1 = service.getEntities(id1,t1);
        List<Entity> e2 = service.getEntities(id2,t2);

        Node n1 = new Node(id1,t1);
        n1.hop1 = e1;
        Node n2 = new Node(id2,t2);
        n2.hop1 = e2;

        List<Node> hop1 = getNextHop(n1,t1);
        List<Node> hop2 = getNextHop(n2,t2);

        //1-hop
        if (hasRelation(n1,n2)){
            JsonArray h1 = new JsonArray();
            h1.add(id1);
            h1.add(id2);
            array.add(h1);
        }

        //2-hop
        for (Node node1 : hop1){
            for (Node node2 : hop2){
                if (node1.id.equals(node2.id) && (node1.type == node2.type)){
                    JsonArray h2 = new JsonArray();
                    h2.add(id1);
                    h2.add(node1.id);
                    h2.add(id2);
                    array.add(h2);
                }
            }
        }

        //3-hop
        for (Node node1 : hop1){
            for (Node node2 : hop2){
                if (hasRelation(node1,node2)){
                    JsonArray h3 = new JsonArray();
                    h3.add(id1);
                    h3.add(node1.id);
                    h3.add(node2.id);
                    h3.add(id2);
                    array.add(h3);
                }
            }
        }
        return array.encode();
    }
}
