package recommenderSystem.service.impl;

import org.springframework.stereotype.Service;
import recommenderSystem.dao.NewDao;
import recommenderSystem.dao.RateDao;
import recommenderSystem.dao.UserDao;
import recommenderSystem.domain.New;
import recommenderSystem.domain.Rate;
import recommenderSystem.domain.User;
import recommenderSystem.service.RecServices;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecServicesimpl implements RecServices {

    @Resource
    private UserDao userDao;
    @Resource
    private NewDao newdao;
    @Resource
    private RateDao rateDao;
    @Override
    public Object createRateMatrix() {
        List<User> users =  userDao.findAll();
        double [][] weight = new double[users.size()][(int)newdao.count()];
        List<Rate> rates = rateDao.findAll();

        List<Object> obj = new ArrayList<Object>();

        return obj;
    }
}
