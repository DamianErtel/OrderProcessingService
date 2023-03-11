package com.order.bo;

import com.order.bo.exception.BOException;
import com.order.dto.Order;

public interface OrderBO {

    boolean placeOrder(Order order) throws BOException;
    boolean cancelOrder(int id) throws BOException;
    boolean deleteOrder(int id) throws BOException;

}

