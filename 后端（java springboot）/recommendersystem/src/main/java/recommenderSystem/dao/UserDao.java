package recommenderSystem.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import recommenderSystem.domain.User;

import javax.transaction.Transactional;
import java.util.List;


@Repository
@Transactional
public interface UserDao extends JpaRepository<User,Integer> {

    @Modifying
    @Query(nativeQuery = true,value = "update users set users.prefer = :prefer where user_name =:user_name")
    void updateByUserName(@Param("user_name")String user_name,@Param("prefer")String prefer);
    @Query(nativeQuery = true, value = "select distinct uid from score")
    List<Integer> findALLUid();
}
