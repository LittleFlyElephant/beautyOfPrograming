package utility;

/**
 * Created by raychen on 16/5/7.
 */
public enum SearchType {
    ID,AUID,AFID,FID,JID,CID,RID;

    @Override
    public String toString() {
        switch (this){
            case ID: return "Id";
            case AUID: return "AA.AuId";
            case AFID: return "AA.AfId";
            case FID: return "F.FId";
            case JID: return "J.JId";
            case CID: return "C.CId";
            case RID: return "RId";
            default: return null;
        }
    }
}
