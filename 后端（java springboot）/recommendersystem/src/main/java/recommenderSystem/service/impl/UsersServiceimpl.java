package recommenderSystem.service.impl;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import recommenderSystem.dao.UserDao;
import recommenderSystem.domain.User;
import recommenderSystem.service.UsersServices;

import javax.annotation.Resource;
import java.util.List;


@Service
public class UsersServiceimpl implements UsersServices {
    @Resource
    private UserDao userDao;


    @Override
    public User findById(int id) {
        return userDao.findById(id).orElse(null);
    }

    @Override
    public User findByName(String name) {
        User user = new User();
        user.setUserName(name);
        Example<User> userExample = Example.of(user);
        return userDao.findOne(userExample).orElse(null);
    }


    @Override
    public void delById(int id) {
        userDao.deleteById(id);
    }

    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public Page<User> Page(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum-1,pageSize);
        return userDao.findAll(pageable);
    }

    @Override
    public List<User> all() {
        return userDao.findAll();
    }

    @Override
    public void updateByUserName(String name, String prefer) {
        userDao.updateByUserName(name,prefer);
    }

    @Override
    public List<Integer> findALLUid() {
        return userDao.findALLUid();
    }

}
