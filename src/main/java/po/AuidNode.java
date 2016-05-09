package po;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* beautyOfPrograming/po/AuidNode.java
* @author cxworks
* 2016年5月8日 下午3:19:19
*/

public class AuidNode {
	public Long auid;
	public List<IdNode> subID;
	public List<AuidNode> subAuid;
	public List<Entity> entities;
	public AuidNode(long auid,List<Entity> entities) {
		this.auid=auid;
		this.entities=entities;
	}
	public void buildSubID(Map<Long, Entity> src){
		subID=src
				.entrySet()
				.stream()
				.filter(e->e.getValue()!=null)
				.map(entry->new IdNode(entry.getKey(), entry.getValue()))
				.collect(Collectors.toList()	);
	}
	public void buildSubAuid(Map<Long, List<Entity>> src){
		subAuid=src.entrySet().stream()
				.map(e->new AuidNode(e.getKey(), e.getValue()))
				.collect(Collectors.toList());
	}
}
