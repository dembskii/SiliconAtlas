public class TestBook {
    public static void main(String[] args) {
        Author author = new Author("Jan Kowalski", "jan.kowalski@email.com", Author.Gender.MALE);
        Book book1 = new Book("Java Basics", 49.99, author);
        Book book2 = new Book("Advanced Java", 79.99, author, 10);
        System.out.println(author);
        System.out.println(book1);
        System.out.println(book2);
        book1.setPrice(59.99);
        book2.setQty(20);
        System.out.println(book1.getName());
        System.out.println(book1.getAuthor());
        System.out.println(book1.getPrice());
        System.out.println(book2.getQty());
        System.out.println(book1);
        System.out.println(book2);
    }
}
