package by.grigoriev.jooq;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.util.Properties;

import static org.jooq.util.maven.example.Tables.BOOK;

@Log
public class JooqTest {
    private String path = "src/main/resources/application.properties";
    private Properties properties = new Properties();

    @BeforeEach
    public void init() throws IOException {
        properties.load(new FileInputStream(path));
    }

    @Test
    public void testSelectAllBooks() {
        try (Connection connection = DriverManager.getConnection(
                properties.getProperty("url"),
                properties.getProperty("user"),
                properties.getProperty("password")
                ))
        {
            DSLContext ctx = DSL.using(connection, SQLDialect.POSTGRES);

            Result<Record> result = ctx.select().from(BOOK).fetch();

            for (Record rec : result) {
                Integer id = rec.get(BOOK.ID);
                String title = rec.get(BOOK.TITLE);
                LocalDate publishingDate = rec.get(BOOK.PUBLISHINGDATE);
                log.info("Book: id=" +  id + " title=" + title + " date " + publishingDate);

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
