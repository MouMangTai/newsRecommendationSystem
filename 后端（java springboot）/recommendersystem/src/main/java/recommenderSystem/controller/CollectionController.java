package recommenderSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import recommenderSystem.domain.Collection;
import recommenderSystem.domain.Rate;
import recommenderSystem.entity.ResponseResult;
import recommenderSystem.service.CollectionServices;
import recommenderSystem.service.NewsServices;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("Collection")
public class CollectionController {

    @Autowired
    private CollectionServices collectionServices;
    @Autowired
    private NewsServices newsServices;
    @PostMapping("add")
    public ResponseResult<Void> add(@RequestBody Collection collection){
        collection.setTitle(newsServices.findById(collection.getNid()).getNewTitle());
        Timestamp t = new Timestamp(new Date().getTime());
        collection.setTime(t);
        collectionServices.save(collection);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK_INSERT,"创建成功");
    }

    @GetMapping("findByUidAndNid/{uid}/{nid}")
    public ResponseResult<Collection> findById(@PathVariable int uid,@PathVariable int nid){
        Collection collection =  collectionServices.findRateByUidAndNid(uid,nid);

        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"获取成功",collection);
    }


    @DeleteMapping("delById/{id}")
    public ResponseResult<Void> delById(@PathVariable int id){
        collectionServices.delById(id);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"删除成功");
    }

    @PutMapping("update")
    public ResponseResult<Void> update(@RequestBody Collection collection){
        collection.setTitle(newsServices.findById(collection.getNid()).getNewTitle());
        Timestamp t = new Timestamp(new Date().getTime());
        collection.setTime(t);
        collectionServices.save(collection);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"更新成功");
    }

    @GetMapping("findAllCollectionByUserId/{page}/{size}/{uid}")
    public ResponseResult<Page<Collection>> findAllCollectionByUserId(@PathVariable("page") Integer pageNum,
                                           @PathVariable("size") Integer pageSize,
                                           @PathVariable int uid){
        Page page =  collectionServices.PageByUid(pageNum,pageSize,uid);

        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"获取成功",page);
    }
    @GetMapping("findByUid/{uid}")
    public ResponseResult<List<Collection>> findByUid(@PathVariable int uid){
        List<Collection> collections =  collectionServices.findRateByUid(uid);

        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"获取成功",collections);
    }
}
