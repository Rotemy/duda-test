package duda;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
class School {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message="Name cannot be missing or empty")
    private String name;

    @NotNull(message="Latitude cannot be missing or empty")
    private Double lat;

    @NotNull(message="Longitude cannot be missing or empty")
    private Double lon;

    @NotNull(message="Minimum GPA cannot be missing or empty")
    private Double minimumGpa; // GPA can be with a decimal point

    @NotNull(message="Max Number Of Pupils cannot be missing or empty")
    private Integer maxNumberOfPupils;

    @OneToMany(mappedBy = "school")
    private List<Pupil> pupils;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Pupil> getPupils() {
        return pupils;
    }

    public void setPupils(List<Pupil> pupils) {
        this.pupils = pupils;
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

    public Double getMinimumGpa() {
        return minimumGpa;
    }

    public void setMinimumGpa(Double minimumGpa) {
        this.minimumGpa = minimumGpa;
    }

    public Integer getMaxNumberOfPupils() {
        return maxNumberOfPupils;
    }

    public void setMaxNumberOfPupils(Integer maxNumberOfPupils) {
        this.maxNumberOfPupils = maxNumberOfPupils;
    }

}