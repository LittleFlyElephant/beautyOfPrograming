package utility;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import po.AA;
import po.Entity;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by raychen on 16/5/7.
 */
public class FileHelper {

    public static List<Long> idTable;
    public static List<Long> auidTable;
    private static String idTablePath = "papers_id.txt";
    public static String auidTablePath = "papers_auid.txt";
    public static String papersPath = "papers.txt";

    public static void getExistedIdTable() {
        idTable = new LinkedList<>();
        auidTable = new LinkedList<>();
        try {
            FileReader fr = new FileReader(idTablePath);
            BufferedReader br = new BufferedReader(fr);
            FileReader afr = new FileReader(auidTablePath);
            BufferedReader abr = new BufferedReader(afr);
            String line;
            while ((line = br.readLine()) != null) {
                idTable.add(Long.parseLong(line));
            }
            while ((line = abr.readLine()) != null) {
                auidTable.add(Long.parseLong(line));
            }
            br.close();
            abr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateTables(List<Long> newAuids) {

        try {
            FileWriter afw = new FileWriter(auidTablePath);
            BufferedWriter abw = new BufferedWriter(afw);

            for (Long i : newAuids) {
                abw.write(i.toString());
                abw.newLine();
            }

            abw.flush();
            abw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveToFile(JsonArray entities) {
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        //getExistedIdTable();
        try {
            fileWriter = new FileWriter(papersPath, true);
            bufferedWriter = new BufferedWriter(fileWriter);
            FileWriter ifw = new FileWriter(idTablePath, true);
            BufferedWriter ibw = new BufferedWriter(ifw);

            for (int i = 0; i < entities.size(); i++) {
                JsonObject obj = entities.getJsonObject(i);
                Long id = obj.getLong("Id");
                if (!idTable.contains(id)) {
                    idTable.add(id);
                    System.out.println(id);
                    ibw.write(id.toString());
                    ibw.newLine();
                    bufferedWriter.write(entities.getJsonObject(i).encode());
                    bufferedWriter.newLine();
                }
            }

            ibw.flush();
            ibw.close();
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.print("FileHelper: ");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<Entity> getFromFile(String path) {
        List<Entity> entities = new LinkedList<>();
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                entities.add(convertToEntity(new JsonObject(line)));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entities;
    }

    public static Entity convertToEntity(JsonObject jsonObject) {
        Long cid = null;
        List<Long> fids = new LinkedList<>();
        Long jid = null;
        List<AA> aas = new LinkedList<>();
        List<Long> rids = new LinkedList<>();
        JsonObject arrayObj = null;
        JsonArray aa = jsonObject.getJsonArray("AA");
        if (aa != null)
            for (int i = 0; i < aa.size(); i++) {
                arrayObj = aa.getJsonObject(i);
                if (arrayObj != null) {
                    Long auid = arrayObj.getLong("AuId");
                    Long afid = arrayObj.getLong("AfId");
                    if (auid != null) aas.add(new AA(auid, afid));
                }
            }
        JsonObject c = jsonObject.getJsonObject("C");
        if (c != null)
            cid = c.getLong("CId");
        JsonArray f = jsonObject.getJsonArray("F");
        if (f != null)
            for (int i = 0; i < f.size(); i++) {
                arrayObj = f.getJsonObject(i);
                if (arrayObj != null) {
                    Long fid = arrayObj.getLong("FId");
                    if (fid != null) fids.add(fid);
                }
            }
        JsonArray r = jsonObject.getJsonArray("RId");
        if (r != null)
            for (int i = 0; i < r.size(); i++) {
                Long rid = r.getLong(i);
                rids.add(rid);
            }
        JsonObject j = jsonObject.getJsonObject("J");
        if (j != null)
            jid = j.getLong("JId");

        Entity entity = new Entity(jsonObject.getLong("Id"), cid, fids, jid, aas, rids);
        return entity;
    }

    public static void saveSingleAns(String ans, int i) {
        try {
            FileWriter afw = new FileWriter("ans"+i+".txt");
            BufferedWriter abw = new BufferedWriter(afw);

            abw.write(ans);
            abw.newLine();

            abw.flush();
            abw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getSingleAns(int i) {
        try {
            FileReader fr = new FileReader("ans"+i+".txt");
            BufferedReader br = new BufferedReader(fr);
            String line;
            line = br.readLine();
            br.close();
            return line;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
