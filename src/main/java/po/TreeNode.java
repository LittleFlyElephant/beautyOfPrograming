package po;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* beautyOfPrograming/datastructure/TreeNode.java
* @author cxworks
* 2016年5月7日 下午6:13:57
*/
public class TreeNode {
	public long n;
	List<TreeNode> next;
	public TreeNode(long n) {
		this.n=n;
	}
	public List<TreeNode> getNext() {
		return next;
	}
	
	public void addNode(Set<Long> set){
		next=set.stream().map(s->new TreeNode(s)).collect(Collectors.toList());
	}
	
}
