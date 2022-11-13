package hr.fer.oprpp1.hw04.db;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentDatabase {

    private final Integer JMBAG_INDEX = 0;
    private final Integer LASTNAME_INDEX = 1;
    private final Integer FIRSTNAME_INDEX = 2;
    private final Integer GRADE_INDEX = 3;

    private List<StudentRecord> studentRecords;
    private Map<String, StudentRecord> index;

    public StudentDatabase(List<String> records) {
        studentRecords = new ArrayList<>();
        index = new HashMap<>();
        for(String s : records) {
            ArrayList<String> studentRecordList = new ArrayList<>(List.of(s.split("\t")));


            if(index.containsKey(studentRecordList.get(JMBAG_INDEX)))
                throw new KeyAlreadyExistsException("Given jmbag already exist in database!");
            try {
                if(Integer.parseInt(studentRecordList.get(GRADE_INDEX)) < 1
                        && Integer.parseInt(studentRecordList.get(GRADE_INDEX)) > 5)
                    throw new IllegalArgumentException("Given grade is not between 1 and 5!");
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Given grade is not a number!");
            }

            StudentRecord sr = new StudentRecord(
                    studentRecordList.get(JMBAG_INDEX),
                    studentRecordList.get(LASTNAME_INDEX),
                    studentRecordList.get(FIRSTNAME_INDEX),
                    Integer.parseInt(studentRecordList.get(GRADE_INDEX))
            );
            studentRecords.add(sr);
            index.put(sr.getJmbag(), sr);
        }
    }

    public StudentRecord forJMBAG(String jmbag) {
        return index.get(jmbag);
    }

    public List<StudentRecord> filter(IFilter filter) {
        List<StudentRecord> acceptedList = new ArrayList<>();

        for(StudentRecord sr : studentRecords) {
            if(filter.accepts(sr)) acceptedList.add(sr);
        }

        return acceptedList;
    }


    //for testing purposes
    public List<StudentRecord> getStudentRecords() {
        return studentRecords;
    }

    public Map<String, StudentRecord> getIndex() {
        return index;
    }
}
