package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name="Unit.findByName", query = "select u from Unit u join u.section p where p.name = :parentName " +
        "and u.name = :name"),
        @NamedQuery(name="Unit.findBySection", query = "select u from Unit u join u.section s where s.id = :sectionId"),
        @NamedQuery(name="Unit.findAllBySectionName", query = "select u from Unit u join u.section s where s.name = :sectionName"),
        @NamedQuery(name="Unit.updateManager", query = "update Unit u set u.manager.userId = :userId where" +
                " u.id= :id")
})
public class Unit {
    Integer id;
    String name;
    Section section;
    List<User> staffs;
    User manager;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne
    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "unit")
    public List<User> getStaffs() {
        return staffs;
    }

    public void setStaffs(List<User> staffs) {
        this.staffs = staffs;
    }

    //防止toJson时的循环引用， manager->unit->manger->......
    @JsonIgnore
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT))
    @NotFound(action = NotFoundAction.IGNORE)
    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }
}
