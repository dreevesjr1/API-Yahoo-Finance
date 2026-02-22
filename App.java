package org.example;

import yahoofinance.YahooFinance;
import yahoofinance.Stock;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {

    public static void main(String[] args) {

    	Queue<StockRecord> queue = new ConcurrentLinkedQueue<>();
    	
        ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            try {
                Stock dow = YahooFinance.get("^DJI");
                if (dow != null && dow.getQuote() != null) {
                    double price = dow.getQuote().getPrice();

                    if (price != null) {
                        StockRecord record =
                                new StockRecord(price, LocalDateTime.now());
                        queue.add(record);
                        System.out.println("Added to queue: " + record);
                    }
                }

            } catch (Exception e) {
                System.out.println("Error fetching stock data:");
                e.printStackTrace();
            }
        };
        //Unofficial API crashes when I run it at 5 seconds
        scheduler.scheduleAtFixedRate(task, 0, 5, TimeUnit.SECONDS);
    }
    static class StockRecord {
        private double price;
        private LocalDateTime timestamp;

        public StockRecord(double price, LocalDateTime timestamp) {
            this.price = price;
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return "StockRecord{" +
                    "price=" + price +
                    ", timestamp=" + timestamp +
                    '}';
        }
    }
}