package calculate;

import build.BuildMap;
import dataservice.DataImpl;
import dataservice.DataService;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import po.AuidNode;
import po.Entity;
import po.IdNode;
import utility.FileHelper;
import utility.SearchType;

import java.util.List;
import java.util.Map;

/**
 * Created by Lenovo on 2016/5/7.
 */
public class CalImpl implements CalService {


    @Override
    public String calculate(String start, String end) {
        BuildMap map = new BuildMap();
//        DataService service = new DataImpl(FileHelper.getFromFile("src/main/resources/papers.txt"));
        DataService service = new DataImpl();
        int i = 0,j = 0,k = 0;
        Long id1 = Long.parseLong(start);
        Long id2 = Long.parseLong(end);

        System.out.println("start " + start);

        boolean id1isid = false;
        boolean id2isid = false;
        IdNode id1node = null,id2node = null;
        AuidNode auid1node = null,auid2node = null;
        JsonArray array = new JsonArray();
        //
        List<Entity> entitie1=service.getEntities(id1, SearchType.ID);
        if (entitie1.size()==1 && entitie1.get(0).getAuids().size() == 0){
            entitie1=service.getEntities(id1,SearchType.AUID);
            id1isid=false;
            auid1node=map.getAuidTree(id1,entitie1,service);

        }else {
            id1isid=true;
            id1node=map.getIdTree(id1,entitie1.get(0),service);
        }
        //
        List<Entity> entitie2=service.getEntities(id2, SearchType.ID);
        if (entitie2.size()==1 && entitie2.get(0).getAuids().size() == 0){
            entitie2=service.getEntities(id2,SearchType.AUID);
            id2isid=false;
            auid2node=map.getAuidTree(id2,entitie2,service);

        }else {
            id2isid=true;
            id2node=map.getIdTree(id2,entitie2.get(0),service);
        }

//        System.out.println("finish build");

        //1-hop
        if (id2isid){
            List<IdNode> subID1;
            if(id1isid) subID1 = id1node.subID;
            else subID1 = auid1node.subID;
            int subID1Len = subID1.size();

            for (i = 0;i < subID1Len;i++){
//                System.out.println("id     " + subID1.get(i).id);
                if (id2.equals(subID1.get(i).id)){
                    JsonArray tempArray = new JsonArray();
                    tempArray.add(id1);
                    tempArray.add(id2);
                    array.add(tempArray);

//                    System.out.println("add1-1");

                    break;
                }
            }
        }else{
            List<AuidNode> subAuid1;
            if(id1isid) subAuid1 = id1node.subAuid;
            else subAuid1 = auid1node.subAuid;
            int subAuid1Len = subAuid1.size();

            for (i = 0;i < subAuid1Len;i++){
//                System.out.println("auid     " + subAuid1.get(i).auid);
                if (id2.equals(subAuid1.get(i).auid)){
                    JsonArray tempArray = new JsonArray();
                    tempArray.add(id1);
                    tempArray.add(id2);
                    array.add(tempArray);

//                    System.out.println("add1-2");

                    break;
                }
            }
        }
        //2-hop
        List<IdNode> subID1;
        List<AuidNode> subAuid1;
        List<IdNode> subID2;
        List<AuidNode> subAuid2;
        int subID1_len;
        int subID2_len;
        int subAuid1_len;
        int subAuid2_len;
        if (id1isid && id2isid){
            subID1 = id1node.subID;
            subAuid1 = id1node.subAuid;
            subID2 = id2node.subID;
            subAuid2 = id2node.subAuid;
        }else if (id1isid && (!id2isid)){
            subID1 = id1node.subID;
            subAuid1 = id1node.subAuid;
            subID2 = auid2node.subID;
            subAuid2 = auid2node.subAuid;
        }else if ((!id1isid) && (id2isid)){
            subID1 = auid1node.subID;
            subAuid1 = auid1node.subAuid;
            subID2 = id2node.subID;
            subAuid2 = id2node.subAuid;
        }else{
            subID1 = auid1node.subID;
            subAuid1 = auid1node.subAuid;
            subID2 = auid2node.subID;
            subAuid2 = auid2node.subAuid;
        }
        subID1_len = subID1.size();
        subID2_len = subID2.size();
        subAuid1_len = subAuid1.size();
        subAuid2_len = subAuid2.size();

        for (i = 0;i < subID1_len;i++){
            Long subTemp = subID1.get(i).id;
            for (j = 0;j < subID2_len;j++){
                if(subTemp.equals(subID2.get(j).id)){
                    JsonArray tempArray = new JsonArray();
                    tempArray.add(id1);
                    tempArray.add(subTemp);
                    tempArray.add(id2);
                    array.add(tempArray);

//                    System.out.println("add2-1");

                    break;
                }
            }
        }
        for (i = 0;i < subAuid1_len;i++){
            Long subTemp = subAuid1.get(i).auid;
            for (j = 0;j < subAuid2_len;j++){
                if (subTemp.equals(subAuid2.get(j).auid)){
                    JsonArray tempArray = new JsonArray();
                    tempArray.add(id1);
                    tempArray.add(subTemp);
                    tempArray.add(id2);
                    array.add(tempArray);
//                    System.out.println(tempArray);

//                    System.out.println("add2-2");

                    break;
                }
            }
        }
        //3-hop
        List<Long> fids1;
        List<Long> fids2;
        List<Long> rid1;
//        List<Long> rid2;
        Long cid1;
        Long cid2;
        Long jid1;
        Long jid2;
        boolean findFile = false;
        boolean findRid = false;
        int f1size = 0,f2size = 0;
        int ridSize = 0;
        //subid1 and subid2
        for (i = 0;i < subID1_len;i++){
            Long sub1ID = subID1.get(i).id;
            fids1 = subID1.get(i).entity.getFids();
            cid1 = subID1
                    .get(i)
                    .entity
                    .getCid();
            jid1 = subID1.get(i).entity.getJid();
            if (fids1 != null){
                f1size = fids1.size();
            }
            rid1 = subID1.get(i).entity.getRids();

            if (rid1 != null){ridSize = rid1.size();}

            for (j = 0;j < subID2_len;j++){
                Long sub2ID = subID2.get(j).id;
                if (sub2ID.equals(id1)) continue;
                if (sub1ID.equals(id2)) continue;
                fids2 = subID2.get(j).entity.getFids();
                cid2 = subID2.get(j).entity.getCid();
                jid2 = subID2.get(j).entity.getJid();
                if (fids2 != null){f2size = fids2.size();}
//                rid2 = subID2.get(j).entity.getRids();

                for (k = 0;k < f1size;k++){
                    Long tempFile = fids1.get(k);
                    for (int r = 0;r < f2size;r++){
                        if (tempFile.equals(fids2.get(r))){
                            findFile = true;
                            break;
                        }
                    }
                    if (findFile) break;
                }
                for (k = 0;k < ridSize;k++){
                    if (sub2ID.equals(id1)) continue;
                    if (sub1ID.equals(id2)) continue;
                    if (rid1.get(k).equals(sub2ID)){
                        findRid = true;
                        break;
                    }
                }

                boolean findCID = false;
                boolean findJID = false;

                if ((cid1 != null)&&(cid2 != null)&&(cid1.equals(cid2))) findCID = true;
                if ((jid1 != null)&&(jid2 != null)&&(jid1.equals(jid2))) findJID = true;

                if (findRid || findFile || findCID || findJID){
                    if (!sub2ID.equals(id1) && !sub1ID.equals(id2)) {
                        JsonArray tempArray = new JsonArray();
//                        System.out.println("test:"+id1+" "+sub1ID+" "+sub2ID+" "+id2);
                        tempArray.add(id1);
                        tempArray.add(sub1ID);
                        tempArray.add(sub2ID);
                        tempArray.add(id2);
                        array.add(tempArray);
                    }
                }
                findFile = false;
                findRid = false;
            }
        }
        //subid1 and subAuid2
        List<Entity> authEntity2;
        for (i = 0;i < subID1_len;i++){
            Long sub1ID = subID1.get(i).id;
            for (j = 0;j < subAuid2_len;j++){
                Long sub2ID = subAuid2.get(j).auid;
                authEntity2 = subAuid2.get(j).entities;
                int entitySize = 0;
                if (authEntity2 != null){authEntity2.size();}

                for (k = 0;k < entitySize;k++){
                    if (sub1ID.equals(authEntity2.get(k).getId())){
                        if (!sub2ID.equals(id1) && !sub1ID.equals(id2)) {
                            JsonArray tempArray = new JsonArray();
                            tempArray.add(id1);
                            tempArray.add(sub1ID);
                            tempArray.add(sub2ID);
                            tempArray.add(id2);
                            array.add(tempArray);
                            break;
                        }
                    }
                }
            }
        }
        //subAuid1 and subId2
        List<Entity> authEntity1;
        int authEntity1Size = 0;
        boolean findEntity = false;
        for (i = 0;i < subAuid1_len;i++){
            Long sub1ID = subAuid1.get(i).auid;
            authEntity1 = subAuid1.get(i).entities;
            if (authEntity1 != null){authEntity1Size = authEntity1.size();}

            for (j = 0;j < authEntity1Size;j++){
                Long entityID = authEntity1.get(j).getId();
                for (k = 0;k < subID2_len;k++){
                    if (entityID.equals(subID2.get(k).id)){
                        if (!subID2.get(k).id.equals(id1) && !sub1ID.equals(id2)) {
                            JsonArray tempArray = new JsonArray();
                            tempArray.add(id1);
                            tempArray.add(sub1ID);
                            tempArray.add(subID2.get(k).id);
                            tempArray.add(id2);
                            array.add(tempArray);
                            findEntity = true;
                            break;
                        }
                    }
                }
                if (findEntity) break;
            }
            findEntity = false;
        }

        //subAuid1 and subAuid2
        List<Long> fID1;
        List<Long> fID2;
        boolean findAuth = false;
        for (i = 0;i < subAuid1_len;i++){
            Long sub1ID = subAuid1.get(i).auid;
            authEntity1 = subAuid1.get(i).entities;
            int authE1Size = 0;
            if (authEntity1 != null){authEntity1.size();}

            for (j = 0;j < authE1Size;j++){
                Entity e1 = authEntity1.get(j);
                fID1 = e1.getFids();
                int f1Size = 0;
                if (fID1 != null){fID1.size();}
                for (k = 0;k < subAuid2_len;k++){
                    Long sub2ID = subAuid2.get(k).auid;
                    authEntity2 = subAuid2.get(k).entities;
                    int authE2Size = 0;
                    if (authEntity2 != null){authEntity2.size();}
                    for (int r = 0;r < authE2Size;r++){
                        Entity e2 = authEntity2.get(r);
                        fID2 = e2.getFids();
                        int f2Size = 0;
                        if (fID2 != null ){fID2.size();}
                        for (int l = 0;l < f1size;l++){
                            Long f1IDnow = fID1.get(l);
                            for (int m = 0;m < f2Size;m++){
                                if(f1IDnow.equals(fID2.get(m))){
                                    if (!sub2ID.equals(id1) && !sub1ID.equals(id2)) {
                                        JsonArray tempArray = new JsonArray();
                                        tempArray.add(id1);
                                        tempArray.add(sub1ID);
                                        tempArray.add(sub2ID);
                                        tempArray.add(id2);
                                        array.add(tempArray);
                                        findAuth = true;
                                        break;
                                    }
                                }
                            }
                            if (findAuth) break;
                        }
                    if (findAuth) break;
                    }
                    findAuth = false;
                }
            }
        }
//        System.out.println(array.size());
        return array.encode();
    }
}
