package eu.execom.todolistgrouptwo.model.dto;

/**
 * Created by Sara on 03-Dec-16.
 */

public class RegisterDTO {

        private final String email;

        private final String password;

        private final String confirmPassword;

        public RegisterDTO(String username, String password, String confirmPassword) {
            this.email = username;
            this.password = password;
            this.confirmPassword = confirmPassword;
        }

        @Override
        public String toString() {
            return "RegisterDTO{" +
                    "username='" + email + '\'' +
                    ", password='" + password + '\'' +
                    ", confirmPassword='" + confirmPassword + '\'' +
                    '}';
        }
}

