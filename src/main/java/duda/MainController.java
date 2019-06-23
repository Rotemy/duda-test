package duda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class MainController {

    @Autowired
    private PupilRepository pupilRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @PostMapping("/pupil")
    public String addStudent(@Valid @RequestBody Pupil pupil) {

        pupil.getGrades().forEach(grade->grade.setPupil(pupil));

        pupilRepository.save(pupil);

        return String.valueOf(pupil.getId());

    }

    @PostMapping("/school")
    public String addStudent(@Valid @RequestBody School school) {

        schoolRepository.save(school);

        return String.valueOf(school.getId());

    }

}
