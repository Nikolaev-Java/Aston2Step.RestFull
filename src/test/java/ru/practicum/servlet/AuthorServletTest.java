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
import ru.practicum.model.Author;
import ru.practicum.repository.dataUtils.CreatedData;
import ru.practicum.service.author.AuthorService;
import ru.practicum.servlet.dto.author.AuthorRequestDto;
import ru.practicum.servlet.dto.author.AuthorResponseDto;
import ru.practicum.servlet.dto.author.AuthorResponseDtoShort;
import ru.practicum.servlet.dto.mapping.AuthorMapper;

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
class AuthorServletTest {
    @Mock
    private AuthorService authorService;
    @Mock
    private AuthorMapper authorMapper;
    @InjectMocks
    private AuthorServlet authorServlet = new AuthorServlet();

    private HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    private HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Endpoint /author GET request functionality. Get all authors")
    void givenAuthorServlet_whenDoGetAll_thenCorrect() throws ServletException, IOException {
        BDDMockito.given(request.getPathInfo()).willReturn("");
        Author author = CreatedData.createAuthor(1);
        BDDMockito.given(authorService.getAll()).willReturn(List.of(author));
        BDDMockito.given(authorMapper.authorListToResponseDtoShortList(anyList()))
                .willReturn(List.of(new AuthorResponseDtoShort()));
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        authorServlet.doGet(request, response);
        verify(request, times(1)).getPathInfo();
        verify(authorService, times(1)).getAll();
        verify(authorService, times(0)).getById(anyLong());
        verify(authorMapper, times(1)).authorListToResponseDtoShortList(anyList());
        verify(response, times(1)).getWriter();
    }

    @Test
    @DisplayName("Endpoint /author/{id} GET request functionality. Get by id author")
    void givenAuthorServlet_whenDoGetById_thenCorrect() throws ServletException, IOException {
        BDDMockito.given(request.getPathInfo()).willReturn("/1");
        Author author = CreatedData.createAuthor(1);
        BDDMockito.given(authorService.getById(anyLong())).willReturn(author);
        BDDMockito.given(authorMapper.authorToResponseDto(author))
                .willReturn(new AuthorResponseDto());
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        authorServlet.doGet(request, response);
        verify(request, times(1)).getPathInfo();
        verify(authorService, times(0)).getAll();
        verify(authorService, times(1)).getById(anyLong());
        verify(authorMapper, times(1)).authorToResponseDto(author);
        verify(response, times(1)).getWriter();
    }

    @Test
    @DisplayName("Endpoint /author/{id} GET request functionality. Get by id not exist author")
    void givenAuthorServlet_whenDoGetByIdNotExistAuthor_thenCorrect() throws ServletException, IOException {
        BDDMockito.given(request.getPathInfo()).willReturn("/1");
        BDDMockito.given(authorService.getById(anyLong())).willThrow(new NotFoundException("Author " + 1 + " not found"));
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        authorServlet.doGet(request, response);
        verify(request, times(1)).getPathInfo();
        verify(authorService, times(0)).getAll();
        verify(authorService, times(1)).getById(anyLong());
        verify(authorMapper, times(0)).authorToResponseDto(any(Author.class));
        verify(response, times(1)).getWriter();
        verify(response, times(1)).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Endpoint /author POST request functionality. Create author")
    void givenAuthorServlet_whenDoPostCreateAuthor_thenCorrect() throws ServletException, IOException {
        AuthorRequestDto dto = new AuthorRequestDto("name1", "lastName1");
        Author author = CreatedData.createAuthor(1);
        String json = objectMapper.writeValueAsString(dto);
        Reader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);
        BDDMockito.given(request.getReader()).willReturn(bufferedReader);
        BDDMockito.given(authorMapper.fromAuthorRequestDto(any(AuthorRequestDto.class))).willReturn(author);
        BDDMockito.given(authorService.create(author)).willReturn(author);
        BDDMockito.given(authorMapper.authorToResponseDtoShort(any(Author.class)))
                .willReturn(new AuthorResponseDtoShort());
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        authorServlet.doPost(request, response);
        verify(request, times(1)).getReader();
        verify(authorService, times(1)).create(author);
        verify(authorMapper, times(1)).fromAuthorRequestDto(any(AuthorRequestDto.class));
        verify(authorMapper, times(1)).authorToResponseDtoShort(any(Author.class));
        verify(response, times(1)).getWriter();
    }

