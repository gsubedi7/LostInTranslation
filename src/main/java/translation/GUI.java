package translation;

import javax.swing.*;
import java.awt.event.*;
import java.util.*;


// TODO Task D: Update the GUI for the program to align with UI shown in the README example.
//            Currently, the program only uses the CanadaTranslator and the user has
//            to manually enter the language code they want to use for the translation.
//            See the examples package for some code snippets that may be useful when updating
//            the GUI.
public class GUI {
    private static JComboBox<String> languageComboBox;
    private static JList<String> countryList;
    private static JLabel translationLabel;

    private static Translator translator;
    private static CountryCodeConverter countryCodeConverter;
    private static LanguageCodeConverter languageCodeConverter;

    private static final Map<String, String> countryNameToCodeMap = new HashMap<>();
    private static final Map<String, String> languageNameToCodeMap = new HashMap<>();



    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        translator = new JSONTranslator();
        countryCodeConverter = new CountryCodeConverter();
        languageCodeConverter = new LanguageCodeConverter();

        String[] countryDisplayNames = loadCountries();
        String[] languageDisplayNames = loadLanguages();

        // Language UI
        languageComboBox = new JComboBox<>(languageDisplayNames);
        JPanel languagePanel = new JPanel();
        languagePanel.add(new JLabel("Language:"));
        languagePanel.add(languageComboBox);

        // Translation UI
        JPanel resultPanel = new JPanel();
        resultPanel.add(new JLabel("Translation:"));
        translationLabel = new JLabel(" ");
        resultPanel.add(translationLabel);

        // Country UI
        countryList = new JList<>(countryDisplayNames);
        JScrollPane countryListScrollPane = new JScrollPane(countryList);

        // listener
        languageComboBox.addActionListener(e -> updateTranslation());
        countryList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateTranslation();
            }
        });

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(languagePanel);
        mainPanel.add(resultPanel);
        mainPanel.add(countryListScrollPane);

        JFrame frame = new JFrame("Country Name Translator");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    //Country laoding
    private static String[] loadCountries() {
        List<String> countryCodes = translator.getCountryCodes();
        String[] names = new String[countryCodes.size()];

        for (int i = 0; i < countryCodes.size(); i++) {
            String code = countryCodes.get(i);
            String fullName = countryCodeConverter.fromCountryCode(code);
            names[i] = fullName;
            countryNameToCodeMap.put(fullName, code);
        }
        Arrays.sort(names);
        return names;
    }

    //language loading
    private static String[] loadLanguages() {
        List<String> languageCodes = translator.getLanguageCodes();
        String[] names = new String[languageCodes.size()];

        for (int i = 0; i < languageCodes.size(); i++) {
            String code = languageCodes.get(i);
            String fullName = languageCodeConverter.fromLanguageCode(code);
            names[i] = fullName;
            languageNameToCodeMap.put(fullName, code);
        }
        Arrays.sort(names);
        return names;
    }

    /** 更新翻译 */
    private static void updateTranslation() {
        String targetLanguageName = (String) languageComboBox.getSelectedItem();
        String targetCountryName = countryList.getSelectedValue();

        if (targetLanguageName == null || targetCountryName == null) {
            translationLabel.setText("");
            return;
        }

        String targetCountryCode = countryNameToCodeMap.get(targetCountryName);
        String targetLanguageCode = languageNameToCodeMap.get(targetLanguageName);

        String result;
        if (targetLanguageCode != null && targetCountryCode != null) {
            result = translator.translate(targetCountryCode, targetLanguageCode);
            if (result == null || result.isEmpty()) {
                result = targetCountryName + " (No translation in " + targetLanguageName + ")";
            }
        } else {
            result = "Invalid selection.";
        }

        translationLabel.setText(result);
    }
}
