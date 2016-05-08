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
    private List<Long> auids;
    private List<Long> afids;
    private List<Long> rids;

    public Entity(Long id, Long cid, List<Long> fids, Long jid, List<Long> auids, List<Long> afids, List<Long> rids) {
        this.id = id;
        this.cid = cid;
        this.fids = fids;
        this.jid = jid;
        this.auids = auids;
        this.afids = afids;
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

    public List<Long> getAuids() {
        return auids;
    }

    public List<Long> getAfids() {
        return afids;
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

    public void setAuids(List<Long> auids) {
        this.auids = auids;
    }

    public void setAfids(List<Long> afids) {
        this.afids = afids;
    }

    public void setRids(List<Long> rids) {
        this.rids = rids;
    }
}
