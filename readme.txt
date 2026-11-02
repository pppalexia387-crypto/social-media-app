LABORATORY 4 - MAP (Java 8 Refactoring)
Social Media Network Project

1. BEFORE / AFTER TABLE - REPLACED CLASSIC LOOPS
--------------------------------------------------------------------------------
Below are the main classic loops from Lab 3 that were refactored into Stream 
pipelines in Lab 4:

| Nr | Class              | Method                       | Before (Lab 3)                | After (Lab 4 - Stream Operations Used)                                     |
|----|--------------------|------------------------------|-------------------------------|----------------------------------------------------------------------------|
| 1  | Repository         | findAll(Predicate<E>)        | for-each over findAll()       | StreamSupport.stream, filter(Objects::nonNull), filter(predicate), collect |
| 2  | UserServiceImpl    | deleteUser(String)           | for-each to find by username  | findAll(Predicate), stream(), findFirst(), map(), orElseThrow()            |
| 3  | UserServiceImpl    | deleteAllFriendshipsForUser  | for-each + temp delete list   | iteratorStream(), filter(), map(), forEach(friendshipRepo::delete)         |
| 4  | NetworkServiceImpl | getNumberOfCommunities / DFS | for-each over neighbors       | iteratorStream(), filter(v -> !visited.contains(v)), map(), forEach        |
| 5  | NetworkServiceImpl | getMostSociableCommunity     | for-each tracking max size    | stream(), max(Comparator.comparingInt(List::size)), orElse()               |


2. FUNCTIONAL INTERFACES USED AND WHERE
--------------------------------------------------------------------------------
- Validator<T> (custom @FunctionalInterface in domain.validators.Validator):
  Used to validate entities (UserAccount, UserProfile, Friendship). Also used 
  the default method `andThen(Validator<T> other)` to chain new validation rules 
  written directly as lambdas without creating extra validator classes.

- Predicate<T> (java.util.function.Predicate):
  Used in Repository.findAll(Predicate<E>), the criteria classes 
  (UserAccountCriteria, UserProfileCriteria, FriendshipCriteria), and inside 
  UserServiceImpl / NetworkServiceImpl to filter collections and combine search 
  conditions with `.or()`, `.and()`, and `Predicate.not()`.

- Function<T, R> (java.util.function.Function):
  Used in the generic FileRepository constructor to convert strings to entities 
  and entities back to strings (`lineToEntity` and `entityToLine`). Also used in 
  Stream and Optional `.map(...)` calls (e.g., `UserProfile::getId`).

- Supplier<T> (java.util.function.Supplier):
  Used in RepositoryFactory inside a Map to instantiate the right repositories 
  without using a switch statement, and in `Optional.orElseThrow(...)` to throw 
  custom exceptions when data is missing.

- Consumer<T> (java.util.function.Consumer):
  Used in `.forEach(...)` and `.ifPresent(...)` in the service, repository, and 
  UI layers (e.g., `friendshipRepo::delete`, `System.out::println`).

- Comparator<T> (java.util.Comparator):
  Used in NetworkServiceImpl with `Comparator.comparingInt(...)` to compare 
  communities and find the most sociable one.


3. THE 4 TYPES OF METHOD REFERENCES USED
--------------------------------------------------------------------------------
- Static method reference: `Objects::nonNull` (in Repository.findAll) and `Integer::parseInt`
- Instance method of a specific object: `friendshipRepo::delete` (in UserServiceImpl), `System.out::println` (in UI)
- Instance method of an arbitrary object: `UserProfile::getId`, `UserProfile::getUserName`, `UserAccount::getEmail`
- Constructor reference: `ArrayList::new` (in `Collectors.toCollection(ArrayList::new)` in Repository.findAll)


4. ARCHITECTURAL CHOICES AND JUSTIFICATIONS
--------------------------------------------------------------------------------
A) Using Optional<E> in the Repository interface:
   I chose to have the Repository methods (like findOne, save, delete, update) 
   return Optional<E> instead of null. Returning Optional makes it much harder 
   to accidentally get a NullPointerException in the service layer because it 
   forces you to handle the case where an ID is not found. It also made the code 
   in UserServiceImpl and NetworkServiceImpl cleaner since we can chain `.map()`, 
   `.ifPresent()`, and `.orElseThrow()` instead of writing manual null checks.

B) Composition (FileRepository with Function) vs. Template Method (AbstractFileRepository):
   Instead of keeping AbstractFileRepository with separate subclasses for every 
   entity, I switched to a single generic `FileRepository<ID, E>` class that 
   receives `Function<String, E>` and `Function<E, String>` in its constructor. 
   Using composition here is much simpler because we don't need three different 
   file repository classes just to change how a line is parsed. All the file I/O 
   logic with `Files.lines` and `Files.write` stays in one place, and we just 
   pass the parsing functions from the RepositoryFactory.


5. KEPT CLASSIC LOOPS AND WHY
--------------------------------------------------------------------------------
The only classic loops kept are:
1. The main `while (true)` loop in the UI console class that reads user commands 
   until the user types exit. This was kept because it is the main application 
   menu loop waiting for user input, not a loop iterating over a collection.
2. A classic backtracking `for` loop was kept in `longestPathFromNode` because
    rewriting recursive backtracking graph searches entirely with flat
    stream pipelines degrades code readability.