    @Test
    @DisplayName("Endpoint /author POST request functionality. Create author, exception")
    void givenAuthorServlet_whenDoPostCreateAuthor_thenException() throws ServletException, IOException {
        AuthorRequestDto dto = new AuthorRequestDto("name1", "lastName1");
        Author author = CreatedData.createAuthor(1);
        String json = objectMapper.writeValueAsString(dto);
        Reader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);
        BDDMockito.given(request.getReader()).willReturn(bufferedReader);
        BDDMockito.given(authorMapper.fromAuthorRequestDto(any(AuthorRequestDto.class))).willReturn(author);
        BDDMockito.given(authorService.create(author)).willThrow(RuntimeException.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        authorServlet.doPost(request, response);
        verify(request, times(1)).getReader();
        verify(authorService, times(1)).create(author);
        verify(authorMapper, times(1)).fromAuthorRequestDto(any(AuthorRequestDto.class));
        verify(authorMapper, times(0)).authorToResponseDtoShort(any(Author.class));
        verify(response, times(1)).getWriter();
        verify(response, times(1)).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Endpoint /author POST request functionality. Request empty body")
    void givenAuthorServlet_whenDoPostEmptyBody_thenCorrect() throws ServletException, IOException {
        Reader reader = new StringReader("");
        BufferedReader bufferedReader = new BufferedReader(reader);
        BDDMockito.given(request.getReader()).willReturn(bufferedReader);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        authorServlet.doPost(request, response);
        verify(request, times(1)).getReader();
        verify(authorService, times(0)).create(any(Author.class));
        verify(authorMapper, times(0)).fromAuthorRequestDto(any(AuthorRequestDto.class));
        verify(authorMapper, times(0)).authorToResponseDtoShort(any(Author.class));
        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response, times(1)).getWriter();
    }

