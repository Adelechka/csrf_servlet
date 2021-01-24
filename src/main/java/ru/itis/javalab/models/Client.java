package ru.itis.javalab.models;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    private Long id;
    private String login;
    private String password;
    private String Uuid;
    private Boolean isDeleted;

    public Client(String login, String password) {
        this.login = login;
        this.password = password;
        this.Uuid = UUID.randomUUID().toString();
    }
}
