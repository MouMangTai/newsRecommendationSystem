package recommenderSystem.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import recommenderSystem.domain.New;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface NewDao extends JpaRepository<New,Integer> {
    @Modifying
    @Query(nativeQuery = true,value = "update news set news.heat_num = news.heat_num+1 where news_id =:id")
    void heat(@Param("id")Integer id);


    @Query(nativeQuery = true,value = "select * from news where type_id = ?1 order by heat_num desc limit 0,?2")
    List<New> findByTypeAndNum(int type_id, int num);
}