package hello.core.member;

public class Member {

    private Long id;
    private String name;
    private Grade greade;

    public Member(Long id, String name, Grade greade) {
        this.id = id;
        this.name = name;
        this.greade = greade;
    }

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

    public Grade getGreade() {
        return greade;
    }

    public void setGreade(Grade greade) {
        this.greade = greade;
    }
}
