
package po;

import java.util.List;

/**
 * Created by raychen on 16/5/7.
 */
public class Entity {
    private Long id;
    private Long cid;
    private List<Long> fids;
    private Long jid;
    private List<AA> aas;
    private List<Long> rids;

    public Entity(Long id, Long cid, List<Long> fids, Long jid, List<AA> aas, List<Long> rids) {
        this.id = id;
        this.cid = cid;
        this.fids = fids;
        this.jid = jid;
        this.aas = aas;
        this.rids = rids;
    }

    public Entity() {
    }

    public Long getId() {
        return id;
    }

    public Long getCid() {
        return cid;
    }

    public List<Long> getFids() {
        return fids;
    }

    public Long getJid() {
        return jid;
    }

    public List<AA> getAas() {
        return aas;
    }

    public List<Long> getRids() {
        return rids;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public void setFids(List<Long> fids) {
        this.fids = fids;
    }

    public void setJid(Long jid) {
        this.jid = jid;
    }

    public void setAas(List<AA> aas) {
        this.aas = aas;
    }

    public void setRids(List<Long> rids) {
        this.rids = rids;
    }
    
    @Override
    public int hashCode(){
    	return new Long(id).intValue();
    	
    }
    @Override
    public boolean equals(Object obj){
    	if (!(obj instanceof Entity)) {
			return false;
		}
    	return ((Entity)obj).id==this.id;
    }
}
