package web;

import org.apache.http.impl.client.HttpClients;
import utility.APIHelper;
import utility.FileHelper;

import java.io.IOException;
import java.util.List;

/**
 * Created by raychen on 16/5/8.
 */
public class Spider {
    public static void main(String[] args) {
        APIHelper.client = HttpClients.createDefault();
        spider();
        try {
            APIHelper.client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void spider(){
        FileHelper.getExistedIdTable();
        List<Long> existed = FileHelper.auidTable;
        for (Long auid: existed) {
            APIHelper.spideFromAuId(auid);
        }
    }
}
