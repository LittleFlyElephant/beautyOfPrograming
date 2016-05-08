package po;

import java.util.List;

/**
* beautyOfPrograming/po/AuidNode.java
* @author cxworks
* 2016年5月8日 下午3:19:19
*/

public class AuidNode {
	public long auid;
	List<IdNode> subID;
	List<AuidNode> subAuid;
	public List<Entity> entities;
	public AuidNode(long auid,List<Entity> entities) {
		this.auid=auid;
		this.entities=entities;
	}
}
