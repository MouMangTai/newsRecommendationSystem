package recommenderSystem.service.impl;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import recommenderSystem.dao.NewDao;
import recommenderSystem.dao.RateDao;
import recommenderSystem.domain.New;
import recommenderSystem.domain.Rate;
import recommenderSystem.service.NewsServices;
import recommenderSystem.service.RateServices;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RateServiceimpl implements RateServices {
    @Resource
    private RateDao rateDao;
    @Resource
    private NewsServices newsServices;
    @Override
    public void save(Rate rate) {
        rateDao.save(rate);
    }

    @Override
    public Rate findRateByUidAndNid(int uid, int nid) {
        Rate rate = new Rate();
        rate.setUid(uid);
        rate.setNid(nid);
        Example<Rate> rateExample = Example.of(rate);
        return rateDao.findOne(rateExample).orElse(null);
    }
    @Override
    public List<Rate> findRateByUid(int uid) {
        Rate rate = new Rate();
        rate.setUid(uid);
        Example<Rate> rateExample = Example.of(rate);
        return rateDao.findAll(rateExample);
    }



    @Override
    public void delById(int id) {
        rateDao.deleteById(id);
    }

    @Override
    public Page<Rate> PageByUid(Integer pageNum, Integer pageSize, int uid) {
        Rate rate = new Rate();
        rate.setUid(uid);
        Example<Rate> rateExample = Example.of(rate);
        Pageable pageable = PageRequest.of(pageNum-1,pageSize);
        Page<Rate>  rates = rateDao.findAll(rateExample,pageable);
        for(Rate r:rates.getContent()){
            New n = newsServices.findById(r.getNid());
            r.setTitle(n.getNewTitle());
        }
        return rates;
    }

    @Override
    public List<Integer> findALLNid() {
        return rateDao.findAllNid();
    }

    @Override
    public List<Rate> all() {
        return rateDao.findAll();
    }


}
