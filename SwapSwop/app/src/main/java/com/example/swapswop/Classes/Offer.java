package com.example.swapswop.Classes;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Date;

/**
 * This an offer class it contains details about the offer that is to be posted.
 * @author Mohammed Yaseen, Ali Caner
 * @version 1.00 19/05/04
 */
public class Offer {

    // properties
    String            title;
    String            description;
    String            imageUrl;
    String            location;
    boolean           exchanged;
    Date              date;
    String            category;
    String            userName;
    String            userId;
    boolean           onLoan;
    String            processType;
    String            profileImageUrl;
    long              order;
    double            averageUserRating;
    ArrayList<Rating> ratings;
    double            rating;

    // constructors
    /**
     * An empty constructor needed for creating object and then setting attributes
     * to put object on to Firebase database.
     */
    public Offer(){

    }

    /**
     * Another constructor for this class.
     * @param title the title of the offer.
     * @param userName the username of the user who posts the offer.
     * */
    public Offer(String title, String userName ){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        User user = new User();

        user.setUsername(userName);

        if ( firebaseUser.getUid().equals( user.getUserID()))
        {
            this.userName = user.getUserID();
        }
        else
        {
            this.userName = "";
        }

        this.title = title;
        this.description   = "";
        this.imageUrl      = "";
        this.location      = "";
        this.exchanged     = false;
        this.date          = new Date();
        this.category      = "";
        this.userId        = "";
        this.processType   = "";
        profileImageUrl    = "";
        this.onLoan        = false;
        this.averageUserRating = 0;
    }

    // methods


    /**
     * This method returns the image url of the user profile picture.
     * @return profileImageUrl the url of the user profile picture in the Firebase storage
     */
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    /**
     * This method returns the average rating of the user.
     * @return averageUserRating the average rating of the user
     */
    public double getAverageUserRating() {
        return averageUserRating;
    }

    /**
     * This method returns the position of the offer in the feed.
     * @return order the position of the offer in the home feed
     */
    public long getOrder() {
        return order;
    }

    /**
     * This method returns the type of the process the offer is about: swap or loan.
     * @return processType the type of process
     */
    public String getProcessType() {
        return processType;
    }

    /**
     * This method returns if the item is exchanged or not.
     * @return exchanged the state of the item
     */
    public boolean isExchanged() {
        return exchanged;
    }

    /**
     * This method returns the state of the loan item, if it is on loan or not.
     * @return onLoan the state of the item to be loaned
     */
    public boolean isOnLoan() {
        return onLoan;
    }

    /**
     * This method returns the title of the offer.
     * @return title the title of the offer
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method returns the description of the offer.
     * @return description the description of the offer
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method returns the image url of the picture of the offer that is stored.
     * @return imageUrl the url of the image in the Firebase storage
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * This method returns the location the offer is available.
     * @return location the location the offer is available
     */
    public String getLocation() {
        return location;
    }

    /**
     * This method returns the date the offer is posted.
     * @return date the date the offer was posted
     */
    public Date getDate() {
        return date;
    }

    /**
     * This method returns the category the offer was posted in.
     * @return category the category of the offer
     */
    public String getCategory() {
        return category;
    }

    /**
     * This method returns the username of the user who posted the offer.
     * @return username the username of the user
     */
    public String getUserName() {
        return userName;
    }

    /**
     * This method returns the userid as in Firebase of the user who posted the offer.
     * @return userId the id of the user as in firebase
     */
    public String getUserId() {
        return userId;
    }

    /**
     * This method sets the title of the offer.
     * @param title the title of the offer
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * This method sets the description of the offer.
     * @param description the description of the offer
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * This method sets the image url of the offer.
     * @param imageUrl imageUrl of the offer
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * This method sets the location of the offer
     * @param location the location of the offer
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * This method sets the exchanged status of the offer.
     * @param exchanged true if exchanged, false otherwise
     */
    public void setExchanged(boolean exchanged) {
        this.exchanged = exchanged;
    }

    /**
     * This method sets date the offer was posted.
     * @param date the date the offer was posted.
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * This method sets the category of the offer.
     * @param category the category type
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * This method sets the username of the user who posted the offer.
     * @param userName the username of the user
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * This method sets the user id of the user, according to Firebase, who posted the offer.
     * @param userId firebaseUID according to firebase
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * This method sets if the item is on loan or not.
     * @param onLoan the state of the loaned item
     */
    public void setOnLoan(boolean onLoan) {
        this.onLoan = onLoan;
    }

    /**
     * This method sets the process type of the offer, if it is to swap or loan.
     * @param processType the process type of the offer
     */
    public void setProcessType(String processType){
        this.processType = processType;
    }

    /**
     * This method returns the order of the post in the feed of the users
     * @param order the position of the offer in the feed
     */
    public void setOrder(long order) {
        this.order = order;
    }

