package lk.ijse.agencyla.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class OrderDetailDTO implements Serializable {
    private String orderId;
    private String itemCode;
    private int qty;
}
