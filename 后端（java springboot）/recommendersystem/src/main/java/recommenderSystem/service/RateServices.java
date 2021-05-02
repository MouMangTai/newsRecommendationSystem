package recommenderSystem.service;


import org.springframework.data.domain.Page;
import recommenderSystem.domain.New;
import recommenderSystem.domain.Rate;

import java.util.List;

public interface RateServices {
    public void save(Rate rate);
    public Rate findRateByUidAndNid(int uid,int nid);
    public void delById(int id);
    public List<Rate> findRateByUid(int uid);
    public Page<Rate> PageByUid(Integer pageNum, Integer pageSize,int uid);
    public List<Integer> findALLNid();
    public List<Rate> all();

}
