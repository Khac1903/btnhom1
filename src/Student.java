public class Student {

    // TODO: khai bao cac thuoc tinh cho Student

    // TODO: khai bao cac phuong thuc getter, setter cho Student
    private String name;
    private String id;
    private String group;
    private String email;
    /**
     * Constructor 1
     */
    public String getName() {
        return name;
    }
    public void setName(String n) {
        this.name=n;

    }
    public String getId() {
        return id;
    }
    public void setId(String i) {
        this.id=i;
    }
    public String getGroup() {
        return group;
    }
    public void setGroup(String g) {
        this.group=g;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String e) {
        this.email=e;
    }

    Student() {
        // TODO:
        name = "Student";
        id="000";
        group="K62CB";
        email="uet@vnu.edu.vn";
    }

    /**
     * Constructor 2
     * @param n
     * @param sid
     * @param em
     */
    Student(String name, String id, String email) {
        // TODO:
        group="K62CB";
    }

    /**
     * Constructor 3
     * @param s
     */
    Student(Student s) {
        // TODO:
        this.name=s.name;
        this.id=s.id;
        this.group=s.group;
        this.email=s.email;
    }

    String getInfo() {
        // TODO:
        return this.name + " - " +  this.id + " - " + this.group + " - " + this.email;
    }
    public static void main(String[] args) {
        Student s  = new Student();
        s.name="Nguyen Van An";
        s.id = "17020001";
        s.group = "K62CC" ;
        s.email = "17020001@vnu.edu.vn";
        System.out.println(s.getInfo());
    }
}