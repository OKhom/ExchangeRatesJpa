package com.javapro;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javapro.bank.Exchange;
import com.javapro.db.Currency;
import com.javapro.utils.Server;

import javax.persistence.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {
    private static final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
    private static final Scanner sc = new Scanner(System.in);
    private static final Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy").create();

    static EntityManagerFactory emf;
    static EntityManager em;

    public static void main(String[] args) {
        try {
            emf = Persistence.createEntityManagerFactory("Bank");
            em = emf.createEntityManager();
            try {
                while (true) {
                    System.out.println("\nSelect your action:");
                    System.out.println("\t1. show currency statistic");
                    System.out.println("\t2. load new data");
                    System.out.println("\t0. exit");
                    System.out.print(" -> ");
                    String input = sc.nextLine();

                    switch (input) {
                        case "1" -> showStatistic();
                        case "2" -> loadDB("USD");
                        case "0" -> {
                            return;
                        }
                        default -> System.out.println("Entered value is invalid. Try again...");
                    }
                }
            } finally {
                em.close();
                emf.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void loadDB(String currencyName) throws IOException {
        Date dateStart, dateFinish;
        while (true) {
            dateStart = dateInput("Enter first date (in format DD.MM.YYYY):");
            dateFinish = dateInput("Enter last date (in format DD.MM.YYYY):");
            if (dateStart != null && dateFinish != null) break;
            System.out.println("Date format is incorrect. Try again...");
        }

        em.getTransaction().begin();
        try {
            for (int i = 0; i < daysBetween(dateStart, dateFinish); i++) {
                System.out.println("Uploading " + df.format(dateIncrement(dateStart, i)));
                String json = Server.request(df.format(dateIncrement(dateStart, i)));
                Exchange exchange = gson.fromJson(json, Exchange.class);
                Currency currency = new Currency(exchange.getDate(),
                        exchange.getBank(),
                        exchange.getBaseCurrency(),
                        exchange.getBaseCurrencyLit(),
                        exchange.getExchangeRateCur(currencyName).getCurrency(),
                        exchange.getExchangeRateCur(currencyName).getSaleRateNB(),
                        exchange.getExchangeRateCur(currencyName).getPurchaseRateNB(),
                        exchange.getExchangeRateCur(currencyName).getSaleRate(),
                        exchange.getExchangeRateCur(currencyName).getPurchaseRate()
                );
                em.persist(currency);
            }
            em.getTransaction().commit();
            System.out.println("Uploading is complete!");
        } catch (Exception ex) {
            em.getTransaction().rollback();
            System.out.println("Transaction not complete...");
        }
    }

    private static void showStatistic() {
        Date dateStart, dateFinish;
        while (true) {
            dateStart = dateInput("Enter first date (in format DD.MM.YYYY):");
            dateFinish = dateInput("Enter last date (in format DD.MM.YYYY):");
            if (dateStart != null && dateFinish != null) break;
            System.out.println("Date format is incorrect. Try again...");
        }

        Query query = em.createQuery(
                "SELECT MIN(c.saleRateNB), MAX(c.saleRateNB), AVG(c.saleRateNB), " +
                        "MIN(c.saleRate), MAX(c.saleRate), AVG(c.saleRate), " +
                        "MIN(c.purchaseRate), MAX(c.purchaseRate), AVG(c.purchaseRate) " +
                        "FROM Currency c WHERE c.date BETWEEN :start AND :finish");
        query.setParameter("start", dateStart);
        query.setParameter("finish", dateFinish);
        Object[] list = (Object[]) query.getSingleResult();

        printTable(list, dateStart, dateFinish, "nbu rate", "sale", "purchase");
    }

    private static void printTable(Object[] list, Date start, Date finish, String... rates) {
        System.out.println("\nCurrency statistic from " + df.format(start) + " till " + df.format(finish) + ":");
        System.out.printf("%-10s%12s%12s%12s%n", "PRIVATBANK", "MIN  ", "MAX  ", "AVERAGE");
        System.out.println("----------------------------------------------");
        int size = rates.length;
        for (int i = 0; i < size; i++) {
            System.out.printf("%-10s%12.4f%12.4f%12.4f%n", rates[i], list[i * size], list[i * size + 1], list[i * size + 2]);
        }
    }

    private static Date dateInput(String inputString) {
        System.out.println(inputString);
        String date = sc.nextLine();
        try {
            return df.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    private static int daysBetween(Date start, Date finish) {
        return (int) ((finish.getTime() - start.getTime())/(24 * 60 * 60 * 1000) + 1);
    }

    private static Date dateIncrement(Date date, int increment) {
        return new Date(date.getTime() + ((long) increment * 24 * 60 * 60 * 1000));
    }
}
