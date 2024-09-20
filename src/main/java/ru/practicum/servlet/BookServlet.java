package ru.practicum.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.practicum.factory.Factory;
import ru.practicum.model.Book;
import ru.practicum.service.book.BookService;
import ru.practicum.servlet.dto.book.BookDto;
import ru.practicum.servlet.dto.book.BookResponseDto;
import ru.practicum.servlet.dto.book.BookUpdateDto;
import ru.practicum.servlet.dto.mapping.BookMapper;
import ru.practicum.servlet.dto.mapping.BookMapperImpl;
import ru.practicum.servlet.utils.ErrorMessage;
import ru.practicum.servlet.utils.JsonMapper;
import ru.practicum.servlet.utils.Utils;

import java.io.IOException;
import java.util.List;

@WebServlet("/books/*")
public class BookServlet extends HttpServlet {
    private transient BookService bookService;
    private transient BookMapper bookMapper;

    @Override
    public void init() {
        bookService = Factory.getBookService();
        bookMapper = new BookMapperImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathVar = req.getPathInfo();
        if (pathVar == null || pathVar.isEmpty()) {
            getAllBook(resp);
        } else {
            getBookByID(resp, Utils.parsePathVariableToId(pathVar));
        }
    }

    private void getAllBook(HttpServletResponse resp) throws IOException {
        List<Book> books = bookService.getAllBooks();
        Utils.settingResponse(resp);
        List<BookResponseDto> dtoList = bookMapper.toBookResponseDtoList(books);
        resp.getWriter().print(JsonMapper.parseToJson(dtoList));
    }

    private void getBookByID(HttpServletResponse resp, long id) throws IOException {
        try {
            Book book = bookService.getById(id);
            BookResponseDto dto = bookMapper.toBookWithPublisherDto(book);
            Utils.settingResponse(resp);
            resp.getWriter().print(JsonMapper.parseToJson(dto));
        } catch (RuntimeException e) {
            Utils.setErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            BookDto dto = JsonMapper.parseRequestBody(req, BookDto.class);
            Book book = bookService.create(bookMapper.fromBookDto(dto));
            resp.setStatus(HttpServletResponse.SC_CREATED);
            Utils.settingResponse(resp);
            resp.getWriter().println(JsonMapper.parseToJson(bookMapper.toBookDto(book)));
        } catch (RuntimeException e) {
            Utils.setErrorResponse(resp, HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        } catch (IOException e) {
            Utils.setErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, ErrorMessage.EMPTY_BODY.msg());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathVar = req.getPathInfo();
        if (pathVar == null || pathVar.isEmpty()) {
            Utils.setErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, ErrorMessage.EMPTY_PATH_VARIABLE.msg());
            return;
        }
        try {
            BookUpdateDto dto = JsonMapper.parseRequestBody(req, BookUpdateDto.class);
            Book book = bookService.update(bookMapper.fromBookUpdateDto(dto),
                    Utils.parsePathVariableToId(pathVar));
            resp.setStatus(HttpServletResponse.SC_OK);
            Utils.settingResponse(resp);
            resp.getWriter().println(JsonMapper.parseToJson(bookMapper.toBookDto(book)));
        } catch (RuntimeException e) {
            Utils.setErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (IOException e) {
            Utils.setErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, ErrorMessage.EMPTY_BODY.msg());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathVar = req.getPathInfo();
        if (pathVar == null || pathVar.isEmpty()) {
            Utils.setErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, ErrorMessage.EMPTY_PATH_VARIABLE.msg());
            return;
        }
        boolean isDeleted = bookService.deleteById(Utils.parsePathVariableToId(pathVar));
        if (isDeleted) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
