package utility;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by raychen on 16/5/7.
 */
public class FileHelper {

    public static List<Long> idTable;
    public static List<Long> auidTable;
    private static String idTablePath = "src/main/resources/papers_id.txt";
    public static String auidTablePath = "src/main/resources/papers_auid.txt";

    public static void getExistedIdTable(){
        idTable = new LinkedList<>();
        auidTable = new LinkedList<>();
        try {
            FileReader fr = new FileReader(idTablePath);
            BufferedReader br = new BufferedReader(fr);
            FileReader afr = new FileReader(auidTablePath);
            BufferedReader abr = new BufferedReader(afr);
            String line;
            while ((line = br.readLine()) != null){
                idTable.add(Long.parseLong(line));
            }
            while ((line = abr.readLine()) != null){
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

    public static void updateTables(List<Long> newIds, List<Long> newAuids){

        try {
            FileWriter ifw = new FileWriter(idTablePath);
            BufferedWriter ibw = new BufferedWriter(ifw);
            FileWriter afw = new FileWriter(auidTablePath);
            BufferedWriter abw = new BufferedWriter(afw);

            for (Long i: newIds) {
                ibw.write(i.toString());
                ibw.newLine();
            }
            for (Long i: newAuids) {
                abw.write(i.toString());
                abw.newLine();
            }

            ibw.flush();
            abw.flush();
            ibw.close();
            abw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveToFile(JsonArray entities, String path){
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        //getExistedIdTable();
        try {
            fileWriter = new FileWriter(path, true);
            bufferedWriter = new BufferedWriter(fileWriter);

            for (int i = 0; i < entities.size(); i++) {
                JsonObject obj = entities.getJsonObject(i);
                Long id = obj.getLong("Id");
                if (!idTable.contains(id)){
                    idTable.add(id);
                    System.out.println(id);
                    bufferedWriter.write(entities.getJsonObject(i).encode());
                    bufferedWriter.newLine();
                }
            }

            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.print("FileHelper: ");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static JsonArray getFromFile(String path){
        JsonArray entities = new JsonArray();
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null){
                entities.add(new JsonObject(line));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entities;
    }
}
