package duda;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Pupil {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message="Latitude cannot be missing or empty")
    private Double lat;

    @NotNull(message="Longitude cannot be missing or empty")
    private Double lon;

    @NotNull(message="Grades cannot be missing or empty")
    @OneToMany(mappedBy = "pupil", cascade = CascadeType.ALL)
    private List<PupilGrade> grades;

    @NotNull(message="Name cannot be missing or empty")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public List<PupilGrade> getGrades() {
        return grades;
    }

    public void setGrades(List<PupilGrade> grades) {
        this.grades = grades;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

@Entity
class PupilGrade {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message="Course name cannot be missing or empty")
    private String courseName;

    @NotNull(message="Grade cannot be missing or empty")
    private Integer grade;

    @NotNull(message="Pupil cannot be missing or empty")
    @ManyToOne
    @JoinColumn(name="pupil_id")
    private Pupil pupil;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
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