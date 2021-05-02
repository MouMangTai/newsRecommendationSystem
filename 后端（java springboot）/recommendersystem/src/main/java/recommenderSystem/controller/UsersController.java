package recommenderSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import recommenderSystem.domain.User;
import recommenderSystem.entity.ResponseResult;
import recommenderSystem.entity.typeDomain;
import recommenderSystem.service.UsersServices;

import java.util.List;


@RestController
@RequestMapping("Users")
public class UsersController {
    @Autowired
    private UsersServices usersServices;

    @GetMapping("findById/{id}")
    public ResponseResult<User> findById(@PathVariable int id){
        User user =  usersServices.findById(id);

        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"获取成功",user);
    }

    @GetMapping("findByName/{name}")
    public ResponseResult<User> findByName(@PathVariable String name){
        User user =  usersServices.findByName(name);

        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"获取成功",user);
    }
    @PostMapping("add")
    public ResponseResult<Void> add(@RequestBody User user){
        usersServices.save(user);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK_INSERT,"创建成功");
    }
    @DeleteMapping("delById/{id}")
    public ResponseResult<Void> delById(@PathVariable int id){
        usersServices.delById(id);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"删除成功");
    }

    @PutMapping("update")
    public ResponseResult<Void> update(@RequestBody User user){
        usersServices.save(user);
        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"更新成功");
    }
    @PutMapping("updateByUserName")
    public ResponseResult<Void> updateByUserName(@RequestBody typeDomain user){
        usersServices.updateByUserName(user.getUserName(),user.getPrefer());
        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"更新成功");
    }
    @GetMapping("Page/{page}/{size}")
    public ResponseResult<Page<User>> Page(@PathVariable("page") Integer pageNum,
                                           @PathVariable("size") Integer pageSize){
        Page page =  usersServices.Page(pageNum,pageSize);

        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"获取成功",page);
    }
    @GetMapping("all")
    public ResponseResult<List<User>> all(){
        List<User> users =  usersServices.all();

        return new ResponseResult<>(ResponseResult.CodeStatus.OK,"获取成功",users);
    }



}
