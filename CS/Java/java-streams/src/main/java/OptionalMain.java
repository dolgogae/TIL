import model.User;

import java.util.Optional;

public class OptionalMain {
    /**
     * NullPointerException
     * Null상태인 오브젝트를 레퍼런스 할 때 발생
     * RuntimeError라서 컴파일 타임에서 알기 힘들다.
     */
    public static void main(String[] args){

        // user1과 user2를 비교하면 email로 인해서 NPE가 난다.
        User user1 = new User()
                    .setId(1001)
                    .setName("Alice")
                    .setEmail("alice@gmail.com")
                    .setVerified(false);
        User user2 = new User()
                    .setId(1002)
                    .setName("Alice")
                    // .setEmail("alice@gmail.com")
                    .setVerified(false);

        String someEmail = "some@email.com";
        String nullEmail = null;

        Optional<String> maybeEmail = Optional.of(someEmail);
        Optional<String> maybeEamil2 = Optional.empty();
        Optional<String> maybeEamil3 = Optional.ofNullable(someEmail);
        Optional<String> maybeEamil4 = Optional.ofNullable(nullEmail);

        String email = maybeEmail.get();
        // 에러
        if(maybeEamil2.isPresent()){
            String email2 = maybeEamil2.get();
//            System.out.println(email2);
        }

        String defaultEmail = "default@email.com";
        String email3 = maybeEamil2.orElse(defaultEmail);
        String email4 = maybeEamil2.orElseGet(() -> defaultEmail);
//        String email5 = maybeEamil2.orElseThrow(() -> new RuntimeException("email not present"));

        Optional<User> maybeUser = Optional.ofNullable(maybeGetUser(true));
//        maybeUser.ifPresent(user -> System.out.println(user));

        Optional<Integer> maybeId = Optional.ofNullable(maybeGetUser(true))
                .map(User::getId);
//        maybeId.ifPresent(System.out::println);
        String userName = Optional.ofNullable(maybeGetUser(true))
                .map(User::getName)
                .map(name -> "The name is " + name)
                .orElse("Name is empty");
//        System.out.println(userName);

        Optional<String> maybeEmailOptional = Optional.ofNullable(maybeGetUser(false))
                .flatMap(User::getEmail);
        maybeEmail.ifPresent(System.out::println);
    }

    private static User maybeGetUser(boolean returnUser){
        if(returnUser){
            return new User()
                    .setId(1001)
                    .setName("Alice")
                    .setEmail("alice@email.com")
                    .setVerified(false);
        }
        return null;
    }

    private static boolean userEquals(User u1, User u2){
         return u1.getId() == u2.getId() 
                && u1.getName() == u2.getName()
                && u1.getEmail() == u2.getEmail()
                && u1.isVerified() == u2.isVerified();
    }
}
