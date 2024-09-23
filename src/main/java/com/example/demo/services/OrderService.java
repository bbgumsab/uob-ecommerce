package com.example.demo.services;

import java.math.BigDecimal;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.LineItem;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionListLineItemsParams;
import com.example.demo.repo.OrderRepo;
import com.example.demo.repo.OrderItemRepo;
import com.example.demo.models.Order;
import com.example.demo.models.OrderItem;
import com.example.demo.models.Product;
import com.example.demo.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public OrderService(OrderRepo orderRepo, OrderItemRepo orderItemRepo, ProductService productService, UserService userService) {
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.productService = productService;
        this.userService = userService;
    }

    @Transactional
    public void handleSuccessfulPayment(Event event) {
        System.out.println(event);
        System.out.println("STRIPE API VERSION = " + Stripe.API_VERSION);
        Session session = (Session) event.getDataObjectDeserializer().getObject().get();
        String sessionId = session.getId();
        long userId = Long.valueOf(session.getClientReferenceId());
        User user = userService.findById(userId).orElse(null);

        try {

            // expand each line item so that we can get the product id and quantity
            // Retrieve the session with line items expanded
            SessionListLineItemsParams listItemParams = SessionListLineItemsParams.builder()
                    .addExpand("data.price.product")
                    .build();
            List<LineItem> lineItems = session.listLineItems(listItemParams).getData();

            Map<String, Long> orderedProducts = new HashMap<>();

            Order order = new Order(user, "Processing");
            orderRepo.save(order);

            for (LineItem item : lineItems) {
                String productId = item.getPrice()
                        .getProductObject()
                        .getMetadata().get("product_id");
                if (productId == null || productId.isEmpty()) {
                    System.err.println("Product ID not found for: " + item.getId());
                    continue; // Skip this item
                }
                Product product = productService.findById(Long.parseLong(productId)).orElse(null);
                long quantity = item.getQuantity();
                BigDecimal price = BigDecimal.valueOf(item.getPrice().getUnitAmount()/100);
                //orderedProducts.put(product, quantity);
                OrderItem orderItem = new OrderItem(product, quantity, price, order);
                orderItemRepo.save(orderItem);
            }

            String paymentIntentId = session.getPaymentIntent();
            //System.out.println(orderedProducts);


            System.out.println("Payment was successful. PaymentIntent ID: " + paymentIntentId);
        } catch (StripeException e) {
            System.out.println(e);
        }

    }

    @Transactional
    public void handleInvoicePaid(Event event) {
        // Handle paid invoice, if applicable to your system
    }

    @Transactional
    public void handlePaymentFailed(Event event) {
        // Handle failed payment
        // You might want to notify the user, mark the order as failed, etc.
    }
}
