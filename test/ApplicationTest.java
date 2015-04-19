import org.junit.Test;
import play.twirl.api.Html;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;

// todo: not using the right spring context when using fakeApplication()
public class ApplicationTest {

    @Test
    public void indexTemplate() {
        running(fakeApplication(), () -> {
            Html html = views.html.guest.index.render();
            assertThat(contentType(html)).isEqualTo("text/html");
            assertThat(contentAsString(html)).contains("HAWAI");
        });
    }

}
