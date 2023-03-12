package com.order.bo;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
    void placeOrder_Should_Create_An_Order() throws SQLException, BOException {
        Order order = new Order();
        Integer integer = Integer.valueOf(1);
        when(dao.create(any(Order.class))).thenReturn(integer);
        boolean result = bo.placeOrder(order);
        assertTrue(result);
        verify(dao, atLeast(1)).create(order);
    }

    @Test
    void placeOrder_Should_not_Create_An_Order() throws SQLException, BOException {
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
    void cancelOrder_Should_NOT_Cancel_An_Order() throws SQLException, BOException {
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
    void deleteOrder_Should_Delete_An_Order() throws SQLException, BOException {
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


    // Zadanie 16
    @Test
    void modifyOrder_Should_Throw_BOException() throws SQLException, BOException {
        Order order = new Order();
        when(dao.update(order)).thenThrow(SQLException.class);
        Assertions.assertThrows(BOException.class, () -> {
            bo.modifyOrder(ORDER_ID, order);
        });
    }

    @Test
    void modifyOrder_Should_Not_Throw_Exception() throws SQLException, BOException {
        Order order = new Order();
        when(dao.update(order)).thenReturn(1);
        Assertions.assertDoesNotThrow(() -> {
            bo.modifyOrder(ORDER_ID, order);
        });
    }

    @Test
    void createOrder_Should_Throw_BOException() throws SQLException, BOException {
        Order order = new Order();
        when(dao.create(order)).thenThrow(SQLException.class);
        Assertions.assertThrows(BOException.class, () -> {
            bo.placeOrder(order);
        });
    }

    @Test
    void createOrder_Should_Not_Throw_Exception() throws SQLException, BOException {
        Order order = new Order();
        when(dao.create(order)).thenReturn(1);
        Assertions.assertDoesNotThrow(() -> {
            bo.placeOrder(order);
        });
    }

    // Zadanie 15

    @Test
    void modifyOrder_Should_Call_Read() throws SQLException, BOException {
        when(dao.update(any(Order.class))).thenReturn(1);
        Order order = new Order();
        boolean result = bo.modifyOrder(ORDER_ID, order);
        assertTrue(result);
        verify(dao, atLeast(1)).read(ORDER_ID);
    }

    @Test
    void modifyOrder_Should_Call_Update() throws SQLException, BOException {
        when(dao.update(any(Order.class))).thenReturn(1);
        Order order = new Order();
        boolean result = bo.modifyOrder(ORDER_ID, order);
        assertTrue(result);
        verify(dao, atLeast(1)).update(order);
    }

    @Test
    void modifyOrder_Should_Not_Call_Create() throws SQLException, BOException {
        when(dao.update(any(Order.class))).thenReturn(1);
        Order order = new Order();
        boolean result = bo.modifyOrder(ORDER_ID, order);
        assertTrue(result);
        verify(dao, never()).create(order);
    }

    @Test
    void modifyOrder_Should_Not_Call_Delete() throws SQLException, BOException {
        when(dao.update(any(Order.class))).thenReturn(1);
        Order order = new Order();
        boolean result = bo.modifyOrder(ORDER_ID, order);
        assertTrue(result);
        verify(dao, never()).delete(anyInt());
    }

    @Test
    void placeOrder_Should_Call_Create() throws SQLException, BOException {
        when(dao.create(any(Order.class))).thenReturn(1);
        Order order = new Order();
        boolean result = bo.placeOrder(order);
        assertTrue(result);
        verify(dao, atLeast(1)).create(order);
    }

    @Test
    void placeOrder_Should_Not_Call_Read() throws SQLException, BOException {
        when(dao.create(any(Order.class))).thenReturn(1);
        Order order = new Order();
        boolean result = bo.placeOrder(order);
        assertTrue(result);
        verify(dao, never()).read(anyInt());
    }

    @Test
    void placeOrder_Should_Not_Call_Delete() throws  SQLException, BOException {
        when(dao.create(any(Order.class))).thenReturn(1);
        Order order = new Order();
        boolean result = bo.placeOrder(order);
        assertTrue(result);
        verify(dao, never()).delete(anyInt());
    }

    @Test
    void placeOrder_Should_Not_Call_Update() throws  SQLException, BOException {
        when(dao.create(any(Order.class))).thenReturn(1);
        Order order = new Order();
        boolean result = bo.placeOrder(order);
        assertTrue(result);
        verify(dao, never()).update(any(Order.class));
    }

    @Test
    void deleteOrder_delete_Should_Accept_Any_Int() throws SQLException, BOException {
        when(dao.delete(anyInt())).thenReturn(1);
        boolean result = bo.deleteOrder(2);
        assertTrue(result);
    }

    @Test
    void deleteOrder_Should_Call_Delete() throws SQLException, BOException {
        when(dao.delete(anyInt())).thenReturn(1);
        boolean result = bo.deleteOrder(2);
        assertTrue(result);
        verify(dao, atLeast(1)).delete(anyInt());
    }


}