    /**
     * This method sets the average user rating.
     * @param averageUserRating the average rating of the user
     */
    public void setAverageUserRating(double averageUserRating) {
        this.averageUserRating = averageUserRating;
    }

    /**
     * This method sets the url of the profile picture of the user in the storage
     * @param profileImageUrl the url of the profile picture
     */
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    /**
     * This method returns all the ratings the user as received.
     * @return arraylist of ratings
     */

    public ArrayList<Rating> getAllRatings(){
        if(ratings != null)
            return ratings;
        else
            return new ArrayList<Rating>();
    }

    /**
     * This method returns the average rating of the user.
     * @return rating the rating of the user
     */
    public double getRating() {

        if (ratings != null) {
            double a = 0;
            int    i = 0;

            while (i < ratings.size()) {
                a = a + (double) ratings.get(i).getRate();
                i = i + 1;
            }
            rating = (a / (i));
            return rating;
        }
        else
            return rating;
    }

    //static methods

    /**
     * This method sorts the offers according to the input it receives.
     * @param input the type of search
     * @param listToSearch the list to search in
     * @return Results the new searched list in order
     */
    public static ArrayList<Offer> sort(String input , ArrayList<Offer> listToSearch) {
        ArrayList<Offer> Results = new ArrayList<>();
        ArrayList<Offer> tmp     = listToSearch;

        if(input.equalsIgnoreCase("Rating: High to Low")) {

            while(listToSearch.size() > 0){
                int a = findBigestRating(listToSearch);
                Results.add(listToSearch.get(a));
                listToSearch.remove(a);
            }
        }

        if(input.equalsIgnoreCase("Rating: Low to High") ) {

            while(listToSearch.size() > 0){
                int a = findLowestRating(listToSearch);
                Results.add(listToSearch.get(a));
                listToSearch.remove(a);
            }
        }

        if(input.equalsIgnoreCase("Title: Alphabetically Ascending")) {

            while( listToSearch.size() > 0) {
                int a = findBigestLetter(listToSearch);
                Results.add(listToSearch.get(a));
                listToSearch.remove(a);
            }
        }

        if(input.equalsIgnoreCase("Title: Alphabetically Descending")) {

            while( listToSearch.size() > 0) {
                int a = findSmallestLetter(listToSearch);
                Results.add(listToSearch.get(a));
                listToSearch.remove(a);
            }
        }

        if(input.equalsIgnoreCase("Rating: Low to High")) {

            while( listToSearch.size() > 0) {
                int a = findBigestRating(listToSearch);
                Results.add(listToSearch.get(a));
                listToSearch.remove(a);
            }
        }

        listToSearch = tmp;
        return Results;
    }

    /**
     * This method finds the offer with the largest title and returns the index.
     * @param listToSearch the arraylist to search in
     * @return index the index of the post with the largest title
     */

    public static int findBigestLetter(ArrayList<Offer> listToSearch) {
        String bigest = listToSearch.get(0).getTitle();
        int index = 0;
        int i = 0;
        while( i < listToSearch.size() - 1 ) {
            if( bigest.compareTo( listToSearch.get(i+1).getTitle()) > 0) {
                bigest = listToSearch.get(i+1).getTitle();
                index = i + 1;
            }
            i = i + 1;
        }
        return index;
    }

    /**
     * This method finds the offer with the smallest title and returns the index.
     * @param listToSearch the arraylist to search in
     * @return index the index of the post with the smallest title
     */
    public static int findSmallestLetter(ArrayList<Offer> listToSearch) {

        // variables
        String smallest = listToSearch.get(0).getTitle();
        int    index    = 0;
        int    i        = 0;

        while( i < listToSearch.size() - 1 ) {
            if(  smallest.compareTo( listToSearch.get(i+1).getTitle()) < 0) {
                smallest = listToSearch.get(i+1).getTitle();
                index = i + 1;
            }
            i = i + 1;
        }
        return index;
    }

    /**
     * This method finds the user with the smallest rating in the ArrayList and returns it index.
     * @param listToSearch the list to search in
     * @return index the index of the offer with the smallest user rating
     */
    public static int findLowestRating(ArrayList<Offer> listToSearch) {
        // variables
        double smallest = 0;
        int    index    = 0;

        for(int i = 0; i < listToSearch.size(); i++) {
            if( listToSearch.get(i).getRating() < smallest ) {
                index = i;
            }
        }
        return index;
    }

    /**
     * This method finds the user with the largest rating in the ArrayList and returns it index.
     * @param listToSearch the list to search in
     * @return index the index of the offer with the highest user rating
     */
    public static int findBigestRating(ArrayList<Offer> listToSearch) {
        // variables
        double bigest = 0;
        int    index  = 0;

        for(int i = 0; i < listToSearch.size(); i++) {
            if( listToSearch.get(i).getRating() > bigest ) {
                index = i;
            }
        }
        return index;
    }
}