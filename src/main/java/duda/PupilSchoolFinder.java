package duda;

import duda.dao.Pupil;
import duda.dao.PupilGrade;
import duda.dao.School;
import net.sf.geographiclib.Geodesic;
import net.sf.geographiclib.GeodesicLine;
import net.sf.geographiclib.GeodesicMask;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class PupilSchoolFinder {

    private List<School> schools;
    private Pupil pupil;

    HashMap<School,Double> schoolsScore = new HashMap<>();

    private Geodesic geod = Geodesic.WGS84;

    public PupilSchoolFinder(List<School> schools, Pupil pupil) {
        this.schools = schools;
        this.pupil = pupil;
    }

    public School find() {

        Stream<Pupil> pupilFriendsStream = Arrays.stream(pupil.getFriends().toArray(new Pupil[pupil.getFriends().size()]));

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
                currScore = schoolsScore.get(school);
                currSchool = school;
            }

        }

        return currSchool;

    }

    // ----


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
