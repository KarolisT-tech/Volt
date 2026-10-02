package com.example.kursinis.utils;

public class Constants {
    public static final String HOME_URL = "http://192.168.81.234:8080/";
    public static final String VALIDATE_USER_URL = HOME_URL + "validateUser";
    public static final String CREATE_CUSTOMER_URL = HOME_URL + "insertCustomer";
    public static final String CREATE_DRIVER_URL = HOME_URL + "insertDriver";
    public static final String GET_CUSTOMER_BY_ID = HOME_URL + "getCustomerById/%s";
    public static final String GET_DRIVER_BY_ID = HOME_URL + "getDriverById/%s";
    public static final String UPDATE_CUSTOMER = HOME_URL + "updateCustomer/%s";
    public static final String UPDATE_DRIVER = HOME_URL + "updateDriver/%s";
    public static final String GET_ALL_RESTAURANTS = HOME_URL + "getAllRestaurants";
    public static final String GET_RESTAURANT_MENU = HOME_URL + "getCuisinesByRestaurantId/%s";
    public static final String GET_RESTAURANT = HOME_URL + "getRestaurantById/%s";
    public static final String CREATE_ORDER_URL = HOME_URL + "insertOrder";
    public static final String GET_CUSTOMER_ORDERS = HOME_URL + "getOrderByUserId/%s";
    public static final String GET_DRIVER_ORDERS = HOME_URL + "getOrderByDriverId/%s";
    public static final String GET_ORDER_URL = HOME_URL + "getOrderById/%s";
    public static final String GET_AVAILABLE_ORDERS = HOME_URL + "getOrderByStatus/%s";
    public static final String UPDATE_ORDER_STATUS_TO_IN_DELIVERY = HOME_URL + "updateOrderStatus";
    public static final String CREATE_REVIEW = HOME_URL + "createReview";
    public static final String CREATE_CHAT = HOME_URL + "CreateChat";
    public static final String GET_MESSAGES_BY_CHAT = HOME_URL + "getMessagesByChatId/%s";
    public static final String CREATE_MESSAGE = HOME_URL + "createMessage";
    public static final String GET_REVIEW_BY_ORDER_ID = HOME_URL + "getReviewByOrderId/%s";




}
