package com.order.bo;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.order.bo.exception.BOException;
import com.order.dao.OrderDAO;
import com.order.dto.Order;

class OrderBOImplTest {
    private static final int ORDER_ID = 123;
    @Mock
    OrderDAO dao;
    private OrderBOImpl bo;

    @BeforeEach // (Before on Windows solve some errors)
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        bo = new OrderBOImpl();
        bo.setDao(dao);
    }

    @Test
    void placeOrder_Shoud_Create_An_Order() throws SQLException, BOException {
        Order order = new Order();
        Integer integer = Integer.valueOf(1);
        when(dao.create(any(Order.class))).thenReturn(integer);
        boolean result = bo.placeOrder(order);
        assertTrue(result);
        verify(dao, atLeast(1)).create(order);
    }

    @Test
    void placeOrder_Shoud_not_Create_An_Order() throws SQLException, BOException {
        Order order = new Order();
        Integer integer = Integer.valueOf(0);
        when(dao.create(any(Order.class))).thenReturn(integer);
        boolean result = bo.placeOrder(order);
        assertFalse(result);
        verify(dao).create(order);
    }

    @Test
    public void placeOrder_Should_Throw_BOException() throws SQLException, BOException {
        Order order = new Order();
        when(dao.create(any(Order.class))).thenThrow(SQLException.class);
        Assertions.assertThrows(BOException.class, () -> {
            boolean result = bo.placeOrder(order);
        });
    }

    @Test
    void cancelOrder_Shoud_Cancel_An_Order() throws SQLException, BOException {
        Order order = new Order();
        when(dao.read(anyInt())).thenReturn(order);
        when(dao.update(order)).thenReturn(1);
        boolean result = bo.cancelOrder(123);
        assertTrue(result);
        verify(dao).read(anyInt());
        verify(dao).update(order);
    }

    @Test
    void cancelOrder_Shoud_NOT_Cancel_An_Order() throws SQLException, BOException {
        Order order = new Order();
        when(dao.read(ORDER_ID)).thenReturn(order);
        when(dao.update(order)).thenReturn(0);
        boolean result = bo.cancelOrder(123);
        assertFalse(result);
        verify(dao).read(ORDER_ID);
        verify(dao).update(order);
    }

    @Test
    public void cancelOrder_Should_Throw_BOException() throws SQLException, BOException {
        when(dao.read(anyInt())).thenThrow(SQLException.class);
        Assertions.assertThrows(BOException.class, () -> {
            boolean result = bo.cancelOrder(ORDER_ID);
        });
    }

    @Test
    public void cancelOrder_Should_Throw_BOException_On_Update() throws SQLException, BOException {
        Order order = new Order();
        when(dao.read(ORDER_ID)).thenReturn(order);
        when(dao.update(order)).thenThrow(SQLException.class);
        Assertions.assertThrows(BOException.class, () -> {
            boolean result = bo.cancelOrder(ORDER_ID);
        });
    }

    @Test
    void deleteOrder_Shoud_Delete_An_Order() throws SQLException, BOException {
        when(dao.delete(ORDER_ID)).thenReturn(1);
        boolean result = bo.deleteOrder(ORDER_ID);
        assertTrue(result);
        verify(dao).delete(ORDER_ID);
    }

    @Test
    void modifyOrder_Should_Return_True() throws SQLException, BOException {
        Order order = new Order();
        when(dao.update(order)).thenReturn(2);
        boolean result = bo.modifyOrder(ORDER_ID, order);
        assertTrue(result);
    }

    @Test
    void modifyOrder_Should_Return_False() throws SQLException, BOException {
        Order order = new Order();
        when(dao.update(order)).thenReturn(0);
        boolean result = bo.modifyOrder(ORDER_ID, order);
        assertFalse(result);
    }
}