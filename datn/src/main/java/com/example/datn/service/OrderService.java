package com.example.datn.service;

import com.example.datn.domain.entity.*;
import com.example.datn.domain.model.OrderDetail;
import com.example.datn.domain.model.OrderModel;
import com.example.datn.domain.repository.*;
import com.example.datn.domain.utility.DataResponse;
import com.example.datn.domain.utility.FilterParam;
import com.example.datn.domain.utility.SequenceGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Autowired
    SuggestUserRepository suggestUserRepository;

    public List<Long> createOrder(OrderModel orderModel){
        List<Long> res = new ArrayList<>();
        long orderID = SequenceGenerator.getInstance().nextId();
        Order order = orderModel.getOrder();
        order.setOrderID(orderID);
        order.setCreateTime(new Timestamp(System.currentTimeMillis()));
        List<OrderItem> orderItemList = new ArrayList<>();
        orderModel.getOrderDetails().forEach(orderDetail -> {
            Item i = itemRepository.checkQuantity(orderDetail.getItemID(), orderDetail.getQuantity());
            if( i == null) res.add(orderDetail.getItemID());
        });
        if(res.size()>0) return res;
        orderModel.getOrderDetails().forEach(orderDetail -> {
            OrderItem orderItem = OrderItem.builder()
                    .id(SequenceGenerator.getInstance().nextId())
                    .orderID(orderID)
                    .itemID(orderDetail.getItemID())
                    .quantity(orderDetail.getQuantity())
                    .build();
            orderItemList.add(orderItem);
            itemRepository.updateQuantity(orderDetail.getQuantity(), orderDetail.getItemID());
        });
        orderRepository.save(order);
        orderItemRepository.saveAll(orderItemList);
        cartItemRepository.deleteAllByUserID(orderModel.getOrder().getUserID());
        return res;

    }

    public OrderModel getOrderByID(long orderID) {
        Optional<Order> optionalOrder = orderRepository.findById(orderID);
        if(!optionalOrder.isPresent()) return new OrderModel();
        Order order = optionalOrder.get();
        List<OrderDetail> orderDetailList = new ArrayList<>();
        List<OrderItem> orderItemList = orderItemRepository.findAllByOrderID(orderID);
        orderItemList.forEach(orderItem -> {
            Item item = itemRepository.findById(orderItem.getItemID()).get();
            Product product = productRepository.findById(item.getProductID()).get();
            Type type = productService.getType(item.getTypeID());
            OrderDetail orderDetail = OrderDetail.builder()
                    .orderID(orderID)
                    .productID(product.getProductID())
                    .productCode(product.getProductCode())
                    .productName(product.getName())
                    .itemID(item.getItemID())
                    .typeID(type.getTypeID())
                    .typeName(type.getName())
                    .size(item.getSize())
                    .price(product.getCost())
                    .orderItemID(orderItem.getId())
                    .quantity(orderItem.getQuantity())
                    .imageURL(product.getImageURL())
                    .build();
            orderDetailList.add(orderDetail);
        });
        OrderModel orderModel = new OrderModel(order, orderDetailList);
        return orderModel;
    }

    @Transactional
    public int updateStatus(Order order) {
        if(order.getStatus() == 1 || order.getStatus() == 5) {
            List<OrderItem> orderItemList = orderItemRepository.findAllByOrderID(order.getOrderID());
            orderItemList.forEach(orderItem -> {
                itemRepository.addQuantity(orderItem.getQuantity(), orderItem.getItemID());
            });
        }
        if(order.getStatus() == 4) {
            log.info("order: {}", order.getOrderID());
            List<Long> listProductID = new ArrayList<>();
            List<OrderItem> orderItemList = orderItemRepository.findAllByOrderID(order.getOrderID());

            orderItemList.forEach(orderItem -> {
                long productID = itemRepository.getProductID(orderItem.getItemID());
                listProductID.add(productID);
                productRepository.updateSoldNb(productID, orderItem.getQuantity());

            });
            suggestUserRepository.updateDoc(order.getUserID(), listProductID);
        }

        return orderRepository.updateStatus(order.getStatus(), order.getOrderID());

    }

    public DataResponse getFilterPaging(FilterParam param) {
        List<Order> orderList;
        Timestamp createDateMin = param.getCreateDateMin() == null ? new Timestamp(1580830816000l) : param.getCreateDateMin();
        Timestamp createDateMax = param.getCreateDateMax() == null ? new Timestamp(System.currentTimeMillis()) : param.getCreateDateMax();
        if(param.getKeyword() == null) {
            param.setKeyword("");
        }

        if(param.getUserID() != null && param.getUserID() != 0) {
            orderList = orderRepository.filterUser(param.getUserID(), param.getStatus());
            return DataResponse.builder()
                    .data(orderList)
                    .totalPage(1)
                    .totalRecord(orderList.size()).build();
        }
        log.info("filter order param: ", param);
        if(param.getStatus() == null && param.getUserID() == null) {
            orderList = orderRepository.filter(createDateMin, createDateMax, param.getKeyword(), 100, 0);
        }
        else orderList = orderRepository.filterStatus(createDateMin, createDateMax, param.getKeyword(), 100, 0, param.getStatus());
        return DataResponse.builder()
                .data(orderList)
                .totalPage(1)
                .totalRecord(orderList.size()).build();

    }

}
