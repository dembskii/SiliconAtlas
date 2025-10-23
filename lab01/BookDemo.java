public class BookDemo {
    public static String runDemo() {
        Author author = new Author("Jan Kowalski", "jan.kowalski@email.com", Author.Gender.MALE);
        Book book1 = new Book("Java Basics", 49.99, author);
        Book book2 = new Book("Advanced Java", 79.99, author, 10);
        StringBuilder sb = new StringBuilder();
        sb.append(author).append('\n');
        sb.append(book1).append('\n');
        sb.append(book2).append('\n');
        book1.setPrice(59.99);
        book2.setQty(20);
        sb.append(book1.getName()).append('\n');
        sb.append(book1.getAuthor()).append('\n');
        sb.append(book1.getPrice()).append('\n');
        sb.append(book2.getQty()).append('\n');
        sb.append(book1).append('\n');
        sb.append(book2).append('\n');
        return sb.toString();
    }
}
