package Resource;

public class userInfo {

    String id;
    int counter;

    public userInfo(String userID, int banNum) {
        this.id = userID;
        this.counter = banNum;
    }

    public String getId(userInfo userInfo){
        return id;
    }

    public int getCounter(){
        return counter;
    }

    public void setId(String userID){
        this.id = userID;
    }

    public void setCounter(int banNum){
        this.counter = banNum;
    }

    public void addWarning(){
        this.counter = counter++;
    }

}
