
public class StudentManagement  {
    private Student[] students = new Student[100];
    private int sum=0;
    public void addStudent(Student newStudent) {
        students[sum]=newStudent;
        sum++;
    }
    public static boolean sameGroup(Student s1, Student s2) {
        String a=s1.getGroup();
        String b=s2.getGroup();
        if (a.equals(b)) {
            return true;
        }
        return false;

    }
    public String studentsByGroup() {
        String result="";
        boolean[] kt= new boolean[sum];
        for (int i=0;i<sum;i++) {
            if(kt[i]) continue;
            String cur = students[i].getGroup();
            result += cur + "/n";
            for (int j=i;j<sum;j++) {
                if (cur == students[j].getGroup()) {
                    result += students[j].getInfo() + "/n" ;
                    kt[j]=true;
                }
            }
        }
        return result;
    }
    public void removeStudent(String id) {
        for (int i=0;i<sum;i++) {
            if (students[i].getId().equals(id)) {
                for (int j = i; j < sum; j++) {
                    students[j] = students[j + 1];
                }
                students[sum--] = null;
                sum--;
                break;
            }
        }
    }

}
