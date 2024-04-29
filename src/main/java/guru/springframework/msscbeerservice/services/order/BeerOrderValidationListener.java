package guru.springframework.msscbeerservice.services.order;

import guru.sfg.brewery.model.BeerOrderDto;
import guru.sfg.brewery.model.ValidateOrderRequest;
import guru.sfg.brewery.model.ValidateOrderResult;
import guru.springframework.msscbeerservice.config.JmsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class BeerOrderValidationListener {
    private final JmsTemplate jmsTemplate;
    private final BeerOrderValidator beerOrderValidator;
    @JmsListener(destination = JmsConfig.VALIDATE_ORDER_QUEUE)
    public void validateOrder(ValidateOrderRequest request) {
        BeerOrderDto beerOrderDto = request.getBeerOrderDto();
        Boolean isValid = beerOrderValidator.validateOrder(beerOrderDto);
        UUID orderId = beerOrderDto.getId();
        jmsTemplate.convertAndSend(JmsConfig.VALIDATE_ORDER_RESULT_QUEUE, new ValidateOrderResult(orderId,isValid));
    }
}
