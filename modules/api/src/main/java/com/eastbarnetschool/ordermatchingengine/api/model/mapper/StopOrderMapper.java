package com.eastbarnetschool.ordermatchingengine.api.model.mapper;

import com.eastbarnetschool.ordermatchingengine.api.model.entity.StopOrderEntity;
import com.eastbarnetschool.ordermatchingengine.domain.orders.StopOrder;
import org.springframework.stereotype.Component;

@Component
public class StopOrderMapper {
    public StopOrderEntity toEntity(StopOrder stopOrder) {
        return new StopOrderEntity(
                stopOrder.getOrderId(),
                stopOrder.getOrder().getOrderId(),
                stopOrder.getTriggerPrice()
        );
    }
}
