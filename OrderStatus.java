import java.io.Serializable;
/**
 * Enum data type with two constants:
 * QUEUEING
 * COMPLETED
 *
 * These constants define the current status of a given order.
 */
public enum OrderStatus implements Serializable {
    QUEUING,
    COMPLETED
}
