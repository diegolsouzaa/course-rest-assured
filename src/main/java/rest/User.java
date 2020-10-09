package rest;

public class User {
    private long id;
    private  String name;
    private Integer age;
    private Double salary;

    public User(String name, Integer age) {
        super();
        this.name = name;
        this.age = age;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public Double getSalary() {
        return salary;
    }

    @Override
    public String toString(){
        return  "User [id="+id+", name="+name+", age"+age+", salary="+salary+"]";
    }
}
