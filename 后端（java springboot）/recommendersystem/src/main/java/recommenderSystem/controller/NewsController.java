package recommenderSystem.controller;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import recommenderSystem.domain.New;
import recommenderSystem.domain.Rate;
import recommenderSystem.domain.User;
import recommenderSystem.entity.ResponseResult;
import recommenderSystem.service.NewsServices;
import recommenderSystem.service.RateServices;
import recommenderSystem.service.UsersServices;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@RestController
@RequestMapping("News")
public class NewsController {
    @Autowired
    private NewsServices newsServices;
    @Autowired
    private RateServices rateServices;

    @GetMapping("findById/{id}")
    public ResponseResult<New> findById(@PathVariable int id){
        New news =  newsServices.findById(id);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"获取成功",news);
    }
    @PostMapping("add")
    public ResponseResult<Void> add(@RequestBody New news){
        newsServices.save(news);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK_INSERT,"创建成功");
    }
    @DeleteMapping("delById/{id}")
    public ResponseResult<Void> delById(@PathVariable int id){
        newsServices.delById(id);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"删除成功");
    }

    @PutMapping("update")
    public ResponseResult<Void> update(@RequestBody New news){
        newsServices.save(news);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"更新成功");
    }
    @GetMapping("heat/{id}")
    public ResponseResult<Void> heat(@PathVariable int id){
        newsServices.heat(id);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"更新成功");
    }
    @GetMapping("Page/{page}/{size}")
    public ResponseResult<Page<New>> Page(@PathVariable("page") Integer pageNum,
                                           @PathVariable("size") Integer pageSize){
        Page page =  newsServices.Page(pageNum,pageSize);

        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"获取成功",page);
    }
    @GetMapping("PageByHeatNum/{page}/{size}")
    public ResponseResult<Page<New>> PageByHeatNum(@PathVariable("page") Integer pageNum,
                                          @PathVariable("size") Integer pageSize){
        Page page =  newsServices.PageByHeatNum(pageNum,pageSize);

        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"获取成功",page);
    }
    @GetMapping("all")
    public ResponseResult<List<New>> all(){
        List<New> news =  newsServices.all();

        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"获取成功",news);
    }



}
