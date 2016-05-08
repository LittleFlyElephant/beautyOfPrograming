package po;

import java.util.List;

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
	
}
