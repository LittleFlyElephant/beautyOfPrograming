package calculate;

import build.BuildMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import po.TreeNode;

import java.util.List;

/**
 * Created by Lenovo on 2016/5/7.
 */
public class CalImpl implements CalService {


    @Override
    public String calculate(String id1, String id2) {
        BuildMap map = new BuildMap();
        int i = 0,j = 0,k = 0;
        long startLong = Long.parseLong(id1);
        long endLong = Long.parseLong(id2);

        TreeNode[] node = map.get(startLong,endLong);

        if(node.length < 2) return null;

        TreeNode start = node[0];
        TreeNode end = node[1];

        JsonArray array = new JsonArray();

        //hop-1
        List<TreeNode> start1 = start.getNext();
        int start1Len = start1.size();
        for(i = 0;i < start1Len;i++){
            if (start1.get(i).n == endLong){
                JsonArray tempArray = new JsonArray();
                tempArray.add(startLong);
                tempArray.add(endLong);
                array.add(tempArray);
                break;
            }
        }
        //hop-2
        List<TreeNode> end1 = end.getNext();
        int end1Len = end1.size();
        for(i = 0;i < start1Len;i++){
            long startNum = start1.get(i).n;
            for(j = 0;j < end1Len;j++){
                if (startNum == end1.get(j).n){
                    JsonArray tempArray = new JsonArray();
                    tempArray.add(startLong);
                    tempArray.add(startNum);
                    tempArray.add(endLong);
                    array.add(tempArray);
                    break;
                }
            }
        }
        //hop-3
        for (i = 0;i < start1Len;i++){
            List<TreeNode> start2temp = start1.get(i).getNext();
            long start1num = start1.get(i).n;
            int start2tempLen = start2temp.size();

            for (j = 0;j < start2tempLen;j++){
                long start2tempNum = start2temp.get(j).n;
                for (k = 0;k < end1Len;k++){
                    if (start2tempNum == end1.get(k).n){
                        JsonArray tempArray = new JsonArray();
                        tempArray.add(startLong);
                        tempArray.add(start1num);
                        tempArray.add(start2tempNum);
                        tempArray.add(endLong);
                        array.add(tempArray);
                        break;
                    }
                }
            }
        }
        return array.encode();
    }
}
