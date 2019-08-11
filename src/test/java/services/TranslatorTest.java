package services;

import org.junit.jupiter.api.Test;
import services.lang.Translator;

import static org.junit.jupiter.api.Assertions.*;

class TranslatorTest {

    @Test
    void toRussian() {
        String english = "Action, Sci-Fi, Drama";
        assertEquals("Экшен, Фантастика, Драма", Translator.toRussian(english));
    }
    @Test
    void openSyllable()
    {
        String english = "Mishelle, Michael";
        assertEquals("Мишель, Майкл",Translator.translitToRussian(english));
    }
}