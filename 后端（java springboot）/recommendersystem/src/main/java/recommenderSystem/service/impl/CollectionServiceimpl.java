package recommenderSystem.service.impl;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import recommenderSystem.dao.CollectionDao;
import recommenderSystem.domain.Collection;
import recommenderSystem.domain.New;
import recommenderSystem.domain.Rate;
import recommenderSystem.service.CollectionServices;
import recommenderSystem.service.NewsServices;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CollectionServiceimpl implements CollectionServices {
    @Resource
    private CollectionDao collectionDao;
    @Resource
    private NewsServices newsServices;
    @Override
    public void save(Collection collection) {
        collectionDao.save(collection);
    }

    @Override
    public Collection findRateByUidAndNid(int uid, int nid) {
        Collection c  = new Collection();
        c.setUid(uid);
        c.setNid(nid);
        Example<Collection> rateExample = Example.of(c);
        return collectionDao.findOne(rateExample).orElse(null);
    }
    @Override
    public List<Collection> findRateByUid(int uid) {
        Collection c  = new Collection();
        c.setUid(uid);
        Example<Collection> rateExample = Example.of(c);
        return collectionDao.findAll(rateExample);
    }
    @Override
    public void delById(int id) {
        collectionDao.deleteById(id);
    }

    @Override
    public Page<Collection> PageByUid(Integer pageNum, Integer pageSize, int uid) {
        Collection c  = new Collection();
        c.setUid(uid);
        Example<Collection> rateExample = Example.of(c);
        Pageable pageable = PageRequest.of(pageNum-1,pageSize);
        Page<Collection>  collections = collectionDao.findAll(rateExample,pageable);
        for(Collection collection:collections.getContent()){
            New n = newsServices.findById(collection.getNid());
            collection.setTitle(n.getNewTitle());
        }
        return collections;
    }
}
