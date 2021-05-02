package recommenderSystem.service;

import org.springframework.data.domain.Page;
import recommenderSystem.domain.Collection;
import recommenderSystem.domain.Rate;

import java.util.List;

public interface CollectionServices {
    public void save(Collection collection);
    public Collection findRateByUidAndNid(int uid,int nid);
    public void delById(int id);
    public Page<Collection> PageByUid(Integer pageNum, Integer pageSize, int uid);
    public List<Collection> findRateByUid(int uid);
}
