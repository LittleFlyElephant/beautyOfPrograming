package calculate;

import po.AA;
import po.Entity;
import utility.SearchType;

import java.util.List;

/**
 * Created by raychen on 16/5/10.
 */
public class Node {
    public Long id;
    public SearchType type;
    public AA aa;
    public List<Entity> hop1;

    public Node(Long id, SearchType type) {
        this.id = id;
        this.type = type;
    }
}
