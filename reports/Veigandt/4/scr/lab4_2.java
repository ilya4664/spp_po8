import java.util.ArrayList;
import java.util.List;

public class Paragraph {
    private List<Word> words;

    public Paragraph() {
        this.words = new ArrayList<>();
    }

    public void addWord(String wordText) {
        Word word = new Word(wordText);
        words.add(word);
    }

    public void displayParagraph() {
        System.out.print("Paragraph: ");
        for (Word word : words) {
            System.out.print(word.getWordText() + " ");
        }
        System.out.println();
    }

    public int countWords() {
        return words.size();
    }

    public static void main(String[] args) {
        Paragraph paragraph = new Paragraph();
        paragraph.addWord("my");
        paragraph.addWord("name");
        paragraph.addWord("is");
        paragraph.addWord("Ilya");

        paragraph.displayParagraph();
        System.out.println("Number of words in the paragraph: " + paragraph.countWords());

        Word word = paragraph.words.get(3);
        word.capitalizeFirstLetter();
        word.reverseWord();
        word.isPalindrome();

        paragraph.displayParagraph();
    }
}
class Word {
    private String wordText;

    public Word(String wordText) {
        this.wordText = wordText;
    }

    public String getWordText() {
        return wordText;
    }

    public void setWordText(String wordText) {
        this.wordText = wordText;
    }

    public void capitalizeFirstLetter() {
        char firstChar = Character.toUpperCase(wordText.charAt(0));
        wordText = firstChar + wordText.substring(1);
    }
    public void reverseWord(){
        StringBuilder stringBuilder = new StringBuilder(wordText);
        stringBuilder.reverse();
        wordText = stringBuilder.toString();
    }
    public void isPalindrome(){
        StringBuilder stringBuilder = new StringBuilder(wordText);
        String reverseWord = stringBuilder.reverse().toString();
        if (wordText.equals(reverseWord)){
            System.out.println("The " + wordText + " is a palindrome");
        } else {
            System.out.println("The " + wordText + " is not a palindrome");
        }
    }
}