    @Test
    @DisplayName("Endpoint /author/{id} PUT request functionality. Request empty body")
    void givenAuthorServlet_whenDoPutEmptyBody_thenCorrect() throws ServletException, IOException {
        BDDMockito.given(request.getPathInfo()).willReturn("/1");
        Reader reader = new StringReader("");
        BufferedReader bufferedReader = new BufferedReader(reader);
        BDDMockito.given(request.getReader()).willReturn(bufferedReader);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        authorServlet.doPut(request, response);
        verify(request, times(1)).getPathInfo();
        verify(request, times(1)).getReader();
        verify(authorService, times(0)).create(any(Author.class));
        verify(authorMapper, times(0)).fromAuthorRequestDto(any(AuthorRequestDto.class));
        verify(authorMapper, times(0)).authorToResponseDtoShort(any(Author.class));
        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response, times(1)).getWriter();
    }

    @Test
    @DisplayName("Endpoint /author/{id} PUT request functionality. Update not exist author")
    void givenAuthorServlet_whenDoPutIncorrectId_thenException() throws ServletException, IOException {
        BDDMockito.given(request.getPathInfo()).willReturn("/1");
        AuthorRequestDto dto = new AuthorRequestDto("name1", "lastName1");
        Author author = CreatedData.createAuthor(1);
        String json = objectMapper.writeValueAsString(dto);
        Reader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);
        BDDMockito.given(request.getReader()).willReturn(bufferedReader);
        BDDMockito.given(authorMapper.fromAuthorRequestDto(any(AuthorRequestDto.class))).willReturn(author);
        BDDMockito.given(authorService.update(author, 1)).
                willThrow(new NotFoundException("Author not found"));
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        authorServlet.doPut(request, response);
        verify(request, times(1)).getReader();
        verify(authorService, times(1)).update(author, 1);
        verify(authorMapper, times(1)).fromAuthorRequestDto(any(AuthorRequestDto.class));
        verify(authorMapper, times(0)).authorToResponseDtoShort(any(Author.class));
        verify(response, times(1)).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(response, times(1)).getWriter();
    }

    @Test
    @DisplayName("Endpoint /author/{id} PUT request functionality. Update author")
    void givenAuthorServlet_whenDoPutUpdateById_thenException() throws ServletException, IOException {
        BDDMockito.given(request.getPathInfo()).willReturn("/1");
        AuthorRequestDto dto = new AuthorRequestDto("name1", "lastName1");
        Author author = CreatedData.createAuthor(1);
        String json = objectMapper.writeValueAsString(dto);
        Reader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);
        BDDMockito.given(request.getReader()).willReturn(bufferedReader);
        BDDMockito.given(authorMapper.fromAuthorRequestDto(any(AuthorRequestDto.class))).willReturn(author);
        BDDMockito.given(authorService.update(author, 1)).willReturn(author);
        BDDMockito.given(authorMapper.authorToResponseDtoShort(author)).willReturn(new AuthorResponseDtoShort());
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        authorServlet.doPut(request, response);
        verify(request, times(1)).getReader();
        verify(authorService, times(1)).update(author, 1);
        verify(authorMapper, times(1)).fromAuthorRequestDto(any(AuthorRequestDto.class));
        verify(authorMapper, times(1)).authorToResponseDtoShort(author);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);
        verify(response, times(1)).getWriter();
    }

    @Test
    @DisplayName("Endpoint /author/{id} PUT request functionality. Null path variable")
    void givenAuthorServlet_whenDoPutNullPathVariable_thenException() throws ServletException, IOException {
        BDDMockito.given(request.getPathInfo()).willReturn("");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        authorServlet.doPut(request, response);
        verify(request, times(1)).getPathInfo();
        verify(request, times(0)).getReader();
        verify(authorService, times(0)).update(any(Author.class), anyLong());
        verify(authorMapper, times(0)).fromAuthorRequestDto(any(AuthorRequestDto.class));
        verify(authorMapper, times(0)).authorToResponseDtoShort(any(Author.class));
        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response, times(1)).getWriter();
    }

    @Test
    @DisplayName("Endpoint /author/{id} DELETE request functionality. Delete by id author")
    void givenAuthorServlet_whenDoDeleteById_thenCorrect() throws ServletException, IOException {
        BDDMockito.given(request.getPathInfo()).willReturn("/1");
        BDDMockito.given(authorService.deleteById(anyLong())).willReturn(true);
        authorServlet.doDelete(request, response);
        verify(request, times(1)).getPathInfo();
        verify(authorService, times(1)).deleteById(anyLong());
        verify(response, times(1)).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    @DisplayName("Endpoint /author/{id} DELETE request functionality. Delete by id not exist author")
    void givenAuthorServlet_whenDoDeleteByIdNotExistAuthor_thenCorrect() throws ServletException, IOException {
        BDDMockito.given(request.getPathInfo()).willReturn("/1");
        BDDMockito.given(authorService.deleteById(anyLong())).willReturn(false);
        authorServlet.doDelete(request, response);
        verify(request, times(1)).getPathInfo();
        verify(authorService, times(1)).deleteById(anyLong());
        verify(response, times(1)).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Endpoint /author/{id} DELETE request functionality. Null path variable")
    void givenAuthorServlet_whenDoDeleteByIdNullPathVariable_thenException() throws ServletException, IOException {
        BDDMockito.given(request.getPathInfo()).willReturn("");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        BDDMockito.given(response.getWriter()).willReturn(writer);
        authorServlet.doDelete(request, response);
        verify(request, times(1)).getPathInfo();
        verify(authorService, times(0)).deleteById(anyLong());
        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}