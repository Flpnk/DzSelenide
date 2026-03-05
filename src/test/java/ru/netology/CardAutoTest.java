package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CardAutoTest {

    public String generateDate(int days, String pattern){
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern(pattern));

    }

    @Test
    void happyPath() {
        String planningDate = generateDate(4,"dd.MM.yyyy");

        Selenide.open("http://localhost:9999");
        SelenideElement form = $("form");

        form.$("[data-test-id='city'] input").setValue("Псков");
        form.$("[data-test-id='date'] input").press(Keys.chord(Keys.SHIFT,Keys.HOME),Keys.DELETE).setValue(planningDate);
        form.$("[data-test-id='name'] input").setValue("Иванов Иван");
        form.$("[data-test-id='phone'] input").setValue("+79532469696");
        form.$("[data-test-id='agreement']").click();
        $$("button").find(Condition.text("Забронировать")).click();
        $("[data-test-id='notification']").should(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='notification']")
                .should(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15)).should(Condition.visible);

    }
    @Test
    void complexPath() {
        Selenide.open("http://localhost:9999");
        SelenideElement form = $("form");
        LocalDate targetDate = LocalDate.now().plusDays(7);
        String day = String.valueOf(targetDate.getDayOfMonth());
        String expectedDate = targetDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        form.$("[data-test-id='city'] input").setValue("Пс");
        $$(".menu-item__control").find(Condition.text("Псков")).click();

        form.$("[data-test-id='date'] input").click();
        $$(".calendar__day").find(Condition.text(day)).click();

        form.$("[data-test-id='name'] input").setValue("Иванов Иван");
        form.$("[data-test-id='phone'] input").setValue("+79532469696");

        form.$("[data-test-id='agreement']").click();
        $$("button").find(Condition.text("Забронировать")).click();

        $("[data-test-id='notification']")
                .should(Condition.text("Встреча успешно забронирована на " + expectedDate), Duration.ofSeconds(15))
                .should(Condition.visible);

    }

}
