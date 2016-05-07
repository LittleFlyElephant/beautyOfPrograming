package po;

import java.util.List;

/**
 * Created by raychen on 16/5/7.
 */
public class Entity {
    private Long id;
    private List<Long> cids;
    private List<Long> fids;
    private List<Long> jids;
    private List<Long> auids;
    private List<Long> afids;

    public Entity(Long id, List<Long> cids, List<Long> fids, List<Long> jids, List<Long> auids, List<Long> afids) {
        this.id = id;
        this.cids = cids;
        this.fids = fids;
        this.jids = jids;
        this.auids = auids;
        this.afids = afids;
    }

    public Entity() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCids(List<Long> cids) {
        this.cids = cids;
    }

    public void setFids(List<Long> fids) {
        this.fids = fids;
    }

    public void setJids(List<Long> jids) {
        this.jids = jids;
    }

    public void setAuids(List<Long> auids) {
        this.auids = auids;
    }

    public void setAfids(List<Long> afids) {
        this.afids = afids;
    }

    public Long getId() {
        return id;
    }

    public List<Long> getCids() {
        return cids;
    }

    public List<Long> getFids() {
        return fids;
    }

    public List<Long> getJids() {
        return jids;
    }

    public List<Long> getAuids() {
        return auids;
    }

    public List<Long> getAfids() {
        return afids;
    }
}
