package webserver;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import utils.HttpMethod;

public class RequestLineTest {
	private RequestLine requestLine = new RequestLine();

	@DisplayName("GET 요청에 대한 RequestLine을 통해 정상적 파싱되었다.")
	@Test
	void parsingGetRequest() {
		// given
		String path = "/users";
		String protocol = "HTTP";
		String version = "1.1";
		String request = "GET /users HTTP/1.1";

		// when
		RequestLine result = requestLine.parse(request);

		// then
		assertAll(
			() -> assertThat(result.getMethod()).isEqualTo(HttpMethod.GET),
			() -> assertThat(result.getPath()).isEqualTo(path),
			() -> assertThat(result.getProtocol()).isEqualTo(protocol),
			() -> assertThat(result.getVersion()).isEqualTo(version)
		);
	}

	@ParameterizedTest(name = "요청의 값이 null 또는 비어있는 경우, 예외처리 된다.")
	@NullAndEmptySource
	void exceptionRequestIsNullOrEmpty(String request) {
		// when & then
		assertThatThrownBy(() -> requestLine.parse(request))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@ParameterizedTest(name = "요청의 속성들이 충분하지 않은 경우, 예외처리 된다.")
	@ValueSource(strings = {"GET /users", "GET /users HTTP/1.1GET /users HTTP/1.1", "POST /users", "POST /users HTTP/1.1GET /users HTTP/1.1"})
	void exceptionRequestNotFitPropertyNumber(String request) {
		// when & then
		assertThatThrownBy(() -> requestLine.parse(request))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@ParameterizedTest(name = "요청의 메서드 값이 GET이 아닌 경우, 예외처리 된다.")
	@ValueSource(strings = {"GAT /users HTTP/1.1", "PAST /users HTTP/1.1"})
	void exceptionRequestMethodNotMatchedHttpMethod(String request) {
		// when & then
		assertThatThrownBy(() -> requestLine.parse(request))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@ParameterizedTest(name = "요청에 대해 Protocol과 Version의 파싱 결과 갯수가 알맞지 않는 경우, 예외처리 된다.")
	@ValueSource(strings = {"GET /users HTTP1.1", "GET /users HTTP/1.1/1.2/1.3", "POST /users HTTP1.1", "POST /users HTTP/1.1/1.2/1.3"})
	void exceptoinProtocolAndVersionNotFitNumber(String request) {
		// when & then
		assertThatThrownBy(() -> requestLine.parse(request))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("POST 요청에 대한 RequestLine을 통해 정상적 파싱되었다.")
	@Test
	void parsingPostRequest() {
		// given
		String path = "/users";
		String protocol = "HTTP";
		String version = "1.1";
		String request = "POST /users HTTP/1.1";

		// when
		RequestLine result = requestLine.parse(request);

		// then
		assertAll(
			() -> assertThat(result.getMethod()).isEqualTo(HttpMethod.POST),
			() -> assertThat(result.getPath()).isEqualTo(path),
			() -> assertThat(result.getProtocol()).isEqualTo(protocol),
			() -> assertThat(result.getVersion()).isEqualTo(version)
		);
	}

	@DisplayName("유효한 요청에 대해 Query String 파싱이 정상적으로 이루어졋다.")
	@Test
	void parsingQueryParameter() {
	    // given
		String request = "GET /users?userId=javajigi&password=password&name=JaeSung HTTP/1.1";

	    // when
		RequestLine result = requestLine.parse(request);

	    // then
		Map<String, String> queryString = result.getRequestPathQueryString().getQueryStringOfPath();
		assertAll(
			() -> assertThat(queryString).hasSize(3),
			() -> assertThat(queryString.get("userId")).isEqualTo("javajigi"),
			() -> assertThat(queryString.get("password")).isEqualTo("password"),
			() -> assertThat(queryString.get("name")).isEqualTo("JaeSung")
		);
	}
}