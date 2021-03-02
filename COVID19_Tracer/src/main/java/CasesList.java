public class CasesList {
    private int ID;
    private int age;
    private String city;
    private String disease;
    private String name;
    private String risk;
    private String timeStamp;

    public CasesList() {
    }

    public CasesList(int ID,String name,int age, String city,String timeStamp,String disease, String risk) {
        this.ID = ID;
        this.age = age;
        this.city = city;
        this.disease = disease;
        this.name = name;
        this.risk = risk;
        this.timeStamp = timeStamp;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

}
