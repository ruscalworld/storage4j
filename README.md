# StorageLib
Lots of projects need to store some data persistently, but some projects are not too large to use big libraries such as Hibernate ORM. StorageLib provides a very simple ORM that makes storing data in your app much easier. It does not increase size of your binary file in many times, but saves you from writing boring SQL requests and manually mapping fields from ResultSet to your classes. Let me show you an example:

```Java
public class Test {
    // @Model annotation says to StorageLib in what table it should store data for models of this type
    @Model(table = "users")
    // DefaultModel is class with "id" field
    // It's not necessary to extend it, but extending it allows you to use more methods of StorageLib
    public static class User extends DefaultModel {
        // @Property annotation determines name of column in model's table for this field
        // In this case StorageLib will put value of "name" column to "userName" field
        // Fields without this annotation are ignored
        @Property(column = "name")
        // Fields mustn't be static, but they can be final or not
        // You can use any type, some default types are supported out of box
        // But you can also use your own types, but you should override toString() method, 
        // so StorageLib can will be able them to database, and register converter using storage.registerConverter(...),
        // so StorageLib will be able to get them from database
        private final String userName;

        // Fields with @DefaultGenerated annotation will be ignored on INSERT statements
        // You should use this if you want to use "DEFAULT" in SQL
        @DefaultGenerated
        @Property(column = "created_at")
        private final Timestamp createdAt;

        public User(String userName, Timestamp createdAt) {
            this.userName = userName;
            
            // This will be ignored on INSERT, because "createdAt" field is annotated as @DefaultGenerated
            this.createdAt = createdAt;
        }

        // Model must have a constructor that does not require any arguments, 
        // so StorageLib will be able to retrieve model from database
        public User() {
            this.userName = null;
            
            // This will be ignored on INSERT, because "createdAt" field is annotated as @DefaultGenerated
            this.createdAt = new Timestamp(System.currentTimeMillis());
        }

        // Getters are omitted
    }

    public static void main(String[] args) throws SQLException, IOException, InvalidModelException, NotFoundException {
        // Just create new SQLiteStorage and pass JDBC connection URL to it
        // storage variable provides access to all method you need to work with StorageLib
        // You can save it as static field or pass it to your classes that require access to database
        SQLiteStorage storage = new SQLiteStorage("jdbc:sqlite:/home/example/example.db");

        // This tells StorageLib to run script from schema/users.sql file when storage is being initialized
        // You can use this to create tables in your database
        // For example, with "CREATE TABLE IF NOT EXISTS `users` (...)"
        // If you need, you can add more scripts and register them with registerMigration() method
        storage.registerMigration("users");

        // When all migrations are registered, you should call setup() method
        // It will register SQLite driver and run migrations
        storage.setup();

        // Just create instance of the model as you want
        User user = new User("Test", new Timestamp(System.currentTimeMillis()));

        // This will INSERT a new row to your table if ID is not set
        // Otherwise, it will update existing row with ID from your model
        // It returns ID of the row
        long id = storage.save(user);

        // Retrieve model with given type and given ID
        // If there is no row with given ID, it throws NotFoundException
        user = storage.retrieve(User.class, id);
        System.out.println(user.getUserName()); // Prints "Test"

        // Delete row with ID from the model
        storage.delete(user);
        storage.retrieve(User.class, id); // Now this throws NotFoundException
    }
}
```

More documentation is comming soon (or not)...

## Installing
![](https://img.shields.io/github/v/tag/ruscalworld/storagelib?label=latest%20version)

### Maven
```XML
<repositories>
  <repository>
    <id>ruscalworld-releases</id>
    <url>https://maven.ruscalworld.ru/releases</url>
  </repository>
</repositories>
```

```XML
<dependencies>
  <dependency>
    <groupId>ru.ruscalworld</groupId>
    <artifactId>storagelib</artifactId>
    <version>VERSION</version>
  </dependency>
</dependencies>
```

### Gradle
```Groovy
repositories {
  maven {
    name = 'RuscalWorld Releases'
    url = 'https://maven.ruscalworld.ru/releases'
  }
}
```

```Groovy
dependencies {
  implementation 'ru.ruscalworld:storagelib:VERSION'
}
```
