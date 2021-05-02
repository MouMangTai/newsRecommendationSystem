package recommenderSystem.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import recommenderSystem.dao.TypeDao;
import recommenderSystem.dao.UserDao;
import recommenderSystem.domain.Type;
import recommenderSystem.domain.User;
import recommenderSystem.service.TypeServices;
import recommenderSystem.service.UsersServices;

import javax.annotation.Resource;
import java.util.List;


@Service
public class TypeServiceimpl implements TypeServices {
    @Resource
    private TypeDao typeDao;


    @Override
    public Type findById(int id) {
        return typeDao.findById(id).orElse(null);
    }


    @Override
    public void delById(int id) {
        typeDao.deleteById(id);
    }

    @Override
    public void save(Type type) {
        typeDao.save(type);
    }

    @Override
    public Page<Type> Page(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum-1,pageSize);
        return typeDao.findAll(pageable);
    }

    @Override
    public List<Type> all() {
        return typeDao.findAll();
    }

    @Override
    public int findIdByType(String type) {
        return typeDao.findIdByType(type);
    }


}
