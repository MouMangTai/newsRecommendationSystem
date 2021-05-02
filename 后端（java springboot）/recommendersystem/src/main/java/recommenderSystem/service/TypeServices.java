package recommenderSystem.service;

import org.springframework.data.domain.Page;
import recommenderSystem.domain.New;
import recommenderSystem.domain.Type;

import java.util.List;

public interface TypeServices {
    public Type findById(int id);
    public void save(Type type);
    public void delById(int id);
    public Page<Type> Page(Integer pageNum, Integer pageSize);
    public List<Type> all();
    public int findIdByType(String type);

}