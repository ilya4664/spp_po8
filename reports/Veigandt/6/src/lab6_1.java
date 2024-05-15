import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

interface Command {
    double getPrice(); 
    void execute();
}

class BurgerCommand implements Command {
    private final String burgerType;
    private final double price;

    public BurgerCommand(String burgerType, double price) {
        this.burgerType = burgerType;
        this.price = price;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void execute() {
        System.out.println("Заказан бургер: " + burgerType);
    }
}

class DrinkCommand implements Command {
    private final String drinkType;
    private final double price;

    public DrinkCommand(String drinkType, double price) {
        this.drinkType = drinkType;
        this.price = price;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void execute() {
        System.out.println("Заказан напиток: " + drinkType);
    }
}


class OrderManager {
    private final Map<String, Command> commands = new HashMap<>();
    private double totalPrice = 0; 

    public void addCommand(String itemName, Command command) {
        commands.put(itemName, command);
    }

    public void executeCommand(String itemName) {
        Command command = commands.get(itemName);
        if (command != null) {
            command.execute();
            totalPrice += command.getPrice(); 
        } else {
            System.out.println("Такого элемента меню нет.");
        }
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        OrderManager orderManager = new OrderManager();
        orderManager.addCommand("Веганский бургер", new BurgerCommand("Веганский бургер", 5.99));
        orderManager.addCommand("Куриный бургер", new BurgerCommand("Куриный бургер", 4.49));
        orderManager.addCommand("Пепси", new DrinkCommand("Пепси", 1.99));
        orderManager.addCommand("Кока-кола", new DrinkCommand("Кока-кола", 1.99));

        System.out.println("Добро пожаловать в Бургер-закусочную!");
        System.out.println("Введите название элемента меню (например, 'Веганский бургер' или 'Пепси'):");

       
        System.out.println("Выберите тип упаковки ('с собой' или 'на месте'):");
        String packagingType = scanner.nextLine();

        while (true) {
            String itemName = scanner.nextLine();
            if (itemName.equalsIgnoreCase("Готово")) {
                break;
            }
            orderManager.executeCommand(itemName);
        }

        double totalPrice = orderManager.getTotalPrice();
        System.out.println("Итоговая стоимость заказа: " + totalPrice + " руб.");

        System.out.println("Спасибо за заказ! Приятного аппетита!");
    }
}
