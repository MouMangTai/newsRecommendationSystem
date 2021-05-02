package recommenderSystem.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import recommenderSystem.domain.Type;
import recommenderSystem.domain.User;

import javax.transaction.Transactional;


@Repository
//@Transactional
public interface TypeDao extends JpaRepository<Type, Integer> {

//    @Modifying
    @Query(nativeQuery = true, value = "select Id from newstype where type = ?1")
    int findIdByType(@Param("type") String type);
}
