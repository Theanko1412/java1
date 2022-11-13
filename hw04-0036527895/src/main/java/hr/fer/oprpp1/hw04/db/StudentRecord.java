package hr.fer.oprpp1.hw04.db;

import java.util.Objects;

public class StudentRecord {

    private String jmbag;
    private String lastName;
    private String firstName;
    private Integer finalGrade;

    public StudentRecord(String jmbag, String lastName, String firstName, Integer finalGrade) {
        this.jmbag = jmbag;
        this.lastName = lastName;
        this.firstName = firstName;
        this.finalGrade = finalGrade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentRecord that = (StudentRecord) o;
        return jmbag.equals(that.jmbag) && lastName.equals(that.lastName) && firstName.equals(that.firstName) && finalGrade.equals(that.finalGrade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jmbag, lastName, firstName, finalGrade);
    }

    @Override
    public String toString() {
        return "StudentRecord{" +
                "jmbag='" + jmbag + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", finalGrade=" + finalGrade +
                '}';
    }

    public String getJmbag() {
        return jmbag;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public Integer getFinalGrade() {
        return finalGrade;
    }
}
