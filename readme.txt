README - Social Media App

1. Persistence & Factory Architecture
- How it works: RepoFactory.createFactory("config.properties") reads the configuration file using try-with-resources and instantiates the configured persistence factory (MemoryRepoFactory, FileRepoFactory, or DbRepoFactory).
- Switching to Database: Change repo.type=DATABASE in config.properties and implement the SQL logic inside the DbRepo classes. Main, Services, and UI require zero changes.
- Simple Factory vs Abstract Factory (OCP): A Simple Factory uses a single class with a switch statement, breaking the Open-Closed Principle (OCP) because adding new storage types requires modifying that class. Using an Abstract Factory (RepoFactory interface with dedicated implementations for File, Memory, and DB) respects OCP because adding a new storage engine only requires creating a new factory class.

2. File Handling & Fault Tolerance
- Missing files: If a data file does not exist on startup, AbstractFileRepo initializes an empty repository without crashing and creates the file automatically on the first save.
- Corrupt data: If a line is malformed, the application fails fast—stopping the startup process immediately and reporting the exact line number and reason. Failing fast prevents the application from running in an inconsistent state or silently losing user data.

3. Validation Strategy
- Repository level: AbstractFileRepo runs the domain Validator on every entity parsed from disk and prior to any save/update operation, preventing invalid objects from entering memory or storage.
- Service level: verifyFileIntegrity() enforces cross-repository business rules on startup (ensuring friendships only reference existing users and checking for duplicate accounts).

4. SOLID Principles
- SRP (Single Responsibility): Responsibilities are strictly separated—Validators check domain rules, Repositories manage data persistence, Services coordinate business logic, and the UI handles user input.
- OCP (Open-Closed): AbstractFileRepo manages all file I/O operations, while subclasses only implement string-to-entity conversion. New file-backed entities can be added without modifying the base file I/O code.
- LSP (Liskov Substitution): File, Memory, and Database repositories all implement the Repository interface and are completely interchangeable without affecting Service behavior.
- DIP (Dependency Inversion): Main, Services, and UI depend strictly on the Repository interface, never on concrete storage implementations.

5. Design Patterns
- Factory: RepoFactory abstracts the creation of storage repositories based on external configuration.
- Template Method: AbstractFileRepo defines the algorithm for loadData() and writeToFile(), deferring extractEntity() and createStringFromEntity() to subclasses.
- Strategy: The Validator<T> interface encapsulates validation algorithms, allowing different validation rules to be injected into repositories.
- Decorator: BufferedReader and BufferedWriter wrap the underlying file streams in AbstractFileRepo to provide buffered line-by-line I/O.