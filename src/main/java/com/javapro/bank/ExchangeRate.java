package com.javapro.bank;

public class ExchangeRate {
    private String baseCurrency;
    private String currency;
    private Float saleRate;
    private Float purchaseRate;
    private Float saleRateNB;
    private Float purchaseRateNB;

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Float getSaleRate() {
        return saleRate;
    }

    public void setSaleRate(Float saleRate) {
        this.saleRate = saleRate;
    }

    public Float getPurchaseRate() {
        return purchaseRate;
    }

    public void setPurchaseRate(Float purchaseRate) {
        this.purchaseRate = purchaseRate;
    }

    public Float getSaleRateNB() {
        return saleRateNB;
    }

    public void setSaleRateNB(Float saleRateNB) {
        this.saleRateNB = saleRateNB;
    }

    public Float getPurchaseRateNB() {
        return purchaseRateNB;
    }

    public void setPurchaseRateNB(Float purchaseRateNB) {
        this.purchaseRateNB = purchaseRateNB;
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "baseCurrency='" + baseCurrency + '\'' +
                ", currency='" + currency + '\'' +
                ", saleRate=" + saleRate +
                ", purchaseRate=" + purchaseRate +
                ", saleRateNB=" + saleRateNB +
                ", purchaseRateNB=" + purchaseRateNB +
                '}';
    }
}
