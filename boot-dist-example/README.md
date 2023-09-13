This is an example for how to extend this setup. The following steps should get you started from scratch:
1. Create a new empy Maven project of type `jar`.
2. Add the following as a parent and replace the version value:

```
    <parent>
        <groupId>com.essaid.jg.boot</groupId>
        <artifactId>com.essaid.jg.boot.boot-dist</artifactId>
        <version>${revision}</version>
    </parent>
```

3. Name your `groupId:artifactId:version` as you like.
4. Run the following command to initialize the project with `distribution` content and also build the Spring Boot jar file. You will need to have `mvn` installed for this initial step since the other self-contained scripts under `bin` are not yet installed from the distribution.

```
mvn package
```