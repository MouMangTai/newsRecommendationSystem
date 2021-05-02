package recommenderSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import recommenderSystem.domain.New;
import recommenderSystem.domain.Rate;
import recommenderSystem.domain.User;
import recommenderSystem.entity.ResponseResult;
import recommenderSystem.service.NewsServices;
import recommenderSystem.service.RateServices;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("Rate")
public class RateController {

    @Autowired
    private RateServices rateServices;
    @Autowired
    private NewsServices newsServices;
    @PostMapping("add")
    public ResponseResult<Void> add(@RequestBody Rate rate){
        rate.setTitle(newsServices.findById(rate.getNid()).getNewTitle());
        Timestamp t = new Timestamp(new Date().getTime());
        rate.setTime(t);
        rateServices.save(rate);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK_INSERT,"创建成功");
    }

    @GetMapping("findByUidAndNid/{uid}/{nid}")
    public ResponseResult<Rate> findById(@PathVariable int uid,@PathVariable int nid){
        Rate rate =  rateServices.findRateByUidAndNid(uid,nid);

        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"获取成功",rate);
    }
    @GetMapping("findByUid/{uid}")
    public ResponseResult<List<Rate>> findByUid(@PathVariable int uid){
        List<Rate> rates =  rateServices.findRateByUid(uid);

        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"获取成功",rates);
    }

    @DeleteMapping("delById/{id}")
    public ResponseResult<Void> delById(@PathVariable int id){
        rateServices.delById(id);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"删除成功");
    }

    @PutMapping("update")
    public ResponseResult<Void> update(@RequestBody Rate rate){
        rate.setTitle(newsServices.findById(rate.getNid()).getNewTitle());
        Timestamp t = new Timestamp(new Date().getTime());
        rate.setTime(t);
        rateServices.save(rate);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"更新成功");
    }

    @GetMapping("findAllRateByUserId/{page}/{size}/{uid}")
    public ResponseResult<Page<Rate>> findAllRateByUserId(@PathVariable("page") Integer pageNum,
                                           @PathVariable("size") Integer pageSize,
                                           @PathVariable int uid){
        Page page =  rateServices.PageByUid(pageNum,pageSize,uid);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"获取成功",page);
    }




}
