package http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("path 클래스")
public class PathTest {

    @Test
    @DisplayName("Path가 null인 경우 예외가 발생한다.")
    void parseException() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Path(null));
    }

    @Test
    void parse() {
        Path path = new Path("/users?name1=value1&name2=value2");

        assertThat(path.getPath()).isEqualTo("/users");
        assertThat(path.getParameter("name1")).isEqualTo("value1");
        assertThat(path.getParameter("name2")).isEqualTo("value2");
    }

    @ParameterizedTest
    @CsvSource(value = {"/index.html,html", "/style.css,css", "/test.img,img", "/test.test.js,js"}, delimiter = ',')
    @DisplayName("path 내부에 확장자가 존재한다면 잘 추출하는지")
    void getExtension(final String pathStr, final String expected) {
        Path path = new Path(pathStr);

        assertThat(path.getExtension()).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/test", "/wowwwww"})
    @DisplayName("path 내부에서 확장자가 존재하지 않을때는 null 을 리턴한다")
    void getExtensionReturnNull(final String pathStr) {
        Path path = new Path(pathStr);

        assertThat(path.getExtension()).isNull();
    }
}