import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Library library = new Library();

        Book book1 = new Book( "Toyota. Путь к совершенству", "Цунёси Нодзи");
        Book book2 = new Book("Цунёси Нодзи", "Дэвид Килпатрик");
        Book book3 = new Book("Чистый код. Создание, анализ и рефакторинг", "Роберт Мартин");
        Book book4 = new Book("Удивительный мир звука", "Игорь Клюкин");


        Catalog catalog = new Catalog();

        catalog.addBook(book1);
        catalog.addBook(book2);
        catalog.addBook(book3);
        catalog.addBook(book4);

        Reader reader1 = new Reader("Илья");
        Reader reader2 = new Reader("Семён");
        Reader reader3 = new Reader("Егор");

        Order order1 = reader1.placeOrderForReadingRoom(book1);
        Order order2 = reader2.placeOrderForHome(book2,LocalDate.of(2024, 3,1));
        Order order3 = reader3.placeOrderForHome(book3, LocalDate.of(2024, 5,5));
        Order order4 = reader1.placeOrderForReadingRoom(book3);

        LibraryWorker libraryWorker = new LibraryWorker(library);

        libraryWorker.addOrder(order1);
        libraryWorker.addOrder(order2);
        libraryWorker.addOrder(order3);
        libraryWorker.addOrder(order4);

        libraryWorker.issueBookToReader(order1);
        libraryWorker.issueBookToReader(order2);
        libraryWorker.issueBookToReader(order3);
        libraryWorker.issueBookToReader(order4);

        reader1.returnBook(book1,libraryWorker);
        reader2.returnBook(book2,libraryWorker);
        reader3.returnBook(book3,libraryWorker);

        Order order5 = reader2.placeOrderForReadingRoom(book3);
        libraryWorker.issueBookToReader(order5);
        libraryWorker.issueBookToReader(order4);
    }
}
class Book {
    private String title;
    private String author;
    private boolean available =true;
    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
class Catalog {
    ArrayList<Book> books;

    public Catalog() {
        this.books = new ArrayList<>();
    }
    public void addBook(Book book){
        books.add(book);
    }
    public Book searchBook(String title){
        for (Book book: books){
            if(book.getTitle().equals(title)){
                return book;
            }
        }
        return null;
    }
}
class Order{
    private Reader reader;
    private Book book;
    boolean isForReadingRoom;

    private LocalDate returnDate = null;
    public Order(Reader reader, Book book, boolean isForReadingRoom) {
        this.reader = reader;
        this.book = book;
        this.isForReadingRoom = isForReadingRoom;
    }
    public Order(Reader reader, Book book, boolean isForReadingRoom, LocalDate date) {
        this.reader = reader;
        this.book = book;
        this.isForReadingRoom = isForReadingRoom;
        this.returnDate = date;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public boolean isForReadingRoom() {
        return isForReadingRoom;
    }

    public void setForReadingRoom(boolean forReadingRoom) {
        isForReadingRoom = forReadingRoom;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
class Library{
    Catalog catalog;
    protected List<Reader> blackList;
    public List<Order> orders;

    public Library(Catalog catalog) {
        this.catalog = catalog;
    }
    public Library() {
        this.blackList = new ArrayList<>();
        this.orders = new ArrayList<>();
    }

}
class Reader extends Library{
    List<Book> books;
    String name;
    boolean inBlackList = false;

    public Reader(String name) {
        this.name = name;
        this.books = new ArrayList<>();
    }

    Order placeOrderForReadingRoom(Book book){
        if(!inBlackList && book.isAvailable()) {
            Order newOrder = new Order(this, book, true);
            return newOrder;
        }else if(inBlackList) {
            System.out.println("Читатель " + name + " в черном списке и не может взять книгу " + book.getTitle());
            return null;
        }else {
            System.out.println("Книга " + book.getTitle() + " находится в прокате " + name+ "не может ее взять");
            return null;
        }
    }
    Order placeOrderForHome(Book book, LocalDate returnDate){
        if(!inBlackList && book.isAvailable()) {
            Order newOrder = new Order(this, book, false, returnDate);
            return newOrder;
        }else if(inBlackList) {
            System.out.println("Читатель " + name + " в черном списке и не может взять книгу " + book.getTitle());
            return null;
        }else {
            System.out.println("Книга " + book.getTitle() + " находится в прокате " + name+ "не может ее взять");
            return null;
        }
    }
    public void returnBook(Book book,LibraryWorker libraryWorker) {
        if (books.contains(book)) {
            books.remove(book);
            System.out.println("Книга " + book.getTitle() + " возвращена читателем " + name);
            libraryWorker.bookReception(book);
        }
    }


    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInBlackList() {
        return inBlackList;
    }

    public void setInBlackList(boolean inBlackList) {
        this.inBlackList = inBlackList;
    }
}
class LibraryWorker extends Library{
    Library library;

    public LibraryWorker(Library library) {
        this.library = library;
    }

    public void issueBookToReader(Order order) {
        if(order != null){
            Reader reader = order.getReader();
            Book book = order.getBook();
            if(!reader.inBlackList && book.isAvailable()) {
                reader.books.add(book);
                book.setAvailable(false);
                System.out.println("Книга " + book.getTitle()+ " выдана читателю " + reader.name);
            }else if(reader.inBlackList) {
                System.out.println("Читатель " + reader.name + " находится в черном списке и не может взять книгу" + book.getTitle());
            }else {
                System.out.println("Книга " + book.getTitle() + " находится в прокате " + reader.name +" не может взять эту книгу");
            }
        }
    }
    public void bookReception(Book book) {
        for(Order order : library.orders){
            if(order.getBook().equals(book)){
                book.setAvailable(true);
                if(!order.isForReadingRoom() && !returnedOnTime(order.getReturnDate())){
                    Administrator administrator = new Administrator();
                    administrator.addToBlackList(order.getReader());
                }
            }

        }


    }
    boolean returnedOnTime(LocalDate returnDate){
        LocalDate currentDate = LocalDate.now();
        if(currentDate.isBefore(returnDate) || currentDate.equals(returnDate)){
            return true;
        }
        return false;
    }

    void addOrder(Order order){
        library.orders.add(order);
    }
    class Administrator {
        public void addToBlackList(Reader reader) {
            reader.setInBlackList(true);
            blackList.add(reader);
            System.out.println(reader.name + " добавлен в черный список");
        }
    }

}
