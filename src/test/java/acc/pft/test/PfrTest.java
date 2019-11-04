package acc.pft.test;

import com.codeborne.selenide.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.WebDriverRunner.url;


public class PfrTest {

    @Before
    public void setUp() {
        Configuration.startMaximized  = true;

    }

    @Test
    //Тест-кейс 1: проверка перехода с главной страницы на страницу "Формирование платёжных документов"
    public void toFormationPaymentDocuments() {
        open("https://es.pfrf.ru/");
        $(byText("Сформировать платёжный документ")).click();
        //Создать и сохранить список открытых вкладок
        ArrayList<String> tabs = new ArrayList<String>((getWebDriver().getWindowHandles()));
        //Выбрать вторую вкладку
        switchTo().window(tabs.get(1));
        //Создание переменной с полученным url'ом
        String urlNow = new String(url());
        //проверка перехода на вторую вкладку
        urlNow.equals("https://www.pfrf.ru/eservices/pay_docs/");
    }

    @Test
    //Тест-кейс 2: проверка реквизитов получателя платежа
    public void verificationRequisitesRecipient() {
        //установить флаг "Застрахованное лицо"
        $("#payment_form > div > label:nth-child(2) > input[type=radio]").click();
        //Выбрать регион: Тверская область
        $("#region").click();
        $("#region > option:nth-child(70)").click();
        $("#payment_form").click();
        //Проверка реквизита "Получатель платежа"
        $("#group_CITIZEN > table > tbody > tr:nth-child(1) > td.val-opfr")
                .shouldHave(exactText("УФК по Тверской области (ГУ - Отделение Пенсионного фонда РФ по Тверской области)"));
        //Проверка реквизита "ИНН получателя"
        $("#group_CITIZEN > table > tbody > tr:nth-child(2) > td.val-inn").shouldHave(exactText("6903005441"));
        //Проверка реквизита "КПП Получателя"
        $("#group_CITIZEN > table > tbody > tr:nth-child(3) > td.val-kpp").shouldHave(exactText("695001001"));
        //Проверка реквизита "Банк получателя"
        $("#group_CITIZEN > table > tbody > tr:nth-child(4) > td.val-bankName").shouldHave(exactText("ОТДЕЛЕНИЕ ТВЕРЬ Г.ТВЕРЬ"));
        //Проверка реквизита "Счет получателя"
        $("#group_CITIZEN > table > tbody > tr:nth-child(5) > td.val-accountNumber")
                .shouldHave(exactText("40101810600000010005"));
        //Проверка реквизита "Бик получателя"
        $("#group_CITIZEN > table > tbody > tr:nth-child(6) > td.val-bik").shouldHave(exactText("042809001"));
        //Проверка реквизита "КБК"
        $("#group_CITIZEN > table > tbody > tr:nth-child(7) > td.val-kbk").shouldHave(exactText("392 1 02 02041 06 1100 160"));
        //Проверка реквизита "ОКТМО"
        $("#group_CITIZEN > table > tbody > tr:nth-child(8) > td.val-oktmo").shouldHave(exactText("28701000"));
        //Проверка реквизита "Назначение платежа"
        $("#group_CITIZEN > table > tbody > tr:nth-child(9) > td.val-paymentName")
                .shouldHave(exactText("Дополнительные страховые взносы на накопительную пенсию и взносы работодателя в" +
                        " пользу застрахованных лиц, уплачивающих дополнительные страховые взносы на накопительную" +
                        " пенсию, зачисляемые в Пенсионный фонд Российской Федерации"));
         }


    @Test
    //Тест-кейс 3: проверка заполнения формы "Формирование платёжных документов"
    public void fillFormationPaymentDocuments(){
          //Заполнить имя
        $("#group_CITIZEN > div.hide-on-mobile > input:nth-child(5)").setValue("ФИО").pressEnter();
        //Заполнить поле Адрес
        $("#group_CITIZEN > div.hide-on-mobile > input:nth-child(7)").setValue("г.Тверь").pressEnter();
        //Заполнить поле Снилс
        $("#group_CITIZEN > div.hide-on-mobile > input:nth-child(9)").setValue("00000000000").pressEnter();
        //Заполнить поле Сумма платежа
        $("#group_CITIZEN > div.hide-on-mobile > input:nth-child(11)").setValue("5000").pressEnter();
        //Кнопка Скачать в формате PDF
        $("#group_CITIZEN > div.hide-on-mobile > input.blue-button.fr.pdf.btn.btn-primary").click();
        //Создать и сохранить список открытых вкладок
        ArrayList<String> tabs = new ArrayList<String>((getWebDriver().getWindowHandles()));
        //Выбрать 3ю вкладку
        switchTo().window(tabs.get(2));
        sleep(1000);
        screenshot("pdf_actual");
    }


    @Test
    //проверка сообщения об ошибке при заполнении поля СНИЛС
    public void snilsError() {
        //Создать и сохранить список открытых вкладок
        ArrayList<String> tabs = new ArrayList<String>((getWebDriver().getWindowHandles()));
        //Выбрать вторую вкладку
        switchTo().window(tabs.get(1));
        //установить флаг "Застрахованное лицо"
        $("#payment_form > div > label:nth-child(2) > input[type=radio]").click();
        //Выбрать регион: Тверская область
        $("#region").click();
        $("#region > option:nth-child(70)").click();
        //Заполнить поле Снилс
        $("#group_CITIZEN > div.hide-on-mobile > input:nth-child(9)").setValue("1").pressEnter();
        //Кнопка Скачать в формате PDF
        $("#group_CITIZEN > div.hide-on-mobile > input.blue-button.fr.pdf.btn.btn-primary").click();
        sleep(1000);
        //поле с ошибкой снилс
        String error1 = new String($("#group_CITIZEN > div.hide-on-mobile > div.error-block > h2").getText());
        String error2 = new String($("#group_CITIZEN > div.hide-on-mobile > div.error-block > ul > li").getText());
        error1.equals("Для формирования документа для оплаты:");
        error2.equals("Укажите корректный номер СНИЛС");
        close();
    }

}