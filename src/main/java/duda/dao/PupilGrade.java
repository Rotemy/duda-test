package duda.dao;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class PupilGrade {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "Course name cannot be missing or empty")
    private String courseName;

    @NotNull(message = "Grade cannot be missing or empty")
    private String grade;

    @NotNull(message = "Pupil cannot be missing or empty")
    @ManyToOne
    @JoinColumn(name = "pupil_id")
    private Pupil pupil;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pupil getPupil() {
        return pupil;
    }

    public void setPupil(Pupil pupil) {
        this.pupil = pupil;
    }

}
