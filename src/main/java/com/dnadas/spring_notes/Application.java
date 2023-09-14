package com.dnadas.spring_notes;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
  private static final String ENV_FILE = "config.env";

  public static void main(String[] args) {
    Dotenv dotenv = Dotenv.configure().filename(ENV_FILE).load();
    dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

    SpringApplication.run(Application.class, args);
  }
}
