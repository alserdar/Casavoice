package alserdar.casavoice.models;


import android.net.Uri;

import java.util.Date;

/**
 * Created by ALAZIZY on 11/19/2017.
 */

public class UserModel {

    public UserModel() {
    }

    private Uri profilePicture ;
    private String theUID ,originalName , userName ,gender , id , littleID  , email , phone , vip ,
            country , BirthDate , house , pet , car ;
    private int  theProgressOfLevel , theProgressOfLevelInPar, level , balanceOfCoins , dailySendBalance ,
            dailyRecBalance , weeklySendBalance ,
            weeklyRecBalance , monthlySendBalance , monthlyRecBalance  , happinessRoom , happinessPerson , happinessCar , happinessPet , happinessHouse;
    private boolean userSus , hasRoom , hasVip , hasNotification , hasMessages , redMe , spin , terms;

    private Date startVip ;
    private Date endVip ;


    public int getHappinessRoom() {
        return happinessRoom;
    }

    public void setHappinessRoom(int happinessRoom) {
        this.happinessRoom = happinessRoom;
    }

    public int getHappinessPerson() {
        return happinessPerson;
    }

    public void setHappinessPerson(int happinessPerson) {
        this.happinessPerson = happinessPerson;
    }

    public int getHappinessCar() {
        return happinessCar;
    }

    public void setHappinessCar(int happinessCar) {
        this.happinessCar = happinessCar;
    }

    public int getHappinessPet() {
        return happinessPet;
    }

    public void setHappinessPet(int happinessPet) {
        this.happinessPet = happinessPet;
    }

    public int getHappinessHouse() {
        return happinessHouse;
    }

    public void setHappinessHouse(int happinessHouse) {
        this.happinessHouse = happinessHouse;
    }

    public boolean isSpin() {
        return spin;
    }

    public boolean isTerms() {
        return terms;
    }

    public void setTerms(boolean terms) {
        this.terms = terms;
    }

    public void setSpin(boolean spin) {
        this.spin = spin;
    }

    public String getTheUID() {
        return theUID;
    }

    public void setTheUID(String theUID) {
        this.theUID = theUID;
    }

    public Date getStartVip() {
        return startVip;
    }

    public void setStartVip(Date startVip) {
        this.startVip = startVip;
    }

    public Date getEndVip() {
        return endVip;
    }

    public void setEndVip(Date endVip) {
        this.endVip = endVip;
    }


    public int getTheProgressOfLevelInPar() {
        return theProgressOfLevelInPar;
    }

    public void setTheProgressOfLevelInPar(int theProgressOfLevelInPar) {
        this.theProgressOfLevelInPar = theProgressOfLevelInPar;
    }

    public boolean isRedMe() {
        return redMe;
    }

    public void setRedMe(boolean redMe) {
        this.redMe = redMe;
    }

    public boolean isUserSus() {
        return userSus;
    }

    public void setUserSus(boolean userSus) {
        this.userSus = userSus;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public boolean isHasVip() {
        return hasVip;
    }

    public void setHasVip(boolean hasVip) {
        this.hasVip = hasVip;
    }

    public boolean isHasMessages() {
        return hasMessages;
    }

    public void setHasMessages(boolean hasMessages) {
        this.hasMessages = hasMessages;
    }

    public boolean isHasNotification() {
        return hasNotification;
    }

    public void setHasNotification(boolean hasNotification) {
        this.hasNotification = hasNotification;
    }

    public String getLittleID() {
        return littleID;
    }

    public int getTheProgressOfLevel() {
        return theProgressOfLevel;
    }

    public void setTheProgressOfLevel(int theProgressOfLevel) {
        this.theProgressOfLevel = theProgressOfLevel;
    }

    public void setLittleID(String littleID) {
        this.littleID = littleID;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public int getDailySendBalance() {
        return dailySendBalance;
    }

    public void setDailySendBalance(int dailySendBalance) {
        this.dailySendBalance = dailySendBalance;
    }

    public int getDailyRecBalance() {
        return dailyRecBalance;
    }

    public void setDailyRecBalance(int dailyRecBalance) {
        this.dailyRecBalance = dailyRecBalance;
    }

    public int getWeeklySendBalance() {
        return weeklySendBalance;
    }

    public void setWeeklySendBalance(int weeklySendBalance) {
        this.weeklySendBalance = weeklySendBalance;
    }

    public int getWeeklyRecBalance() {
        return weeklyRecBalance;
    }

    public void setWeeklyRecBalance(int weeklyRecBalance) {
        this.weeklyRecBalance = weeklyRecBalance;
    }

    public int getMonthlySendBalance() {
        return monthlySendBalance;
    }

    public void setMonthlySendBalance(int monthlySendBalance) {
        this.monthlySendBalance = monthlySendBalance;
    }

    public int getMonthlyRecBalance() {
        return monthlyRecBalance;
    }

    public void setMonthlyRecBalance(int monthlyRecBalance) {
        this.monthlyRecBalance = monthlyRecBalance;
    }

    public Uri getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Uri profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getPet() {
        return pet;
    }

    public void setPet(String pet) {
        this.pet = pet;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public int getBalanceOfCoins() {
        return balanceOfCoins;
    }

    public void setBalanceOfCoins(int balanceOfCoins) {
        this.balanceOfCoins = balanceOfCoins;
    }

    public boolean isHasRoom() {
        return hasRoom;
    }

    public void setHasRoom(boolean hasRoom) {
        this.hasRoom = hasRoom;
    }
}
