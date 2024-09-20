package ru.practicum.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.practicum.model.Book;
import ru.practicum.repository.dataUtils.CreatedData;
import ru.practicum.service.book.BookService;
import ru.practicum.servlet.dto.book.BookDto;
import ru.practicum.servlet.dto.book.BookResponseDto;
import ru.practicum.servlet.dto.book.BookUpdateDto;
import ru.practicum.servlet.dto.book.BookWithPublisherDto;
import ru.practicum.servlet.dto.mapping.BookMapper;

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
class BookServletTest {
    @Mock
    private BookService service;
    @Mock
    private BookMapper mapper;
    @InjectMocks
    private BookServlet publisherServlet = new BookServlet();

    private HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    private HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Endpoint /book GET request functionality. Get all book")
    void givenBookServlet_whenDoGetAll_thenCorrect() throws IOException {
        BDDMockito.given(request.getPathInfo()).willReturn("");
        Book book = CreatedData.createBook(1);
        BDDMockito.given(service.getAllBooks()).willReturn(List.of(book));
        BDDMockito.given(mapper.toBookResponseDtoList(anyList()))
                .willReturn(List.of(new BookResponseDto()));
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        publisherServlet.doGet(request, response);
        verify(request, times(1)).getPathInfo();
        verify(service, times(1)).getAllBooks();
        verify(service, times(0)).getById(anyLong());
        verify(mapper, times(1)).toBookResponseDtoList(anyList());
        verify(response, times(1)).getWriter();
    }

    @Test
    @DisplayName("Endpoint /book/{id} GET request functionality. Get by id book")
    void givenBookServlet_whenDoGetById_thenCorrect() throws IOException {
        BDDMockito.given(request.getPathInfo()).willReturn("/1");
        Book book = CreatedData.createBook(1);
        BDDMockito.given(service.getById(anyLong())).willReturn(book);
        BDDMockito.given(mapper.toBookWithPublisherDto(book))
                .willReturn(new BookWithPublisherDto());
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        publisherServlet.doGet(request, response);
        verify(request, times(1)).getPathInfo();
        verify(service, times(0)).getAllBooks();
        verify(service, times(1)).getById(anyLong());
        verify(mapper, times(1)).toBookWithPublisherDto(book);
        verify(response, times(1)).getWriter();
    }

    @Test
    @DisplayName("Endpoint /book/{id} GET request functionality. Get by id not exist book")
    void givenBookServlet_whenDoGetByIdNotExistBook_thenCorrect() throws IOException {
        BDDMockito.given(request.getPathInfo()).willReturn("/1");
        BDDMockito.given(service.getById(anyLong())).willThrow(new NotFoundException("Book " + 1 + " not found"));
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        publisherServlet.doGet(request, response);
        verify(request, times(1)).getPathInfo();
        verify(service, times(0)).getAllBooks();
        verify(service, times(1)).getById(anyLong());
        verify(mapper, times(0)).toBookWithPublisherDto(any(Book.class));
        verify(response, times(1)).getWriter();
        verify(response, times(1)).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Endpoint /book POST request functionality. Create book")
    void givenBookServlet_whenDoPostCreateBook_thenCorrect() throws IOException {
        BookDto dto = new BookDto(1, "name1", 1, 1, List.of(1L));
        Book book = CreatedData.createBook(1);
        String json = objectMapper.writeValueAsString(dto);
        Reader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);
        BDDMockito.given(request.getReader()).willReturn(bufferedReader);
        BDDMockito.given(mapper.fromBookDto(any(BookDto.class))).willReturn(book);
        BDDMockito.given(service.create(book)).willReturn(book);
        BDDMockito.given(mapper.toBookDto(any(Book.class)))
                .willReturn(new BookDto());
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        publisherServlet.doPost(request, response);
        verify(request, times(1)).getReader();
        verify(service, times(1)).create(book);
        verify(mapper, times(1)).fromBookDto(any(BookDto.class));
        verify(mapper, times(1)).toBookDto(any(Book.class));
        verify(response, times(1)).getWriter();
    }

    @Test
    @DisplayName("Endpoint /book POST request functionality. Create book, exception")
    void givenBookServlet_whenDoPostCreateBook_thenException() throws IOException {
        BookDto dto = new BookDto(1, "name1", 1, 1, List.of(1L));
        Book book = CreatedData.createBook(1);
        String json = objectMapper.writeValueAsString(dto);
        Reader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);
        BDDMockito.given(request.getReader()).willReturn(bufferedReader);
        BDDMockito.given(mapper.fromBookDto(any(BookDto.class))).willReturn(book);
        BDDMockito.given(service.create(book)).willThrow(RuntimeException.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        publisherServlet.doPost(request, response);
        verify(request, times(1)).getReader();
        verify(service, times(1)).create(book);
        verify(mapper, times(1)).fromBookDto(any(BookDto.class));
        verify(mapper, times(0)).toBookResponseDto(any(Book.class));
        verify(response, times(1)).getWriter();
        verify(response, times(1)).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Endpoint /book POST request functionality. Request empty body")
    void givenBookServlet_whenDoPostEmptyBody_thenCorrect() throws IOException {
        Reader reader = new StringReader("");
        BufferedReader bufferedReader = new BufferedReader(reader);
        BDDMockito.given(request.getReader()).willReturn(bufferedReader);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        publisherServlet.doPost(request, response);
        verify(request, times(1)).getReader();
        verify(service, times(0)).create(any(Book.class));
        verify(mapper, times(0)).fromBookDto(any(BookDto.class));
        verify(mapper, times(0)).toBookResponseDto(any(Book.class));
        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response, times(1)).getWriter();
    }

