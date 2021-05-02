package recommenderSystem.service;

import org.springframework.data.domain.Page;
import recommenderSystem.domain.New;

import java.util.List;

public interface NewsServices {
    public New findById(int id);
    public void save(New news);
    public void delById(int id);
    public Page<New> Page(Integer pageNum, Integer pageSize);
    public Page<New> PageByHeatNum(Integer pageNum, Integer pageSize);
    public List<New> all();
    public void heat(int id);
    public List<New> findByTypeAndNum(int type_id,int num);
}