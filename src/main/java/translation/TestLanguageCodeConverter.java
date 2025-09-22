package translation;

public class TestLanguageCodeConverter {
    public static void main(String[] args) {
        LanguageCodeConverter converter = new LanguageCodeConverter();

        // Test English
        String langName = converter.fromLanguageCode("en");
        System.out.println("Code 'en' -> " + langName);

        // Test French
        String langCode = converter.fromLanguage("French");
        System.out.println("Language 'French' -> " + langCode);

        // Test Chinese
        System.out.println("Code 'zh' -> " + converter.fromLanguageCode("zh"));

        // Show number of languages loaded
        System.out.println("Total languages loaded: " + converter.getNumLanguages());
    }
}
