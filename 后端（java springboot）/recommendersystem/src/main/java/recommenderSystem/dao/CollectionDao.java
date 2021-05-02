package recommenderSystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import recommenderSystem.domain.Collection;

@Repository
public interface CollectionDao extends JpaRepository<Collection,Integer> {
}
