package com.example.swapswop.Classes;

import java.util.ArrayList;

/**
 * This user class stores the details of the users and performs operations by methods.
 * @author Osama Tanveer, Mohammed Sameer Yaseen
 * @version 1.5, 13 May 2019
 */
public class User {

    String            username, phoneNumber, defaultLocation, profilePicUrl, gender, firstName, lastName, email, userID;
    int               averageRating;
    ArrayList<Rating> ratings;
    ArrayList<String> activeOffersIds;

    // constructor
    /**
     * An empty constructor for this class used for easier firebase implementation.
     */
    public User(){
        // empty constructor for firebase database
    }

    /**
     * Another constructor
     * @param firstName first name of user
     * @param lastName last name of user
     */
    public User(String firstName, String lastName ) {
        this.firstName       = firstName;
        this.lastName        = lastName;
        gender               = "";
        this.username        = "";
        this.phoneNumber     = "";
        this.defaultLocation = "";
        this.profilePicUrl   = "";
        this.averageRating   = 0;
        this.email           = email;
        this.ratings         = new ArrayList <Rating>();
        activeOffersIds      = new ArrayList <String>();
    }

    /**
     * This method returns the user id of the user as assigned by Firebase.
     * @return userId userId as by firebase
     */
    public String getUserID() {
        return userID;
    }

    /**
     * This method sets the user id of the user as assigned by Firebase;
     * @param userID userId as given by ifrebase
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * This method returns the email of the user
     * @return email user email
     */
    public String getEmail() {
        return email;
    }

    /**
     * This method sets the email of the user
     * @param email email user email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    // methods

    /**
     * This method returns the first name of the user.
     * @return firstName the first name of user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * This method sets the first name of the user.
     * @param firstName first name of the user
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * This method returns the last name of the user
     * @return lastName the last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * This method sets the last name of the user.
     * @param lastName the last name of the user.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * This method returns the active offers of the user.
     * @return activeOffersIds the ids of the active offers.
     */
    public ArrayList<String> getActiveOffersIds() {
        return activeOffersIds;
    }

    /**
     * This method adds active offers for the user.
     * @param activeOffer the active offers of the users
     */
    public void addActiveOfferId(String activeOffer) {
        this.activeOffersIds.add(activeOffer);
    }

    /**
     * This method removes an offer from the active offer lists.
     * @param activeOffer the active offer
     */
    public void removeActiveOfferId(String activeOffer){
        for (int i = 0; i < activeOffersIds.size(); i++){
            if (activeOffersIds.get(i).equals(activeOffer)){
                activeOffersIds.remove(i);
            }
        }
    }

    /**
     * This method returns the gender of the user
     * @return gender the gender of the user
     */
    public String getGender() {
        return gender;
    }

    /**
     * This method sets the gender of the user
     * @param gender the gender of user
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * This method returns the average rating of the user
     * @return the average rating of the user
     */
    public double getAverageRating() {
        int ratingsSum = 0;
        int ratingsCount = 0;

        for (int i = 0; i < ratings.size(); i++){
            ratingsSum = ratings.get(i).getRate();
            ratingsCount ++;
        }

        if (ratingsCount == 0){
            return 0;
        }else{
            return (ratingsSum/ratingsCount);
        }
    }

    /**
     * This method returns all the ratings of the user
     * @return ratings the ratings of the user
     */
    public ArrayList <Rating> getRatings() {
        return ratings;
    }

    /**
     * This method adds the ratings of the user
     * @param ratings all the ratings of the user
     */
    public void addRating( Rating ratings ) {
        this.ratings.add(ratings);
    }

    /**
     * This method sets username of the user.
     * @param username the username of the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * This method sets the phone number of the location.
     * @param phoneNumber the user's phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * This method sets the default location of the user.
     * @param defaultLocation the default location.
     */
    public void setDefaultLocation(String defaultLocation) {
        this.defaultLocation = defaultLocation;
    }

    /**
     * This method returns the url of the profile picture
     * @param profilePicUrl the url
     */
    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    /**
     * This method returns the username of the user.
     * @return username the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * This methods gets the phone number of the user.
     * @return phoneNumber user's phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * This method gets the default location of the user.
     * @return defaultLoction the location of the user
     */
    public String getDefaultLocation() {
        return defaultLocation;
    }

    /**
     * This method returns the profile picture of the user.
     * @return profilePicUrl the profile picture url of the user
     */
    public String getProfilePicUrl() {
        return profilePicUrl;
    }
}
