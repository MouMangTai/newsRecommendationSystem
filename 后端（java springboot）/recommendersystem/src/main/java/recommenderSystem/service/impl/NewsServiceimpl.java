package recommenderSystem.service.impl;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;
import recommenderSystem.dao.NewDao;
import recommenderSystem.domain.New;
import recommenderSystem.service.NewsServices;

import javax.annotation.Resource;
import java.util.List;


@Service
public class NewsServiceimpl implements NewsServices {
    @Resource
    private NewDao newdao;


    @Override
    public New findById(int id) {
        return newdao.findById(id).orElse(null);
    }


    @Override
    public void delById(int id) {
        newdao.deleteById(id);
    }

    @Override
    public void save(New news) {
        newdao.save(news);
    }

    @Override
    public Page<New> Page(Integer pageNum, Integer pageSize) {
        JpaSort sort = JpaSort.unsafe(Sort.Direction.DESC,"newCreattime");

        Pageable pageable = PageRequest.of(pageNum-1,pageSize,sort);
        return newdao.findAll(pageable);
    }

    @Override
    public Page<New> PageByHeatNum(Integer pageNum, Integer pageSize) {
        JpaSort sort = JpaSort.unsafe(Sort.Direction.DESC,"heatNum");

        Pageable pageable = PageRequest.of(pageNum-1,pageSize,sort);
        return newdao.findAll(pageable) ;
    }

    @Override
    public List<New> all() {
        return newdao.findAll();
    }

    @Override
    public void heat(int id) {
        newdao.heat(id);
    }

    @Override
    public List<New> findByTypeAndNum(int type_id, int num) {
        return newdao.findByTypeAndNum(type_id,num);
    }


}
