package model;

public record Trade(Order buyOrder, Order sellOrder, Investor buyer, Investor seller) {
}
