package duda;

import net.sf.geographiclib.Geodesic;
import net.sf.geographiclib.GeodesicLine;
import net.sf.geographiclib.GeodesicMask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
public class MainController {

    @Autowired
    private PupilRepository pupilRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    private Geodesic geod = Geodesic.WGS84;

    @PostMapping("/pupil")
    public String addPupil(@Valid @RequestBody Pupil pupil) {

        pupil.getGrades().forEach(grade->grade.setPupil(pupil));

        pupilRepository.save(pupil);

        return String.valueOf(pupil.getId());

    }

    @PostMapping("/school")
    public String addSchool(@Valid @RequestBody School school) {

        schoolRepository.save(school);

        return String.valueOf(school.getId());

    }

    @PostMapping("/setFriendShip/{firstPupilId}/{secondPupilId}")
    public void addFriends(@PathVariable("firstPupilId") Long firstPupilId, @PathVariable("secondPupilId") Long secondPupilId) {

        Assert.isTrue(firstPupilId != secondPupilId, "Pupil cannot be friend with himself");

        Pupil firstPupil = Optional.ofNullable(pupilRepository.findById(firstPupilId)).get().orElseThrow(
                NullPointerException::new);

        Pupil secondPupil = Optional.ofNullable(pupilRepository.findById(secondPupilId)).get().orElseThrow(
                NullPointerException::new);

        firstPupil.getFriends().add(secondPupil);
        secondPupil.getFriends().add(firstPupil);

        pupilRepository.saveAll(Arrays.asList(firstPupil, secondPupil));

    }

    @PostMapping("/enroll/{pupilId}")
    public void enroll(@PathVariable("pupilId") Long pupilId) {

        Pupil pupil = Optional.ofNullable(pupilRepository.findById(pupilId)).get().orElseThrow(
                NullPointerException::new);

        Assert.isNull(pupil.getSchool(), "User already enrolled a school");

        List<School> schools = schoolRepository.findAll();

        Assert.notEmpty(schools, "No schools found");

        Stream<Pupil> pupilFriendsStream = Arrays.stream(pupil.getFriends().toArray(new Pupil[pupil.getFriends().size()]));

        HashMap<School,Double> schoolsScore = new HashMap<>();

        schools.forEach(school -> {

            double pupilGPA = caluclateGPA(pupil);

            if (school.getMinimumGpa() <= pupilGPA) {

                Stream<Pupil> schoolPupils = Arrays.stream(school.getPupils().toArray(new Pupil[school.getPupils().size()]));

                long numberOfFriendsInSchool = schoolPupils.filter(schoolPupil->pupilFriendsStream.anyMatch(friend->friend.getId() == schoolPupil.getId())).count();

                double distance = getDistance(school.getLat(), school.getLon(), pupil.getLat(), pupil.getLon());

                schoolsScore.put(school, numberOfFriendsInSchool * (1 / distance));

            }
            else {

                schoolsScore.put(school, 0d);

            }

        });

        Double currScore = 0d;
        School currSchool = null;

        for (School school : schoolsScore.keySet()) {

            if (schoolsScore.get(school) >= currScore) {
                currSchool = school;
            }

        }

        pupil.setSchool(currSchool);

        pupilRepository.save(pupil);

    }

    // ---

    /**
     * Get the distance between two points in meters.
     * @param lat1 First point's latitude
     * @param lon1 First point's longitude
     * @param lat2 Second point's latitude
     * @param lon2 Second point's longitude
     * @return Distance between the first and the second point in meters
     */
    private double getDistance(double lat1, double lon1, double lat2, double lon2) {
        GeodesicLine line = geod.InverseLine(lat1, lon1, lat2, lon2, GeodesicMask.DISTANCE_IN | GeodesicMask.LATITUDE | GeodesicMask.LONGITUDE);
        return line.Distance();
    }

    private double caluclateGPA(Pupil pupil) {

        String[] letters = { "A+", "A",  "A-", "B+", "B",  "B-", "C+", "C",  "C-", "D",  "F"  };
        double[] grades =  { 4.33, 4.00, 3.67, 3.33, 3.00, 2.67, 2.33, 2.00, 1.67, 1.00, 0.00 };

        // build the symbol table
        HashMap<String, Double> letterGrades = new HashMap<String, Double>();
        for (int i = 0; i < letters.length; i++) {
            letterGrades.put(letters[i], grades[i]);
        }

        // Read list of letter grades and compute GPA
        double sum = 0.0;
        int count = 0;

        for (PupilGrade grade : pupil.getGrades()) {

            double num = letterGrades.get(grade.getGrade());
            sum += num;
            count++;

        }

        double gpa = sum / count;

        return gpa;

    }

}
