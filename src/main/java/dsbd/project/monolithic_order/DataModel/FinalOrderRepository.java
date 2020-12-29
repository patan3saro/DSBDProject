package dsbd.project.monolithic_order.DataModel;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FinalOrderRepository extends CrudRepository<FinalOrder, Integer> {
    public Iterable<FinalOrder> findAllByUser(Optional<User> user);
    //public Optional<FinalOrder> findFinalOrderByIdAndUserIsIn(Integer orderId, Optional<User> user);
}
