package com.example.swapswop.Classes;

/**
 * This rating class is for the ratings of the users.
 * @author Ali Caner
 * @version 1.00, 11 May 2019
 */
public class Rating {

    // Properties
    int rate;

    /**
     * An empty constructor for this class.
     */
    public Rating(){
        // No argument constructor is needed for firebase database
    }

    /**
     * Another constructor.
     * @param rate the rating
     */
    public Rating( int rate ){
        this.rate = rate;
    }

    /**
     * The rating in string format
     * @return rate the string
     */
    public String toString(){
        return "" + rate;
    }

    /**
     * This method sets the rate
     * @param rate the rate
     */
    public void setRate( int rate){
        this.rate = rate;
    }

    /**
     * The method returns the rate
     * @return rate the rate
     */
    public int getRate(){
        return rate;
    }
}
