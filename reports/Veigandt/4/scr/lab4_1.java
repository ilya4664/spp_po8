import java.util.ArrayList;
import java.util.List;

public class Department {
    private String name;
    private List<Post> posts;

    public Department(String name) {
        this.name = name;
        this.posts = new ArrayList<>();
    }

    public void addPost(String name) {
        posts.add(new Post(name));
    }

    public int getTotalEmployees() {
        int totalEmployees = 0;
        for (Post post : posts) {
            totalEmployees += post.getEmployees().size();
        }
        return totalEmployees;
    }
    public void displayEmployees() {
        System.out.println("Employees in the " + name + " department:");
        for (Post post : posts) {
            System.out.println("Position: " + post.getName() + ", Employees: " + post.getEmployees());
        }
        System.out.println("Total employees in the " + name + " department: " + getTotalEmployees());
    }

    public class Post {
        private String name;
        private List<String> employees;

        public Post(String name) {
            this.name = name;
            this.employees = new ArrayList<>();
        }

        public Post(String name, List<String> employees) {
            this.name = name;
            this.employees = employees;
        }

        public String getName() {
            return name;
        }

        public List<String> getEmployees() {
            return employees;
        }

        public void addEmployee(String employeeName) {
            employees.add(employeeName);
        }


    }

    public static void main(String[] args) {
        Department itDepartment = new Department("IT");
        itDepartment.addPost("Software Developer");
        itDepartment.addPost("System Analyst");

        itDepartment.posts.get(0).addEmployee("Ilya");
        itDepartment.posts.get(0).addEmployee("Semyon");

        itDepartment.posts.get(1).addEmployee("Egor");

        itDepartment.displayEmployees();

    }
}
