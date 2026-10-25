package com.alexia.socialmediaapp.ui;

import com.alexia.socialmediaapp.domain.Friendship;
import com.alexia.socialmediaapp.domain.UserProfile;
import com.alexia.socialmediaapp.exceptions.RepoException;
import com.alexia.socialmediaapp.exceptions.ServiceException;
import com.alexia.socialmediaapp.exceptions.ValidationException;
import com.alexia.socialmediaapp.service.FriendshipService;
import com.alexia.socialmediaapp.service.NetworkService;
import com.alexia.socialmediaapp.service.UserService;

import java.util.Scanner;

public class ConsoleUI {
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final NetworkService networkService;
    private final Scanner scanner;

    public ConsoleUI(UserService userService, FriendshipService friendshipService, NetworkService networkService) {
        this.userService = userService;
        this.friendshipService= friendshipService;
        this.networkService= networkService;
        this.scanner= new Scanner(System.in);
    }

    private void menu(){
        System.out.println("===============MENU================");
        System.out.println("1.add user");
        System.out.println("2.delete user");
        System.out.println("3.view all users");
        System.out.println("4.add friend");
        System.out.println("5.remove friend");
        System.out.println("6.view number of communities");
        System.out.println("7.view most sociable community");
        System.out.println("0.exit");
        System.out.println("===================================");
    }

    public void run() {
        boolean isRunning = true;

        while (isRunning) {
            menu();
            System.out.print("Enter a command: ");
            String command = scanner.nextLine();

            switch (command) {
                case "1": {
                    System.out.print("Enter your name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter a username: ");
                    String userName = scanner.nextLine();

                    System.out.print("Enter your email: ");
                    String email = scanner.nextLine();

                    System.out.print("Enter your phone number: ");
                    String phone = scanner.nextLine();

                    System.out.print("Enter a password: ");
                    String password = scanner.nextLine();

                    try {
                        userService.addUser(email, phone, name, password, userName);
                        System.out.println("User added successfully!");
                    } catch (ServiceException exp) {
                        System.out.println("Error: " + exp.getMessage());
                    } catch (ValidationException exp) {
                        System.out.println("Error: " + exp.getMessage());
                    } catch (RepoException exp) {
                        System.out.println("Error: " + exp.getMessage());
                    }
                    break;
                }
                case "2": {
                    System.out.print("Enter the username: ");
                    String userName = scanner.nextLine();

                    System.out.print("Are you sure? y/n: ");
                    String cmd = scanner.nextLine();

                    switch (cmd) {
                        case "y": {
                            try {
                                userService.deleteUser(userName);
                                System.out.println("User was successfully deleted!");
                            } catch (ServiceException exp) {
                                System.out.println("Error: " + exp.getMessage());
                            } catch (ValidationException exp) {
                                System.out.println("Error: " + exp.getMessage());
                            } catch (RepoException exp) {
                                System.out.println("Error: " + exp.getMessage());
                            }
                            break;
                        }
                        case "n": {
                            System.out.println("Deletion cancelled.");
                            break;
                        }
                        default: {
                            System.out.println("Invalid option.");
                            break;
                        }
                    }
                    break;
                }
                case "3": {
                    boolean hasUsers=false;
                    for (UserProfile user : userService.getAllUserProfiles()) {
                        hasUsers=true;
                        System.out.println("@" + user.getUserName() + " - "+ user.getName() + " Bio: " + user.getBio());
                    }
                    if(!hasUsers)
                        System.out.println("There are no users!");
                    break;
                }
                case "4": {
                    System.out.print("Enter the first username: ");
                    String user1 = scanner.nextLine();

                    System.out.print("Enter the second username: ");
                    String user2 = scanner.nextLine();

                    try {
                        friendshipService.addFriend(user1,user2);
                        System.out.println("Friendship added successfully!");
                    } catch (ServiceException exp) {
                        System.out.println("Error: " + exp.getMessage());
                    } catch (ValidationException exp) {
                        System.out.println("Error: " + exp.getMessage());
                    } catch (RepoException exp) {
                        System.out.println("Error: " + exp.getMessage());
                    }
                    break;
                }
                case "5": {
                    System.out.print("Enter the first username: ");
                    String user1 = scanner.nextLine();

                    System.out.print("Enter the second username: ");
                    String user2 = scanner.nextLine();

                    System.out.print("Are you sure? y/n: ");
                    String cmd = scanner.nextLine();

                    switch (cmd) {
                        case "y": {
                            try {
                                friendshipService.removeFriend(user1,user2);
                                System.out.println("Friendship was successfully deleted!");
                            }catch (ValidationException exp) {
                                System.out.println("Error: " + exp.getMessage());
                            } catch (RepoException exp) {
                                System.out.println("Error: " + exp.getMessage());
                            }
                            catch (ServiceException exp) {
                                System.out.println("Error: " + exp.getMessage());
                            }
                            break;
                        }
                        case "n": {
                            System.out.println("Deletion cancelled.");
                            break;
                        }
                        default: {
                            System.out.println("Invalid option.");
                            break;
                        }
                    }
                    break;
                }
                case "6": {
                    try{
                        int nr=networkService.getNumberOfCommunities();
                        System.out.println("Number of communities: " + nr);

                    }
                    catch(ServiceException exp){
                        System.out.println(exp.getMessage());
                    }
                    catch(ValidationException exp){
                        System.out.println(exp.getMessage());
                    }
                    catch(RepoException exp){
                        System.out.println(exp.getMessage());
                    }
                    break;
                }
                case "7": {
                    try{
                        for(Integer id: networkService.getMostSociableCommunity()){
                            UserProfile user= userService.getProfileDetails(id).get();
                            System.out.print(user.getUserName()+ "<-->");

                        }
                        System.out.println("");
                    }
                    catch(ServiceException exp){
                        System.out.println(exp.getMessage());
                    }
                    catch(ValidationException exp){
                        System.out.println(exp.getMessage());
                    }
                    catch(RepoException exp){
                        System.out.println(exp.getMessage());
                    }
                    break;
                }
                case "8": {
                    for(Friendship f: friendshipService.getAllFriendships())
                        System.out.println(f.getUser1Id()+" "+f.getUser2Id());
                    break;
                }
                case "0": {
                    System.out.println("Exiting...");
                    isRunning = false;
                    break;
                }
                default: {
                    System.out.println("Please enter a valid number from the menu!");
                    break;
                }
            }
        }
    }
}