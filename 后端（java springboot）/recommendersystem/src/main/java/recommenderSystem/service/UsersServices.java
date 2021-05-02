package recommenderSystem.service;


import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;
import recommenderSystem.domain.User;


import java.util.ArrayList;
import java.util.List;

public interface UsersServices {
    public User findById(int id);
    public User findByName(String name);
    public void save(User user);
    public void delById(int id);
    public Page<User> Page(Integer pageNum, Integer pageSize);
    public List<User> all();
    public void updateByUserName(String name,String prefer);

    public List<Integer> findALLUid();
}
