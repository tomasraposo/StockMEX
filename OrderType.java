import java.io.Serializable;

/**
 * Enum data type with two constants:
 * LIMIT
 * MARKET
 *
 * These constants define the type of a given order.
 */
public enum OrderType implements Serializable  {
    LIMIT,
    MARKET
}
