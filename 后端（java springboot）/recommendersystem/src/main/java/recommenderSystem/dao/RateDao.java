package recommenderSystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import recommenderSystem.domain.Rate;

import java.util.List;

@Repository
public interface RateDao extends JpaRepository<Rate,Integer> {
    @Query(nativeQuery = true, value = "select distinct nid from score")
    List<Integer> findAllNid();
}
