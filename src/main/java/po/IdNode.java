package po;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* beautyOfPrograming/po/IdNode.java
* @author cxworks
* 2016年5月8日 下午3:18:43
*/

public class IdNode {
	public long id;
	List<IdNode> subID;
	List<AuidNode> subAuid;
	public Entity entity;
	public IdNode(long id,Entity entity) {
		this.id=id;
		this.entity=entity;
	}
	public void buildSubID(Map<Long, Entity> src){
		subID=src
				.entrySet()
				.stream()
				.map(entry->new IdNode(entry.getKey(), entry.getValue()))
				.collect(Collectors.toList()	);
	}
	public void buildSubAuid(Map<Long, List<Entity>> src){
		subAuid=src.entrySet().stream()
				.map(e->new AuidNode(e.getKey(), e.getValue()))
				.collect(Collectors.toList());
	}
}
