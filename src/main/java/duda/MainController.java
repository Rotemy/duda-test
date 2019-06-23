package duda;

import duda.dao.Pupil;
import duda.dao.PupilGrade;
import duda.dao.School;
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

        pupil.setSchool(new PupilSchoolFinder(schools, pupil).find());

        pupilRepository.save(pupil);

    }

}
