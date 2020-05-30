import java.util.*;
import java.io.Serializable;
/**
 * The actual comparator, whose implementation defines the ordering of
 * all placed orders in the orderbook
 */
public class OrderComparator implements Comparator<Order>, Serializable  {
    public int compare(Order order1, Order order2) {
        return Double.compare(order1.getOrderPrice(),
			       order2.getOrderPrice());
    }
}
