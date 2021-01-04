package dsbd.project.ordermanager.data;

import order.FinalOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import user.User;

import java.util.Optional;

public interface FinalOrderRepository extends PagingAndSortingRepository<FinalOrder, Integer> {
    //public Iterable<FinalOrder> findAllByUser(Optional<User> user, Pageable pageable);

    public Page<FinalOrder> findAllByUser(Optional<User> user, Pageable pageable);

    public Optional<FinalOrder> findFinalOrderById(Integer id);

    public Optional<FinalOrder> findFinalOrderByIdAndUser(Integer orderId, Optional<User> user);

}
