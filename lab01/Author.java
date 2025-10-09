public class Author {
    public enum Gender { MALE, FEMALE }
    private String name;
    private String email;
    private Gender gender;

    public Author(String name, String email, Gender gender) {
        this.name = name;
        this.email = email;
        this.gender = gender;
    }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Gender getGender() { return gender; }
    public String toString() {
        return String.format("Author[name=%s,email=%s,gender=%s]", name, email, gender);
    }
}
