package build;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import dataservice.DataImpl;
import dataservice.DataService;
import po.AuidNode;
import po.Entity;
import po.IdNode;
import po.TreeNode;
import utility.FileHelper;
import utility.SearchType;

/**
* beautyOfPrograming/build/BuildMap.java
* @author cxworks
* 2016年5月7日 下午7:03:16
*/
public class BuildMap {
	
	
	public IdNode getIdTree(long id,Entity entity,DataService service) {
		IdNode ans=new IdNode(id,entity);
		//id to id
		List<Entity> id2idcid=service.getEntities(entity.getCid(), SearchType.CID);
		
	}
	
	public AuidNode getAuidTree(long auid,List<Entity> entities,DataService service) {
		
	}
	
	private Set<Long> getEqual(long n){
		List<Entity> entities=service.getEntities(n, SearchType.ID);
		
		IdNode idNode=getIdTree(n, entities.get(0), service);
		
		if (entities.isEmpty()) {
			entities=service.getEntities(n, SearchType.AUID);
		}
		//
		Set<Long> set=new HashSet<>();
		for(Entity entity: entities){
			//cid
			Set<Long> cids=service.getEntities(entity.getCid(), SearchType.CID).stream().map(e->e.getId()).collect(Collectors.toSet());
			//fid
			Set<Long> fids=getChild(entity.getFids(), SearchType.FID);
			//jid
			Set<Long> jids=service.getEntities(entity.getJid(), SearchType.JID).stream().map(e->e.getId()).collect(Collectors.toSet());
			
			//auid
			Set<Long> auids=getChild(entity.getAuids(), SearchType.AUID);
			//afid
			Set<Long> afids=getChild(entity.getAfids(), SearchType.AFID);
			set.addAll(cids);
			set.addAll(fids);
			set.addAll(jids);
			set.addAll(auids);
			set.addAll(afids);
		}
		//
		return set;
	}
	private Set<Long> getChild(List<Long> src,SearchType searchType){
		if (src==null) {
			return new HashSet<>();
		}
		Set<Long> ans=src.stream()
				.flatMap(
						l->
						service.getEntities(l, searchType)
						.stream().map(e->e.getId()))
				.collect(Collectors.toSet());
		return ans;
	}
}
