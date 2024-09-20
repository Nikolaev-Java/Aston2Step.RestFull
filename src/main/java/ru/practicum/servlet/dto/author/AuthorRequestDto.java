package ru.practicum.servlet.dto.author;

public class AuthorRequestDto {
    private String firstName;
    private String lastName;

    public AuthorRequestDto() {
    }

    public AuthorRequestDto(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
