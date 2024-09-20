package ru.practicum.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Publisher;
import ru.practicum.repository.dataUtils.CreatedData;
import ru.practicum.service.publisher.PublisherService;
import ru.practicum.servlet.dto.mapping.PublisherMapper;
import ru.practicum.servlet.dto.publisher.PublisherRequestDto;
import ru.practicum.servlet.dto.publisher.PublisherResponseDto;
import ru.practicum.servlet.dto.publisher.PublisherResponseDtoShort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PublisherServletTest {
    @Mock
    private PublisherService service;
    @Mock
    private PublisherMapper mapper;
    @InjectMocks
    private PublisherServlet publisherServlet = new PublisherServlet();

    private HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    private HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Endpoint /publisher GET request functionality. Get all publisher")
    void givenPublisherServlet_whenDoGetAll_thenCorrect() throws IOException, ServletException {
        BDDMockito.given(request.getPathInfo()).willReturn("");
        Publisher publisher = CreatedData.createPublisher(1);
        BDDMockito.given(service.getAllPublishers()).willReturn(List.of(publisher));
        BDDMockito.given(mapper.toPublisherResponseDtoShortList(anyList()))
                .willReturn(List.of(new PublisherResponseDtoShort()));
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        publisherServlet.doGet(request, response);
        verify(request, times(1)).getPathInfo();
        verify(service, times(1)).getAllPublishers();
        verify(service, times(0)).getById(anyLong());
        verify(mapper, times(1)).toPublisherResponseDtoShortList(anyList());
        verify(response, times(1)).getWriter();
    }

    @Test
    @DisplayName("Endpoint /publisher/{id} GET request functionality. Get by id publisher")
    void givenPublisherServlet_whenDoGetById_thenCorrect() throws IOException, ServletException {
        BDDMockito.given(request.getPathInfo()).willReturn("/1");
        Publisher publisher = CreatedData.createPublisher(1);
        BDDMockito.given(service.getById(anyLong())).willReturn(publisher);
        BDDMockito.given(mapper.toPublisherResponseDto(publisher))
                .willReturn(new PublisherResponseDto());
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        publisherServlet.doGet(request, response);
        verify(request, times(1)).getPathInfo();
        verify(service, times(0)).getAllPublishers();
        verify(service, times(1)).getById(anyLong());
        verify(mapper, times(1)).toPublisherResponseDto(publisher);
        verify(response, times(1)).getWriter();
    }

    @Test
    @DisplayName("Endpoint /publisher/{id} GET request functionality. Get by id not exist publisher")
    void givenPublisherServlet_whenDoGetByIdNotExistPublisher_thenCorrect() throws IOException, ServletException {
        BDDMockito.given(request.getPathInfo()).willReturn("/1");
        BDDMockito.given(service.getById(anyLong())).willThrow(new NotFoundException("Publisher " + 1 + " not found"));
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        publisherServlet.doGet(request, response);
        verify(request, times(1)).getPathInfo();
        verify(service, times(0)).getAllPublishers();
        verify(service, times(1)).getById(anyLong());
        verify(mapper, times(0)).toPublisherResponseDto(any(Publisher.class));
        verify(response, times(1)).getWriter();
        verify(response, times(1)).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Endpoint /publisher POST request functionality. Create publisher")
    void givenPublisherServlet_whenDoPostCreatePublisher_thenCorrect() throws IOException {
        PublisherRequestDto dto = new PublisherRequestDto("name1", "city1");
        Publisher publisher = CreatedData.createPublisher(1);
        String json = objectMapper.writeValueAsString(dto);
        Reader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);
        BDDMockito.given(request.getReader()).willReturn(bufferedReader);
        BDDMockito.given(mapper.fromRequestDtoToPublisher(any(PublisherRequestDto.class))).willReturn(publisher);
        BDDMockito.given(service.createPublisher(publisher)).willReturn(publisher);
        BDDMockito.given(mapper.toPublisherResponseDtoShort(any(Publisher.class)))
                .willReturn(new PublisherResponseDtoShort());
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        publisherServlet.doPost(request, response);
        verify(request, times(1)).getReader();
        verify(service, times(1)).createPublisher(publisher);
        verify(mapper, times(1)).fromRequestDtoToPublisher(any(PublisherRequestDto.class));
        verify(mapper, times(1)).toPublisherResponseDtoShort(any(Publisher.class));
        verify(response, times(1)).getWriter();
    }

    @Test
    @DisplayName("Endpoint /publisher POST request functionality. Create publisher, exception")
    void givenPublisherServlet_whenDoPostCreatePublisher_thenException() throws IOException {
        PublisherRequestDto dto = new PublisherRequestDto("name1", "city1");
        Publisher publisher = CreatedData.createPublisher(1);
        String json = objectMapper.writeValueAsString(dto);
        Reader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);
        BDDMockito.given(request.getReader()).willReturn(bufferedReader);
        BDDMockito.given(mapper.fromRequestDtoToPublisher(any(PublisherRequestDto.class))).willReturn(publisher);
        BDDMockito.given(service.createPublisher(publisher)).willThrow(RuntimeException.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        publisherServlet.doPost(request, response);
        verify(request, times(1)).getReader();
        verify(service, times(1)).createPublisher(publisher);
        verify(mapper, times(1)).fromRequestDtoToPublisher(any(PublisherRequestDto.class));
        verify(mapper, times(0)).toPublisherResponseDtoShort(any(Publisher.class));
        verify(response, times(1)).getWriter();
        verify(response, times(1)).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Endpoint /publisher POST request functionality. Request empty body")
    void givenPublisherServlet_whenDoPostEmptyBody_thenCorrect() throws IOException {
        Reader reader = new StringReader("");
        BufferedReader bufferedReader = new BufferedReader(reader);
        BDDMockito.given(request.getReader()).willReturn(bufferedReader);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        publisherServlet.doPost(request, response);
        verify(request, times(1)).getReader();
        verify(service, times(0)).createPublisher(any(Publisher.class));
        verify(mapper, times(0)).fromRequestDtoToPublisher(any(PublisherRequestDto.class));
        verify(mapper, times(0)).toPublisherResponseDtoShort(any(Publisher.class));
        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response, times(1)).getWriter();
    }

    @Test
    @DisplayName("Endpoint /publisher/{id} PUT request functionality. Request empty body")
    void givenPublisherServlet_whenDoPutEmptyBody_thenCorrect() throws IOException, ServletException {
        BDDMockito.given(request.getPathInfo()).willReturn("/1");
        Reader reader = new StringReader("");
        BufferedReader bufferedReader = new BufferedReader(reader);
        BDDMockito.given(request.getReader()).willReturn(bufferedReader);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        publisherServlet.doPut(request, response);
        verify(request, times(1)).getPathInfo();
        verify(request, times(1)).getReader();
        verify(service, times(0)).createPublisher(any(Publisher.class));
        verify(mapper, times(0)).fromRequestDtoToPublisher(any(PublisherRequestDto.class));
        verify(mapper, times(0)).toPublisherResponseDtoShort(any(Publisher.class));
        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response, times(1)).getWriter();
    }

    @Test
    @DisplayName("Endpoint /publisher/{id} PUT request functionality. Update not exist publisher")
    void givenPublisherServlet_whenDoPutIncorrectId_thenException() throws IOException, ServletException {
        BDDMockito.given(request.getPathInfo()).willReturn("/1");
        PublisherRequestDto dto = new PublisherRequestDto("name1", "city1");
        Publisher publisher = CreatedData.createPublisher(1);
        String json = objectMapper.writeValueAsString(dto);
        Reader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);
        BDDMockito.given(request.getReader()).willReturn(bufferedReader);
        BDDMockito.given(mapper.fromRequestDtoToPublisher(any(PublisherRequestDto.class))).willReturn(publisher);
        BDDMockito.given(service.update(publisher, 1)).
                willThrow(new NotFoundException("Author not found"));
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        publisherServlet.doPut(request, response);
        verify(request, times(1)).getReader();
        verify(service, times(1)).update(publisher, 1);
        verify(mapper, times(1)).fromRequestDtoToPublisher(any(PublisherRequestDto.class));
        verify(mapper, times(0)).toPublisherResponseDtoShort(any(Publisher.class));
        verify(response, times(1)).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(response, times(1)).getWriter();
    }

    @Test
    @DisplayName("Endpoint /publisher/{id} PUT request functionality. Update publisher")
    void givenPublisherServlet_whenDoPutUpdateById_thenException() throws IOException, ServletException {
        BDDMockito.given(request.getPathInfo()).willReturn("/1");
        PublisherRequestDto dto = new PublisherRequestDto("name1", "city1");
        Publisher publisher = CreatedData.createPublisher(1);
        String json = objectMapper.writeValueAsString(dto);
        Reader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);
        BDDMockito.given(request.getReader()).willReturn(bufferedReader);
        BDDMockito.given(mapper.fromRequestDtoToPublisher(any(PublisherRequestDto.class))).willReturn(publisher);
        BDDMockito.given(service.update(publisher, 1)).willReturn(publisher);
        BDDMockito.given(mapper.toPublisherResponseDtoShort(publisher)).willReturn(new PublisherResponseDtoShort());
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        publisherServlet.doPut(request, response);
        verify(request, times(1)).getReader();
        verify(service, times(1)).update(publisher, 1);
        verify(mapper, times(1)).fromRequestDtoToPublisher(any(PublisherRequestDto.class));
        verify(mapper, times(1)).toPublisherResponseDtoShort(publisher);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);
        verify(response, times(1)).getWriter();
    }

    @Test
    @DisplayName("Endpoint /publisher/{id} PUT request functionality. Null path variable")
    void givenPublisherServlet_whenDoPutNullPathVariable_thenException() throws IOException, ServletException {
        BDDMockito.given(request.getPathInfo()).willReturn("");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        publisherServlet.doPut(request, response);
        verify(request, times(1)).getPathInfo();
        verify(request, times(0)).getReader();
        verify(service, times(0)).update(any(Publisher.class), anyLong());
        verify(mapper, times(0)).fromRequestDtoToPublisher(any(PublisherRequestDto.class));
        verify(mapper, times(0)).toPublisherResponseDtoShort(any(Publisher.class));
        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response, times(1)).getWriter();
    }

    @Test
    @DisplayName("Endpoint /publisher/{id} DELETE request functionality. Delete by id publisher")
    void givenPublisherServlet_whenDoDeleteById_thenCorrect() throws IOException {
        BDDMockito.given(request.getPathInfo()).willReturn("/1");
        BDDMockito.given(service.deleteById(anyLong())).willReturn(true);
        publisherServlet.doDelete(request, response);
        verify(request, times(1)).getPathInfo();
        verify(service, times(1)).deleteById(anyLong());
        verify(response, times(1)).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    @DisplayName("Endpoint /publisher/{id} DELETE request functionality. Delete by id not exist publisher")
    void givenPublisherServlet_whenDoDeleteByIdNotExistPublisher_thenCorrect() throws IOException {
        BDDMockito.given(request.getPathInfo()).willReturn("/1");
        BDDMockito.given(service.deleteById(anyLong())).willReturn(false);
        publisherServlet.doDelete(request, response);
        verify(request, times(1)).getPathInfo();
        verify(service, times(1)).deleteById(anyLong());
        verify(response, times(1)).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Endpoint /publisher/{id} DELETE request functionality. Null path variable")
    void givenPublisherServlet_whenDoDeleteByIdNullPathVariable_thenException() throws IOException {
        BDDMockito.given(request.getPathInfo()).willReturn("");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        publisherServlet.doDelete(request, response);
        verify(request, times(1)).getPathInfo();
        verify(service, times(0)).deleteById(anyLong());
        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

}