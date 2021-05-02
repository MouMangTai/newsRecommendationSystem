package recommenderSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import recommenderSystem.domain.Type;
import recommenderSystem.domain.User;
import recommenderSystem.entity.ResponseResult;
import recommenderSystem.service.TypeServices;

import java.util.List;


@RestController
@RequestMapping("Type")
public class TypeController {
    @Autowired
    private TypeServices typeServices;

    @GetMapping("findById/{id}")
    public ResponseResult<Type> findById(@PathVariable int id){
        Type type =  typeServices.findById(id);

        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"获取成功",type);
    }
    @PostMapping("add")
    public ResponseResult<Void> add(@RequestBody Type type){
        typeServices.save(type);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK_INSERT,"创建成功");
    }
    @DeleteMapping("delById/{id}")
    public ResponseResult<Void> delById(@PathVariable int id){
        typeServices.delById(id);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"删除成功");
    }

    @PutMapping("update")
    public ResponseResult<Void> update(@RequestBody Type type){
        typeServices.save(type);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"更新成功");
    }

    @GetMapping("Page/{page}/{size}")
    public ResponseResult<Page<Type>> Page(@PathVariable("page") Integer pageNum,
                                           @PathVariable("size") Integer pageSize){
        Page page =  typeServices.Page(pageNum,pageSize);

        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"获取成功",page);
    }
    @GetMapping("all")
    public ResponseResult<List<Type>> all(){
        List<Type> types =  typeServices.all();

        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"获取成功",types);
    }

}
