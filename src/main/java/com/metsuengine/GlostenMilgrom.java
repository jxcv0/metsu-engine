package com.metsuengine;

public class GlostenMilgrom {

    /**
     * The probability a trade is from a trader with private information
     */
    private double speculatorProbability;

    /**
     * The prior probability that the asset value is high TODO - get from delta
     */
    private double theta;

    /**
     * 
     * @param speculatorProbability the probability a trade is from a trader with private information
     * @param theta the prior probability that the asset value is high
     */
    public GlostenMilgrom(double speculatorProbability, double theta) {
        this.speculatorProbability = speculatorProbability;
        this.theta = theta;
    }

    /**
     * Calculates the conditional probability of observing a buy order given the value of the asset is high
     * @return the probability
     */
    public double conditionalBuyIfHigh() {
        return speculatorProbability + ((1 - speculatorProbability) * 0.5);
    }

    /**
     * Calculates the conditional probability of observing a buy order given the value of the asset is high
     * @return
     */
    public double conditionalSellIfHigh() {
        return 1 - conditionalBuyIfHigh();
    }

    /**
     * Calculates the conditional probability of observing a buy order given the value of the asset is low
     * @return the probability
     */
    public double conditionalBuyIfLow() {
        return (1 - speculatorProbability) * 0.5;
    }

    /**
     * Calculates the conditional probability of observing a sell order given the value of the asset is low
     * @return the probability
     */
    public double conditionalSellIfLow() {
        return 1 - conditionalBuyIfLow();
    }

    /**
     * Calculates the unconditional probability of observing a buy order
     * @return the probability
     */
    public double unconditionalBuy() {
        return (conditionalBuyIfHigh() * theta) + (conditionalBuyIfLow() * (1 - theta));
    }

    /**
     * Calculates the unconditional probability of observing a sell order
     * @return the probability
     */
    public double unconditionalSell() {
        return (conditionalSellIfHigh() * theta) + (conditionalSellIfLow() * (1 - theta));
    }

    /**
     * Calculates the conditional probability the value of the asset is high conditional on recieving a buy order
     * @return the probability
     */
    public double conditionalHighIfBuy() {
        return (theta * conditionalBuyIfHigh()) / unconditionalBuy();
    }

    /**
     * Calculates the conditional probability the value of the asset is low conditional on recieving a sell order
     * @return the probability
     */
    public double conditionalLowIfSell() {
        return (theta * (1 - conditionalSellIfLow())) / unconditionalSell();
    }

    /**
     *  Calculates the expected asset value upon recieving a buy trade (the price of the ask quote)
     * @param highValue the high value of the asset
     * @param lowValue the low value of the asset
     * @return the probability
     */
    public double expectedAssetValueAfterBuy(double highValue, double lowValue) {
        return (conditionalHighIfBuy() * highValue) + ((1 - conditionalHighIfBuy()) * lowValue);
    }

    /**
     *  Calculates the expected asset value upon recieving a sell trade (the price of the bid quote)
     * @param highValue the high value of the asset
     * @param lowValue the low value of the asset
     * @return the probability
     */
    public double expectedAssetValueAfterSell(double highValue, double lowValue) {
        return (conditionalLowIfSell() * highValue) + ((1 - conditionalLowIfSell()) * lowValue);
    }
}
