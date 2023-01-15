`Noroff Assignment #05`

[![Java CI with Maven](https://github.com/weskeiser/hiTunes/actions/workflows/maven.yml/badge.svg)](https://github.com/weskeiser/hiTunes/actions/workflows/maven.yml)

# Data Access With JDBC

A simple execution of assignment #05. Fun fact: the only software used for this project was Neovim.

---

## Folder structure

##### `/.github/workflows/maven.yml`
GitHub Actions workflow document that triggers a build when commiting to the main branch. Runs `mvn -B package --file pom.xml` to verify the project builds successfully.

##### `/src/main/java/com/hitunes`

- ##### `/models`
  Models for the Customer entity
- ##### `/repositories`
  Repository interface for Customer in addition to a generic repository interface
- ##### `/runners`
  App runner with methods for running implementations of the Customer repository
- ##### `/services`
  Implementations of the Customer repository
