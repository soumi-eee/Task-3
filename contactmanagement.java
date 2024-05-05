import java.io.*;
import java.util.*;

class ContactsManager {
    private static Map<String, List<String>> contacts = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadContactsFromFile();

        boolean running = true;
        while (running) {
            System.out.println("1. Add Contact");
            System.out.println("2. View Contacts");
            System.out.println("3. Edit Contact");
            System.out.println("4. Delete Contact");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addContact();
                    break;
                case 2:
                    viewContacts();
                    break;
                case 3:
                    editContact();
                    break;
                case 4:
                    deleteContact();
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }

        saveContactsToFile();
    }

    private static void addContact() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();

        List<String> numbers = contacts.getOrDefault(name, new ArrayList<>());
        numbers.add(phoneNumber);
        contacts.put(name, numbers);
        System.out.println("Contact added successfully!");
    }

    private static void viewContacts() {
        if (contacts.isEmpty()) {
            System.out.println("No contacts available!");
        } else {
            System.out.println("Contacts:");
            for (Map.Entry<String, List<String>> entry : contacts.entrySet()) {
                System.out.println("Name: " + entry.getKey());
                System.out.println("Phone Numbers: " + entry.getValue());
                System.out.println();
            }
        }
    }

    private static void editContact() {
        System.out.print("Enter the name of the contact you want to edit: ");
        String name = scanner.nextLine();
    
        List<String> numbers = contacts.get(name);
        if (numbers == null) {
            System.out.println("Contact not found!");
            return;
        }
    
        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Edit Name");
            System.out.println("2. Edit Phone Numbers");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
    
            switch (choice) {
                case 1:
                    System.out.print("Enter new name: ");
                    String newName = scanner.nextLine();
                    if (!newName.equals(name)) {
                        if (contacts.containsKey(newName)) {
                            System.out.println("A contact with that name already exists!");
    
                            System.out.print("Do you want to merge the two contacts into one? (yes/no): ");
                            String mergeChoice = scanner.nextLine();
                            if (mergeChoice.equalsIgnoreCase("yes")) {
                                List<String> existingNumbers = contacts.get(newName);
                                existingNumbers.addAll(numbers);
                                contacts.put(newName, existingNumbers);
                                contacts.remove(name);
                                System.out.println("Contacts merged successfully!");
                                return;
                            } else {
                                return;
                            }
                        } else {
                            List<String> phoneNumbers = contacts.remove(name);
                            contacts.put(newName, phoneNumbers);
                            System.out.println("Contact name updated successfully!");
                        }
                    } else {
                        System.out.println("New name is the same as the old one. No changes made.");
                    }
                    break;
                case 2:
                    if (numbers.size() == 1) {
                        System.out.print("Enter new phone number: ");
                        String newPhoneNumber = scanner.nextLine();
                        numbers.set(0, newPhoneNumber);
                        System.out.println("Phone number updated successfully!");
                    } else {
                        System.out.println("Multiple phone numbers found for this contact:");
                        for (int i = 0; i < numbers.size(); i++) {
                            System.out.println((i + 1) + ". " + numbers.get(i));
                        }
                        System.out.print("Enter the index of the number you want to edit (0 to cancel): ");
                        int index = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        if (index > 0 && index <= numbers.size()) {
                            System.out.print("Enter new phone number: ");
                            String newPhoneNumber = scanner.nextLine();
                            numbers.set(index - 1, newPhoneNumber);
                            System.out.println("Phone number updated successfully!");
                        } else {
                            System.out.println("Invalid index or operation cancelled.");
                        }
                    }
                    break;
                case 3:
                    System.out.println("Exiting edit mode without making any changes.");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
    
    private static void deleteContact() {
        System.out.print("Enter the name of the contact you want to delete: ");
        String name = scanner.nextLine();
    
        List<String> numbers = contacts.get(name);
        if (numbers == null) {
            System.out.println("Contact not found!");
            return;
        }
    
        if (numbers.size() == 1) {
            if (contacts.remove(name) != null) {
                System.out.println("Contact deleted successfully!");
            } else {
                System.out.println("Contact not found!");
            }
        } else {
            System.out.println("Multiple phone numbers found for this contact:");
            for (int i = 0; i < numbers.size(); i++) {
                System.out.println((i + 1) + ". " + numbers.get(i));
            }
            System.out.print("Enter the index of the number you want to delete (0 to delete the entire contact): ");
            int index = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (index > 0 && index <= numbers.size()) {
                numbers.remove(index - 1);
                if (numbers.isEmpty()) {
                    contacts.remove(name);
                } else {
                    contacts.put(name, numbers);
                }
                System.out.println("Phone number deleted successfully!");
            } else {
                if (contacts.remove(name) != null) {
                    System.out.println("Contact deleted successfully!");
                } else {
                    System.out.println("Invalid index or contact not found.");
                }
            }
        }
    }
    
    private static void loadContactsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("Contacts.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0].trim();
                String[] phoneNumbers = parts[1].trim().split(";");
                List<String> numbers = Arrays.asList(phoneNumbers);
                contacts.put(name, numbers);
            }
        } catch (IOException e) {
            // Ignore if file not found or other IO errors
        }
    }

    private static void saveContactsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("Contacts.txt"))) {
            for (Map.Entry<String, List<String>> entry : contacts.entrySet()) {
                writer.print(entry.getKey() + ", ");
                List<String> numbers = entry.getValue();
                for (int i = 0; i < numbers.size(); i++) {
                    writer.print(numbers.get(i));
                    if (i < numbers.size() - 1) {
                        writer.print("; ");
                    }
                }
                writer.println();
            }
        } catch (IOException e) {
            System.out.println("Error saving contacts to file!");
        }
    }
}

   
