package duda.dao;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Pupil {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "Latitude cannot be missing or empty")
    private Double lat;

    @NotNull(message = "Longitude cannot be missing or empty")
    private Double lon;

    @NotNull(message = "Grades cannot be missing or empty")
    @OneToMany(mappedBy = "pupil", cascade = CascadeType.ALL)
    private List<PupilGrade> grades;

    @NotNull(message = "Name cannot be missing or empty")
    private String name;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "friends",
            joinColumns = @JoinColumn(name = "pupilA", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "pupilB",
                    referencedColumnName = "id"))
    private List<Pupil> friends;

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

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public List<Pupil> getFriends() {
        return friends;
    }

    public void setFriends(List<Pupil> friends) {
        this.friends = friends;
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