    @Test
    @DisplayName("Endpoint /book/{id} PUT request functionality. Request empty body")
    void givenBookServlet_whenDoPutEmptyBody_thenCorrect() throws IOException {
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
        verify(service, times(0)).create(any(Book.class));
        verify(mapper, times(0)).fromBookDto(any(BookDto.class));
        verify(mapper, times(0)).toBookResponseDto(any(Book.class));
        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response, times(1)).getWriter();
    }

    @Test
    @DisplayName("Endpoint /book/{id} PUT request functionality. Update not exist book")
    void givenBookServlet_whenDoPutIncorrectId_thenException() throws IOException {
        BDDMockito.given(request.getPathInfo()).willReturn("/1");
        BookUpdateDto dto = new BookUpdateDto("name1", 1, 1);
        Book book = CreatedData.createBook(1);
        String json = objectMapper.writeValueAsString(dto);
        Reader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);
        BDDMockito.given(request.getReader()).willReturn(bufferedReader);
        BDDMockito.given(mapper.fromBookUpdateDto(any(BookUpdateDto.class))).willReturn(book);
        BDDMockito.given(service.update(book, 1)).
                willThrow(new NotFoundException("Author not found"));
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        publisherServlet.doPut(request, response);
        verify(request, times(1)).getReader();
        verify(service, times(1)).update(book, 1);
        verify(mapper, times(1)).fromBookUpdateDto(any(BookUpdateDto.class));
        verify(mapper, times(0)).toBookResponseDto(any(Book.class));
        verify(response, times(1)).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(response, times(1)).getWriter();
    }

    @Test
    @DisplayName("Endpoint /book/{id} PUT request functionality. Update book")
    void givenBookServlet_whenDoPutUpdateById_thenException() throws IOException {
        BDDMockito.given(request.getPathInfo()).willReturn("/1");
        BookUpdateDto dto = new BookUpdateDto("name1", 1, 1);
        Book book = CreatedData.createBook(1);
        String json = objectMapper.writeValueAsString(dto);
        Reader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);
        BDDMockito.given(request.getReader()).willReturn(bufferedReader);
        BDDMockito.given(mapper.fromBookUpdateDto(any(BookUpdateDto.class))).willReturn(book);
        BDDMockito.given(service.update(book, 1)).willReturn(book);
        BDDMockito.given(mapper.toBookDto(book)).willReturn(new BookDto());
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        publisherServlet.doPut(request, response);
        verify(request, times(1)).getReader();
        verify(service, times(1)).update(book, 1);
        verify(mapper, times(1)).fromBookUpdateDto(any(BookUpdateDto.class));
        verify(mapper, times(1)).toBookDto(book);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);
        verify(response, times(1)).getWriter();
    }

    @Test
    @DisplayName("Endpoint /book/{id} PUT request functionality. Null path variable")
    void givenBookServlet_whenDoPutNullPathVariable_thenException() throws IOException {
        BDDMockito.given(request.getPathInfo()).willReturn("");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        publisherServlet.doPut(request, response);
        verify(request, times(1)).getPathInfo();
        verify(request, times(0)).getReader();
        verify(service, times(0)).update(any(Book.class), anyLong());
        verify(mapper, times(0)).fromBookUpdateDto(any(BookUpdateDto.class));
        verify(mapper, times(0)).toBookResponseDto(any(Book.class));
        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response, times(1)).getWriter();
    }

    @Test
    @DisplayName("Endpoint /book/{id} DELETE request functionality. Delete by id book")
    void givenBookServlet_whenDoDeleteById_thenCorrect() throws IOException {
        BDDMockito.given(request.getPathInfo()).willReturn("/1");
        BDDMockito.given(service.deleteById(anyLong())).willReturn(true);
        publisherServlet.doDelete(request, response);
        verify(request, times(1)).getPathInfo();
        verify(service, times(1)).deleteById(anyLong());
        verify(response, times(1)).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    @DisplayName("Endpoint /book/{id} DELETE request functionality. Delete by id not exist book")
    void givenBookServlet_whenDoDeleteByIdNotExistBook_thenCorrect() throws IOException {
        BDDMockito.given(request.getPathInfo()).willReturn("/1");
        BDDMockito.given(service.deleteById(anyLong())).willReturn(false);
        publisherServlet.doDelete(request, response);
        verify(request, times(1)).getPathInfo();
        verify(service, times(1)).deleteById(anyLong());
        verify(response, times(1)).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Endpoint /book/{id} DELETE request functionality. Null path variable")
    void givenBookServlet_whenDoDeleteByIdNullPathVariable_thenException() throws IOException {
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