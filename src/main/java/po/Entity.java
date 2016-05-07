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

    public Entity(Long id, Long cid, List<Long> fids, Long jid, List<Long> auids, List<Long> afids) {
        this.id = id;
        this.cid = cid;
        this.fids = fids;
        this.jid = jid;
        this.auids = auids;
        this.afids = afids;
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
}
