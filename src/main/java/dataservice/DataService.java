package dataservice;

import po.Entity;
import utility.SearchType;

import java.util.List;

/**
 * Created by raychen on 16/5/7.
 */
public interface DataService {

    /**
     * 根据id和type得到所有paper的List
     * @param id
     * @param type
     * @return
     */
    public List<Entity> getEntities(Long id, SearchType type);
}
